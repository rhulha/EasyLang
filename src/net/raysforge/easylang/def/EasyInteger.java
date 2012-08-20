package net.raysforge.easylang.def;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.raysforge.commons.Generics;
import net.raysforge.easylang.EasyClassInterface;
import net.raysforge.easylang.EasyLang;
import net.raysforge.easylang.EasyMethod;
import net.raysforge.easylang.EasyMethodInterface;
import net.raysforge.easylang.utils.EasyLog;

public class EasyInteger implements EasyClassInterface {

	private long intValue;

	static Map<String, EasyMethodInterface> methods = new HashMap<String, EasyMethodInterface>();

	public EasyInteger() {
	}

	public EasyInteger(long value) {
		this.setIntValue(value);
	}

	@Override
	public EasyClassInterface getNewInstance(List<EasyClassInterface> parameter) {
		return new EasyInteger();
	}

	private static void add(NativeMethod nativeMethod) {
		methods.put(nativeMethod.getName(), nativeMethod);
	}

	static {
		add(new NativeMethod(EasyLang.rb.getString("Number"), EasyLang.rb.getString("Number.plus") + "!", null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyInteger instanceInt = (EasyInteger) instance;
				EasyInteger parameterInt = (EasyInteger) parameter.get(0);
				instanceInt.setIntValue(instanceInt.intValue + parameterInt.intValue);
				return instance;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Number"), EasyLang.rb.getString("Number.plus"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyInteger instanceInt = (EasyInteger) instance;
				EasyInteger parameterInt = (EasyInteger) parameter.get(0);
				return new EasyInteger(instanceInt.intValue + parameterInt.intValue);
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Number"), EasyLang.rb.getString("Number.minus") + "!", null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyInteger instanceInt = (EasyInteger) instance;
				EasyInteger parameterInt = (EasyInteger) parameter.get(0);
				instanceInt.setIntValue(instanceInt.intValue - parameterInt.intValue);
				return instance;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Number"), EasyLang.rb.getString("Number.minus"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyInteger instanceInt = (EasyInteger) instance;
				EasyInteger parameterInt = (EasyInteger) parameter.get(0);
				return new EasyInteger(instanceInt.intValue - parameterInt.intValue);
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Number"), EasyLang.rb.getString("Number.times") + "!", null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyInteger instanceInt = (EasyInteger) instance;
				EasyInteger parameterInt = (EasyInteger) parameter.get(0);
				instanceInt.setIntValue(instanceInt.intValue * parameterInt.intValue);
				return instance;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Number"), EasyLang.rb.getString("Number.times"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				EasyInteger instanceInt = (EasyInteger) instance;
				if (closure == null) {
					assertParameterSize(parameter, 1);
					EasyInteger parameterInt = (EasyInteger) parameter.get(0);
					return new EasyInteger(instanceInt.intValue * parameterInt.intValue);
				} else {
					List<EasyClassInterface> p = Generics.newArrayList();
					for (int i = 0; i < instanceInt.intValue; i++) {
						p.clear();
						p.add(new EasyInteger(i));
						closure.invoke(null, null, null, p);
					}
					return null;
				}
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Number"), EasyLang.rb.getString("Number.dividedBy") + "!", null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyInteger instanceInt = (EasyInteger) instance;
				EasyInteger parameterInt = (EasyInteger) parameter.get(0);
				instanceInt.setIntValue(instanceInt.intValue / parameterInt.intValue);
				return instance;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Number"), EasyLang.rb.getString("Number.dividedBy"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyInteger instanceInt = (EasyInteger) instance;
				EasyInteger parameterInt = (EasyInteger) parameter.get(0);
				return new EasyInteger(instanceInt.intValue / parameterInt.intValue);
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Number"), EasyLang.rb.getString("Number.square") + "!", null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 0);
				EasyInteger instanceInt = (EasyInteger) instance;
				instanceInt.setIntValue(instanceInt.intValue * instanceInt.intValue);
				return instance;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Number"), EasyLang.rb.getString("Number.square"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyInteger instanceInt = (EasyInteger) instance;
				return new EasyInteger(instanceInt.intValue + instanceInt.intValue);
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("String"), EasyLang.rb.getString("Number.append"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyInteger instanceInt = (EasyInteger) instance;
				return new EasyString(instanceInt.intValue + parameter.get(0).toString());
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Number"), EasyLang.rb.getString("Number.write"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 0);
				EasyInteger instanceInt = (EasyInteger) instance;
				EasyLang.instance.writeln(instanceInt);
				return instance;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Number"), EasyLang.rb.getString("Number.random"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 0);
				EasyInteger instanceInt = (EasyInteger) instance;
				return new EasyInteger(EasyLang.instance.random.nextInt((int) instanceInt.getIntValue()));
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Boolean"), EasyLang.rb.getString("Number.equals"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyInteger instanceInt = (EasyInteger) instance;
				EasyInteger parameterInt = (EasyInteger) parameter.get(0);
				boolean theyAreTheSame = (instanceInt.intValue == parameterInt.intValue);
				if (closure != null) {
					if (theyAreTheSame) {
						List<EasyClassInterface> p = Generics.newArrayList();
						closure.invoke(null, null, null, p);
					} else {
						if (elseClosure != null)
							elseClosure.invoke(null, null, null, null);
					}
				}
				return new EasyBoolean(theyAreTheSame);
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Boolean"), EasyLang.rb.getString("Number.isLowerThan"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyInteger instanceInt = (EasyInteger) instance;
				EasyInteger parameterInt = (EasyInteger) parameter.get(0);
				boolean isLowerThan = (instanceInt.intValue < parameterInt.intValue);
				if (closure != null) {
					List<EasyClassInterface> p = Generics.newArrayList();
					if (isLowerThan) {
						closure.invoke(null, null, null, p);
					} else {
						if (elseClosure != null) {
							elseClosure.invoke(null, null, null, p);
						}
					}
				}
				return new EasyBoolean(isLowerThan);
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Boolean"), EasyLang.rb.getString("Number.isBiggerThan"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, EasyMethod elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyInteger instanceInt = (EasyInteger) instance;
				EasyInteger parameterInt = (EasyInteger) parameter.get(0);
				boolean isBiggerThan = (instanceInt.intValue > parameterInt.intValue);
				if (closure != null) {
					List<EasyClassInterface> p = Generics.newArrayList();
					if (isBiggerThan) {
						closure.invoke(null, null, null, p);
					} else {
						if (elseClosure != null) {
							elseClosure.invoke(null, null, null, p);
						}
					}
				}
				return new EasyBoolean(isBiggerThan);
			}
		});
	}

	public long getIntValue() {
		return intValue;
	}

	public void setIntValue(long intValue) {
		EasyLog.debug.log(getName() + ".setIntValue: " + intValue);
		this.intValue = intValue;
	}

	@Override
	public String toString() {
		return "" + intValue;
	}

	@Override
	public String getName() {
		return EasyLang.rb.getString("Number");
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
