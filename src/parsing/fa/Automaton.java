package parsing.fa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Automaton {

	public static final char eps = 'ε';

	private Map<String, State> states;
	private List<Transition> transitions;
	
	private List<State> startStates;
	private List<State> currentState;
	
	public Automaton() {
		this.states = new HashMap<>();
		this.transitions = new ArrayList<>();
		currentState = new ArrayList<>();
		startStates = new ArrayList<>();
	}
	
	public void addState(State t) {
		this.states.put(t.getName(), t);
		if(t.isStart()) {
			currentState.add(t);
			startStates.add(t);
		}
	}
	
	public void addTransition(String from, String transRegex, String to) {
		State stateFrom = states.get(from),
				stateTo = states.get(to);
		
		if(stateFrom == null || stateTo == null) {
			System.err.println("[DFA] Error while adding Transitions, a state is undefined");
			System.err.println("\t" + from + " " + transRegex + " " + to);
			System.exit(-1);
		}
		
		transitions.add(new Transition(stateFrom, transRegex, stateTo));
	}
	
	public void reset() {
		this.currentState = startStates;
	}
	
	public List<State> softInput(char e) {
		List<State> newStates = new ArrayList<>();
				
		for(State s: currentState) {
			List<State> transitionStates = transitions.stream().filter(t -> (t.from.equals(s) && ("" + e).matches(t.transRegex))).map(t -> t.to).collect(Collectors.toList());
			newStates.addAll(transitionStates);
		}
		
		for(int i = 0; i < newStates.size(); i++) {
			State s = newStates.get(i);
			List<State> transitionStates = transitions.stream().filter(t -> (t.from.equals(s) && t.transRegex.equals(eps + ""))).map(t->t.to).collect(Collectors.toList());
			newStates.addAll(transitionStates);
		}
				
		return newStates;
	}
	
	public boolean input(char e) {
		this.currentState = softInput(e);
		
		return currentState.stream().anyMatch(State::isEnd);
	}
	
	public boolean input(String e) {
		for(int i = 0; i < e.length(); i++) {
			input(e.charAt(i));
		}
		
		return currentState.stream().anyMatch(State::isEnd);
	}
	
	public List<State> getCurrentStates() {
		return currentState;
	}
	
	public List<State> getHoldingStates() {
		return currentState.stream().filter(State::isEnd).collect(Collectors.toList());
	}

	private static class Transition {
		public State from, to;
		public String transRegex;
		
		public Transition(State from, String transRegex, State to) {
			this.from = from;
			this.transRegex = transRegex;
			this.to = to;
		}
		
	}
}
