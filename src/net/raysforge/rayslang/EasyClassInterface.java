package net.raysforge.rayslang;

import java.util.List;


public interface EasyClassInterface {

	public String getName();
	public EasyClassInterface invoke( String methodName, EasyMethod closure, List<EasyClassInterface> parameter);
	public EasyClassInterface getNewInstance( List<EasyClassInterface> parameter);

}
