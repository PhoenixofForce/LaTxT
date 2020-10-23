package translating.documents;

import translating.Alignment;

import java.util.ArrayList;
import java.util.List;

public class BoxDocument extends Document {

	private String header;
	private int margin, padding;

	public BoxDocument(Document parent, String header, int padding, int margin) {
		super(parent, Alignment.LEFT, 0);
		this.header = header;
		this.padding = padding >= 0? padding: 1;
		this.margin = margin >= 0? margin: 1;
	}

	@Override
	public int getLineWidth() {
		return getParent().getLineWidth() - 2 * (margin >= 0? margin: 1) - 2 * (padding >= 0? padding: 1) - 2;
	}

	@Override
	public List<String> toText() {
		List<String> out = new ArrayList<>();

		String sepMargin = Alignment.repeat(" ", margin + 1);
		String firstSep =  sepMargin + Alignment.repeat("_", getLineWidth() + 2 * padding) + sepMargin;
		String lastSep = sepMargin + Alignment.repeat("¯", getLineWidth() + 2 * padding) + sepMargin;

		List<String> childrenOut = new ArrayList<>();
		String marginString = Alignment.repeat(" ", margin);
		String paddingString = Alignment.repeat(" ", padding);
		for(Document d: getChildren()) childrenOut.addAll(d.toText());

		out.add(firstSep);

		if(header != null) {
			out.add(marginString + "|" + paddingString + Alignment.alignLeft(header, getLineWidth()) + paddingString + "|" + marginString);
			out.add(marginString + "|" + Alignment.repeat("¯", getLineWidth() + 2 * padding) + "|" + marginString);
		}

		for(int i = 0; i < childrenOut.size(); i++) {
			out.add(marginString + "|" + paddingString + childrenOut.get(i) + paddingString + "|" + marginString);
		}

		out.add(lastSep);

		return out;
	}
}
