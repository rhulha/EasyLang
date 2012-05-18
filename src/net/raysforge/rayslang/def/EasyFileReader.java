package net.raysforge.rayslang.def;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import net.raysforge.commons.Generics;
import net.raysforge.rayslang.KeyWords;
import net.raysforge.rayslang.EasyClassInterface;
import net.raysforge.rayslang.EasyMethod;
import net.raysforge.rayslang.EasyUtils;

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

	@Override
	public String getName() {
		return KeyWords.CLASS_FILE_READER;
	}

	@Override
	public EasyClassInterface invoke(String methodName, EasyMethod closure, List<EasyClassInterface> parameter) {
		if (methodName.equals("leseZeile")) {
			try {
				return new EasyString(br.readLine());
			} catch (IOException e) {
				EasyUtils.runtimeExcp(e.getMessage());
			}
		} else if (methodName.equals("ende")) {
			try {
				br.close();
			} catch (IOException e) {
				EasyUtils.runtimeExcp(e.getMessage());
			}
		} else if (methodName.equals("fürJedeZeile") && closure != null) {
			String line;
			try {
				while ((line = br.readLine()) != null) {
					List<EasyClassInterface> p = Generics.newArrayList();
					p.add(new EasyString(line));
					closure.invoke(p );
				}
			} catch (IOException e) {
				EasyUtils.runtimeExcp(e);
			}
		}

		return null;
	}

	@Override
	public EasyClassInterface getNewInstance(List<EasyClassInterface> parameter) {
		return new EasyFileReader(parameter.get(0).toString());
	}

}
