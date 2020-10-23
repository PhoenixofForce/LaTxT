package translating.documents.figures;

public class StringFigure extends Figure {

	private int x, y;
	private String text;
	public StringFigure(int x, int y, String text) {
		this.x = x;
		this.y = y;
		this.text = text;
	}

	@Override
	public void draw(char[][] map) {
		for(int i = 0; i < text.length(); i++) {
			map[y][x + i] = text.charAt(i);
		}
	}

	@Override
	public int getSmallX() {
		return x;
	}

	@Override
	public int getSmallY() {
		return y;
	}

	@Override
	public int getBigX() {
		return x + text.length();
	}

	@Override
	public int getBigY() {
		return y;
	}
}
