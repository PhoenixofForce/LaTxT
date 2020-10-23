package parsing.fa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AutomatonLoader {
	
	public static Automaton loadFromFile(String fileName) {
		return loadFromFile(new File(fileName));
	}
	
	public static Automaton loadFromFile(File file) {
		Automaton out = new Automaton();

		try {
			BufferedReader r = new BufferedReader(new FileReader(file));

			String line = r.readLine();
			int state = 0;

			while (line != null) {
				if (line.equals("states")) {
					state = 1;
				} else if (line.startsWith("#")) {
					line = r.readLine();
					continue;
				} else if (line.equals("transitions")) {
					state = 2;
				} else if (state == 1) {
					String name = line.contains("-") ? line.split("-")[0].trim() : line;

					boolean isStart = line.contains("-") && line.split("-")[1].contains("s");
					boolean isEnd = line.contains("-") && line.split("-")[1].contains("e");

					out.addState(new State(name, isStart, isEnd));
				} else if (state == 2 && line.split(" ").length > 2) {
					out.addTransition(line.split(" ")[0], line.split(" ")[1], line.split(" ")[2]);
				}

				line = r.readLine();
			}

			r.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return out;
	}
}