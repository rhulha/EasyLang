package net.raysforge.rayslang;

import java.util.Arrays;

public class RaySource {

	protected int pos = 0;
	protected char src[];

	public RaySource(char[] src) {
		this.src = src;
	}

	private boolean isLetter() {
		return  Character.isLetter(src[pos]) ;
	}

	private boolean isDigit() {
		return  Character.isDigit(src[pos]) ;
	}

	private boolean isDigitOrMinus() {
		return  isDigit() || src[pos] == '-';
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
		while ((more()) && (isDivider(src[pos])))
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
			if (c == open) { // TODO: what about open and close inside strings ?
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
		return new RaySource(code.toString().toCharArray());
	}

	public Token getSourceToken() {
		return getSourceToken(false);
	}

	public Token getSourceToken(boolean peak) {
		eatSpacesAndReturns();
		int start = pos;

		if (!more()) {
			return null;
		}

		if (isDigitOrMinus()) // number with possible minus sign
    	{
			pos++;
			if( more() && isDigit())
			{
				while (more() && isDigit())
					pos++;
			}
			else if( more() && src[pos] == '>') // "->" for closures
			{
				pos++;
			}
    	} else if (isLetter())
		{
			while (more() && isLetterOrDigitOrBang()) // ending! like ruby
				pos++;
		} else if (src[pos] == '#') // kommentare ignorieren
		{
			while (more() && src[pos] != '\n')
				pos++;
			return getSourceToken(peak);
		} else if (src[pos] == '/') // kommentare ignorieren
		{
			pos++;
			if( src[pos] == '/')
				while (more() && src[pos] != '\n')
					pos++;
			return getSourceToken(peak);
		} else if (src[pos] == '"') {
			pos++;
			while (more() && src[pos] != '"')
				pos++;
			if (pos >= src.length)
				throw new RuntimeException("Unclosed String");
			pos++;
		} else if (src[pos] == '\'') {
			pos++;
			while (more() && src[pos] != '\'')
				pos++;
			if (pos >= src.length)
				throw new RuntimeException("Unclosed String");
			pos++;
		} else
			pos++;
		Token token = new Token(new String(src, start, pos - start));
		if (peak == true) {
			pos = start;
		}
		return token;
	}

	public String getToken() {
		eatSpacesAndReturns();
		int a = pos;
		while (more()) {
			if (isDivider())
				break;
			pos++;
		}
		return new String(src, a, pos - a);
	}

	public void eatSpaces() {
		while (more() && (src[pos] == ' ' || src[pos] == '\t'))
			pos++;
	}

	@Override
	public String toString() {
		return new String(src, pos, src.length - pos);
	}

	public boolean contains(String string) {
		return indexOf(string) != -1;
	}

	public int indexOf(String string) {
		int sourceOffset = pos;
		int fromIndex = 0;
		int max = src.length;
		char[] source = src;
		char[] target = string.toCharArray();
		char first = target[0];
		int targetCount = target.length;
		int targetOffset = 0;

		for (int i = sourceOffset + fromIndex; i <= max; i++) {
			/* Look for first character. */
			if (source[i] != first) {
				while (++i <= max && source[i] != first)
					;
			}

			/* Found first character, now look at the rest of v2 */
			if (i <= max) {
				int j = i + 1;
				int end = j + targetCount - 1;
				for (int k = targetOffset + 1; j < end && source[j] == target[k]; j++, k++)
					;

				if (j == end) {
					/* Found whole string. */
					return i - sourceOffset;
				}
			}
		}
		return -1;
	}

	public void removeCodeBeforePosAndResetPos() {
		src = Arrays.copyOfRange(src, pos, src.length);
		pos=0;
	}

	public RaySource copyAtPos() {
		return new RaySource(Arrays.copyOfRange(src, pos, src.length));
	}
	
}
