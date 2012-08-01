package net.raysforge.easylang.def;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.raysforge.easylang.EasyClassInterface;
import net.raysforge.easylang.EasyLang;
import net.raysforge.easylang.EasyMethod;
import net.raysforge.easylang.EasyMethodInterface;

public class EasyAssert implements EasyClassInterface {

	@Override
	public String getName() {
		return EasyLang.rb.getString("Assert");
	}

	static Map<String, EasyMethodInterface> methods = new HashMap<String, EasyMethodInterface>();

	static {
		add(new NativeMethod(EasyLang.rb.getString("Assert"), EasyLang.rb.getString("Assert.equals"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 2);
				EasyClassInterface p0 = parameter.get(0);
				EasyClassInterface p1 = parameter.get(1);
				if( ! (p0.getName().equals(p1.getName()) && p0.toString().equals(p1.toString())))
				{
					EasyLang.instance.writeln("ungleich: " + p0 + ", " + p1);
					System.err.println("ungleich: " + p0 + ", " + p1);
				}
				return null;
			}
		});

	}
	
	private static void add(NativeMethod nativeMethod) {
		methods.put(nativeMethod.getName(), nativeMethod);
	}
	
	@Override
	public EasyClassInterface getNewInstance(List<EasyClassInterface> parameter) {
		return new EasyAssert();
	}

	@Override
	public EasyMethodInterface getMethod(String methodName) {
		return methods.get(methodName);
	}

	@Override
	public Map<String, EasyMethodInterface> getMethods() {
		return methods;
	}

}
