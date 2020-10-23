package translating.documents;

import com.sun.istack.internal.Nullable;
import translating.Alignment;

import java.util.ArrayList;
import java.util.List;

public class ItemizeDocument extends Document {

	private String itemMarker = "*";
	private String style = "standart";

	public ItemizeDocument(Document parent, @Nullable String[] optionsString) {
		super(parent, parent.getAlignment(), 0);

		if(optionsString != null) {
			for(String s: optionsString) {
				if(s.startsWith("point")) {
					itemMarker = s.split("=")[1].trim();
					itemMarker = itemMarker.substring(1, itemMarker.length()-1);
				} else if(s.trim().startsWith("style")) {
					style = s.split("=")[1].trim();
				}
			}

		}
	}

	@Override
	public int getLineWidth() {
		if(style.equalsIgnoreCase("enumerate")) {
			return getParent().getLineWidth() - 1 - ((int) (Math.log10(getChildrenCount()) + 1));
		} else {
			return getParent().getLineWidth() - 2 - itemMarker.length();
		}
	}

	@Override
	public List<String> toText() {
		List<String> out = new ArrayList<>();
		int maxNumbers = (int) (Math.log10(getChildrenCount()) + 1);


		for(int j = 0; j < getChildrenCount(); j++) {
			Document d = getChild(j);
			List<String> docText = d.toText();

			int currentNumberLength =  (int) (Math.log10(j+1) + 1);;

			if(style.equalsIgnoreCase("enumerate")) {
				out.add(" " + Alignment.repeat(" ", maxNumbers - currentNumberLength) + (j+1) + ")" + docText.get(0));
				for(int i = 1; i < docText.size(); i++) out.add(Alignment.repeat(" ", maxNumbers + 3) + docText.get(i));
			} else {
				out.add(" " + itemMarker + docText.get(0));
				for(int i = 1; i < docText.size(); i++) out.add("   " + docText.get(i));
			}
		}

		return out;
	}
}
