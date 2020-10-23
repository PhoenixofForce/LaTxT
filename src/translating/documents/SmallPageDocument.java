package translating.documents;

import translating.Alignment;

import java.util.ArrayList;
import java.util.List;

public class SmallPageDocument extends Document {

	public SmallPageDocument(Document parent, int lineWidth) {
		super(parent, parent.getAlignment(), lineWidth);
	}

	@Override
	public List<String> toText() {
		List<String> out = new ArrayList<>();

		for(Document d: getChildren()) {
			for(String s: d.toText()) out.add(Alignment.align(getAlignment(), s, getParent().getLineWidth()));
		}


		return out;
	}
}
