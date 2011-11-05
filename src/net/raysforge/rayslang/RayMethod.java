package net.raysforge.rayslang;

import java.util.ArrayList;
import java.util.List;

public class RayMethod {
	
	String name;
	
	RayClass parentClass;
	
	List<RayVar> parameter = new ArrayList<RayVar>();
	
	RayVar returnType;
	
	String code;
	
	public static RayMethod parse( RayClass parentClass, String type, String name, RaySource rs) {
		
		RayMethod rm = new RayMethod();
		rm.parentClass = parentClass;
		
		String parameter = rs.getInnerText( '(', ')');
		System.out.println("parameter "+ parameter);
		
		String sourceToken = rs.getSourceToken();
		if( ! sourceToken.equals("{"))
			RayUtils.RunExp("missing { " + rs.pos);
		
		rm.code = rs.getInnerText( '{', '}');
		
		return rm;
	}

}
