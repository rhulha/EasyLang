package net.raysforge.rayslang;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

import net.raysforge.rayslang.def.RayString;

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
				rc.variables.put( varName.s(), new RayVar( v, varName.s(), type.getNewInstance()));
				RayLog.trace("var: "+ type + " - " + varName);
			} else if (last.equals("(")) {
				
				Token methodName = tokenList.pollLast();
				Token typeStr = tokenList.pollLast();
				//Token visStr = tokenList.pollLast();
				RayClass type = rayLang.classes.get("default."+typeStr);

				RayMethod.parse( rc, type, methodName.s(), rs);

				// RayLog.log("innerText: "+ innerText);
			} else if (last.equals("{")) {
				RayLog.warn("hm");
			}
			//RayLog.log("XX" + token + "YY");
		}

		return rc;
	}

	public RayClass getNewInstance() {
		RayClass rc = extracted();
		rc.methods = methods;
		rc.package_ = package_;
		rc.variables = RayUtils.newHashMap();
		Set<String> keySet = variables.keySet();
		for (String key : keySet) {
			RayVar rayVar = rc.variables.get(key);
			rc.variables.put(key, new RayVar(rayVar.visibility, rayVar.name, rayVar.reference));
		}
		return rc;
	}

	private RayClass extracted()  {
		RayClass rc=null;
		try {
			rc = (RayClass) this.getClass().getConstructors()[0].newInstance(rayLang);
		} catch (Exception e) {
			RayUtils.RunExp(e.getMessage());
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
	
	public String getValue()
	{
		return "class: " + getFullName();
	}
	
	public void setValue(String s) {
		RayUtils.RunExp("setValue not supported on: " + this.getFullName());
	}

	public RayClass invokeNative(RayClass rc, String methodName, RayClass ... parameter) {
		if( methodName.equals("print") && (parameter.length == 0) )
		{
			System.err.println(((RayString)rc).getValue());
		} else {
			RayUtils.RunExp("method not found: " + methodName);
		}
		return null;
	}

}
