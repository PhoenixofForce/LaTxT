import parsing.parser.Parser;
import parsing.parser.Tree;
import parsing.parser.TreeBuilder;
import parsing.scanner.Scanner;
import parsing.tokens.Token;
import translating.LaTxTTranslator;

import java.util.List;

public class Latxt {

	public static void main(String[] args) {
		List<Token> tokenList = Scanner.scanFile(Token.loadTokenFile("res/token.tmap"), "res/test.in");
		System.out.println(tokenList);
		Tree t = TreeBuilder.toTree(Parser.parse("res/lang.gram", tokenList), tokenList);

		new LaTxTTranslator().translate(t);
	}

}
