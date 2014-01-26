package net.raysforge.easylang.def;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.raysforge.commons.Generics;
import net.raysforge.easylang.EasyClassInterface;
import net.raysforge.easylang.EasyLang;
import net.raysforge.easylang.EasyMethodInterface;

public class EasyArray implements EasyClassInterface {

	HashMap<String, EasyClassInterface> map = new HashMap<String, EasyClassInterface>();
	ArrayList<EasyClassInterface> list = new ArrayList<EasyClassInterface>();

	private final String type;

	public EasyArray(String type) {
		this.type = type;
	}

	@Override
	public String getName() {
		return type;
	}

	static Map<String, EasyMethodInterface> methods = new HashMap<String, EasyMethodInterface>();

	static {
		add(new NativeMethod("", EasyLang.rb.getString("Array.get"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethodInterface closure, EasyMethodInterface elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyArray easyArray = (EasyArray) instance;
				EasyClassInterface p0 = parameter.get(0);
				EasyClassInterface value = null;
				if (p0 instanceof EasyInteger) {
					EasyInteger ei = (EasyInteger) p0;
					value = easyArray.list.get((int) ei.getIntValue());
				} else if (p0 instanceof EasyString) {
					EasyString rs = (EasyString) p0;
					value = easyArray.map.get(rs.getStringValue());
				} else {
					EasyLang.instance.writeln("array index must be int or string.");
				}
				EasyLang.instance.writeln(easyArray.type.substring(0, easyArray.type.length() - 3));
				return value != null ? value : EasyLang.instance.getClass(easyArray.type.substring(0, easyArray.type.length() - 3)).getNewInstance(null);
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("void"), EasyLang.rb.getString("Array.clear"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethodInterface closure, EasyMethodInterface elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 0);
				EasyArray easyArray = (EasyArray) instance;
				easyArray.map.clear();
				easyArray.list.clear();
				return null;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("void"), EasyLang.rb.getString("Array.remove"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethodInterface closure, EasyMethodInterface elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyArray easyArray = (EasyArray) instance;
				EasyClassInterface p0 = parameter.get(0);
				if (p0 instanceof EasyString) {
					easyArray.map.remove(p0.toString());
				} else if (p0 instanceof EasyInteger) {
					EasyInteger ei = (EasyInteger) p0;
					easyArray.list.remove(ei.getIntValue());
				}
				return null;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Number"), EasyLang.rb.getString("Array.keyCount"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethodInterface closure, EasyMethodInterface elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 0);
				EasyArray easyArray = (EasyArray) instance;
				return new EasyInteger(easyArray.map.size());
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Boolean"), EasyLang.rb.getString("Array.containsKey"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethodInterface closure, EasyMethodInterface elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyArray easyArray = (EasyArray) instance;
				EasyClassInterface p0 = parameter.get(0);
				if (!(p0 instanceof EasyString)) {
					throw new RuntimeException("parameter must be a String");
				}
				if (closure != null && easyArray.map.containsKey(p0.toString())) {
					closure.invoke(null, null, null, null);
				}
				return new EasyBoolean(easyArray.map.containsKey(p0.toString()));
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Boolean"), EasyLang.rb.getString("Array.containsKeyNot"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethodInterface closure, EasyMethodInterface elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyArray easyArray = (EasyArray) instance;
				EasyClassInterface p0 = parameter.get(0);
				if (!(p0 instanceof EasyString)) {
					throw new RuntimeException("parameter must be a String");
				}
				if (closure != null && !easyArray.map.containsKey(p0.toString())) {
					closure.invoke(null, null, null, null);
				}
				EasyBoolean easyBoolean = new EasyBoolean(!easyArray.map.containsKey(p0.toString()));
				return easyBoolean;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("void"), EasyLang.rb.getString("Array.write"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethodInterface closure, EasyMethodInterface elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 0);
				EasyArray easyArray = (EasyArray) instance;

				for (String key : easyArray.map.keySet()) {
					EasyLang.instance.writeln(key + ": " + easyArray.map.get(key));
				}
				for (EasyClassInterface rci : easyArray.list) {
					EasyLang.instance.writeln(rci);
				}
				EasyLang.instance.writeln("");
				return null;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("void"), EasyLang.rb.getString("Array.add"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethodInterface closure, EasyMethodInterface elseClosure, List<EasyClassInterface> parameter) {
				EasyArray easyArray = (EasyArray) instance;

				EasyClassInterface p0 = parameter.get(0);

				if (parameter.size() == 1) {
					if ((p0.getName() + "[]").equals(easyArray.type)) {
						easyArray.list.add(p0);
					}
					return p0;
				} else if (parameter.size() == 2) {
					EasyClassInterface p1 = parameter.get(1);

					if (p0 instanceof EasyInteger) {
						EasyInteger ri = (EasyInteger) p0;
						easyArray.list.add((int) ri.getIntValue(), p1);
					} else if (p0 instanceof EasyString) {
						EasyString rs = (EasyString) p0;
						easyArray.map.put(rs.getStringValue(), p1);
					}
					return p1;
				} else {
					throw new RuntimeException("wrong parameter count ( 1 or 2 )");
				}
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("void"), EasyLang.rb.getString("Array.forEachKey"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethodInterface closure, EasyMethodInterface elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 0);
				assertClosure(closure);
				EasyArray easyArray = (EasyArray) instance;
				
				List<EasyClassInterface> p = Generics.newArrayList();
				for (String key : easyArray.map.keySet()) {
					p.clear();
					p.add(new EasyString(key));
					closure.invoke(null, null, null, p);
				}
				return null;
			}
		});

	}

	private static void add(NativeMethod nativeMethod) {
		methods.put(nativeMethod.getName(), nativeMethod);
	}

	public EasyClassInterface get(int i) {
		return list.get(i) != null ? list.get(i) : EasyLang.instance.getClass(type.substring(0, type.length() - 2)).getNewInstance(null);
	}

	public EasyClassInterface get(String key) {
		return map.get(key) != null ? map.get(key) : EasyLang.instance.getClass(type.substring(0, type.length() - 2)).getNewInstance(null);
	}

	@Override
	public EasyClassInterface getNewInstance(List<EasyClassInterface> parameter) {
		return new EasyArray(type); // parameter.get(0).toString()
	}

	public EasyClassInterface put(String key, EasyClassInterface value) {
		return map.put(key, value);
	}

	public EasyClassInterface put(int index, EasyClassInterface value) {
		while (list.size() <= index)
			list.add(null); // TODO: is there really no better way to do this ?
		return list.set(index, value);
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
