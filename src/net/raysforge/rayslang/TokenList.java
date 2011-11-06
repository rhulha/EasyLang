package net.raysforge.rayslang;

import java.util.LinkedList;

public class TokenList {
	
	private final LinkedList<Token> tokens;

	public TokenList(LinkedList<Token> tokens) {
		this.tokens = tokens;
	}

	public boolean isEmpty() {
		return tokens.isEmpty();
	}

	public Token get(int i) {
		return tokens.get(i);
	}

	
	// e.g.: "ii=v;" => identifier, identifier, equals sign, value, semicolon.
	
	public boolean equalsPattern(String string) {
		
		char[] charArray = string.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			Token token = get(i);
			
			switch (c) {
				case 'i':
					if( ! token.isIdentifier() )
						return false;
					break;
				case '=':
					if( ! token.isEqualsSign() )
						return false;
					break;
				case 'v':
					if( ! token.isValue() )
						return false;
					break;
				case ';':
					if( ! token.isSemicolon() )
						return false;
					break;
				case '.':
					if( ! token.isDot() )
						return false;
					break;
				case '(':
					if( ! token.isOpenParentheses() )
						return false;
					break;

				default:
					RayUtils.RunExp("unknown pattern element");
			}
			
		}
		return true;
	}

	public int size() {
		return tokens.size();
	}

	public Token pollLast() {
		return tokens.pollLast();
	}

	public Token pop() {
		return tokens.pop();
	}
	
	@Override
	public String toString() {
		return tokens.toString();
	}
	
	public static void main(String[] args) {
		RaySource rs = new RaySource("x.add!(7);".toCharArray());
		TokenList tl = rs.getSourceTokenUntil(";", "(");
		System.out.println(tl);
		boolean ep = tl.equalsPattern("i.i(");
		System.out.println(ep);
	}
}
