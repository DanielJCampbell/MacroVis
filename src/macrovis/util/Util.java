package macrovis.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class Util {

	public static List<ExpansionTrace> readAnalysis(String filename) throws IOException {
		ArrayList<ExpansionTrace> result = new ArrayList<ExpansionTrace>();
		String filestring = String.join("\n", Files.readAllLines(Paths.get(filename)));
		int index_s = filestring.indexOf("macro_refs") + 13;
		int index_e = filestring.lastIndexOf(']');
		filestring = filestring.substring(index_s, index_e);

		String[] objs = filestring.split(",\\{");
		for (int i = 0; i < objs.length; i++) {
			String s = (i == 0) ? objs[i].substring(1) : objs[i];
			ExpansionTrace trace = parseTrace(s);
			if (trace != null)
				result.add(trace);
		}
		return result;
	}

	// MacroRef JSON has the form "span":Span,"qualname":String,"callee_span":Span,"trace":{}}
	// Need to extract the span and the trace - return null if the trace is null,
	// otherwise return an ExpansionTrace.
	// Note opening brace is removed in parsing.
	private static ExpansionTrace parseTrace(String json) {
		int index = json.indexOf('}') + 2;
		Span span = parseSpan(json.substring(7, index-1));

		index = json.indexOf("trace", index);

		Pair<List<String>, Boolean> trace =
				parseStringTrace(json.substring(json.indexOf(":", index) + 1, json.length()-1));
		// If size < 2 we know there's been an error
		if (trace == null || trace.first.size() < 2) {
			return null;
		}
		return new ExpansionTrace(span, trace.second, trace.first);
	}

	// Span JSON has form {"file_name":String,"byte_start":int,"byte_end":int, etc.}
	private static Span parseSpan(String json) {
		// We can extract the desired fields by searching for `:`
		int index = json.indexOf(":") + 2;
		String filename = json.substring(index, json.indexOf('"', index));
		index = json.indexOf(":", index) + 1;
		int low = Integer.parseInt(json.substring(index, json.indexOf(",", index)));
		index = json.indexOf(":", index) + 1;
		int high = Integer.parseInt(json.substring(index, json.indexOf(",", index)));
		index = json.indexOf(":", index) + 1;
		int lineLow = Integer.parseInt(json.substring(index, json.indexOf(",", index)));
		index = json.indexOf(":", index) + 1;
		int lineHigh = Integer.parseInt(json.substring(index, json.indexOf(",", index)));
		index = json.indexOf(":", index) + 1;
		int colLow = Integer.parseInt(json.substring(index, json.indexOf(",", index)));
		index = json.indexOf(":", index) + 1;
		int colHigh = Integer.parseInt(json.substring(index, json.indexOf('}', index)));
		return new Span(filename, low, high, lineLow, lineHigh, colLow, colHigh);
	}

	// Trace can be one of two things:
	// null
	// {trace:[s1,s2,s3...],failed:bool}
	private static Pair<List<String>, Boolean> parseStringTrace(String json) {
		if (json.equals("null"))
			return null;

		int index = json.indexOf(":") + 3;
		List<String> trace = new ArrayList<String>();
		for (String s: json.substring(index, json.lastIndexOf("]")-1).split("\",\"")) {
			trace.add(s);
		}
		index = json.indexOf(":", json.lastIndexOf("]")) + 1;
		Boolean failed = Boolean.parseBoolean(json.substring(index, json.length()-1));
		// Do some editing: all the traces are quoted, and contain escape sequences,
		// as an artefact from the transformation into CSV
		// We convert escaped newlines and quotations into their original form
		// Note we must ensure the characters weren't meant to be escaped (check for absence of additional `\`)
		Pattern newline = Pattern.compile("([^\\\\]|^)\\\\n");
		Pattern quote = Pattern.compile("([^\\\\]|^)\\\\\"");
		for (int i = 0; i < trace.size(); i++) {
			String s = trace.get(i);
			Matcher lineMatcher = newline.matcher(s);
			while (lineMatcher.find()) {
				s = lineMatcher.replaceFirst("$1\n");
				lineMatcher = newline.matcher(s);
			}
			s = s.replace("\\\\n", "\\n");
			Matcher quoteMatcher = quote.matcher(s);
			while (quoteMatcher.find()) {
				s = quoteMatcher.replaceFirst("$1\"");
				quoteMatcher = quote.matcher(s);
			}
			s = s.replace("\\\\\"", "\\\"");
			trace.set(i, s);
		}
		return new Pair<List<String>, Boolean>(trace, failed);
	}

	private static class Pair<T1, T2> {
		public final T1 first;
		public final T2 second;

		public Pair(T1 f, T2 s) {
			first = f; second = s;
		}
	}

	// Stream to read from a process output as it runs (to prevent hangs).
	// Writes to a supplied text pane as it goes.
	public static class ProcessStream extends Thread {
		InputStream is;
		JTextPane text;

	    public ProcessStream(InputStream is, JTextPane text) {
	        this.is = is;
	        this.text = text;
	    }

	    public void run() {
	        try {
	            InputStreamReader isr = new InputStreamReader(is);
	            BufferedReader br = new BufferedReader(isr);
	            String line = null;
	            while ((line = br.readLine()) != null) {
	            	Document doc = text.getDocument();
	                doc.insertString(doc.getLength(), "\n", null);
	                doc.insertString(doc.getLength(), line, null);
	            }
	        } catch (IOException | BadLocationException e) {
	        	e.printStackTrace();
	        }
	    }
	}
}
