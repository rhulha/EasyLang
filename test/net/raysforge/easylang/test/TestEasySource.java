package net.raysforge.easylang.test;

import net.raysforge.easylang.EasySource;
import net.raysforge.easylang.Token;

public class TestEasySource {
	
	public static void main(String[] args) {
		new EasySource("Zahl[] za = neu Zahl[];".toCharArray());
	}

	public static void main0(String[] args) {
		EasySource rs = new EasySource("{ \n Zahl !zahl! -> zahl.print()    \n}".toCharArray());
		
		System.out.println( rs.contains("->"));
		
		while( true )
		{
			Token t = rs.getSourceToken();
			if( t == null)
				break;
			System.out.println(t);
		}
			System.out.println();

		
	}

	public static void main1(String[] args) {
		EasySource rs = new EasySource("{as{d as}d\n}\r\n".toCharArray());
		System.out.println(rs.getSourceToken());
		System.out.println(rs.getSourceToken());
		System.out.println(rs.getSourceToken());
		rs.getInnerText('{', '}');
		System.out.println();
		rs.eatSpacesAndReturns();
		System.out.println(rs);
	}

}
