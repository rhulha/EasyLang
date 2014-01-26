package net.raysforge.easylang;

import java.util.ArrayList;
import java.util.Collections;

import net.raysforge.easylang.utils.EasyUtils;

public class TokenList {

	private final ArrayList<Token> tokens;
	int offset;
	int position = 0;
	int limit;

	public TokenList(ArrayList<Token> tokens) {
		this(tokens, 0, 0, tokens.size());
	}

	public TokenList(ArrayList<Token> tokens, int offset, int position, int limit) {
		this.tokens = tokens;
		this.offset = offset;
		this.position = position;
		this.limit = Math.min(limit, tokens.size());
	}

	public boolean isEmpty() {
		return (offset + position) >= limit;
	}

	public boolean hasMore() {
		return (offset + position) < limit;
	}

	public int remaining() {
		return limit - (offset + position);
	}

	public Token get(int i) {
		if ((offset + position + i) >= limit)
			EasyUtils.runtimeExcp("(offset + position + i)>=limit -> (" + offset + "+" + position + "+" + i + ")>=" + limit);
		return tokens.get(offset + position + i);
	}

	// e.g.: "ii=v;" => identifier, identifier, equals sign, value, semicolon.

	public boolean startsWithPattern(String string) {

		if (isEmpty())
			return false;

		char[] charArray = string.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			if (i >= remaining())
				return false;
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
				EasyUtils.runtimeExcp("unknown pattern element");
			}

		}
		return true;
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

	public void remove(String string) {
		if (!get(0).equals(string))
			EasyUtils.runtimeExcp(get(0) + " != " + string + " (" + this + ")");
		position++;
	}

	public Token popLast() {
		Token last = getLast();
		limit--;
		return last;
	}

	public String popLastString() {
		return popLast().toString();
	}

	public void removeLast(String string) {
		if (!getLast().equals(string))
			EasyUtils.runtimeExcp(getLast() + " != " + string);
		limit--;
	}

	public String popString() {
		return pop().s();
	}

	public int indexOf(String string) {
		for (int i = 0; i < remaining(); i++) {
			if (get(i).equals(string))
				return i;
		}
		return -1;
	}

	public boolean contains(String string) {
		return indexOf(string) >= 0;
	}

	// final token is always removed, it may be returned though.
	public TokenList getAndRemoveSourceTokenUntil(boolean includeFinalToken, String... any) {
		int pos = position;
		label: while (hasMore()) {
			Token token = pop();
			for (String s : any) {
				if (token.equals(s)) {
					break label;
				}
			}
		}
		return getSubListUsingOnlyOffset(pos, includeFinalToken ? position : position - 1);
	}

	public void hideCodeBeforePosAndResetPos() {
		offset = offset + position;
		position = 0;
	}

	public TokenList copy() {
		return new TokenList(tokens, offset, position, limit);
	}

	public TokenList resetPosition() {
		position = 0;
		return this;
	}

	public Token getLast() {
		return get(remaining() - 1);

	}

	// this method exptects that the FIRST open char IS ALREADY REMOVED !!!
	// this method does NOT return the last matching char.
	// Info: the returned tokens sub list IS removed from the containing token list !
	public TokenList getSubList(char open, char close) {
		int pos = position;

		int brace_counter = 0;
		while (hasMore()) {
			Token t = pop();
			if (t == null)
				EasyUtils.runtimeExcp("unexpected end of tokens");
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
		return getSubListUsingOnlyOffset(pos, position - 1);
	}

	public TokenList getSubListUsingOnlyOffset(int from, int to) {
		return new TokenList(tokens, offset + from, 0, offset + to);
	}

	public TokenList getSubList(int from, int to) {
		return new TokenList(tokens, offset + position + from, 0, offset + position + to);
	}

	public TokenList reverse() {
		ArrayList<Token> clone = new ArrayList<Token>(tokens);
		Collections.reverse(clone);
		return new TokenList(clone);
	}

	/* very bad idea due to offset !!!
	public void push(Token token) {
		tokens.add(0, token);
	}
	 */

}
