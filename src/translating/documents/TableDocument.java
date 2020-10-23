package translating.documents;

import translating.Alignment;

import java.util.ArrayList;
import java.util.List;

public class TableDocument extends Document {

	private boolean[] vlines;
	private Alignment[] alignments;
	private int padding = 1;

	public TableDocument(Document parent, String options) {
		super(parent, parent.getAlignment(), 0);
		options = options.replaceAll("\\|+", "|");

		int totalLines = (int) options.chars().filter(ch -> ch == '|').count();
		alignments = new Alignment[options.length() - totalLines];
		vlines = new boolean[alignments.length+1];

		int linesFound = 0;
		int alignmentsFound = 0;
		for(int i = 0; i < options.length(); i++) {
			char c = options.charAt(i);

			if(c == '|') {
				vlines[i - alignmentsFound] = true;
				linesFound++;
			} else {
				alignments[i - linesFound] = c == 'l'? Alignment.LEFT: (c == 'r'? Alignment.RIGHT: Alignment.MIDDLE);
				if(linesFound > 0) alignmentsFound++;
			}
		}
	}

	@Override
	public List<String> toText() {
		List<String> out = new ArrayList<>();

		List<String[]> lines = new ArrayList<>();
		List<Integer> columnLengths = new ArrayList<>();

		for(Document d: getChildren()) {
			for(String s: d.toText()) {
				if(s.trim().matches("-+")) {
					lines.add(new String[]{s.trim()});
				}
				else {
					String[] line = s.replaceAll("\\\\&", "▄").split("&");	//TODO: find better solution
					for(int i = 0; i < line.length; i++) {
						line[i] = line[i].replaceAll("▄", "&");
					}
					lines.add(line);

					for(int i = 0; i < line.length; i++) {
						if(i == columnLengths.size()) columnLengths.add(line[i].trim().length());
						else if(line[i].trim().length() > columnLengths.get(i)) columnLengths.set(i, line[i].trim().length());
					}
				}
			}
		}

		int lineLength = columnLengths.stream().mapToInt(Integer::intValue).sum();
		for(int i = 0; i < vlines.length; i++) {
			if(vlines[i]) {
				lineLength += 1 + 2 * padding;
				if(i == 0 || i == vlines.length - 1) lineLength -= padding;
			} else {
				lineLength += padding;
			}
		}

		for(String[] line: lines) {
			String lineOut = "";

			if(line.length == 1 && line[0].matches("-+")) lineOut = Alignment.repeat("-", lineLength-1);
			else {
				for(int i = 0; i < alignments.length; i++) {
					if(vlines[i]) lineOut += Alignment.repeat(" ", i == 0? 0: padding) + "|" + Alignment.repeat(" ", padding);
					else lineOut += Alignment.repeat(" ", padding);

					lineOut += Alignment.align(alignments[i], i < line.length? line[i].trim(): "", columnLengths.get(i));
				}

				if(vlines[vlines.length - 1]) lineOut += Alignment.repeat(" ", padding) + "|";
			}


			out.add(Alignment.align(getAlignment() == Alignment.UNDEFINED? Alignment.MIDDLE: getAlignment(), lineOut, getLineWidth()));
		}

		//for(int i = 0; i < out.size(); i++) out.set(i, out.get(i).substring(0, getLineWidth()));

		return out;
	}
}
