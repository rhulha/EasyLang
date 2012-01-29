package net.raysforge.rayslang.def;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import net.raysforge.rayslang.RayClassInterface;
import net.raysforge.rayslang.RayLang;
import net.raysforge.rayslang.RayMethod;

public class RayGraphics implements RayClassInterface {
	
	private Graphics g;

	public RayGraphics() {
	}

	public RayGraphics(Graphics g) {
		this.g = g;
	}

	@Override
	public String getName() {
		return "Grafik";
	}

	@Override
	public RayClassInterface invoke(String methodName, RayMethod closure, List<RayClassInterface> parameter) {
		if( methodName.equals("setzeFarbe") && parameter.size() == 1)
		{
			g.setColor(Color.decode(parameter.get(0).toString()));
		} else if( methodName.equals("holeFarbe") && parameter.size() == 0)
		{
			return new RayString( "0x"+Integer.toHexString( g.getColor().getRGB()&0xffffff));
		} else if( methodName.equals("fülleRechteck") && parameter.size() == 4){
			int i1 = (int) ((RayInteger)parameter.get(0)).getIntValue();
			int i2 = (int) ((RayInteger)parameter.get(1)).getIntValue();
			int i3 = (int) ((RayInteger)parameter.get(2)).getIntValue();
			int i4 = (int) ((RayInteger)parameter.get(3)).getIntValue();
			g.fillRect( i1, i2, i3, i4);
		} else if( methodName.equals("maleRechteck") && parameter.size() == 4) {
			int i1 = (int) ((RayInteger)parameter.get(0)).getIntValue();
			int i2 = (int) ((RayInteger)parameter.get(1)).getIntValue();
			int i3 = (int) ((RayInteger)parameter.get(2)).getIntValue();
			int i4 = (int) ((RayInteger)parameter.get(3)).getIntValue();
			g.drawRect( i1, i2, i3, i4);
		}

		return this;
	}

	@Override
	public RayClassInterface getNewInstance(List<RayClassInterface> parameter) {
		return new RayGraphics();
	}

	public static void main(String[] args) {
		RayLang.instance.writeln(Color.YELLOW);
		String s = Integer.toHexString(Color.YELLOW.getRGB()&0xffffff);
		RayLang.instance.writeln(s);
		RayLang.instance.writeln(Color.decode("0x"+s));
	}
}
