package translating;

import com.sun.istack.internal.Nullable;
import parsing.parser.Tree;
import parsing.tokens.Token;
import translating.documents.*;

public class LaTxTTranslator {

	private Document current;

	public LaTxTTranslator() {}

	public void translate(Tree in) {
		switch(in.getValue()) {
			case "<S>":
				Tree startBlock = in.getChild(0);
				translateStart(startBlock.getChild(1), startBlock.getChild(2), startBlock.getChild(3));

				if(current instanceof MainDocument) {
					((MainDocument) current).export();
				}

				break;
			case "<STRING_FOLLOWUP>":
				Tree first = in.getChild(0);
				Tree options = first.getChildAmount() == 5? first.getChild(1): null;
				Tree args = first.getChildAmount() == 5? first.getChild(2): first.getChild(1);
				Tree content = first.getChildAmount() == 5? first.getChild(3): first.getChild(2);

				translateBlock(options, args, content);

				if(in.getChildAmount() > 1) translate(in.getChild(1));
				break;
			case "<EXPR>":
				translateExpression(in);
				break;
			case "<STRING_START>":
				translateStringStart(in);
				break;
			default:
				System.out.println(in.getValue());
				for(int i = 0; i < in.getChildAmount(); i++) {
					System.out.println("- " + i + " " + in.getChild(i).getValue());
				}
		}
	}

	public void translateStart(Tree options, Tree args, Tree inner) {
		String optionsString = ((Token.StringToken) ((Tree.Leaf) options.getChild(1)).getToken()).getString();
		String[] optionsSplit = splitString(optionsString);

		int lineWidth = 79;
		for(String s: optionsSplit) {
			if(s.trim().matches("[1-9][0-9]*")) lineWidth = Integer.parseInt(s.trim());
		}

		current = new MainDocument(lineWidth, optionsSplit);

		translate(inner);
	}

	public void translateExpression(Tree in) {
		Tree first = in.getChild(0);
		if(first instanceof Tree.Leaf) {
			String content = ((Token.StringToken) ((Tree.Leaf) first).getToken()).getString();
			StringDocument sd = new StringDocument(current, content);
			current.addChildren(sd);

			current = sd;
			if(in.getChildAmount() > 1 && in.getChild(1).getValue().equalsIgnoreCase("<string_start>")) translateStringStart(in.getChild(1));
			current = sd.getParent();

			if((in.getChildAmount() > 1 && in.getChild(1).getValue().equalsIgnoreCase("<string_followup>")) || (in.getChildAmount() > 2 && in.getChild(2).getValue().equalsIgnoreCase("<string_followup>"))) {
				Tree followUp = in.getChildAmount() == 2? in.getChild(1): in.getChild(2);
				translate(followUp);
			}
		} else {
			Tree.Leaf firstLeaf = (Tree.Leaf) first.getChild(0);
			if(firstLeaf.getToken().getName().equalsIgnoreCase("begin")) {
				Tree options = first.getChildAmount() == 5? first.getChild(1): null;
				Tree args = first.getChildAmount() == 5? first.getChild(2): first.getChild(1);
				Tree content = first.getChildAmount() == 5? first.getChild(3): first.getChild(2);

				translateBlock(options, args, content);
				if(in.getChildAmount() > 1) translate(in.getChild(1));
			} else if(first.getValue().equalsIgnoreCase("<CMD>")) {
				Tree cmd = first;

				Tree id = cmd.getChild(0);
				Tree options = null;
				Tree args = null;

				if(cmd.getChildAmount() == 2) {
					if(cmd.getChild(1).getValue().equalsIgnoreCase("<args>")) args = cmd.getChild(1);
					else options = cmd.getChild(1);
				} else if(cmd.getChildAmount() == 3) {
					args = cmd.getChild(2);
					options = cmd.getChild(1);
				}

				String content = Commands.translateCommand(this, current, id, options, args);
				StringDocument sd = new StringDocument(current, content);
				current.addChildren(sd);
				current = sd;

				if(in.getChildAmount() > 1 && in.getChild(1).getValue().equalsIgnoreCase("<string_start>")) translateStringStart(in.getChild(1));

				current = current.getParent();

				if((in.getChildAmount() > 1 && in.getChild(1).getValue().equalsIgnoreCase("<string_followup>")) || (in.getChildAmount() > 2 && in.getChild(2).getValue().equalsIgnoreCase("<string_followup>"))) {
					Tree followUp = in.getChildAmount() == 2? in.getChild(1): in.getChild(2);
					translate(followUp);
				}
			}else {
				translate(first);
			}
		}
	}

	public void translateBlock(@Nullable Tree options, Tree args, Tree content) {
		String type = ((Token.StringToken) ((Tree.Leaf) args.getChild(1)).getToken()).getString();

		if(type.equalsIgnoreCase("box")) translateBox(options, content);
		else if(type.equalsIgnoreCase("table")) translateTable(options, content);
		else if(type.equalsIgnoreCase("center")) translateAlign(Alignment.MIDDLE, content);
		else if(type.equalsIgnoreCase("left")) translateAlign(Alignment.LEFT, content);
		else if(type.equalsIgnoreCase("right")) translateAlign(Alignment.RIGHT, content);
		else if(type.equalsIgnoreCase("smallpage")) translateSmallPage(options, content);
		else if(type.equalsIgnoreCase("multipage")) translateMultipage(options, content);
		else if(type.equalsIgnoreCase("subpage")) translateSubpage(options, content);
		else if(type.equalsIgnoreCase("figure")) translateFigure(options, content);
		else if(type.equalsIgnoreCase("itemize")) translateItemize(options, content);
	}

	public void translateItemize(Tree options, Tree content) {
		String optionsString = options == null? null: ((Token.StringToken) ((Tree.Leaf) options.getChild(1)).getToken()).getString();
		String[] optionsArray = options == null? null: splitString(optionsString);

		ItemizeDocument id = new ItemizeDocument(current, optionsArray);
		current.addChildren(id);
		current = id;
		translate(content);
		current = id.getParent();
	}

	public void translateFigure(Tree options, Tree content) {
		FigureDocument fd = new FigureDocument(current);
		current.addChildren(fd);
		current = fd;
		translate(content);
		current = fd.getParent();
	}

	public void translateMultipage(Tree options, Tree content) {
		MultipageDocument mp = new MultipageDocument(current);
		current.addChildren(mp);
		current = mp;
		translate(content);
		current = mp.getParent();
	}

	public void translateSubpage(Tree options, Tree content) {
		SubpageDocument sp = new SubpageDocument(current);
		current.addChildren(sp);
		current = sp;
		translate(content);
		current = sp.getParent();
	}

	public void translateSmallPage(Tree options, Tree content) {
		String optionsString = ((Token.StringToken) ((Tree.Leaf) options.getChild(1)).getToken()).getString();
		String[] optionsSplit = splitString(optionsString);

		int lineWidth = current.getLineWidth();
		for(String s: optionsSplit) {
			if(s.trim().matches("[1-9][0-9]*")) lineWidth = Integer.parseInt(s.trim());
		}

		SmallPageDocument sm = new SmallPageDocument(current, lineWidth);
		current.addChildren(sm);
		current = sm;
		translate(content);
		current = sm.getParent();
	}

	public void translateAlign(Alignment alignment, Tree content) {
		AlignDocument alignDoc = new AlignDocument(current, alignment);
		current.addChildren(alignDoc);
		current = alignDoc;
		translate(content);
		current = alignDoc.getParent();
	}

	public void translateTable(Tree options, Tree content) {
		String optionsString = ((Token.StringToken) ((Tree.Leaf) options.getChild(1)).getToken()).getString();

		TableDocument tb = new TableDocument(current, optionsString);
		current.addChildren(tb);
		current = tb;
		translate(content);
		current = tb.getParent();
	}

	public void translateBox(@Nullable Tree options, Tree content) {
		int padding = -1;
		int margin = -1;
		String header = null;

		if(options != null) {
			String[] seperatedOptions = splitString(((Token.StringToken) ((Tree.Leaf) options.getChild(1)).getToken()).getString());

			for(String s: seperatedOptions) {
				String[] optionParts = s.split("=");
				String optionType = optionParts[0].trim();
				String val = optionParts[1].trim();

				if(optionType.equalsIgnoreCase("header")) header = val.substring(1, val.length()-1);
				else if(optionType.equalsIgnoreCase("padding")) padding = Integer.parseInt(val);
				else if(optionType.equalsIgnoreCase("margin")) margin = Integer.parseInt(val);
			}
		}

		BoxDocument bd = new BoxDocument(current, header, padding, margin);
		current.addChildren(bd);
		current = bd;
		translate(content);
		current = current.getParent();
	}

	public void translateStringStart(Tree in) {
		if(in.getChild(0) instanceof Tree.Leaf) {
			String line = ((Token.StringToken) ((Tree.Leaf) in.getChild(0)).getToken()).getString();
			((StringDocument) current).addContent(line);
		} else {
			Tree cmd = in.getChild(0);

			Tree id = cmd.getChild(0);
			Tree options = null;
			Tree args = null;

			if(cmd.getChildAmount() == 2) {
				if(cmd.getChild(1).getValue().equalsIgnoreCase("<args>")) args = cmd.getChild(1);
				else options = cmd.getChild(1);
			} else if(cmd.getChildAmount() == 3) {
				args = cmd.getChild(2);
				options = cmd.getChild(1);
			}

			((StringDocument) current).addContent(Commands.translateCommand(this, current, id, options, args));
		}

		if(in.getChildAmount() > 1) translateStringStart(in.getChild(1));
	}

	public static String[] splitString(String in) {
		return in.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
	}

	public void setCurrent(Document doc) {
		current = doc;
	}

}
