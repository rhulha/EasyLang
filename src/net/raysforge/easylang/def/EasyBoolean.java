package net.raysforge.easylang.def;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.raysforge.easylang.EasyClassInterface;
import net.raysforge.easylang.EasyLang;
import net.raysforge.easylang.EasyMethod;
import net.raysforge.easylang.EasyMethodInterface;

public class EasyBoolean implements EasyClassInterface {

	private final boolean b;

	public EasyBoolean() {
		this.b = false;
	}

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
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);

				EasyClassInterface p0 = parameter.get(0);
				if (p0 instanceof EasyBoolean) {
					EasyBoolean rb = (EasyBoolean) p0;
					if (((EasyBoolean) instance).b || rb.b) {
						if (closure != null)
							closure.invoke(null, null, null, null);
						return new EasyBoolean(true);
					}
				} else {
					System.out.println("parameter to Boolean.or must be Boolean");
				}
				return new EasyBoolean(false);
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Boolean"), EasyLang.rb.getString("Boolean.equals"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyClassInterface p0 = parameter.get(0);
				if (p0 instanceof EasyBoolean) {
					EasyBoolean rb = (EasyBoolean) p0;
					if (((EasyBoolean) instance).b == rb.b) {
						if (closure != null)
							closure.invoke(null, null, null, null);
						return new EasyBoolean(true);
					}
				} else {
					System.out.println("parameter to Boolean.equals must be Boolean");
				}
				return new EasyBoolean(false);
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Boolean"), EasyLang.rb.getString("Boolean.notEquals"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyClassInterface p0 = parameter.get(0);
				if (p0 instanceof EasyBoolean) {
					EasyBoolean rb = (EasyBoolean) p0;
					if (((EasyBoolean) instance).b != rb.b) {
						if (closure != null)
							closure.invoke(null, null, null, null);
						return new EasyBoolean(true);
					}
				} else {
					System.out.println("parameter to Boolean.equals must be Boolean");
				}
				return new EasyBoolean(false);
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("void"), EasyLang.rb.getString("Boolean.ifTrue"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 0);
				assertClosure(closure);
				if (((EasyBoolean) instance).b)
					closure.invoke(null, null, null, null);
				return null;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("void"), EasyLang.rb.getString("Boolean.ifFalse"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 0);
				assertClosure(closure);
				if (!((EasyBoolean) instance).b)
					closure.invoke(null, null, null, null);
				return null;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("void"), EasyLang.rb.getString("Boolean.write"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 0);
				EasyLang.instance.writeln(instance.toString());
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

	@Override
	public String toString() {
		return b ? EasyLang.rb.getString("Boolean.true") : EasyLang.rb.getString("Boolean.false");
	}
}
