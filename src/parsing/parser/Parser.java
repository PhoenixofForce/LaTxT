package parsing.parser;

import parsing.fa.Automaton;
import javafx.util.Pair;
import parsing.tokens.Token;

import java.io.*;
import java.util.*;

public class Parser {

	//TODO: add dfa selection to parsing.scanner, beautify parsing.parser calls

	private static Map<String, ParsingTable> parsingTables = new HashMap<>();

	public static List<Pair<String, String[]>> parse(String gram, List<Token> tokenList) {
		return parse(new File(gram), tokenList);
	}

	public static List<Pair<String, String[]>> parse(File gram, List<Token> tokenList) {
		Map<String, String[][]> replacements = analyzeGrammar(gram);
		List<Pair<String, String[]>> out = new ArrayList<>();

		ParsingTable pt = parsingTables.get(gram.getAbsolutePath());
		if(pt == null) {
			pt = new ParsingTable(replacements);
			parsingTables.put(gram.getAbsolutePath(), pt);
		}

		Stack<String> tokenStack = new Stack<>();
		tokenStack.push("<S>");

		int pos = 0;
		while(!tokenStack.isEmpty()) {
			String stackToken = tokenStack.pop();
			Token lookahead = tokenList.get(pos);

			if(!(stackToken.startsWith("<") && stackToken.endsWith(">"))) {
				if(stackToken.equals(lookahead.getName())) {
					pos++;
					if(stackToken.equals("EOF")) break;
				}
				else {
					System.err.println("[PARSER@L49] Error occurred");
					System.err.println("\t" + "S: " + Arrays.toString(tokenList.toArray()) + " T: " + stackToken + " L: " + lookahead.getName());
				}
			}

			else {
				String[] rule = pt.getNextState(stackToken, lookahead.getName());

				if(rule == null) {
					System.err.println("[PARSER] Cant find fitting rule for ");
					System.err.println("\t" + stackToken + " " + lookahead.getName());
					System.exit(-1);
				}

				out.add(new Pair<>(stackToken, rule));

				for(int i = rule.length - 1; i >= 0; i--) {
					if(!rule[i].equals(Automaton.eps + "")) tokenStack.push(rule[i]);
				}
			}
		}

		System.out.println("PARSING COMPLETE");
		return out;
	}

	//>   ANALYZE GRAMMAR

	public static Map<String, String[][]> analyzeGrammar(File gram) {
		Map<String, String[][]> replacements = new HashMap<>();

		try {
			BufferedReader r = new BufferedReader(new FileReader(gram));

			int lineCount = 1;
			String line = r.readLine();
			while(line != null) {
				if(line.length() == 0 || line.matches("[ \\t]]*") || line.startsWith("#")) {
					line = r.readLine();
					lineCount++;
					continue;
				}

				if(line.contains("::=") && line.startsWith("<")) {
					String toBeReplaced = line.split("::=")[0].trim();
					String replacementString = line.split("::=")[1];
					int replacementCount = replacementString.length() - replacementString.replace("|", "").length() + 1;

					String[][] currentReplacements = new String[replacementCount][];

					for(int i = 0; i < replacementCount; i++) {
						String currentReplacement = replacementString.split("\\|")[i];
						currentReplacements[i] = currentReplacement.trim().split(" ");
					}

					for(int j = 0; j < currentReplacements.length; j++) {
						for(int i = 0; i < currentReplacements[j].length; i++) {
							if(currentReplacements[j][0].length() == 0) {
								System.err.println("[PARSER] Right side contains empty production at Line " + lineCount);
								System.err.println("\t" + line);
								System.exit(-1);
							}
						}

						for(int i = j + 1; i < currentReplacements.length; i++) {
							if(currentReplacements[i][0].equals(currentReplacements[j][0])) {
								System.err.println("[PARSER] Grammar has first first conflict L" + lineCount);
								System.err.println("\t" + line);
								System.exit(-1);
							}
						}

						if(toBeReplaced.equals(currentReplacements[j][0])) {
							System.err.println("[PARSER] Grammar contains left recursion at Line " + lineCount);
							System.err.println("\t" + line);
							System.exit(-1);
						}
					}

					replacements.put(toBeReplaced, currentReplacements);
				} else {
					System.err.println("[PARSER] Grammar is not formatted properly at Line " + lineCount);
					System.err.println("\t" + line);
					System.exit(-1);
				}

				line = r.readLine();
				lineCount++;
			}

			r.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return replacements;
	}
}
