package net.raysforge.rayslang;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

import net.raysforge.rayslang.def.NativeClass;

public class RayClass {

	protected String name;
	String package_="default";
	RayLang rayLang;
	public NativeClass nativeClass;
	
	HashMap<String, RayField> fields = new HashMap<String, RayField>();

	HashMap<String, RayMethod> methods = new HashMap<String, RayMethod>();

	public RayClass( RayLang rayLang, String name) {
		this.name = name;
		this.rayLang = rayLang;
		rayLang.classes.put( getFullName(), this);
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
				RayUtils.assert_( type != null);
				Visibility v = Visibility.protected_;
				if( ! tokenList.isEmpty())
					v = Visibility.valueOf(tokenList.pop()+"_"); 
				rc.fields.put( varName.s(), new RayField( v, type, varName.s()));
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

	public RayInstance getNewInstance() {
		
		if( nativeClass != null )
		{
			return new RayInstance(nativeClass.getNewInstance());
		} else {
			RayInstance ri = new RayInstance(this);
			
			Set<String> keySet = fields.keySet();
			for (String key : keySet) {
				RayField rayField = this.fields.get(key);
				ri.variables.put(key, new RayVar(rayField.visibility, 
						this, rayField.name,
						rayField.type.getNewInstance()));
			}
			return ri;
		}
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

	public boolean isNative() {
		return nativeClass != null;
	}

	/*
	public RayClass invokeNative(RayClass rc, String methodName, RayClass ... parameter) {
		if( methodName.equals("print") && (parameter.length == 0) )
		{
			System err.println(((RayString)rc).getValue());
		} else {
			RayUtils.RunExp("method not found: " + methodName);
		}
		return null;
	}
	*/

}
