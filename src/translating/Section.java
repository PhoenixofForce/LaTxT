package translating;


import java.util.ArrayList;
import java.util.List;

public class Section {

	private Section parent;
	private List<Section> subsections;
	private String name;
	private String code;

	public Section(String name, String code) {
		this.name = name;
		this.code = code;
		this.subsections = new ArrayList<>();
	}

	public Section getLatest(int level) {
		if(subsections.size() == 0 || level == 0) return this;
		return subsections.get(subsections.size()-1).getLatest(level-1);
	}

	public void addSection(Section s) {
		this.subsections.add(s);
		s.parent = this;
	}

	public Section addSection(String name) {
		Section s = new Section(name, code + "" + (subsections.size()+1));
		this.subsections.add(s);
		s.parent = this;

		return s;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public List<String> toText(int width) {
		List<String> out = new ArrayList<>();

		out.add(name + Alignment.repeat(".", width - name.length() - 2 - code.length()) + "[" + code + "]");
		for(Section s: subsections) {
			for(String str: s.toText(width - 3)) out.add(Alignment.alignRight(str, width));
		}

		return out;
	}
}
