package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

public class IfEvenNode extends Node {

	private ElementVariable var;
	
	public IfEvenNode(ElementVariable var) {
		this.var = var;
	}
	
	public ElementVariable getVar() {
		return var;
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitIfEvenNode(this);
	}

	@Override
	public String toStringFancy() {
		// ovo sam pisala u zadaci da vidim kako je isparsirano ali necu sada pisati
		return null;
	}

}
