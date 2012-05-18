package net.raysforge.rayslang.def;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.raysforge.commons.Generics;
import net.raysforge.rayslang.EasyLang;
import net.raysforge.rayslang.KeyWords;
import net.raysforge.rayslang.EasyClassInterface;
import net.raysforge.rayslang.EasyMethod;
import net.raysforge.rayslang.EasyUtils;

public class EasyString implements EasyClassInterface {

	private String stringValue = "";

	public EasyString() {
	}

	static Map<String, NativeMethod> methods = new HashMap<String, NativeMethod>();

	static {
		add(new NativeMethod("void", EasyLang.rb.getString("String.write"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				EasyLang.instance.writeln(((EasyString) instance).stringValue);
				return null;
			}
		});
		add(new NativeMethod("void", EasyLang.rb.getString("String.write"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				EasyLang.instance.writeln(((EasyString) instance).stringValue);
				return null;
			}
		});
	}

	public EasyString(String s) {
		stringValue = s;
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
		return KeyWords.CLASS_STRING;
	}

	@Override
	public EasyClassInterface invoke(String methodName, EasyMethod closure, List<EasyClassInterface> parameter) {
		if (methodName.equals("schreibe") && (parameter.size() == 0)) {

		} else if (methodName.equals("und") && parameter.size() == 1) {
			return new EasyString(stringValue + parameter.get(0).toString());
		} else if (methodName.equals("spalte") && (parameter.size() == 1)) {
			EasyClassInterface p0 = parameter.get(0);
			String[] split = stringValue.split(p0.toString());
			EasyArray ra = new EasyArray(getName() + "[]");
			for (String string : split) {
				ra.list.add(new EasyString(string));
			}
			return ra;
		} else if (methodName.equals("alsZahl")) {
			return new EasyInteger(Long.parseLong(stringValue));
		} else if ((methodName.equals("istGleich") || methodName.equals("gleicht")) && (parameter.size() == 1)) {
			EasyClassInterface p0 = parameter.get(0);
			if (closure != null) {
				if (stringValue.equals(p0.toString())) {
					List<EasyClassInterface> p = Generics.newArrayList();
					closure.invoke(p);
				}
			} else {
				return new EasyBoolean(stringValue.equals(p0.toString()));
			}
		} else {
			EasyUtils.runtimeExcp("method not found: " + methodName);
		}
		return null;
	}

	@Override
	public String toString() {
		return stringValue;
	}

}
