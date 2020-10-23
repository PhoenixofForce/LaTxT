package parsing.parser;

import parsing.fa.Automaton;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class ParsingTable {

	private Map<Pair<String, String>, String[]> parsingTable;

	ParsingTable(Map<String, String[][]> replacements) {
		generateTable(replacements);
	}

	private void generateTable(Map<String, String[][]> allReplacements) {
		Map<String, List<String>> firsts = generateFirsts(allReplacements);
		Map<String, List<String>> follows = generateFollows(allReplacements, firsts);
		
		parsingTable = new HashMap<>();

		for(Map.Entry<String, String[][]> e: allReplacements.entrySet()) {
			String replacer = e.getKey();
			String[][] replacements = e.getValue();

			for(String[] replacement: replacements) {
				if(!replacement[0].equals(Automaton.eps + "")) {
					List<String> first = firsts.get(replacement[0]);
					if(!replacement[0].startsWith("<")) first = Collections.singletonList(replacement[0]);

					for(String s: first) {
						parsingTable.put(new Pair<>(replacer, s), replacement);
					}
				}
			}

			for(String[] replacement: replacements) {
				if(replacement[0].equals(Automaton.eps + "")) {
					List<String> follow = follows.get(replacer);

					for(String s: follow) {
						if(parsingTable.get(new Pair<>(replacer, s)) == null) parsingTable.put(new Pair<>(replacer, s), replacement);
						else {
							System.err.println("[TABLE] Grammar has first follow conflict for " + replacer + " " + s);
						}
					}
				}
			}
		}
	}

	private Map<String, List<String>> generateFirsts(Map<String, String[][]> allReplacements) {
		Map<String, List<String>> out = new TreeMap<>();

		allReplacements.keySet().forEach(k -> out.put(k, new ArrayList<>()));

		boolean change = true;
		while(change) {
			change = false;

			for(String k: out.keySet()) {
				List<String> firsts = out.get(k);
				List<String> toAdd = new ArrayList<>();

				String[][] replacements = allReplacements.get(k);
				for(String[] rule: replacements) {
					if(rule[0].startsWith("<")) {
						int eps = 1;
						for(int j = 0; j < Math.min(eps, rule.length); j++) {
							List<String> currentFirsts = new ArrayList<>();
							if(rule[j].startsWith("<")) currentFirsts.addAll(new ArrayList<>(out.get(rule[j])));
							else currentFirsts.add(rule[j]);

							if(currentFirsts.contains(Automaton.eps + "")) {
								currentFirsts.remove(Automaton.eps + "");

								eps++;
							}
							else eps = -5;

							toAdd.addAll(currentFirsts);
						}

						if(eps >= rule.length-1) toAdd.add(Automaton.eps + "");
					} else {
						toAdd.add(rule[0]);
					}
				}

				for(String s: toAdd) {
					if(!firsts.contains(s)) {
						change = true;
						firsts.add(s + "");
					}
				}
			}
		}

		//Error Check
		for(String s: allReplacements.keySet()) {
			for(int i = 0; i < allReplacements.get(s).length; i++) {
				String[] firstReplacement = allReplacements.get(s)[i];
				List<String> firstFirsts = new ArrayList<>();
				{
					boolean allEps = true;
					for(int k = 0; k < firstReplacement.length; k++) {
						if(allEps) {
							if(firstReplacement[k].startsWith("<")) {
								List<String> currentFirsts = out.get(firstReplacement[k]);
								firstFirsts.addAll(currentFirsts);

								if(!currentFirsts.contains(Automaton.eps + "")) allEps = false;
							} else {
								firstFirsts.add(firstReplacement[k]);
								allEps = false;
							}
						}
					}
				}

				for(int j = 0; j < i; j++) {
					String[] secondReplacement = allReplacements.get(s)[j];
					List<String> secondFirsts = new ArrayList<>();
					{
						boolean allEps = true;
						for(int k = 0; k < secondReplacement.length; k++) {
							if(allEps) {
								if(secondReplacement[k].startsWith("<")) {
									List<String> currentFirsts = out.get(secondReplacement[k]);
									secondFirsts.addAll(currentFirsts);

									if(!currentFirsts.contains(Automaton.eps + "")) allEps = false;
								} else {
									secondFirsts.add(secondReplacement[k]);
									allEps = false;
								}
							}
						}
					}

					List<String> errorList = firstFirsts.stream().distinct().filter(secondFirsts::contains).collect(Collectors.toList());
					if(!errorList.isEmpty()) {
						System.err.println("[TABLE] Grammar Rules have common Firsts for");
						System.err.println("\t" + s + " -> " + Arrays.toString(firstReplacement).replaceAll("[\\[\\],]", ""));
						System.err.println("\t" + s + " -> " + Arrays.toString(secondReplacement).replaceAll("[\\[\\],]", ""));
						System.err.println("\t" + errorList);
					}
				}
			}
		}

		return out;
	}

	private Map<String, List<String>> generateFollows(Map<String, String[][]> allReplacements, Map<String, List<String>> firsts) {
		Map<String, List<String>> out = new HashMap<>();

		allReplacements.keySet().forEach(k -> out.put(k, new ArrayList<>()));

		boolean change = true;
		while(change) {
			change = false;

			for(String k: out.keySet()) {
				List<String> follows = out.get(k);
				List<String> toAdd = new ArrayList<>();

				if(k.equals("<S>")) toAdd.add("EOF");

				for(Map.Entry<String, String[][]> e: allReplacements.entrySet()) {
					String replacer = e.getKey();
					String[][] replacements = e.getValue();

					for(String[] rule: replacements) {
						if (Arrays.asList(rule).contains(k)) {
							boolean foundK = false;
							boolean allEps = true;

							for (int i = 0; i < rule.length; i++) {
								if (rule[i].equals(k) && !foundK) foundK = true;
								else if (foundK) {
									if(allEps || rule[i-1].equals(k)) {
										if(rule[i].startsWith("<")) {
											List<String> currentFirsts = new ArrayList<>(firsts.get(rule[i]));
											currentFirsts.remove(Automaton.eps + "");
											toAdd.addAll(currentFirsts);
										} else {
											toAdd.add(rule[i]);
										}
									}

									if (!rule[i].startsWith("<") || !firsts.get(rule[i]).contains(Automaton.eps + "")) {
										allEps = false;
									}
								}
							}

							if (rule[rule.length - 1].equals(k) || allEps) {
								toAdd.addAll(out.get(replacer));
							}
						}
					}
				}

				for(String s: toAdd) {
					if(!follows.contains(s)) {
						follows.add(s);
						change = true;
					}
				}
			}
		}

		return out;
	}

	String[] getNextState(String s, String t) {
		return parsingTable.get(new Pair<>(s, t));
	}

}
