package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * Class represents for-loop node.
 * 
 * @author Ana Bagić
 *
 */
public class ForLoopNode extends Node {
	
	/** Variable in the expression. */
	private ElementVariable variable;
	/** First element in the expression. */
	private Element startExpression;
	/** Last element in the expression. */
	private Element endExpression;
	/** Middle optional element in the expression. */
	private Element stepExpression;
	
	/**
	 * Constructor creates a new node using given parameters.
	 * 
	 * 
	 * @param variable in the expression
	 * @param startExpression first element in the expression
	 * @param endExpression last element in the expression
	 * @param stepExpression middle optional element in the expression
	 * @throws NullPointerException if any argument apart from stepExpression is <code>null</code>
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression,
			Element endExpression, Element stepExpression) {
		this.variable = Objects.requireNonNull(variable);
		this.startExpression = Objects.requireNonNull(startExpression);
		this.endExpression = Objects.requireNonNull(endExpression);;
		this.stepExpression = stepExpression;
	}

	/**
	 * @return variable in the expression
	 */
	public ElementVariable getVariable() {
		return variable;
	}

	/**
	 * @return first element in the expression
	 */
	public Element getStartExpression() {
		return startExpression;
	}

	/**
	 * @return last element in the expression
	 */
	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * @return middle element in the expression, or <code>null</code> if it doesn't exist
	 */
	public Element getStepExpression() {
		return stepExpression;
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
	}
	
	@Override
	public String toStringFancy() {
		StringBuilder sb = new StringBuilder();
		sb.append("ForLoopNode : ");
		sb.append(variable.asText()).append(" ").append(startExpression.asText());
		sb.append(" ").append(endExpression.asText());
		if(stepExpression != null) {
			sb.append(" ").append(stepExpression.asText());
		}
		sb.append("\n");
		
		for(int i = 0; i < numberOfChildren(); i++) {
			sb.append("        ");
			sb.append(getChild(i).toStringFancy());
		}
		
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ForLoopNode))
			return false;
		ForLoopNode other = (ForLoopNode) obj;
		
		if(this.getVariable().equals(other.getVariable()) && this.getStartExpression().equals(other.getStartExpression()) &&
				this.getStepExpression().equals(other.getStepExpression()) && this.getEndExpression().equals(other.getEndExpression()))
			return true;
		
		return false;
	}
	
}
