package net.raysforge.rayslang.def;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.raysforge.rayslang.RayClassInterface;
import net.raysforge.rayslang.RayMethod;

public class RayArray implements RayClassInterface {

	HashMap<String, RayClassInterface> map = new HashMap<String, RayClassInterface>();
	List<RayClassInterface> list = new ArrayList<RayClassInterface>();

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
			if (p0 instanceof RayInteger) {
				RayInteger ri = (RayInteger) p0;
				return list.get((int) ri.getIntValue());
			} else if (p0 instanceof RayString) {
				RayString rs = (RayString) p0;
				return map.get(rs.getStringValue());
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
				System.out.println(key + ": " + map.get(key));
			}
			for (RayClassInterface rci : list) {
				System.out.println(rci);
			}
			System.out.println();
		} else if (methodName.equals("add") && parameter.size() == 1) {
			RayClassInterface p0 = parameter.get(0);

			if ((p0.getName()+"[]").equals(type) ) {
				list.add( p0);
			} else  {
				// error.
			}

		} else {
			// error.
		}
		return null;
	}

	@Override
	public RayClassInterface getNewInstance(List<RayClassInterface> parameter) {
		return new RayArray(parameter.get(0).toString());
	}


}
