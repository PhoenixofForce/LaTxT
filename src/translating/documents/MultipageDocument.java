package translating.documents;

import translating.Alignment;

import java.util.ArrayList;
import java.util.List;

public class MultipageDocument extends Document {

	public MultipageDocument(Document parent) {
		super(parent, Alignment.LEFT, 0);
	}

	@Override
	public int getLineWidth() {
		return (getParent().getLineWidth() - 3 * (getChildrenCount() - 1)) / Math.max(1, getChildrenCount());
	}

	@Override
	public List<String> toText() {
		List<List<String>> plainText = new ArrayList<>();
		List<String> out = new ArrayList<>();

		for(Document d: getChildren()) plainText.add(d.toText());
		int maxLines = plainText.stream().mapToInt(l -> l.size()).max().getAsInt();

		for(int i = 0; i < maxLines; i++) {
			String line = "";
			for(int d = 0; d < plainText.size(); d++) {
				List<String> l = plainText.get(d);

				if(i < l.size()) line += Alignment.align(getAlignment(), l.get(i), getLineWidth());
				else line += Alignment.align(getAlignment(), "", getLineWidth());

				if(d < plainText.size() - 1) line += " | ";
			}

			out.add(line);
		}


		return out;
	}
}
