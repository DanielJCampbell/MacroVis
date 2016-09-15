package macrovis.util;

import java.util.ArrayList;
import java.util.List;

public class ExpansionTrace {

	public final Span span;
	public final boolean failed;
	private List<String> trace;
	private int depth;

	public ExpansionTrace(Span s, boolean f, List<String> t) {
		span = s;
		failed = f;
		trace = new ArrayList<String>(t);
		depth = 0;

		// Normalise for expr macros in stmt position
		if (t.get(1).endsWith(";") && !t.get(0).endsWith(";")) {
			trace.set(0, t.get(0).concat(";"));
		}
	}

	public String getString() {
		return trace.get(depth);
	}

	public String getString(int index) {
		return trace.get(index);
	}

	public void step(int type) {
		if (type == 0) {
			depth = 0;
		}
		if (type == 3) {
			depth = trace.size()-1;
		}
		if (type == 1) {
			depth -= (depth-1 >= 0) ? 1 : 0;
		}
		if (type == 2) {
			depth += (depth+1 < trace.size()) ? 1 : 0;
		}
	}

	public boolean start() {
		return depth == 0;
	}

	public boolean end() {
		return depth == trace.size()-1;
	}

	public int depth() {
		return depth;
	}

	public void setDepth(int d) {
		depth = d;
	}

	public boolean encompasses(int line, int col) {
		int lineStart = span.lineStart;
		int colStart = span.colStart;
		String s = getString();
		int lineEnd = lineStart + (s.split("\n").length-1);
		int index = s.lastIndexOf('\n');
		int colEnd = (index == -1) ? s.length() + colStart : s.length() - index;

		if (line < lineStart || line > lineEnd) return false;
		else if (lineStart == lineEnd) return (col >= colStart && col <= colEnd);
		else if (line == lineStart) return (col >= colStart);
		else if (line > lineStart && line < lineEnd) return true;
		else return (col < colEnd);
	}

	public String name() {
		return trace.get(0).substring(0, trace.get(0).indexOf("!"));
	}

	public int size() {
		return trace.size();
	}

}
