package net.raysforge.rayslang;

import java.util.ArrayList;

public class TokenList {

	private final ArrayList<Token> tokens;
	int position = 0;
	int offset;
	int limit;

	public TokenList(ArrayList<Token> tokens) {
		this(tokens, 0, tokens.size());
	}

	public TokenList(ArrayList<Token> tokens, int offset, int limit) {
		this.tokens = tokens;
		this.offset = offset;
		this.limit = Math.min(limit, tokens.size());
	}

	public boolean isEmpty() {
		return (offset + position) >= limit;
	}

	public boolean hasMore() {
		return (offset + position) < limit;
	}

	public Token get(int i) {
		if ((offset + position + i) >= limit)
			RayUtils.runtimeExcp("(offset + position + i)>=limit -> (" + offset + "+" + position + "+" + i + ")>=" + limit);
		return tokens.get(offset + position + i);
	}

	// e.g.: "ii=v;" => identifier, identifier, equals sign, value, semicolon.

	public boolean startsWithPattern(String string) {

		char[] charArray = string.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			Token token = get(i);

			switch (c) {
				case 'i':
					if (!token.isIdentifier())
						return false;
					break;
				case '=':
					if (!token.isEqualsSign())
						return false;
					break;
				case 'v':
					if (!token.isValue())
						return false;
					break;
				case ';':
					if (!token.isSemicolon())
						return false;
					break;
				case '.':
					if (!token.isDot())
						return false;
					break;
				case '(':
					if (!token.isOpenParentheses())
						return false;
					break;
				case '[':
					if (!token.isOpenBracket())
						return false;
					break;
				case ']':
					if (!token.isClosedBracket())
						return false;
					break;
				case '{':
					if (!token.isOpenBrace())
						return false;
					break;
				case '}':
					if (!token.isClosedBrace())
						return false;
					break;

				default:
					RayUtils.runtimeExcp("unknown pattern element");
			}

		}
		return true;
	}

	public int remaining() {
		return limit - (offset + position);
	}

	public Token pop() {
		Token t = get(0);
		if (t != null)
			position++;
		return t;
	}

	@Override
	public String toString() {
		return tokens.subList((offset + position), limit).toString();
	}

	// this method exptects that the FIRST open char IS ALREADY REMOVED !!!
	// this method does NOT return the last matching char.
	public TokenList getInnerList(char open, char close) {
		int pos = position;

		int brace_counter = 0;
		while (hasMore()) {
			Token t = pop();
			if (t == null)
				RayUtils.runtimeExcp("unexpected end of tokens");
			if (t.equals(open)) {
				brace_counter++;
			} else if (t.equals(close)) {
				if (brace_counter > 0) {
					brace_counter--;
				} else {
					break;
				}
			}
		}
		return subListUsingOnlyOffset( pos, position - 1);
	}

	public void remove(String string) {
		if (!get(0).equals(string))
			RayUtils.runtimeExcp(get(0) + " != " + string);
		position++;
	}

	public void removeLast(String string) {
		if (!getLast().equals(string))
			RayUtils.runtimeExcp(getLast()+ " != " + string);
		limit--;
	}

	public String popString() {
		return pop().s();
	}

	public boolean contains(String string) {
		for (int i = 0; i < remaining(); i++) {
			if (get(i).equals(string))
				return true;
		}
		return false;
	}

	// final token is always removed, it may be returned though.
	public TokenList getAndRemoveSourceTokenUntil(String string, boolean includeFinalToken) {
		int pos = position;
		while (hasMore()) {
			Token t = pop();
			if (t.equals(string))
				break;
		}
		return subListUsingOnlyOffset( pos, includeFinalToken ? position : position - 1);
	}

	public void hideCodeBeforePosAndResetPos() {
		offset = offset + position;
		position = 0;
	}

	// Beware: position is 0 after copy.
	public TokenList copy() {
		return new TokenList(tokens, offset, limit);

	}

	public Token getLast() {
		return get(remaining()-1);

	}

	public TokenList subListUsingOnlyOffset(int from, int to) {
		return new TokenList(tokens, offset+from, offset+to);
	}

	public TokenList subList(int from, int to) {
		return new TokenList(tokens, offset+position+from, offset+position+to);
	}

}
