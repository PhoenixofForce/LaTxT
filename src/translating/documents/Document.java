package translating.documents;

import translating.Alignment;

import java.util.ArrayList;
import java.util.List;

public abstract class Document {

	private Document parent;
	private List<Document> children;

	private Alignment alignment;
	private int lineWidth;

	public Document(Document parent, Alignment alignment, int lineWidth) {
		this.parent = parent;
		this.children = new ArrayList<>();
		this.alignment = alignment;
		this.lineWidth = lineWidth;
	}

	public Document getParent() {
		return parent;
	}

	public int getChildrenCount() {
		return children.size();
	}

	public Document getChild(int pos) {
		return children.get(pos);
	}

	public List<Document> getChildren() {
		return children;
	}

	public boolean addChildren(Document toAdd) {
		return children.add(toAdd);
	}

	public Alignment getAlignment() {
		return alignment;
	}

	public int getLineWidth() {
		if(lineWidth == 0) return parent.getLineWidth();
		return lineWidth;
	}

	public MainDocument getMain() {
		Document main = this;
		while(main.getParent() != null) main = main.getParent();

		return (MainDocument) main;
	}

	public abstract List<String> toText();

}
