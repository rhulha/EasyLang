package net.raysforge.easylang;

import net.raysforge.easylang.def.EasyInteger;
import net.raysforge.easylang.def.EasyString;

public class Token {

	private final String tokenStr;
	private final char fc; // first char

	public Token(String token) {
		this.tokenStr = token;
		this.fc = token.charAt(0);
	}

	public boolean isIdentifier() {
		return Character.isLetter(fc);
	}

	public boolean isEqualsSign() {
		return '=' == fc;
	}

	public boolean isValue() {
		return isDigitorMinus() || '"' == fc;
	}

	public boolean isQuote() {
		return '"' == fc;
	}

	public boolean isDigit() {
		return Character.isDigit(fc);
	}

	public boolean isDigitorMinus() {
		return Character.isDigit(fc) || '-' == fc;
	}

	public String s() {
		return tokenStr;
	}

	@Override
	public String toString() {
		return tokenStr;
	}

	public boolean equals(char c) {
		return c == fc;
	}

	public boolean equals(String str) {
		return tokenStr.equals(str);
	}

	public boolean isSemicolon() {
		return ';' == fc;
	}

	public boolean isDot() {
		return '.' == fc;
	}

	public boolean isOpenBrace() {
		return '{' == fc;
	}

	public boolean isOpenParentheses() {
		return '(' == fc;
	}

	public boolean isOpenBracket() {
		return '[' == fc;
	}

	public int length() {
		return tokenStr.length();
	}

	public int getLength() {
		return tokenStr.length();
	}

	public boolean isClosedParentheses() {
		return ')' == fc;
	}

	public boolean isClosedBracket() {
		return ']' == fc;
	}

	public boolean isClosedBrace() {
		return '}' == fc;
	}

	public String getUnquotedString() {
		return new String(tokenStr.toCharArray(), 1, tokenStr.length() - 2);
	}

	public EasyString getUnquotedEasyString() {
		return new EasyString(getUnquotedString());
	}

	public static void main(String[] args) {
		Token t = new Token("\"hello\"");
		System.out.println(t.getUnquotedString());
	}

	public EasyInteger getEasyInteger() {
		return new EasyInteger(Long.parseLong(s()));
	}

	public EasyClassInterface getValue() {
		if (isDigitorMinus()) {
			return getEasyInteger();
		} else if (isQuote()) {
			return getUnquotedEasyString();
		} else {
			System.err.println("arg456");
			return null; // should never happen, right ? :-)
		}
	}
}
