package parsing.tokens;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sun.xml.internal.bind.v2.model.core.ID;
import parsing.fa.State;

public class Token {
						
	private String name;
	public Token(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override 
	public String toString() {
		return name;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	public static class IntToken extends Token {
		private int num;
		public IntToken(int number) {
			super("INT");
			num = number;
		}

		public int getInt() {
			return num;
		}
	}
	
	public static class DoubleToken extends Token {
		private double num;
		public DoubleToken(double number) {
			super("DOUBLE");
			this.num = number;
		}

		public double getDouble() {
			return num;
		}
	}
	
	public static class FloatToken extends Token {
		private float num;
		public FloatToken(float number) {
			super("FLOAT");
			this.num = number;
		}

		public float getFloat() {
			return num;
		}
	}
	
	public static class LongToken extends Token {
		private long num;
		public LongToken(long number) {
			super("LONG)");
			this.num = number;
		}

		public long getLong() {
			return num;
		}
	}
	
	public static class ShortToken extends Token {
		private short num;
		public ShortToken(short number) {
			super("SHORT)");
			this.num = number;
		}

		public short getShort() {
			return num;
		}
	}
	
	public static class StringToken extends Token {
		private String text;
		public StringToken(String text) {
			super("STR");
			this.text = text;
		}

		@Override
		public String toString() {
			return getName() + "(" + text.replaceAll("[\r\n]", " \\\\r\\\\n ") + ")";
		}

		public String getString() {
			return text;
		}
	}
	
	public static class IDToken extends Token {
		private String id;
		public IDToken(String text) {
			super("ID");
			this.id = text;
		}

		public String getID() {
			return id;
		}
	}
	
	public static Map<String, String[]> loadTokenFile(String fileName) {
		return loadTokenFile(new File(fileName));
	}
	
	public static Map<String, String[]> loadTokenFile(File f) {
		Map<String, String[]> tokenMap = new HashMap<>();
		
		try {
			BufferedReader r = new BufferedReader(new FileReader(f));
			
			String line = r.readLine();
			while(line != null) {
				String[] args = line.split(" ");
				String state = args[0].trim();
				String type = args[1].trim();
				String param = args[2].trim();
				
				tokenMap.put(state, new String[]{type, param});
				
				line = r.readLine();
			}
			
			r.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return tokenMap;
	}
	
	public static Token state2Token(Map<String, String[]> tokenMap, State state, String currentWord, Token lastToken) {
		Token out = null;
		
		String[] args = tokenMap.get(state.getName());
		if(args == null) args = tokenMap.get("default");
		if(args == null) {
			System.err.println("[TOKEN] State has no Token specified, and default not available: " + state.getName());
			System.exit(-1);
		}
		
		String type = args[0];
		String param = args[1];
		
		if(type.equalsIgnoreCase("normal")) {
			out = new Token(param);
		}
		else if(type.equalsIgnoreCase("ID")) {
			out = new IDToken(currentWord);
		}
		else if(type.equalsIgnoreCase("Integer")) {
			out = new IntToken(Integer.parseInt(currentWord));
		}
		else if(type.equalsIgnoreCase("Double")) {
			out = new DoubleToken(Double.parseDouble(currentWord));
		}
		else if(type.equalsIgnoreCase("Float")) {
			out = new FloatToken(Float.parseFloat(currentWord));
		}
		else if(type.equalsIgnoreCase("Long")) {
			out = new LongToken(Long.parseLong(currentWord));
		}
		else if(type.equalsIgnoreCase("Short")) {
			out = new ShortToken(Short.parseShort(currentWord));
		}
		else if(type.equalsIgnoreCase("String")) {
			if(currentWord.equalsIgnoreCase("\r\n") && !(lastToken != null && (lastToken instanceof IDToken))) {
				out = new Token("ignore");
			} else {
				out = new StringToken(currentWord);
			}
		} 
		else {
			System.err.println("[TOKEN] Cant find token type: " + type);
			System.err.println("\tExpected: [Normal, ID, Number, String]");
			System.exit(-1);
		}
		
		return out;
	}
}