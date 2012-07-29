package net.raysforge.easylang.def;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.raysforge.easylang.EasyClassInterface;
import net.raysforge.easylang.EasyLang;
import net.raysforge.easylang.EasyMethod;
import net.raysforge.easylang.EasyMethodInterface;
import net.raysforge.easyswing.EasyList;

public class EasyLangList implements EasyClassInterface {

	EasyList list = new EasyList();
	
	static Map<String, EasyMethodInterface> methods = new HashMap<String, EasyMethodInterface>();

	static {
		add(new NativeMethod("", EasyLang.rb.getString("List.setFontSize"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyLangList easyLangList = (EasyLangList) instance;
				EasyInteger parameterInt = (EasyInteger) parameter.get(0);
				easyLangList.list.setFontSize(parameterInt.getIntValue());
				return null;
			}
		});
		add(new NativeMethod("", EasyLang.rb.getString("List.add"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyLangList easyLangList = (EasyLangList) instance;
				easyLangList.list.addElement(parameter.get(0));
				return null;
			}
		});
		add(new NativeMethod("", EasyLang.rb.getString("List.get"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyLangList easyLangList = (EasyLangList) instance;
				EasyInteger parameterInt = (EasyInteger) parameter.get(0);
				return (EasyClassInterface) easyLangList.list.get((int)parameterInt.getIntValue());
			}
		});
		add(new NativeMethod("", EasyLang.rb.getString("List.set"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 2);
				EasyLangList easyLangList = (EasyLangList) instance;
				EasyInteger parameterInt = (EasyInteger) parameter.get(0);
				easyLangList.list.set((int)parameterInt.getIntValue(), parameter.get(1));
				return null;
			}
		});
		add(new NativeMethod("", EasyLang.rb.getString("List.count"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 0);
				EasyLangList easyLangList = (EasyLangList) instance;
				return new EasyInteger(easyLangList.list.getSize());
			}
		});
	}
	
	@Override
	public String getName() {
		return EasyLang.rb.getString("List");
	}

	@Override
	public EasyMethodInterface getMethod(String methodName) {
		return methods.get(methodName);
	}

	@Override
	public Map<String, EasyMethodInterface> getMethods() {
		return methods;
	}

	@Override
	public EasyClassInterface getNewInstance(List<EasyClassInterface> parameter) {
		return new EasyLangList();
	}

	private static void add(NativeMethod nativeMethod) {
		methods.put(nativeMethod.getName(), nativeMethod);
	}
}
