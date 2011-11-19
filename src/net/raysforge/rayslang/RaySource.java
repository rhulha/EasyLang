package net.raysforge.rayslang;

import java.util.LinkedList;

public class RaySource {

	protected int pos = 0;
	protected char src[];

	public RaySource(char[] src) {
		this.src = src;
	}

	public boolean isLetterOrDigit() {
		return Character.isLetterOrDigit(src[pos]);
	}

	public boolean isLetterOrDigitOrBang() {
		return isLetterOrDigit() || src[pos] == '!';
	}

	public boolean more() {
		return pos < src.length;
	}

	public void eatSpacesAndReturns() {
		while ( (pos < src.length) && (isDivider(src[pos])))
			pos++;
	}

	public boolean isDivider() {
		return isDivider(src[pos]);
	}

	public static boolean isDivider(char c) {
		if (c == ' ' || c == '\t' || c == '\r' || c == '\n')
			return true;
		return false;
	}

	// getInnerText
	// holt den inhalt zwischen open und close
	// wobei das erste open bereits von dem aufrufer
	// geholt sein muss.
	public RaySource getInnerText(char open, char close) {
		StringBuffer code = new StringBuffer();
		int brace_counter = 0;
		while (true) {
			if (pos >= src.length) {
				return null;
			}
			char c = src[pos];
			if (c == open) {
				brace_counter++;
				// code.appendInPlace(parse(rs, hook).src);
				// code.appendInPlace(close);
			}
			if (c == close) //|| src[pos] == close)
			{
				if (brace_counter > 0)
					brace_counter--;
				else {
					pos++;
					break;
				}
			}
			code.append(c); // TODO: optimize
			pos++;
		}
		return new RaySource( code.toString().toCharArray());
	}

	public Token getSourceToken() {
		return getSourceToken(false);
	}
	
	public TokenList getSourceTokenUntil(String ... any)
	{
		LinkedList<Token> queue = new LinkedList<Token>();

		while (true) {
			Token token = getSourceToken();
			if (token == null || token.length() == 0)
				break;
			queue.add(token);
			for( String s : any)
			{
				if (token.equals(s)) {
					return new TokenList( queue);
				}
			}
		}
		return new TokenList( queue);
	}

	public Token getSourceToken(boolean peak) {
		eatSpacesAndReturns();
		int start = pos;

		if (!more()) {
			return null;
		}
		if (isLetterOrDigitOrBang()) // ! like ruby
		{
			while (more() && isLetterOrDigitOrBang())
				pos++;
		} else if (src[pos] == '#') // kommentare ignorieren
		{
			while (pos < src.length && src[pos] != '\n')
				pos++;
			return getSourceToken(peak);
		} else if (src[pos] == '"') {
			pos++;
			while (pos < src.length && src[pos] != '"')
				pos++;
			if (pos >= src.length)
				throw new RuntimeException("Unclosed String");
			pos++;
		} else if (src[pos] == '\'') {
			pos++;
			while (pos < src.length && src[pos] != '\'')
				pos++;
			if (src[pos] != '\'')
				throw new RuntimeException("Unclosed String");
			pos++;
		} else
			pos++;
		if (peak == true) {
			pos = start;
		}
		return new Token( new String(src, start, pos - start));
	}

	public String getToken() {
		eatSpacesAndReturns();
		int a = pos;
		while (pos < src.length) {
			if (isDivider())
				break;
			pos++;
		}
		return new String(src, a, pos - a);
	}

	public void eatSpaces() {
		while (pos < src.length && (src[pos] == ' ' || src[pos] == '\t'))
			pos++;
	}
	
	@Override
	public String toString() {
		return new String(src, pos, src.length-pos);
	}
	
	public static void main(String[] args) {
		RaySource rs = new RaySource("{as{d as}d\n}\r\n".toCharArray());
		System.out.println(rs.getSourceToken());
		System.out.println(rs.getSourceToken());
		System.out.println(rs.getSourceToken());
		rs.getInnerText('{', '}');
		System.out.println();
		rs.eatSpacesAndReturns();
		System.out.println(rs);
	}

}
