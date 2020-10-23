package translating;

import java.util.Collections;

public enum Alignment {

	LEFT, MIDDLE, RIGHT, UNDEFINED;

	public static String align(Alignment align, String in, int width) {
		switch (align) {
			case MIDDLE:
				return alignMiddle(in, width);
			case RIGHT:
				return alignRight(in, width);
			default:
				return alignLeft(in, width);
		}
	}

	public static String alignLeft(String in, int width) {
		if(in.length() >= width) return in;
		int right = width - in.length();

		String out = in + repeat(" ", right);
		return out;
	}

	public static String alignMiddle(String toMiddle, int width) {
		if(toMiddle.length() >= width) return toMiddle;

		int blanks = width - toMiddle.length();

		int left = (int) Math.ceil(blanks / 2.0f);
		int right = (int) Math.floor(blanks / 2.0f);

		String out = repeat(" ", left) + toMiddle + repeat(" ", right);
		return out;
	}

	public static String alignRight(String in, int width) {
		if(in.length() >= width) return in;
		int left = width - in.length();

		String out = repeat(" ", left) + in;
		return out;
	}

	public static String repeat(String s, int n) {
		return String.join("", Collections.nCopies(n, s));
	}
}
