package net.raysforge.easylang.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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

	public static void writeCompleteFile(File file, String text) {
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(text);
			fw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
}
