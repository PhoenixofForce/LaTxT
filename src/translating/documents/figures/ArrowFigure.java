package translating.documents.figures;

public class ArrowFigure extends Figure {

	private LineFigure line;
	public int x1, x2, y1, y2;
	public ArrowFigure(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;

		line = new LineFigure(x1, y1, x2, y2);
	}

	@Override
	public void draw(char[][] map) {
		line.draw(map);
		if(x1 == x2) {
			if(y1 < y2) map[y2][x2] = 'v';
			else map[y2][x2] = '^';
		} else if(y1 == y2) {
			if(x1 < x2) map[y2][x2] = '>';
			else map[y2][x2] = '<';
		} else {
			if(x1 < x2) {
				if(y1 < y2) map[x2][y2] = 'v';
				else map[x2][y2] = '<';

			} else {
				if(y1 < y2) map[x2][y2] = '>';
				else map[x2][y2] = '^';

			}
		}

	}

	@Override
	public int getSmallX() {
		return Math.min(x1, x2);
	}

	@Override
	public int getSmallY() {
		return Math.min(y1, y2);
	}

	@Override
	public int getBigX() {
		return Math.max(x1, x2);
	}

	@Override
	public int getBigY() {
		return Math.max(y1, y2);
	}
}
