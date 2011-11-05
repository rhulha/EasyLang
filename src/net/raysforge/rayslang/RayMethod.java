package net.raysforge.rayslang;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RayMethod {

	String name;

	RayClass parentClass;

	List<RayVar> parameter = new ArrayList<RayVar>();

	RayVar returnType;

	String code;

	public static RayMethod parse(RayClass parentClass, RayClass type, String name, RaySource rs) {

		RayMethod rm = new RayMethod();
		rm.parentClass = parentClass;

		String parameter = rs.getInnerText('(', ')');
		System.out.println("parameter " + parameter);

		String sourceToken = rs.getSourceToken();
		if (!sourceToken.equals("{"))
			RayUtils.RunExp("missing { " + rs.pos);

		rm.code = rs.getInnerText('{', '}');

		return rm;
	}

	public RayVar invoke(String name, RayVar instance) {
		
		List<RayVar> variables = new ArrayList<RayVar>();

		RaySource rs = new RaySource(code.toCharArray());
		while (true) {
			LinkedList<Token> tokens = rs.getSourceTokenUntil(";");
			if (tokens.isEmpty())
				break;
			else {
				System.out.println("ex: " + tokens);
				
				if( tokens.get(0).isIdentifier() && tokens.get(1).isIdentifier() && tokens.get(2).isEqualsSign() && tokens.get(3).isValue() && tokens.get(4).isSemicolon() )
				{
					RayClass rc = parentClass.rayLang.getClass("default", tokens.get(0));
					
					RayVar rv = new RayVar(rc, tokens.get(1).s(), tokens.get(3).s());
					System.out.println("juhu: " + rv);
				}
					
				
			}
		}
		return null;
	}

}
