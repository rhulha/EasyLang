package net.raysforge.rayslang;

import net.raysforge.rayslang.def.RayInteger;
import net.raysforge.rayslang.def.RayString;

public class Token {

	private final String tokenStr;
	private final char fc; // first char

	public Token(String token) {
		this.tokenStr = token;
		this.fc = token.charAt(0);
	}

	@Override
	public String toString() {
		return tokenStr;
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

	public RayString getUnquotedRayString() {
		return new RayString(new String(tokenStr.toCharArray(), 1, tokenStr.length() - 2));
	}

	public static void main(String[] args) {
		Token t = new Token("\"hello\"");
		System.out.println(t.getUnquotedString());
	}

	public RayInteger getRayInteger() {
		return new RayInteger(Long.parseLong(s()));
	}

	public RayClassInterface getValue() {
		if (isDigitorMinus()) {
			return getRayInteger();
		} else if (isQuote()) {
			return getUnquotedRayString();
		} else {
			System.err.println("arg456");
			return null; // should never happen, right ? :-)
		}
	}
}
