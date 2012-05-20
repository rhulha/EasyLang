package net.raysforge.rayslang.def;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.raysforge.rayslang.EasyClassInterface;
import net.raysforge.rayslang.EasyLang;
import net.raysforge.rayslang.EasyMethod;
import net.raysforge.rayslang.EasyMethodInterface;

public class EasyBoolean implements EasyClassInterface {

	private final boolean b;

	public EasyBoolean(boolean b) {
		this.b = b;
	}

	@Override
	public String getName() {
		return EasyLang.rb.getString("Boolean");
	}
	
	static Map<String, EasyMethodInterface> methods = new HashMap<String, EasyMethodInterface>();

	static {
		add(new NativeMethod(EasyLang.rb.getString("Boolean"), EasyLang.rb.getString("Boolean.or"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				assertClosure( closure);
				
				EasyClassInterface p0 = parameter.get(0);
				if (p0 instanceof EasyBoolean) {
					EasyBoolean rb = (EasyBoolean) p0;
					if (((EasyBoolean)instance).b || rb.b)
						closure.invoke(instance, null, null);
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
		return new EasyBoolean(false);
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
