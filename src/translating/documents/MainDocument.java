package translating.documents;

import translating.Alignment;
import translating.Section;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainDocument extends Document {

	private boolean makeTOC = false;
	private List<Section> sections;

	public MainDocument(int lineWidth) {
		super(null, Alignment.UNDEFINED, lineWidth);
		sections = new ArrayList<>();
	}

	public MainDocument(int lineWidth, String[] options) {
		super(null, Alignment.UNDEFINED, lineWidth);
		sections = new ArrayList<>();

		for(String s: options) {
			if(s.trim().equalsIgnoreCase("maketoc")) makeTOC = true;
		}
	}

	@Override
	public List<String> toText() {
		List<String> out = new ArrayList<>();

		for(Document dm: getChildren()) out.addAll(dm.toText());

		if(makeTOC) {
			List<String> section = new ArrayList<>();
			for(Section s: sections) section.addAll(s.toText(getLineWidth()));
			for(int i = 0; i < section.size(); i++) out.add(i, section.get(i));
		}

		return out;
	}

	public Section addSection(String command, int level) {
		if(level == 0) {
			Section s = new Section(command, (sections.size()+1) + "");
			sections.add(s);
			return s;
		}
		else return sections.get(sections.size()-1).getLatest(level-1).addSection(command);
	}

	public void export() {
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter("out.txt"));
			List<String> out = toText();
			for(int i = 0; i < out.size(); i++) {
				String  s = out.get(i);
				System.out.println(s);
				w.write(s);
				if(i < out.size() - 1) w.write("\r\n");
			}
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
