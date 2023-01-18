package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.IfEvenNode;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * Demo program for testing SmartScriptParser.
 * 
 * @author Ana Bagić
 *
 */
public class TreeWriter {

	public static void main(String[] args) {
		if(args.length != 1) {
			throw new IllegalArgumentException("Number of arguments has to be 1: file name.");
		}

		String filepath = args[0];
		String docBody = null;
		
		try {
			docBody = new String(
					 Files.readAllBytes(Paths.get(filepath)),
					 StandardCharsets.UTF_8
					);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		SmartScriptParser parser = new SmartScriptParser(docBody);
		WriterVisitor visitor = new WriterVisitor();
		parser.getDocumentNode().accept(visitor);
		
	}
	
	/**
	 * Class implements INodeVisitor and is used to write nodes.
	 * 
	 * @author Ana Bagić
	 */
	static class WriterVisitor implements INodeVisitor {

		@Override
		public void visitTextNode(TextNode node) {
			System.out.print(node.getText());
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			System.out.print("{$ FOR ");
			System.out.print(node.getVariable().asText() + " " + node.getStartExpression().asText());
			System.out.print(" " + node.getEndExpression().asText());
			if(node.getStepExpression() != null) {
				System.out.print(" " + node.getStepExpression().asText());
			}
			System.out.print(" $}");
				
			for(int i = 0; i < node.numberOfChildren(); i++) {
				node.getChild(i).accept(this);
			}
			
			System.out.print("{$ END $}");
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			System.out.print("{$ = ");
			for(Element el : node.getElements()) {
				System.out.print(el.asText() + " ");
			}
			System.out.print("$}");
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for(int i = 0; i < node.numberOfChildren(); i++) {
				node.getChild(i).accept(this);
			}
		}

		@Override
		public void visitIfEvenNode(IfEvenNode node) {
			// TODO Auto-generated method stub
			
		}
	}
}
