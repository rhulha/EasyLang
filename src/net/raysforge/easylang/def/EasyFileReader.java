package net.raysforge.easylang.def;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.raysforge.commons.Generics;
import net.raysforge.easylang.EasyClassInterface;
import net.raysforge.easylang.EasyLang;
import net.raysforge.easylang.EasyMethod;
import net.raysforge.easylang.EasyMethodInterface;
import net.raysforge.easylang.utils.EasyUtils;

public class EasyFileReader implements EasyClassInterface {

	BufferedReader br;

	public EasyFileReader() {

	}

	public EasyFileReader(String name) {
		try {
			br = new BufferedReader(new FileReader(name));
		} catch (FileNotFoundException e) {
			EasyUtils.runtimeExcp(e.getMessage());
		}
	}
	
	static Map<String, EasyMethodInterface> methods = new HashMap<String, EasyMethodInterface>();

	static {
		add(new NativeMethod(EasyLang.rb.getString("String"), EasyLang.rb.getString("FileReader.readLine"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 0);
				EasyFileReader efr = (EasyFileReader) instance;
				try {
					return new EasyString(efr.br.readLine());
				} catch (IOException e) {
					EasyUtils.runtimeExcp(e.getMessage());
				}
				return null;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("void"), EasyLang.rb.getString("FileReader.close"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 0);
				EasyFileReader efr = (EasyFileReader) instance;
				try {
					efr.br.close();
				} catch (IOException e) {
					EasyUtils.runtimeExcp(e.getMessage());
				}
				return null;
			}
		});
		add(new NativeMethod(EasyLang.rb.getString("void"), EasyLang.rb.getString("FileReader.forEachLine"), null) {
			@Override
			public EasyClassInterface invoke(EasyClassInterface instance, EasyMethod closure, List<EasyClassInterface> parameter) {
				assertParameterSize(parameter, 0);
				assertClosure(closure);
				EasyFileReader efr = (EasyFileReader) instance;
				String line;
				try {
					while ((line = efr.br.readLine()) != null) {
						List<EasyClassInterface> p = Generics.newArrayList();
						p.add(new EasyString(line));
						closure.invoke( instance, null, p );
					}
				} catch (IOException e) {
					EasyUtils.runtimeExcp(e);
				}
				return null;
			}
		});
	}
	
	private static void add(NativeMethod nativeMethod) {
		methods.put(nativeMethod.getName(), nativeMethod);
	}

	@Override
	public String getName() {
		return EasyLang.rb.getString("FileReader");
	}

	@Override
	public EasyClassInterface getNewInstance(List<EasyClassInterface> parameter) {
		return new EasyFileReader(parameter.get(0).toString());
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
