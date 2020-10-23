package parsing.fa;

public class State {

	private String name;
	private boolean end, start;
	
	public State(String name) {
		this(name, false, false);
	}
	
	public State(String name, boolean isStart, boolean isEnd) {
		this.name = name;
		this.start = isStart;
		this.end = isEnd;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isStart() {
		return start;
	}
	
	public boolean isEnd() {
		return end;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object o2) {
		if(o2 instanceof State) {
			State s2 = (State) o2;
			return s2.name.equals(name) && s2.end == end && s2.start == start;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
