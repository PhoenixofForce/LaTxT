package translating;

import com.sun.istack.internal.Nullable;
import parsing.parser.Tree;
import parsing.tokens.Token;
import translating.documents.*;
import translating.documents.figures.*;

import java.util.Arrays;

public class Commands {

	//TODO: Translate Commands at Export Time
	private static char[] normalChar = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '(', ')', '+', '-'};
	private static String[] boldChar = new String[]{"\uD835\uDC00", "\uD835\uDC01", "\uD835\uDC02", "\uD835\uDC03", "\uD835\uDC04", "\uD835\uDC05", "\uD835\uDC06", "\uD835\uDC07", "\uD835\uDC08", "\uD835\uDC09", "\uD835\uDC0A", "\uD835\uDC0B", "\uD835\uDC0C", "\uD835\uDC0D", "\uD835\uDC0E", "\uD835\uDC0F", "\uD835\uDC10", "\uD835\uDC11", "\uD835\uDC12", "\uD835\uDC13", "\uD835\uDC14", "\uD835\uDC15", "\uD835\uDC16", "\uD835\uDC17", "\uD835\uDC18", "\uD835\uDC19", "\uD835\uDC1A", "\uD835\uDC1B", "\uD835\uDC1C", "\uD835\uDC1D", "\uD835\uDC1E", "\uD835\uDC1F", "\uD835\uDC20", "\uD835\uDC21", "\uD835\uDC22", "\uD835\uDC23", "\uD835\uDC24", "\uD835\uDC25", "\uD835\uDC26", "\uD835\uDC27", "\uD835\uDC28", "\uD835\uDC29", "\uD835\uDC2A", "\uD835\uDC2B", "\uD835\uDC2C", "\uD835\uDC2D", "\uD835\uDC2E", "\uD835\uDC2F", "\uD835\uDC30", "\uD835\uDC31", "\uD835\uDC32", "\uD835\uDC33", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "(", ")", "+", "-"};
	private static String[] italicChar = new String[]{"\uD835\uDC34", "\uD835\uDC35", "\uD835\uDC36", "\uD835\uDC37", "\uD835\uDC38", "\uD835\uDC39", "\uD835\uDC3A", "\uD835\uDC3B", "\uD835\uDC3C", "\uD835\uDC3D", "\uD835\uDC3E", "\uD835\uDC3F", "\uD835\uDC40", "\uD835\uDC41", "\uD835\uDC42", "\uD835\uDC43", "\uD835\uDC44", "\uD835\uDC45", "\uD835\uDC46", "\uD835\uDC47", "\uD835\uDC48", "\uD835\uDC49", "\uD835\uDC4A", "\uD835\uDC4B", "\uD835\uDC4C", "\uD835\uDC4D", "\uD835\uDC4E", "\uD835\uDC4F", "\uD835\uDC50", "\uD835\uDC51", "\uD835\uDC52", "\uD835\uDC53", "\uD835\uDC54", "h", "\uD835\uDC56", "\uD835\uDC57", "\uD835\uDC58", "\uD835\uDC59", "\uD835\uDC5A", "\uD835\uDC5B", "\uD835\uDC5C", "\uD835\uDC5D", "\uD835\uDC5E", "\uD835\uDC5F", "\uD835\uDC60", "\uD835\uDC61", "\uD835\uDC62", "\uD835\uDC63", "\uD835\uDC64", "\uD835\uDC65", "\uD835\uDC66", "\uD835\uDC67", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "(", ")", "+", "-"};
	private static String[] boldItalicChar = new String[]{"𝑨", "𝑩", "𝑪", "𝑫", "𝑬", "𝑭", "𝑮", "𝑯", "𝑰", "𝑱", "𝑲", "𝑳", "𝑴", "𝑵", "𝑶", "𝑷", "𝑸", "𝑹", "𝑺", "𝑻", "𝑼", "𝑽", "𝑾", "𝑿", "𝒀", "𝒁", "𝒂", "𝒃", "𝒄", "𝒅", "𝒆", "𝒇", "𝒈", "𝒉", "𝒊", "𝒋", "𝒌", "𝒍", "𝒎", "𝒏", "𝒐", "𝒑", "𝒒", "𝒓", "𝒔", "𝒕", "𝒖", "𝒗", "𝒘", "𝒙", "𝒚", "𝒛", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "(", ")", "+", "-"};
	private static String[] supChar = new String[]{"\u1d2c", "\u1d2e", "C", "\u1d30", "\u1d31", "F", "\u1d33", "\u1d34", "\u1d35", "\u1d36", "\u1d37", "\u1d38", "\u1d39", "\u1d3a", "\u1d3c", "\u1d3e", "Q", "\u1d3f", "S", "\u1d40", "\u1d41", "\u2c7d", "\u1d42", "X", "Y", "Z", "\u1d43", "\u1d47", "\u1d9c", "\u1d48", "\u1d49", "\u1da0", "\u1d4d", "\u02b0", "\u2071", "\u02b2", "\u1d4f", "\u02e1", "\u1d50", "\u207f", "\u1d52", "\u1d56", "q", "\u02b3", "\u02e2", "\u1d57", "\u1d58", "\u1d5b", "\u02b7", "\u02e3", "\u02b8", "z", "\u2070", "\u00B9", "\u00B2", "\u00B3", "\u2074", "\u2075", "\u2076", "\u2077", "\u2078", "\u2079", "\u207D", "\u207E", "\u207A", "\u207B"};
	private static String[] subChar = new String[]{"A" , "B" , "C" , "D" , "E" , "F" , "G" , "H" , "I" , "J" , "K" , "L" , "M" , "N" , "O" , "P" , "Q" , "R" , "S" , "T" , "U" , "V" , "W" , "Y" , "Y" , "Z" , "\u2090" , "b" , "c" , "d" , "\u2091" , "f" , "g" , "\u2095" , "\u1d62" , "\u2c7c" , "\u2096" , "\u2097" , "\u2098" , "\u2099" , "\u2092" , "\u209a" , "q" , "\u1d63" , "\u209b" , "\u209c" , "\u1d64" , "\u1d65" , "w" , "\u2093" , "y" , "z" , "\u2080" , "\u2081" , "\u2082" , "\u2083" , "\u2084" , "\u2085" , "\u2086" , "\u2087" , "\u2088" , "\u2089" , "\u208D" , "\u208E" , "\u208A" , "\u208B"};

	public static String translateCommand(LaTxTTranslator translator, Document current, Tree id, @Nullable Tree options, @Nullable Tree args) {
		String commandName = ((Token.IDToken) ((Tree.Leaf) id).getToken()).getID().substring(1);
		String argsString = args == null? null: ((Token.StringToken) ((Tree.Leaf) args.getChild(1)).getToken()).getString();

		if(commandName.matches("(sub)*section")) return translateSection(current, commandName, argsString);

		switch(commandName) {
			case "Alpha": return "Α";
			case "Beta": return "Β";
			case "Gamma": return "Γ";
			case "Delta": return "Δ";
			case "Epsilon": return "Ε";
			case "Zeta": return "Ζ";
			case "Eta": return "Η";
			case "Theta": return "Θ";
			case "Iota": return "Ι";
			case "Kappa": return "Κ";
			case "Lambda": return "Λ";
			case "Mu": return "Μ";
			case "Nu": return "Ν";
			case "Xi": return "Ξ";
			case "Omicron": return "Ο";
			case "Pi": return "Π";
			case "Rho": return "Ρ";
			case "Sigma": return "Σ";
			case "Tau": return "Τ";
			case "Upsilon": return "Υ";
			case "Phi": return "Φ";
			case "Chi": return "Χ";
			case "Psi": return "Ψ";
			case "Omega": return "Ω";
			case "alpha": return "α";
			case "beta": return "β";
			case "gamma": return "γ";
			case "delta": return "δ";
			case "epsilon": return "ε";
			case "zeta": return "ζ";
			case "eta": return "η";
			case "theta": return "ϑ";
			case "iota": return "ι";
			case "kappa": return "κ";
			case "lambda": return "λ";
			case "mu": return "μ";
			case "nu": return "ν";
			case "xi": return "ξ";
			case "omicron": return "ο";
			case "pi": return "π";
			case "rho": return "ρ";
			case "sigma": return "σ";
			case "tau": return "τ";
			case "upsilon": return "υ";
			case "phi": return "φ";
			case "chi": return "χ";
			case "psi": return "ψ";
			case "omega": return "ω";
			case "forall": return "∀";
			case "exists": return "∃";
			case "nexists": return "∄";
			case "varnothing": return "∅";
			case "triangle": return "∆";
			case "triangledown": return "∇";
			case "in": return "∈";
			case "notin": return "∉";
			case "ni": return "∋";
			case "notni": return "∌";
			case "sum": return "∑";
			case "prod": return "∏";
			case "mp": return "∓";
			case "sqrt": return "√";
			case "infty": return "∞";
			case "angle": return "∠";
			case "sphericalangle": return "∢";
			case "nmid": return "∤";
			case "wedge": return "∧";
			case "vee": return "∨";
			case "cap": return "∩";
			case "cup": return "∪";
			case "integral": return "∫";
			case "neq": return "≠";
			case "le": return "≤";
			case "ge": return "≥";
			case "subset": return "⊂";
			case "supset": return "⊃";
			case "nsubset": return "⊄";
			case "nsupset": return "⊅";
			case "subseteq": return "⊆";
			case "supseteq": return "⊇";
			case "nsubseteq": return "⊈";
			case "nsupseteq": return "⊉";
			case "subsetneq": return "⊊";
			case "supsetneq": return "⊋";
			case "pm": return "±";
			case "times": return "⨯";
			case "circ": return "∘";
			case "not": return "⌐";
			case "lfloor": return "⌊";
			case "rfloor": return "⌋";
			case "lceil": return "⌈";
			case "rceil": return "⌉";
			case "leftarrow": return "←";
			case "rightarrow": return "→";
			case "uparrow": return "↑";
			case "downarrow": return "↓";
			case "leftrightarrow": return "↔";
			case "updownarrow": return "↕";
			case "Leftarrow": return "⇐";
			case "Rightarrow": return "⇒";
			case "Uparrow": return "⇑";
			case "Downarrow": return "⇓";
			case "Leftrightarrow": return "⇔";
			case "Updownarrow": return "⇕";
			case "lbrace": return "{";
			case "rbrace": return "}";
			case "lbracket": return "[";
			case "rbracket": return "]";
			case "hline": return Alignment.repeat("-", current.getLineWidth()-2);
			case "newline": return" \r\n";
			case "empty": return "";
			case "textbf": return boldText(argsString);
			case "textit": return italicText(argsString);
			case "textitbf":
			case "textbfit": return boldItalicText(argsString);
			case "item":
				if(!(current instanceof ItemizeDocument)) {
					Document d = current.getParent();
					StringDocument sd = new StringDocument(d, "");
					d.addChildren(sd);
					translator.setCurrent(sd);
				}
				return "";
			case "string":
			case "box":
			case "arrow":
			case "line":
				return translateFigure(current, commandName, options, argsString);
			default: return commandName;
		}
	}

	public static String boldText(String in) {
		String out = "";

		for(char c: in.toCharArray()) {
			boolean found = false;
			for(int i = 0; i < normalChar.length; i++) {
				if(c == normalChar[i]) {
					found = true;
					out += boldChar[i];
				}
			}
			if(!found) out += c;
		}

		return out;
	}

	public static String italicText(String in) {
		String out = "";

		for(char c: in.toCharArray()) {
			boolean found = false;
			for(int i = 0; i < normalChar.length; i++) {
				if(c == normalChar[i]) {
					found = true;
					out += italicChar[i];
				}
			}
			if(!found) out += c;
		}

		return out;
	}

	public static String boldItalicText(String in) {
		String out = "";

		for(char c: in.toCharArray()) {
			boolean found = false;
			for(int i = 0; i < normalChar.length; i++) {
				if(c == normalChar[i]) {
					found = true;
					out += boldItalicChar[i];
				}
			}
			if(!found) out += c;
		}

		return out;
	}

	public static String translateFigure(Document current, String commandLine, Tree options, String argsString) {

		while(!(current instanceof FigureDocument) && current.getParent() != null) current = current.getParent();

		if(current instanceof FigureDocument) {
			FigureDocument fd = (FigureDocument) current;

			if(commandLine.equals("line")) {
				String[] args = LaTxTTranslator.splitString(argsString);
				int[] pos = Arrays.stream(args).mapToInt(s -> Integer.parseInt(s.trim())).toArray();
				fd.addFigure(new LineFigure(pos[0], pos[1], pos[2], pos[3]));
			} else if(commandLine.equals("arrow")) {
				String[] args = LaTxTTranslator.splitString(argsString);
				int[] pos = Arrays.stream(args).mapToInt(s -> Integer.parseInt(s.trim())).toArray();
				fd.addFigure(new ArrowFigure(pos[0], pos[1], pos[2], pos[3]));
			} else if(commandLine.equals("string")) {
				String[] args = LaTxTTranslator.splitString(argsString);
				int x = Integer.parseInt(args[0].trim());
				int y = Integer.parseInt(args[1].trim());
				String text = args[2].trim();

				fd.addFigure(new StringFigure(x, y, text));
			} else if(commandLine.equals("box")) {
				String[] args = LaTxTTranslator.splitString(argsString);
				int[] pos = Arrays.stream(args).mapToInt(s -> Integer.parseInt(s.trim())).toArray();
				fd.addFigure(new BoxFigure(pos[0], pos[1], pos[2], pos[3]));
			}
		}

		return "";
	}


	public static String translateSection(Document current, String command, String args) {
		MainDocument main = current.getMain();
		int level = getCount(command, "sub");
		Section s = main.addSection(args, level);

		String firstLine = " " + Alignment.repeat("_", 5 + s.getName().length() + s.getCode().length());
		firstLine = Alignment.alignLeft(firstLine, current.getLineWidth()) + "\r\n";

		String secondLine = "| " + s.getCode() + " | " + s.getName() + " \\";
		secondLine += Alignment.repeat("_", current.getLineWidth() - secondLine.length()) + "\r\n";

		String thirdLine = " " + Alignment.repeat("¯", current.getLineWidth() - 1) + "\r\n";

		String out = firstLine + secondLine + thirdLine;
		return out;
	}

	public static int getCount(String str, String findStr) {
		int lastIndex = 0;
		int count = 0;

		while(lastIndex != -1){

			lastIndex = str.indexOf(findStr,lastIndex);

			if(lastIndex != -1){
				count ++;
				lastIndex += findStr.length();
			}
		}

		return count;
	}

}
