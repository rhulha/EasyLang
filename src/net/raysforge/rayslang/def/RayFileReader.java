package net.raysforge.rayslang.def;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import net.raysforge.rayslang.KeyWords;
import net.raysforge.rayslang.RayClassInterface;
import net.raysforge.rayslang.RayMethod;
import net.raysforge.rayslang.RayUtils;

public class RayFileReader implements RayClassInterface {

	BufferedReader br;

	public RayFileReader() {

	}

	public RayFileReader(String name) {
		try {
			br = new BufferedReader(new FileReader(name));
		} catch (FileNotFoundException e) {
			RayUtils.runtimeExcp(e.getMessage());
		}
	}

	@Override
	public String getName() {
		return KeyWords.CLASS_FILE_READER;
	}

	@Override
	public RayClassInterface invoke(String methodName, RayMethod closure, RayClassInterface... parameter) {
		if (methodName.equals("leseZeile")) {
			try {
				return new RayString(br.readLine());
			} catch (IOException e) {
				RayUtils.runtimeExcp(e.getMessage());
			}
		} else if (methodName.equals("ende")) {
			try {
				br.close();
			} catch (IOException e) {
				RayUtils.runtimeExcp(e.getMessage());
			}
		} else if (methodName.equals("f�rJedeZeile")) {
			String line;
			try {
				while ((line = br.readLine()) != null) {
					closure.invoke(closure.getParentClass(), new RayString(line));
				}
			} catch (IOException e) {
				RayUtils.runtimeExcp(e);
			}
		}

		return null;
	}

	@Override
	public RayClassInterface getNewInstance(List<RayClassInterface> parameter) {
		return new RayFileReader(parameter.get(0).toString());
	}

}
