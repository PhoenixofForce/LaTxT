package translating.documents.figures;

public abstract class Figure {
	public abstract void draw(char[][] map);
	public abstract int getSmallX();
	public abstract int getSmallY();
	public abstract int getBigX();
	public abstract int getBigY();
}
