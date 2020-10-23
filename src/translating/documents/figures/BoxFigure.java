package translating.documents.figures;

public class BoxFigure  extends Figure {

	public int x1, x2, y1, y2;
	public BoxFigure(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	@Override
	public void draw(char[][] map) {
		for(int x = getSmallX(); x <= getBigX(); x++) {
			map[y1][x] = '-';
			map[y2][x] = '-';
		}

		for(int y = getSmallY(); y <= getBigY(); y++) {
			map[y][x1] = '|';
			map[y][x2] = '|';
		}

		map[y1][x1] = '+';
		map[y1][x2] = '+';
		map[y2][x1] = '+';
		map[y2][x2] = '+';
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