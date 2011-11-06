package net.raysforge.rayslang;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RayClass {

	protected String name;
	String package_="default";
	RayLang rayLang;

	List<RayVar> variables = new ArrayList<RayVar>();

	HashMap<String, RayMethod> methods = new HashMap<String, RayMethod>();

	public static RayClass parse(RayLang rayLang, String name, File file) {
		return parse( rayLang, name, new RaySource(FileUtils.readCompleteFile(file)));
	}

	public static RayClass parse( RayLang rayLang, String name, RaySource rs ) {
		RayClass rc = new RayClass();
		rc.name = name;
		rc.rayLang = rayLang;

		while (true) {
			TokenList tokenList = rs.getSourceTokenUntil(";", "(");

			if (tokenList.size() == 0)
				break;
			Token last = tokenList.pollLast();
			if ( last.equals(";")) {
				
				Token varName = tokenList.pollLast();
				Token typeStr = tokenList.pollLast();
				RayClass type = rayLang.classes.get("default."+typeStr);
				Visibility v = Visibility.protected_;
				if( ! tokenList.isEmpty())
					v = Visibility.valueOf(tokenList.pop()+"_"); 
				rc.variables.add( new RayVar(v, type, varName.s(), ""));
				System.out.println("var: "+ type + " - " + varName);
			} else if (last.equals("(")) {
				
				Token methodName = tokenList.pollLast();
				Token typeStr = tokenList.pollLast();
				//Token visStr = tokenList.pollLast();
				RayClass type = rayLang.classes.get("default."+typeStr);

				RayMethod rm = RayMethod.parse( rc, type, methodName.s(), rs);
				rc.methods.put(methodName.s(), rm);
				// 
				// System.out.println("innerText: "+ innerText);
			} else if (last.equals("{")) {
				System.out.println("hm");
			}
			//System.out.println("XX" + token + "YY");
		}

		return rc;
	}

	public String getFullName() {
		return package_ + "." + name;
	}

	@Override
	public String toString() {
		return getFullName();
	}

	public RayMethod getMethod(String name) {
		return methods.get(name);
	}

}
