package net.raysforge.rayslang.def;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.raysforge.commons.Generics;
import net.raysforge.rayslang.EasyClassInterface;
import net.raysforge.rayslang.EasyLang;
import net.raysforge.rayslang.EasyMethod;
import net.raysforge.rayslang.EasyMethodInterface;

public class EasyString implements EasyClassInterface {

	private String stringValue = "";

	public EasyString() {
	}

	public EasyString(String s) {
		stringValue = s;
	}

	static Map<String, EasyMethodInterface> methods = new HashMap<String, EasyMethodInterface>();

	static {
		add(new NativeMethod(EasyLang.rb.getString("void"), EasyLang.rb.getString("String.write"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 0);
				EasyLang.instance.writeln(instance.toString());
				return null;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("String"), EasyLang.rb.getString("String.append"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				return new EasyString(instance.toString() + parameter.get(0).toString());
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("String")+"[]", EasyLang.rb.getString("String.split"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyClassInterface p0 = parameter.get(0);
				String[] split = instance.toString().split(p0.toString());
				EasyArray ra = new EasyArray(getName() + "[]");
				for (String string : split) {
					ra.list.add(new EasyString(string));
				}
				return ra;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Number"), EasyLang.rb.getString("String.asNumber"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 0);
				return new EasyInteger(Long.parseLong(instance.toString()));
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Boolean"), EasyLang.rb.getString("String.equals"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyClassInterface p0 = parameter.get(0);
				if (closure != null) {
					if (instance.toString().equals(p0.toString())) {
						List<EasyClassInterface> p = Generics.newArrayList();
						closure.invoke(null, null, p);
					}
					return null;
				} else {
					return new EasyBoolean(instance.toString().equals(p0.toString()));
				}
			}
		});
	}

	private static void add(NativeMethod nativeMethod) {
		methods.put(nativeMethod.getName(), nativeMethod);
	}

	public EasyClassInterface getNewInstance(List<EasyClassInterface> parameter) {
		return new EasyString();
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	@Override
	public String getName() {
		return EasyLang.rb.getString("String");
	}

	@Override
	public String toString() {
		return stringValue;
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
