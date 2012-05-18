package net.raysforge.rayslang.def;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import net.raysforge.rayslang.EasyClassInterface;
import net.raysforge.rayslang.EasyLang;
import net.raysforge.rayslang.EasyMethod;

public class EasyGraphics implements EasyClassInterface {
	
	private Graphics g;

	public EasyGraphics() {
	}

	public EasyGraphics(Graphics g) {
		this.g = g;
	}

	@Override
	public String getName() {
		return "Grafik";
	}

	@Override
	public EasyClassInterface invoke(String methodName, EasyMethod closure, List<EasyClassInterface> parameter) {
		if( methodName.equals("setzeFarbe") && parameter.size() == 1)
		{
			g.setColor(Color.decode(parameter.get(0).toString()));
		} else if( methodName.equals("holeFarbe") && parameter.size() == 0)
		{
			return new EasyString( "0x"+Integer.toHexString( g.getColor().getRGB()&0xffffff));
		} else if( methodName.equals("fülleRechteck") && parameter.size() == 4){
			int i1 = (int) ((EasyInteger)parameter.get(0)).getIntValue();
			int i2 = (int) ((EasyInteger)parameter.get(1)).getIntValue();
			int i3 = (int) ((EasyInteger)parameter.get(2)).getIntValue();
			int i4 = (int) ((EasyInteger)parameter.get(3)).getIntValue();
			g.fillRect( i1, i2, i3, i4);
		} else if( methodName.equals("maleRechteck") && parameter.size() == 4) {
			int i1 = (int) ((EasyInteger)parameter.get(0)).getIntValue();
			int i2 = (int) ((EasyInteger)parameter.get(1)).getIntValue();
			int i3 = (int) ((EasyInteger)parameter.get(2)).getIntValue();
			int i4 = (int) ((EasyInteger)parameter.get(3)).getIntValue();
			g.drawRect( i1, i2, i3, i4);
		}

		return this;
	}

	@Override
	public EasyClassInterface getNewInstance(List<EasyClassInterface> parameter) {
		return new EasyGraphics();
	}

	public static void main(String[] args) {
		EasyLang.instance.writeln(Color.YELLOW);
		String s = Integer.toHexString(Color.YELLOW.getRGB()&0xffffff);
		EasyLang.instance.writeln(s);
		EasyLang.instance.writeln(Color.decode("0x"+s));
	}
}
