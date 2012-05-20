package net.raysforge.rayslang;

import java.util.List;

public interface EasyMethodInterface {
	
	public String getName();
	
	public List<EasyVar> getParameterList();
	
	public String getReturnType();
	
	public EasyClassInterface invoke( EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter);
	

}
