package net.raysforge.rayslang.def;

import java.util.List;

import net.raysforge.rayslang.EasyClassInterface;
import net.raysforge.rayslang.EasyMethod;

public class EasyAssert implements EasyClassInterface {

	@Override
	public String getName() {
		return "Probe";
	}

	@Override
	public EasyClassInterface invoke(String methodName, EasyMethod closure, List<EasyClassInterface> parameter) {
		int pc = 0;
		if( parameter != null)
			pc = parameter.size();

		if( methodName.equals("gleichheit") && pc == 2){
			EasyClassInterface p0 = parameter.get(0);
			EasyClassInterface p1 = parameter.get(1);
			if( ! (p0.getName().equals(p1.getName()) && p0.toString().equals(p1.toString())))
			{
				System.err.println("ungleich: " + p0 + ", " + p1);
			}
		}
		return null;
	}

	@Override
	public EasyClassInterface getNewInstance(List<EasyClassInterface> parameter) {
		return new EasyAssert();
	}

}
