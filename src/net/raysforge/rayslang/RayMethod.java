package net.raysforge.rayslang;

import java.util.ArrayList;
import java.util.List;

public class RayMethod {

	String name;

	RayClass parentClass;

	List<RayVar> parameter = new ArrayList<RayVar>();

	RayVar returnType;

	RaySource code;

	public static RayMethod parse(RayClass parentClass, RayClass type, String name, RaySource rs) {

		RayMethod rm = new RayMethod();
		rm.parentClass = parentClass;

		RaySource parameter = rs.getInnerText('(', ')');
		System.out.println("parameter " + parameter);

		Token token = rs.getSourceToken();
		if (!token.isOpenBrace())
			RayUtils.RunExp("missing { " + rs.pos);

		rm.code = rs.getInnerText('{', '}');

		return rm;
	}

	public RayVar invoke(String name, RayVar instance) {
		
		List<RayVar> variables = new ArrayList<RayVar>();

		RaySource rs = new RaySource(code.src.clone());
		while (true) {
			TokenList tokenList = rs.getSourceTokenUntil(";", "(");
			if (tokenList.isEmpty())
				break;
			else {
				if( tokenList.equalsPattern("ii=v;") )
				{
					System.out.println("variable decl. and assignment found: " + tokenList);

					RayClass rc = parentClass.rayLang.getClass("default", tokenList.get(0));
					
					RayVar rv = new RayVar(rc, tokenList.get(1).s(), tokenList.get(3).s());

					variables.add(rv);
					
				} else if( tokenList.equalsPattern("i.i("))
				{
					System.out.println("message invocation found: " + tokenList);
					RaySource params = rs.getInnerText('(', ')');
					TokenList paramTokenList = params.getSourceTokenUntil();
					
				} else {
					System.out.println("unknown code in line: " + tokenList);
				}
					
				
			}
		}
		return null;
	}

}
