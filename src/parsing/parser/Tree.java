package parsing.parser;

import parsing.tokens.Token;

public class Tree {

	private Tree[] childs;		//child nodes of this node
	private String value;	//replacement used in this step
	private int childAmount;	//amount of child nodes
	private int nonLeafAmount;
	private Tree parent;		//parent node or null if there is none
	private boolean constant;	//if the value of this tree and all of its child nodes is constant

	public Tree(Tree parent, int totalChildAmount, String value, boolean constant) {
		this.childs = new Tree[totalChildAmount];
		this.value = value;
		this.childAmount = 0;
		this.nonLeafAmount = 0;
		this.parent = parent;
		this.constant = constant;
	}

	public Tree(String value) {
		this.childs = new Tree[0];
		this.value = value;
		this.childAmount = 0;
		this.nonLeafAmount = 0;
		this.parent = null;
		this.constant = false;
	}

	/**
	 * @param id of the child node
	 * @return returns the child node with the given id
	 */
	public Tree getChild(int id) {
		return childs[id];
	}

	public Tree getLastChild() {
		Tree out = null;
		for (Tree child : childs) {
			if (child != null && !(child instanceof Leaf)) out = child;
		}

		return out;
	}

	/**
	 * adds a child node to this node
	 * @param tree the node to be added
	 */
	public void addChild(Tree tree) {
		for(int i = 0; i < childs.length; i++) {
			if(childs[i] == null) {
				childs[i] = tree;
				childAmount++;

				if(!(tree instanceof Leaf)) nonLeafAmount++;

				return;
			}
		}
	}

	/**
	 * replaces a child node at the given index by this new one
	 * @param index of the node to be replaced
	 * @param tree the new node to be added
	 */
	public void setChild(int index, Tree tree) {
		if(childs[index] == null) childAmount++;
		else if(childs[index] instanceof Leaf) nonLeafAmount--;
		if(!(tree instanceof Leaf)) nonLeafAmount++;

		childs[index] = tree;

	}

	/**
	 * @return calculates the total amount of nodes below this node
	 */
	public int getSize() {
		int size = 0;
		for (Tree child : childs) {
			size += child.getSize();
		}
		return 1 + size;
	}

	public int getDepth() {
		int depth = 0;
		for (Tree child : childs) {
			depth = Math.max(depth, child.getDepth());
		}
		return depth + 1;
	}

	/**
	 * @return if the value for this and all following nodes is constant
	 */
	public boolean isConstant() {
		boolean constantC = true;
		for (Tree c : childs) {
			constantC = constantC && c.isConstant();
		}
		return constant && constantC;
	}

	public Tree getParent() {
		return parent;
	}

	public int getTotalChildAmount() {
		return childs.length;
	}

	public int getChildAmount() {
		return childAmount;
	}

	public int getNonLeafAmount() {
		return nonLeafAmount;
	}

	public Tree[] getChilds() {
		return childs;
	}

	public static class Leaf extends Tree {
		private Token value;
		public Leaf(Tree parent, Token in) {
			super(parent, 0, in.toString(), !(in instanceof Token.IDToken));
			this.value = in;
		}

		public Token getToken () {
			return value;
		}

		public void setToken(Token in) {
			this.value = in;
		}
	}

	void cut(Tree toCut) {
		Tree[] newChilds = new Tree[childs.length-1];

		boolean nullFound = false;
		for(int i = 0; i < childs.length; i++) {
			Tree child = childs[i];
			if(!nullFound) {
				if(child == toCut) nullFound = true;
				else newChilds[i] = childs[i];
			} else {
				newChilds[i-1] = childs[i];
			}
		}

		childs = newChilds;
		if(newChilds.length == 0) parent.cut(this);
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		String out = "(" + value;

		for(int i = 0; i < getTotalChildAmount(); i++) {
			out += childs[i] != null? childs[i].toString(): "(null)";
		}

		return out + ")";
	}

}