package net.raysforge.rayslang;

public class Token {

	private final String token;

	public Token(String token) {
		this.token = token;
	}
	
	@Override
	public String toString() {
		return token;
	}

	public boolean isIdentifier() {
		return Character.isLetter( token.charAt(0));
	}

	public boolean isEqualsSign() {
		return '=' == token.charAt(0);
	}

	public boolean isValue() {
		char c = token.charAt(0);
		return Character.isDigit( c ) || '"' == c ;
	}
	
	public String s()
	{
		return token;
	}
	
	public boolean equals(String str) {
		return token.equals(str);
	}

	public boolean isSemicolon() {
		return ';' == token.charAt(0);
	}

	public boolean isDot() {
		return '.' == token.charAt(0);
	}

	public boolean isOpenBrace() {
		return '{' == token.charAt(0);
	}

	public boolean isOpenParentheses() {
		return '(' == token.charAt(0);
	}

	public boolean isOpenBracket() {
		return '[' == token.charAt(0);
	}

	public int length() {
		return token.length();
	}
	
	public int getLength() {
		return token.length();
	}

}
