package net.raysforge.rayslang.def;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.raysforge.commons.Generics;
import net.raysforge.rayslang.RayClassInterface;
import net.raysforge.rayslang.RayLang;
import net.raysforge.rayslang.RayMethod;

public class RayArray implements RayClassInterface {

	HashMap<String, RayClassInterface> map = new HashMap<String, RayClassInterface>();
	ArrayList<RayClassInterface> list = new ArrayList<RayClassInterface>();

	private final String type;

	public RayArray(String type) {
		this.type = type;
	}

	@Override
	public String getName() {
		return type;
	}

	@Override
	public RayClassInterface invoke(String methodName, RayMethod closure, List<RayClassInterface> parameter) {
		if (methodName.equals("get") && parameter.size() == 1) {
			RayClassInterface p0 = parameter.get(0);
			RayClassInterface value=null;
			if (p0 instanceof RayInteger) {
				RayInteger ri = (RayInteger) p0;
				value = list.get((int) ri.getIntValue());
			} else if (p0 instanceof RayString) {
				RayString rs = (RayString) p0;
				value = map.get(rs.getStringValue());
			} else {
				RayLang.instance.writeln("array index must be int or string.");
			}
			RayLang.instance.writeln(type.substring(0, type.length()-3));
			return value != null ? value : RayLang.instance.getClass(type.substring(0, type.length()-3)).getNewInstance(null);

		} else if (methodName.equals("löschen") && parameter.size() == 0 && closure == null) {
			map.clear();
			list.clear();
		} else if (methodName.equals("entferne") && parameter.size() == 1 && closure == null) {
			RayClassInterface p0 = parameter.get(0);
			if (p0 instanceof RayString) {
				map.remove(p0.toString());
			}
		} else if (methodName.equals("anzahlSchlüssel") && parameter.size() == 0 && closure == null) {
			return new RayInteger(map.size());
		} else if (methodName.equals("enthältSchlüssel") && parameter.size() == 1 && closure != null) {
			RayClassInterface p0 = parameter.get(0);
			if (p0 instanceof RayString) {
				if (map.containsKey(p0.toString())) {
					closure.invoke(null);
				}
			}
		} else if (methodName.equals("enthältSchlüsselNicht") && parameter.size() == 1 && closure == null) {
			RayClassInterface p0 = parameter.get(0);
			return new RayBoolean(!map.containsKey(p0.toString()));
		} else if (methodName.equals("enthältSchlüsselNicht") && parameter.size() == 1 && closure != null) {
			RayClassInterface p0 = parameter.get(0);
			if (p0 instanceof RayString) {
				if (!map.containsKey(p0.toString())) {
					closure.invoke(null);
				}
			}
		} else if (methodName.equals("put") && parameter.size() == 2) {
			RayClassInterface p0 = parameter.get(0);
			RayClassInterface p1 = parameter.get(1);

			if (p0 instanceof RayInteger) {
				RayInteger ri = (RayInteger) p0;
				list.add((int) ri.getIntValue(), p1);
			} else if (p0 instanceof RayString) {
				RayString rs = (RayString) p0;
				map.put(rs.getStringValue(), p1);
			}
		} else if (methodName.equals("schreibe") && parameter.size() == 0) {
			for (String key : map.keySet()) {
				RayLang.instance.writeln(key + ": " + map.get(key));
			}
			for (RayClassInterface rci : list) {
				RayLang.instance.writeln(rci);
			}
			RayLang.instance.writeln("");
		} else if (methodName.equals("add") && parameter.size() == 1) {
			RayClassInterface p0 = parameter.get(0);

			if ((p0.getName() + "[]").equals(type)) {
				list.add(p0);
			} else {
				// error.
				RayLang.instance.writeln("error");
			}

		} else if (methodName.equals("fürJedenSchlüssel") && closure != null) {
			List<RayClassInterface> p = Generics.newArrayList();
			for (String key : map.keySet()) {
				p.clear();
				p.add(new RayString(key));
				closure.invoke(p);
			}
		} else {
			RayLang.instance.writeln("method not found: " + methodName);
		}
		return null;
	}

	public RayClassInterface get(int i) {
		return list.get(i) != null ? list.get(i) : RayLang.instance.getClass(type.substring(0, type.length()-2)).getNewInstance(null);
	}

	public RayClassInterface get(String key) {
		return map.get(key) != null ?  map.get(key) : RayLang.instance.getClass(type.substring(0, type.length()-2)).getNewInstance(null);
	}

	@Override
	public RayClassInterface getNewInstance(List<RayClassInterface> parameter) {
		return new RayArray(type); // parameter.get(0).toString()
	}

	public RayClassInterface put(String key, RayClassInterface value) {
		return map.put(key, value);
	}

	public RayClassInterface put(int index, RayClassInterface value) {
		while (list.size() <= index)
			list.add(null); // TODO: is there really no better way to do this ?
		return list.set(index, value);
	}

}
