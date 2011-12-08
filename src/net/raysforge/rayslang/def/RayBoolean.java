package net.raysforge.rayslang.def;

import java.util.List;

import net.raysforge.rayslang.RayClassInterface;
import net.raysforge.rayslang.RayMethod;

public class RayBoolean implements RayClassInterface {

	private final boolean b;

	public RayBoolean(boolean b) {
		this.b = b;
	}

	@Override
	public String getName() {
		return "Bool";
	}

	@Override
	public RayClassInterface invoke(String methodName, RayMethod closure, List<RayClassInterface> parameter) {
		if (methodName.equals("oder") && parameter.size() == 1 && closure != null) {
			RayClassInterface p0 = parameter.get(0);
			if (p0 instanceof RayBoolean) {
				RayBoolean rb = (RayBoolean) p0;
				if (b || rb.b)
					closure.invoke(null);

			}
		}
		return null;
	}

	@Override
	public RayClassInterface getNewInstance(List<RayClassInterface> parameter) {
		return new RayBoolean(false);
	}

}
