package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Arrays;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * Class represents a node with dynamic generation of the value.
 * 
 * @author Ana Bagić
 *
 */
public class EchoNode extends Node {
	
	/** Elements of the node. */
	private Element[] elements;
	
	/**
	 * Constructor creates new node using given elements array.
	 * 
	 * @param elements to be stored in the node
	 */
	public EchoNode(Element[] elements) {
		this.elements = elements;
	}

	/**
	 * @return elements of the node
	 */
	public Element[] getElements() {
		return elements;
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}
	
	@Override
	public String toStringFancy() {
		StringBuilder sb = new StringBuilder();
		sb.append("EchoNode : ");
		
		for(Element el : elements)
			sb.append(el.asText()).append(" ");
		
		sb.append("\n");
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof EchoNode))
			return false;
		EchoNode other = (EchoNode) obj;
		
		return Arrays.equals(this.elements, other.elements);
	}
	
}
