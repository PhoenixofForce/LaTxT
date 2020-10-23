package parsing.scanner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import parsing.fa.Automaton;
import parsing.fa.AutomatonLoader;
import parsing.fa.State;
import parsing.tokens.Token;

public class Scanner {

	public static List<Token> scanFile(Map<String, String[]> tokenMap, String fileName) {
		return scanFile(tokenMap, new File(fileName));
	}
	
	public static List<Token> scanFile(Map<String, String[]> tokenMap, File f) {
		List<Token> out = new ArrayList<>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));

			String currentWord = "";
			String line = br.readLine();
			while(line != null) {
				currentWord += line + " \r\n";	//TODO: leerzeichen ja/ nein
				line = br.readLine();
			}

			out.addAll(scan(tokenMap, currentWord, true));
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return out;
	}

	public static List<Token> scan(Map<String, String[]> tokenMap, String in) {
		return scan(tokenMap, in, true);
	}

	private static List<Token> scan(Map<String, String[]> tokenMap, String in, boolean withEOF) {
		List<Token> tokenSequence = new ArrayList<>();
		Automaton tokenChecker = AutomatonLoader.loadFromFile("res/lang.dfa");
		String currentWord = "";
		
		for(int i = 0; i < in.length(); i++) {
			char current = in.charAt(i);
			currentWord += current;
			tokenChecker.input(current);
						
			if((i < in.length() - 1 && tokenChecker.softInput(in.charAt(i+1)).size() == 0) || i == in.length()-1) {	
				if(tokenChecker.getCurrentStates().size() == 0) {
					System.err.println("[SCANNER] Error occurred: undefined character sequence");
					System.err.println("\t" + in);
					System.err.println("\t" + in.substring(0, i) + "^");
					
					System.exit(-1);
				} 
				
				else {
					State state = tokenChecker.getCurrentStates().get(0);
					Token tokenToAdd = Token.state2Token(tokenMap, state, currentWord, tokenSequence.size() > 0? tokenSequence.get(tokenSequence.size()-1): null);
					
					if(tokenToAdd.getName().equalsIgnoreCase("ERROR")) {
						System.err.println("[SCANNER@83] Error occurred");
						System.err.println("\t" + in);
						System.err.println("\t" + in.substring(0, i) + "^");
						
						System.exit(-1);
					}	
					
					else if(!tokenToAdd.getName().equalsIgnoreCase("IGNORE")) {
						tokenSequence.add(tokenToAdd);
					}
				}
				
				tokenChecker.reset();
				currentWord = "";
			}
		}

		if(withEOF) tokenSequence.add(new Token("EOF"));
		return tokenSequence;
	}
}
