package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.exec.helper.ValueWrapper;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.ObjectStack;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class is used to execute the SmartScript document.
 * 
 * @author Ana Bagić
 *
 */
public class SmartScriptEngine {

	/** Root node of a document. */
	private DocumentNode documentNode;
	/** Request context used to execute the document. */
	private RequestContext requestContext;
	/** Multistack used to help execute the document. */
	private ObjectMultistack multistack = new ObjectMultistack();
	/** Implementation of {@link INodeVisitor} used to execute document. */
	private INodeVisitor visitor = new INodeVisitor() {

		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getText());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String varName = node.getVariable().getName();
			String init = node.getStartExpression().asText();
			String end = node.getEndExpression().asText();
			String step = node.getStepExpression().asText();
			
			multistack.push(varName, new ValueWrapper(init));
			while(multistack.peek(varName).numCompare(end) <= 0) {
				for(int i = 0; i < node.numberOfChildren(); i++) {
					node.getChild(i).accept(this);
				}
				multistack.peek(varName).add(step);
			}
			multistack.pop(varName);
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			ObjectStack stack = new ObjectStack();
			Element[] elems = node.getElements();
			
			for(int i = 0; i < elems.length; i++) {
				switch (elems[i].getClass().getSimpleName()) {
				case "ElementConstantDouble" -> {
					stack.push(new ValueWrapper(((ElementConstantDouble)elems[i]).getValue()));
				}
				case "ElementConstantInteger" -> {
					stack.push(new ValueWrapper(((ElementConstantInteger)elems[i]).getValue()));
				}
				case "ElementString" -> {
					stack.push(new ValueWrapper(((ElementString)elems[i]).getValue()));
				}
				case "ElementVariable" -> {
					stack.push(multistack.peek(elems[i].asText()));
				}
				case "ElementOperator" -> {
					ValueWrapper n2 = (ValueWrapper) stack.pop();
					ValueWrapper n1 = (ValueWrapper) stack.pop();
					ValueWrapper result = new ValueWrapper(n1.getValue());
					
					switch (elems[i].asText()) {
					case "+" -> result.add(n2.getValue());
					case "-" -> result.subtract(n2.getValue());
					case "*" -> result.multiply(n2.getValue());
					case "/" -> result.divide(n2.getValue());
					default -> throw new IllegalArgumentException("Unsupported operation: " + elems[i].asText());
					}
					
					stack.push(result);
				}
				case "ElementFunction" -> {
					switch(((ElementFunction)elems[i]).getName()) {
					case "dup" -> {
						stack.push(stack.peek());
					}
					case "swap" -> {
						Object a = stack.pop();
						Object b = stack.pop();
						stack.push(a);
						stack.push(b);
					}
					case "sin" -> {
						ValueWrapper x = (ValueWrapper) stack.pop();
						stack.push(new ValueWrapper(Math.sin(Math.toRadians(asDouble(x)))));
					}
					case "decfmt" -> {
						ValueWrapper f = (ValueWrapper) stack.pop();
						ValueWrapper x = (ValueWrapper) stack.pop();
						DecimalFormat r = new DecimalFormat(f.getValue().toString());
						stack.push(new ValueWrapper(r.format(asDouble(x))));
					}
					case "setMimeType" -> {
						ValueWrapper x = (ValueWrapper) stack.pop();
						requestContext.setMimeType((String) x.getValue());
					}
					case "paramGet" -> {
						paramGet(stack, n -> requestContext.getParameter(n));
					}
					case "pparamGet" -> {
						paramGet(stack, n -> requestContext.getPersistentParameter(n));
					}
					case "tparamGet" -> {
						paramGet(stack, n -> requestContext.getTemporaryParameter(n));
					}
					case "pparamSet" -> {
						paramSet(stack, (n, v) -> requestContext.setPersistentParameter(n, v));
					}
					case "tparamSet" -> {
						paramSet(stack, (n, v) -> requestContext.setTemporaryParameter(n, v));
					}
					case "pparamDel" -> {
						paramDel(stack, n -> requestContext.removePersistentParameter(n));
					}
					case "tparamDel" -> {
						paramDel(stack, n -> requestContext.removeTemporaryParameter(n));
					}
					default ->
						throw new IllegalArgumentException("Unsupported function: " + ((ElementFunction)elems[i]).getName());
					}
				}
				default ->
					throw new IllegalArgumentException("Unexpected value: " + elems[i].getClass().getSimpleName());
				}
			}
			
			ObjectStack temp = new ObjectStack();
			while(!stack.isEmpty()) {
				temp.push(stack.pop());
			}
			while(!temp.isEmpty()) {
				try {
					requestContext.write(((ValueWrapper)temp.pop()).getValue().toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for(int i = 0; i < node.numberOfChildren(); i++) {
				node.getChild(i).accept(this);
			}
		}
		
		/**
		 * Helper method that extracts double value from the given {@link ValueWrapper}.
		 * 
		 * @param n value wrapper to extract double from
		 * @return extracted double
		 */
		private double asDouble(ValueWrapper n) {
			n.add(0.0);
			return ((Double)n.getValue()).doubleValue();
		}
		
		/**
		 * Helper method that gets parameter for name fetched from stack, or gives it default value from stack.
		 * 
		 * @param stack used to get the name and default value
		 * @param getValue executes the fetching of the parameter
		 */
		private void paramGet(ObjectStack stack, Function<String, String> getValue) {
			ValueWrapper dv = (ValueWrapper) stack.pop();
			ValueWrapper name = (ValueWrapper) stack.pop();
			String par = getValue.apply((String) name.getValue());
			stack.push(par == null ? dv : new ValueWrapper(par));
		}
		
		/**
		 * Helper function that sets the parameter value for name. Value and name are fetched from the stack.
		 * 
		 * @param stack used to get the name and value
		 * @param setValue executes the setting of the parameter
		 */
		private void paramSet(ObjectStack stack, BiConsumer<String, String> setValue) {
			ValueWrapper name = (ValueWrapper) stack.pop();
			ValueWrapper value = (ValueWrapper) stack.pop();
			setValue.accept((String) name.getValue(), value.getValue().toString());
		}
		
		/**
		 * Helper function that removes the parameter for the name fetched from the stack.
		 * 
		 * @param stack used to get the name of the parameter
		 * @param delete executes the removal of the parameter
		 */
		private void paramDel(ObjectStack stack, Consumer<String> delete) {
			ValueWrapper name = (ValueWrapper) stack.pop();
			delete.accept((String) name.getValue());
		}
	};
	
	/**
	 * Creates new engine to execute the SmartScript document.
	 * 
	 * @param documentNode root node of the document to execute
	 * @param requestContext for executing the document
	 */
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}
	
	/**
	 * Method executes the document.
	 */
	public void execute() {
		documentNode.accept(visitor);
	}
}
