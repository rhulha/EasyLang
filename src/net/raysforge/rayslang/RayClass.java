package net.raysforge.rayslang;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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

		LinkedList<String> queue = new LinkedList<String>();

		while (true) {
			String token = RayUtils.getSourceToken(rs);
			if (token == null || token.length() == 0)
				break;
			if (token.equals("#")) {
				while (rs.pos < rs.src.length && rs.src[rs.pos] != '\n')
					rs.pos++;
				continue;
			} else if (token.equals(";")) {
				
				String name = queue.pop();
				String type = queue.pop();
				Visibility v = Visibility.protected_;
				if( ! queue.isEmpty())
					v = Visibility.valueOf(queue.pop()+"_"); 
				rc.variables.add( new RayVar(v, type, name, ""));
				System.out.println("var: "+ type + " - " + name);
			} else if (token.equals("(")) {
				String innerText = RayUtils.getInnerText(rs, '(', ')');
				System.out.println("innerText: "+ innerText);
			} else if (token.equals("{")) {
				String innerText = RayUtils.getInnerText(rs, '{', '}');
				System.out.println("innerCode: "+ innerText);
			} else {
				queue.push(token);
			}
			//System.out.println("XX" + token + "YY");
		}

		return rc;
	}

	public String getFullName() {
		return package_ + "." + name;
	}

}
