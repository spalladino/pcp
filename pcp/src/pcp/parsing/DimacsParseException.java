package pcp.parsing;

import java.text.ParseException;


public class DimacsParseException extends ParseException {

	public DimacsParseException(String s) {
		super(s, -1);
	}

	private static final long serialVersionUID = 4196865343542293127L;
}
