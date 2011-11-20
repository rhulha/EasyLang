package net.raysforge.rayslang.test;

import net.raysforge.rayslang.RaySource;
import net.raysforge.rayslang.Token;
import net.raysforge.rayslang.TokenList;

public class TestRaySource {
	
	public static void main0(String[] args) {
		RaySource rs = new RaySource("->".toCharArray());
		boolean b = rs.isLetterOrDigitOrBangOrMinusOrGreaterThan();
		System.out.println(b);
	}
	
	public static void main(String[] args) {
		RaySource rs = new RaySource("{ \n Zahl !zahl! -> zahl.print()    \n}".toCharArray());
		
		System.out.println( rs.contains("->"));
		
		while( true )
		{
			Token t = rs.getSourceToken();
			if( t == null)
				break;
			System.out.println(t);
		}
			System.out.println();

		TokenList parameterList = rs.getSourceTokenUntil("->");
		System.out.println("pl: " + parameterList);
		System.out.println("code: " + rs);
		
	}

	public static void main1(String[] args) {
		RaySource rs = new RaySource("{as{d as}d\n}\r\n".toCharArray());
		System.out.println(rs.getSourceToken());
		System.out.println(rs.getSourceToken());
		System.out.println(rs.getSourceToken());
		rs.getInnerText('{', '}');
		System.out.println();
		rs.eatSpacesAndReturns();
		System.out.println(rs);
	}

}
