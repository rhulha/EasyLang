package net.raysforge.rayslang;

import java.io.File;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;

public class RayClass {

	String name;
	String package_;

	List<RayVar> variables = new ArrayList<RayVar>();

	HashMap<String, RayMethod> methods = new HashMap<String, RayMethod>();

	public static RayClass parse(File file) {
		return parse(new RaySource(FileUtils.readCompleteFile(file)));
	}

	public static RayClass parse(RaySource rs) {
		RayClass rc = new RayClass();


		while (true) {
			Deque<String> deque = rs.getSourceTokenUntil(";", "(");

			if (deque.size() == 0)
				break;
			String last = deque.pollLast();
			if ( last.equals(";")) {
				
				String name = deque.pop();
				String type = deque.pop();
				Visibility v = Visibility.protected_;
				if( ! deque.isEmpty())
					v = Visibility.valueOf(deque.pop()+"_"); 
				rc.variables.add( new RayVar(v, type, name, ""));
				System.out.println("var: "+ type + " - " + name);
			} else if (last.equals("(")) {
				
				String name = deque.pop();
				String type = deque.pop();

				RayMethod rm = RayMethod.parse( type, name, rs);
				rc.methods.put(name, rm);
				// 
				// System.out.println("innerText: "+ innerText);
			} else if (last.equals("{")) {
				System.out.println("hm");
			}
			//System.out.println("XX" + token + "YY");
		}

		return rc;
	}

	public String getFullName() {
		return package_ + "." + name;
	}

}
