package translating.documents;

import translating.Alignment;

import java.util.ArrayList;
import java.util.List;

public class SubpageDocument extends Document {

	public SubpageDocument(Document parent) {
		super(parent, parent.getAlignment(), 0);
	}

	@Override
	public List<String> toText() {
		List<String> out = new ArrayList<>();

		for(Document d: getChildren()) {
			for(String s: d.toText()) out.add(Alignment.align(getAlignment(), s, getLineWidth()));
		}

		return out;
	}
}
