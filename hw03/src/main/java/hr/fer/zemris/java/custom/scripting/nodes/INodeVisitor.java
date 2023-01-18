package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Interface represents visitor of all node types.
 * 
 * @author Ana Bagić
 *
 */
public interface INodeVisitor {

	/**
	 * Visitor for {@link TextNode}.
	 * 
	 * @param node {@link TextNode} to visit
	 */
	public void visitTextNode(TextNode node);
	
	/**
	 * Visitor for {@link ForLoopNode}.
	 * 
	 * @param node {@link ForLoopNode} to visit
	 */
	public void visitForLoopNode(ForLoopNode node);
	
	/**
	 * Visitor for {@link EchoNode}.
	 * 
	 * @param node {@link EchoNode} to visit
	 */
	public void visitEchoNode(EchoNode node);
	
	/**
	 * Visitor for {@link DocumentNode}.
	 * 
	 * @param node {@link DocumentNode} to visit
	 */
	public void visitDocumentNode(DocumentNode node);

}
