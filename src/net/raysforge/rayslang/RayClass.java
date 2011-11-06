package net.raysforge.rayslang;

import java.io.File;
import java.util.HashMap;

public class RayClass {

	protected String name;
	String package_="default";
	RayLang rayLang;

	HashMap<String, RayVar> variables = new HashMap<String, RayVar>();

	HashMap<String, RayMethod> methods = new HashMap<String, RayMethod>();

	public RayClass(RayLang rayLang) {
		this.rayLang = rayLang;
		rayLang.classes.put( getFullName(), this);
	}

	public RayClass(RayLang rayLang, String name) {
		this( rayLang);
		this.name = name;
	}

	public static RayClass parse(RayLang rayLang, String name, File file) {
		return parse( rayLang, name, new RaySource(FileUtils.readCompleteFile(file)));
	}

	public static RayClass parse( RayLang rayLang, String name, RaySource rs ) {
		RayClass rc = new RayClass(rayLang, name);

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
				rc.variables.put( varName.s(), new RayVar(v, type, varName.s(), ""));
				System.out.println("var: "+ type + " - " + varName);
			} else if (last.equals("(")) {
				
				Token methodName = tokenList.pollLast();
				Token typeStr = tokenList.pollLast();
				//Token visStr = tokenList.pollLast();
				RayClass type = rayLang.classes.get("default."+typeStr);

				RayMethod.parse( rc, type, methodName.s(), rs);

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

	public RayVar getNewInstance() {
		return new RayVar(this, "unimportant", "");
	}

	public RayVar invokeNative(RayVar instance, String methodName, RayVar ... parameter) {
		if( methodName.equals("print") && (parameter.length == 0) )
		{
			System.err.println(instance.getValue());
		} else {
			RayUtils.RunExp("method not found: " + methodName);
		}
		return null;
	}

}
