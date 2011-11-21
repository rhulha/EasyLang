package net.raysforge.rayslang;

public class Token {

	private final String tokenStr;

	public Token(String token) {
		this.tokenStr = token;
	}
	
	@Override
	public String toString() {
		return tokenStr;
	}

	public boolean isIdentifier() {
		return Character.isLetter( tokenStr.charAt(0));
	}

	public boolean isEqualsSign() {
		return '=' == tokenStr.charAt(0);
	}

	public boolean isValue() {
		char c = tokenStr.charAt(0);
		return Character.isDigit( c ) || '"' == c ;
	}
	
	public boolean isQuote() {
		return '"' == tokenStr.charAt(0) ;
	}
	
	public boolean isDigit() {
		return Character.isDigit( tokenStr.charAt(0) );
	}
	
	public String s()
	{
		return tokenStr;
	}
	
	public boolean equals(char c) {
		return c == tokenStr.charAt(0);
	}

	public boolean equals(String str) {
		return tokenStr.equals(str);
	}

	public boolean isSemicolon() {
		return ';' == tokenStr.charAt(0);
	}

	public boolean isDot() {
		return '.' == tokenStr.charAt(0);
	}

	public boolean isOpenBrace() {
		return '{' == tokenStr.charAt(0);
	}

	public boolean isOpenParentheses() {
		return '(' == tokenStr.charAt(0);
	}

	public boolean isOpenBracket() {
		return '[' == tokenStr.charAt(0);
	}

	public int length() {
		return tokenStr.length();
	}
	
	public int getLength() {
		return tokenStr.length();
	}

	public boolean isClosedParentheses() {
		return ')' == tokenStr.charAt(0);
	}

	public boolean isClosedBracket() {
		return ']' == tokenStr.charAt(0);
	}

	public boolean isClosedBrace() {
		return '}' == tokenStr.charAt(0);
	}

}
