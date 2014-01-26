package net.raysforge.easylang.def;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.raysforge.easylang.EasyClassInterface;
import net.raysforge.easylang.EasyLang;
import net.raysforge.easylang.EasyMethodInterface;

public class EasyGraphics implements EasyClassInterface {
	
	private Graphics g;

	public EasyGraphics() {
	}

	public EasyGraphics(Graphics g) {
		this.g = g;
	}

	@Override
	public String getName() {
		return EasyLang.rb.getString("Graphics");
	}
	
	static Map<String, EasyMethodInterface> methods = new HashMap<String, EasyMethodInterface>();

	static {
		add(new NativeMethod(EasyLang.rb.getString("Graphics"), EasyLang.rb.getString("Graphics.setColor"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethodInterface closure, EasyMethodInterface elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 1);
				EasyClassInterface p0 = parameter.get(0);
				EasyGraphics eg = (EasyGraphics) instance;
				eg.g.setColor(Color.decode(p0.toString()));
				return instance;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("String"), EasyLang.rb.getString("Graphics.getColor"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethodInterface closure, EasyMethodInterface elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 0);
				EasyGraphics eg = (EasyGraphics) instance;
				return new EasyString( "0x"+Integer.toHexString( eg.g.getColor().getRGB()&0xffffff));
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Graphics"), EasyLang.rb.getString("Graphics.fillRect"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethodInterface closure, EasyMethodInterface elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 4);
				int i1 = (int) ((EasyInteger)parameter.get(0)).getIntValue();
				int i2 = (int) ((EasyInteger)parameter.get(1)).getIntValue();
				int i3 = (int) ((EasyInteger)parameter.get(2)).getIntValue();
				int i4 = (int) ((EasyInteger)parameter.get(3)).getIntValue();
				EasyGraphics eg = (EasyGraphics) instance;
				eg.g.fillRect( i1, i2, i3, i4);
				return instance;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("Graphics"), EasyLang.rb.getString("Graphics.drawRect"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethodInterface closure, EasyMethodInterface elseClosure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 4);
				int i1 = (int) ((EasyInteger)parameter.get(0)).getIntValue();
				int i2 = (int) ((EasyInteger)parameter.get(1)).getIntValue();
				int i3 = (int) ((EasyInteger)parameter.get(2)).getIntValue();
				int i4 = (int) ((EasyInteger)parameter.get(3)).getIntValue();
				EasyGraphics eg = (EasyGraphics) instance;
				eg.g.drawRect( i1, i2, i3, i4);
				return instance;
			}
		});

	}
	
	private static void add(NativeMethod nativeMethod) {
		methods.put(nativeMethod.getName(), nativeMethod);
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

	@Override
	public EasyMethodInterface getMethod(String methodName) {
		return methods.get(methodName);
	}

	@Override
	public Map<String, EasyMethodInterface> getMethods() {
		return methods;
	}

}
