package net.raysforge.rayslang;

import java.util.ArrayList;
import java.util.List;

public class RayMethod {
	
	String name;
	
	List<RayVar> parameter = new ArrayList<RayVar>();
	
	RayVar returnType;
	
	String code;

	public static RayMethod parse(String type, String name, RaySource rs) {
		
		RayMethod rm = new RayMethod();
		
		String parameter = rs.getInnerText( '(', ')');
		
		rm.code = rs.getInnerText( '{', '}');

		
		return rm;
	}

}
