package translating.documents;

import translating.Alignment;

import java.util.ArrayList;
import java.util.List;

public class AlignDocument extends Document {

	public AlignDocument(Document parent, Alignment alignment) {
		super(parent, alignment, 0);
	}

	@Override
	public List<String> toText() {
		List<String> out = new ArrayList<>();

		for(Document d: getChildren()) out.addAll(d.toText());
		if(out.get(out.size() - 1).length() == 0) out.remove(out.size() - 1);

		return out;
	}
}
