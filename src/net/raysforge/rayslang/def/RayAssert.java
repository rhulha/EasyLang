package net.raysforge.rayslang.def;

import java.util.List;

import net.raysforge.rayslang.RayClassInterface;
import net.raysforge.rayslang.RayMethod;

public class RayAssert implements RayClassInterface {

	@Override
	public String getName() {
		return "Probe";
	}

	@Override
	public RayClassInterface invoke(String methodName, RayMethod closure, List<RayClassInterface> parameter) {
		int pc = 0;
		if( parameter != null)
			pc = parameter.size();

		if( methodName.equals("gleichheit") && pc == 2){
			RayClassInterface p0 = parameter.get(0);
			RayClassInterface p1 = parameter.get(1);
			if( ! (p0.getName().equals(p1.getName()) && p0.toString().equals(p1.toString())))
			{
				System.err.println("ungleich: " + p0 + ", " + p1);
			}
		}
		return null;
	}

	@Override
	public RayClassInterface getNewInstance(List<RayClassInterface> parameter) {
		return new RayAssert();
	}

}
