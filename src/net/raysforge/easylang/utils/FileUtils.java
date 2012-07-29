package net.raysforge.easylang.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class FileUtils {
	public static char[] readCompleteFile(File f) {
		try {
			Reader reader = new InputStreamReader(new FileInputStream(f), "utf-8");
			char buf[] = new char[1024];
			
			StringWriter sw = new StringWriter();
			
			while (true)
			{
				int read = reader.read( buf, 0, buf.length);
				if (read < 0)
					break;
				sw.write(buf, 0, read);
			}
			reader.close();
			return sw.toString().toCharArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void writeCompleteFile(File file, String text) {
		try {
			Writer fw = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
			fw.write(text);
			fw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
	
	main
}
