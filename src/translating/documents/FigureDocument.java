package translating.documents;

import translating.Alignment;
import translating.documents.figures.Figure;

import java.util.ArrayList;
import java.util.List;

public class FigureDocument extends Document{

	private List<Figure> figures;

	public FigureDocument(Document parent) {
		super(parent, Alignment.LEFT, 0);
		figures = new ArrayList<>();
	}

	public void addFigure(Figure f) {
		this.figures.add(f);
	}

	@Override
	public boolean addChildren(Document t) {
		return false;
	}

	@Override
	public List<String> toText() {
		int maxX = figures.stream().mapToInt(Figure::getBigX).max().getAsInt();
		int maxY = figures.stream().mapToInt(Figure::getBigY).max().getAsInt();

		char[][] map = new char[maxY+1][maxX+1];
		for(int y = 0; y < map.length; y++) {
			for(int x = 0; x < map[0].length; x++) {
				map[y][x] = ' ';
			}
		}

		for(Figure f: figures) f.draw(map);

		List<String> out = new ArrayList<>();
		for(int i = 0; i < map.length; i++) out.add(Alignment.align(getParent().getAlignment(), new String(map[i]), getParent().getLineWidth()));

		return out;
	}
}
