package net.raysforge.rayslang.test;

import net.raysforge.rayslang.EasySource;
import net.raysforge.rayslang.EasyUtils;
import net.raysforge.rayslang.TokenList;

public class TestTokenList {
	
	public static void main(String[] args) {
		EasySource rs = new EasySource("Zahl[] x = neu Zahl[];".toCharArray());
		TokenList tokenList = EasyUtils.convertSourceToTokenList(rs);
		System.out.println( tokenList.startsWithPattern("i[]i=") );
	}
	
	public static void main1(String[] args) {
		EasySource rs = new EasySource("{\nZeichen zeile -> zeile.schreibe();\n}".toCharArray());
		TokenList tl = EasyUtils.convertSourceToTokenList(rs);
		System.out.println("original: " + tl);
		tl.remove("{");
		TokenList innerList = tl.getSubList('{', '}');
		System.out.println("innerList: " + innerList);
		
		if (innerList.contains("->")) {
			TokenList parameterList = innerList.getAndRemoveSourceTokenUntil( false, "->" );
			System.out.println("parameterList: " + parameterList);

			System.out.println("innerList.get(0): " + innerList.get(0));

			innerList.hideCodeBeforePosAndResetPos();
			System.out.println("innerList: " + innerList);
		}
	}

	public static void main2(String[] args) {
		EasySource rs = new EasySource("x.add!(7);".toCharArray());
		TokenList tl = EasyUtils.convertSourceToTokenList(rs);
		System.out.println(tl);
		EasyUtils.assert_(tl.remaining()==7);
		EasyUtils.assert_(tl.startsWithPattern("i.i("));
		EasyUtils.assert_(!tl.contains("add"));
		EasyUtils.assert_(tl.contains("add!"));
		
		EasySource rs2 = new EasySource("{as{d -> as}d\n}\r\n".toCharArray());
		TokenList tl2 = EasyUtils.convertSourceToTokenList(rs2);

		System.out.println(  tl2.copy().getAndRemoveSourceTokenUntil( true, "->" ));;
		System.out.println(  tl2.copy().getAndRemoveSourceTokenUntil( false, "->" ));;
		
		TokenList test1 = tl2.copy();
		
		test1.pop();
		test1.pop();
		test1.pop();
		TokenList innerList = test1.getSubList('{', '}');
		System.out.println(innerList);
		
		EasyUtils.assert_(innerList.hasMore());
		EasyUtils.assert_(innerList.remaining()==3);
		EasyUtils.assertNotNull( innerList.pop());
		EasyUtils.assert_(innerList.remaining()==2);
		EasyUtils.assertNotNull( innerList.pop());
		EasyUtils.assert_(innerList.remaining()==1);
		EasyUtils.assertNotNull( innerList.pop());
		EasyUtils.assert_(innerList.remaining()==0);
		EasyUtils.assert_(!innerList.hasMore());
		EasyUtils.assert_(innerList.isEmpty());
		

		TokenList test2 = tl2.copy();
		System.out.println(test2);
		EasyUtils.assert_(test2.remaining()==9);
		test2.remove("{");
		EasyUtils.assert_(test2.remaining()==8);
		EasyUtils.assert_( test2.getLast().equals("}"));
		test2.removeLast("}");
		EasyUtils.assert_( test2.getLast().equals("d"));
		test2.removeLast("d");
		EasyUtils.assert_( test2.getLast().equals("}"));
		test2.removeLast("}");
		EasyUtils.assert_( test2.getLast().equals("as"));
		EasyUtils.assert_(test2.remaining()==5);
		
		
		
	}
}
