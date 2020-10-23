package translating.documents;

import translating.Alignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringDocument extends Document {

	private List<String> content;

	public StringDocument(Document parent, String in) {
		super(parent, parent.getAlignment(), 0);
		content = new ArrayList<>();

		this.content.add(in);
	}

	@Override
	public List<String> toText() {
		List<String> out = new ArrayList<>();

		String combinedContent = "";
		for(int i = 0; i < content.size(); i++) combinedContent += content.get(i);

		for(String s: combinedContent.split("\r\n")) {
			if(getLineWidth() > 0) out.addAll(lineToText(s));
			else out.add(s);
		}

		return out;
	}

	private List<String> lineToText(String in) {
		//if(in.length() < 1) return Arrays.asList("");
		if(in.length() <= getLineWidth()) return Arrays.asList(Alignment.align(getAlignment(), in, getLineWidth()));

		List<String> out = new ArrayList<>();

		int lineStart = 0;
		while(lineStart < in.length()) {
			int canonEnd = lineStart + getLineWidth();
			int end = Math.min(canonEnd, in.length());

			String line = in.substring(lineStart, end);

			if(end < in.length() && (Character.isLetterOrDigit(in.charAt(end)) || isPar(in.charAt(end)))) {
				while(Character.isLetterOrDigit(line.charAt(line.length() - 1)) || isPar(line.charAt(line.length() - 1))) {
					end--;
					line = in.substring(lineStart, end);
				}
			}

			String outStr = line;
			if(lineStart > 0) outStr = outStr.trim();
			out.add(Alignment.align(getAlignment(), outStr, getLineWidth()));

			lineStart += line.length();
		}

		return out;
	}

	public void addContent(String line) {
		content.add(line);
	}

	public static boolean isPar(char c) {
		return Arrays.asList('(', ')', '[', ']', '{', '}').contains(c);
	}
}
