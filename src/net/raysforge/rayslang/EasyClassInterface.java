package net.raysforge.rayslang;

import java.util.List;
import java.util.Map;


public interface EasyClassInterface {

	public String getName();
	public EasyMethodInterface getMethod(String methodName);
	public Map<String, EasyMethodInterface> getMethods();
	public EasyClassInterface getNewInstance( List<EasyClassInterface> parameter);

}
