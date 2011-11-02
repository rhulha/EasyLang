package net.raysforge.rayslang;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {
	public static char[] readCompleteFile(File f) {
		try {
			FileReader reader = new FileReader(f);
			int len = (int) f.length();
			char buf[] = new char[len];
			if ((reader.read(buf, 0, len)) != len) {
				throw new RuntimeException("reader.read() != len");
			}
			reader.close();
			return buf;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
