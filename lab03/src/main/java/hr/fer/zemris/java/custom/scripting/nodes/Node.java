package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represents a node in a document.
 * 
 * @author Ana Bagić
 *
 */
public abstract class Node {
	
	/** Child nodes of this node. */
	private List<Node> children;
	
	/**
	 * Adds child node to this node.
	 * 
	 * @param child to add to this node
	 */
	public void addChildNode(Node child) {
		if(children == null)
			children = new ArrayList<>();
		
		children.add(child);
	}
	
	/**
	 * @return number of direct children of this node
	 */
	public int numberOfChildren() {
		return children == null ? 0 : children.size();
	}
	
	/**
	 * Returns a child node from position index, or throws an exception if the index is invalid.
	 * 
	 * @param index from which the child node should be fetched
	 * @return child node from position index
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
	 * @throws NullPointerException if node does not have children
	 */
	public Node getChild(int index) {
		if(children == null)
			throw new NullPointerException("This node does not have children yet.");
		
		return children.get(index);
	}
	
	/**
	 * Calls appropriate visitor method used to visit this object.
	 * 
	 * @param visitor node visitor
	 */
	public abstract void accept(INodeVisitor visitor);
	
	/**
	 * @return String representation of the node in a "fancy" way
	 */
	public abstract String toStringFancy();
	
}
