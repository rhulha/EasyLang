package net.raysforge.rayslang;

import java.util.List;


public interface RayClassInterface {

	public String getName();
	public RayClassInterface invoke( String methodName, RayClassInterface... parameter);
	public RayClassInterface getNewInstance( List<RayClassInterface> parameter);

}
