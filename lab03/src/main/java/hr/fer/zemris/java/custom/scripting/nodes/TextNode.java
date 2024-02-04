package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

/**
 * Class represents a node with textual data.
 * 
 * @author Ana Bagić
 *
 */
public class TextNode extends Node {
	
	/** Text of the node. */
	private String text;

	/**
	 * Constructor creates new node with given text.
	 * 
	 * @param text to be stored in the node
	 * @throws NullPointerException if text is <code>null</code>
	 */
	public TextNode(String text) {
		this.text = Objects.requireNonNull(text);
	}

	/**
	 * @return text stored in the node
	 */
	public String getText() {
		return text;
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitTextNode(this);
	}
	
	@Override
	public String toStringFancy() {
		return "TextNode : " + text + "\n";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof TextNode))
			return false;
		TextNode other = (TextNode) obj;
		
		return this.text.equals(other.text);
	}
	
}
