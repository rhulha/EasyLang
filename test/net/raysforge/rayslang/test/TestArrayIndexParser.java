package net.raysforge.rayslang.test;

import net.raysforge.rayslang.EasySource;
import net.raysforge.rayslang.TokenList;
import net.raysforge.rayslang.utils.EasyUtils;

public class TestArrayIndexParser {

	public static void main1(String[] args) {
		
		String varName ="xasd[1212]";
		
		int openBracketPos = varName.indexOf('[');
		int arrayIndex = Integer.parseInt( varName.substring(openBracketPos+1, varName.length()-1));
		varName = varName.substring(0, openBracketPos);
		
		System.out.println(varName);
		System.out.println(arrayIndex);

	}
	
	public static void main(String[] args) {
		EasySource rs = new EasySource("myarray[myexpr.coolFunc()] = twoarray[12];".toCharArray());
		TokenList tokenList = EasyUtils.convertSourceToTokenList(rs);
		
		if (tokenList.indexOf("[") < tokenList.indexOf("=") && tokenList.indexOf("=") < tokenList.indexOf(";")) {
			System.out.println("juhu");
		}
	}
}
