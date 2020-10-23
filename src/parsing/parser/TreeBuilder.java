package parsing.parser;

import parsing.fa.Automaton;
import javafx.util.Pair;
import parsing.tokens.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TreeBuilder {

	public static Tree toTree(List<Pair<String, String[]>> replacements, List<Token> tokenList) {
		Tree tree = new Tree(null, replacements.get(0).getValue().length, replacements.get(0).getKey(), true);

		for(int i = 1; i < replacements.size(); i++) {
			String replacer = replacements.get(i).getKey();
			String[] replacement = replacements.get(i).getValue();

			Tree current = depth(tree);

			Tree child = new Tree(current, replacement.length, replacer, true);
			if(replacement[0].equals("" + Automaton.eps)) {
				current.cut(null);
			}
			else {
				for(int j = 0; j < replacement.length; j++) {
					if(!replacement[j].startsWith("<")) {
						child.setChild(j, new Tree.Leaf(child, new Token(replacement[j])));
					}
				}

				current.addChild(child);
			}

			//current.addChild(child);
		}

		replaceTokens(tree, tokenList);
		return tree;
	}

	private static void replaceTokens(Tree in, List<Token> tokenList) {
		List<Tree.Leaf> leafs = new ArrayList<>();

		Stack<Tree> treeStack = new Stack<>();
		treeStack.push(in);

		while(!treeStack.isEmpty()) {
			Tree t = treeStack.pop();

			if(t instanceof Tree.Leaf) {
				leafs.add(0, (Tree.Leaf) t);
			}

			for(int i = 0; i < t.getChildAmount(); i++) {
				treeStack.push(t.getChild(i));
			}
		}

		for(int i = 0; i < leafs.size(); i++) {
			leafs.get(i).setToken(tokenList.get(i));
			leafs.get(i).getToken().getName();
		}
	}

	private static Tree depth(Tree in) {
		Tree out = in;
		while (out.getNonLeafAmount() > 0) {
			out = out.getLastChild();
		}

		while (out.getChildAmount() == out.getTotalChildAmount()) {
			out = out.getParent();
		}

		return out;
	}
}
