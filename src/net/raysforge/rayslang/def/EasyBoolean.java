package net.raysforge.rayslang.def;

import java.util.List;

import net.raysforge.rayslang.EasyClassInterface;
import net.raysforge.rayslang.EasyMethod;

public class EasyBoolean implements EasyClassInterface {

	private final boolean b;

	public EasyBoolean(boolean b) {
		this.b = b;
	}

	@Override
	public String getName() {
		return "Bool";
	}

	@Override
	public EasyClassInterface invoke(String methodName, EasyMethod closure, List<EasyClassInterface> parameter) {
		if (methodName.equals("oder") && parameter.size() == 1 && closure != null) {
			EasyClassInterface p0 = parameter.get(0);
			if (p0 instanceof EasyBoolean) {
				EasyBoolean rb = (EasyBoolean) p0;
				if (b || rb.b)
					closure.invoke(null);

			}
		}
		return null;
	}

	@Override
	public EasyClassInterface getNewInstance(List<EasyClassInterface> parameter) {
		return new EasyBoolean(false);
	}

}
