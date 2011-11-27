package net.raysforge.rayslang.test;

public class TestArrayIndexParser {

	public static void main(String[] args) {
		
		String varName ="xasd[1212]";
		
		int openBracketPos = varName.indexOf('[');
		int arrayIndex = Integer.parseInt( varName.substring(openBracketPos+1, varName.length()-1));
		varName = varName.substring(0, openBracketPos);
		
		System.out.println(varName);
		System.out.println(arrayIndex);

	}
}
