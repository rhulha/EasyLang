package net.raysforge.rayslang.test;

import net.raysforge.rayslang.RaySource;
import net.raysforge.rayslang.RayUtils;
import net.raysforge.rayslang.TokenList;

public class TestTokenList {
	
	public static void main(String[] args) {
		RaySource rs = new RaySource("{\nZeichen zeile -> zeile.schreibe();\n}".toCharArray());
		TokenList tl = RayUtils.convertSourceToTokenList(rs);
		System.out.println("original: " + tl);
		tl.remove("{");
		TokenList innerList = tl.getInnerList('{', '}');
		System.out.println("innerList: " + innerList);
		
		if (innerList.contains("->")) {
			TokenList parameterList = innerList.getAndRemoveSourceTokenUntil("->", false);
			System.out.println("parameterList: " + parameterList);

			System.out.println("innerList.get(0): " + innerList.get(0));

			innerList.hideCodeBeforePosAndResetPos();
			System.out.println("innerList: " + innerList);
		}
	}

	public static void main1(String[] args) {
		RaySource rs = new RaySource("x.add!(7);".toCharArray());
		TokenList tl = RayUtils.convertSourceToTokenList(rs);
		System.out.println(tl);
		RayUtils.assert_(tl.remaining()==7);
		RayUtils.assert_(tl.startsWithPattern("i.i("));
		RayUtils.assert_(!tl.contains("add"));
		RayUtils.assert_(tl.contains("add!"));
		
		RaySource rs2 = new RaySource("{as{d -> as}d\n}\r\n".toCharArray());
		TokenList tl2 = RayUtils.convertSourceToTokenList(rs2);

		System.out.println(  tl2.copy().getAndRemoveSourceTokenUntil("->", true));;
		System.out.println(  tl2.copy().getAndRemoveSourceTokenUntil("->", false));;
		
		TokenList test1 = tl2.copy();
		
		test1.pop();
		test1.pop();
		test1.pop();
		TokenList innerList = test1.getInnerList('{', '}');
		System.out.println(innerList);
		
		RayUtils.assert_(innerList.hasMore());
		RayUtils.assert_(innerList.remaining()==3);
		RayUtils.assertNotNull( innerList.pop());
		RayUtils.assert_(innerList.remaining()==2);
		RayUtils.assertNotNull( innerList.pop());
		RayUtils.assert_(innerList.remaining()==1);
		RayUtils.assertNotNull( innerList.pop());
		RayUtils.assert_(innerList.remaining()==0);
		RayUtils.assert_(!innerList.hasMore());
		RayUtils.assert_(innerList.isEmpty());
		

		TokenList test2 = tl2.copy();
		System.out.println(test2);
		RayUtils.assert_(test2.remaining()==9);
		test2.remove("{");
		RayUtils.assert_(test2.remaining()==8);
		RayUtils.assert_( test2.getLast().equals("}"));
		test2.removeLast("}");
		RayUtils.assert_( test2.getLast().equals("d"));
		test2.removeLast("d");
		RayUtils.assert_( test2.getLast().equals("}"));
		test2.removeLast("}");
		RayUtils.assert_( test2.getLast().equals("as"));
		RayUtils.assert_(test2.remaining()==5);
		
		
		
	}
}
