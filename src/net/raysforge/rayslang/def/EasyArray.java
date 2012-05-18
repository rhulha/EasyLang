package net.raysforge.rayslang.def;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.raysforge.commons.Generics;
import net.raysforge.rayslang.EasyClassInterface;
import net.raysforge.rayslang.EasyLang;
import net.raysforge.rayslang.EasyMethod;

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

	@Override
	public EasyClassInterface invoke(String methodName, EasyMethod closure, List<EasyClassInterface> parameter) {
		if (methodName.equals("get") && parameter.size() == 1) {
			EasyClassInterface p0 = parameter.get(0);
			EasyClassInterface value=null;
			if (p0 instanceof EasyInteger) {
				EasyInteger ri = (EasyInteger) p0;
				value = list.get((int) ri.getIntValue());
			} else if (p0 instanceof EasyString) {
				EasyString rs = (EasyString) p0;
				value = map.get(rs.getStringValue());
			} else {
				EasyLang.instance.writeln("array index must be int or string.");
			}
			EasyLang.instance.writeln(type.substring(0, type.length()-3));
			return value != null ? value : EasyLang.instance.getClass(type.substring(0, type.length()-3)).getNewInstance(null);

		} else if (methodName.equals("löschen") && parameter.size() == 0 && closure == null) {
			map.clear();
			list.clear();
		} else if (methodName.equals("entferne") && parameter.size() == 1 && closure == null) {
			EasyClassInterface p0 = parameter.get(0);
			if (p0 instanceof EasyString) {
				map.remove(p0.toString());
			}
		} else if (methodName.equals("anzahlSchlüssel") && parameter.size() == 0 && closure == null) {
			return new EasyInteger(map.size());
		} else if (methodName.equals("enthältSchlüssel") && parameter.size() == 1 && closure != null) {
			EasyClassInterface p0 = parameter.get(0);
			if (p0 instanceof EasyString) {
				if (map.containsKey(p0.toString())) {
					closure.invoke(null);
				}
			}
		} else if (methodName.equals("enthältSchlüsselNicht") && parameter.size() == 1 && closure == null) {
			EasyClassInterface p0 = parameter.get(0);
			return new EasyBoolean(!map.containsKey(p0.toString()));
		} else if (methodName.equals("enthältSchlüsselNicht") && parameter.size() == 1 && closure != null) {
			EasyClassInterface p0 = parameter.get(0);
			if (p0 instanceof EasyString) {
				if (!map.containsKey(p0.toString())) {
					closure.invoke(null);
				}
			}
		} else if (methodName.equals("put") && parameter.size() == 2) {
			EasyClassInterface p0 = parameter.get(0);
			EasyClassInterface p1 = parameter.get(1);

			if (p0 instanceof EasyInteger) {
				EasyInteger ri = (EasyInteger) p0;
				list.add((int) ri.getIntValue(), p1);
			} else if (p0 instanceof EasyString) {
				EasyString rs = (EasyString) p0;
				map.put(rs.getStringValue(), p1);
			}
		} else if (methodName.equals("schreibe") && parameter.size() == 0) {
			for (String key : map.keySet()) {
				EasyLang.instance.writeln(key + ": " + map.get(key));
			}
			for (EasyClassInterface rci : list) {
				EasyLang.instance.writeln(rci);
			}
			EasyLang.instance.writeln("");
		} else if (methodName.equals("add") && parameter.size() == 1) {
			EasyClassInterface p0 = parameter.get(0);

			if ((p0.getName() + "[]").equals(type)) {
				list.add(p0);
			} else {
				// error.
				EasyLang.instance.writeln("error");
			}

		} else if (methodName.equals("fürJedenSchlüssel") && closure != null) {
			List<EasyClassInterface> p = Generics.newArrayList();
			for (String key : map.keySet()) {
				p.clear();
				p.add(new EasyString(key));
				closure.invoke(p);
			}
		} else {
			EasyLang.instance.writeln("method not found: " + methodName);
		}
		return null;
	}

	public EasyClassInterface get(int i) {
		return list.get(i) != null ? list.get(i) : EasyLang.instance.getClass(type.substring(0, type.length()-2)).getNewInstance(null);
	}

	public EasyClassInterface get(String key) {
		return map.get(key) != null ?  map.get(key) : EasyLang.instance.getClass(type.substring(0, type.length()-2)).getNewInstance(null);
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

}
