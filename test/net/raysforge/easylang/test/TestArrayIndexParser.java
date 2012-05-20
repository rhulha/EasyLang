package net.raysforge.easylang.test;

import net.raysforge.easylang.EasySource;
import net.raysforge.easylang.TokenList;
import net.raysforge.easylang.utils.EasyUtils;

public class TestArrayIndexParser {

	public static void main(String[] args) {
		
		String varName ="xasd[1212]";
		
		int openBracketPos = varName.indexOf('[');
		int arrayIndex = Integer.parseInt( varName.substring(openBracketPos+1, varName.length()-1));
		varName = varName.substring(0, openBracketPos);
		
		System.out.println(varName);
		System.out.println(arrayIndex);

	}
	
	public static void main2(String[] args) {
		EasySource rs = new EasySource("myarray[myexpr.coolFunc()] = twoarray[12];".toCharArray());
		TokenList tokenList = EasyUtils.convertSourceToTokenList(rs);
		
		if (tokenList.indexOf("[") < tokenList.indexOf("=") && tokenList.indexOf("=") < tokenList.indexOf(";")) {
			System.out.println("juhu");
		}
	}
}
