package macrovis.util;

public class Span {
	public final String filename;
	public final int byteStart, byteEnd;
	public final int lineStart, lineEnd;
	public final int colStart, colEnd;

	public Span(String f, int bs, int be, int ls, int le, int cs, int ce) {
		filename = f;
		byteStart = bs; byteEnd = be;
		lineStart = ls; lineEnd = le;
		colStart = cs;  colEnd = ce;
	}

	// Returns true if given point is inside this span
	public boolean encompasses(int line, int col) {
		if (line >= lineStart && line <= lineEnd) {
			if (lineStart == lineEnd) {
				return (col >= colStart && col <= colEnd);
			}
			else if (line == lineStart) {
				return (col >= colStart);
			}
			else if (line == lineEnd) {
				return (col <= colEnd);
			}
			else return true;
		}
		return false;
	}

	public int size() {
		return byteEnd - byteStart;
	}

}
