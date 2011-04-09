package db.training.easy.util;

import javax.servlet.ServletContext;

import org.apache.struts.action.ActionServlet;

public class FileSystemHelper {

	public static synchronized String getFilePath(ServletContext context, String path) {
		if (context == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(context.getRealPath("/"));
//		if (path.charAt(0) == '/') {
//			sb.append('/');
//		}
		sb.append(path);
//		if (path.lastIndexOf('/') < path.length()) {
//			sb.append('/');
//		}
		return sb.toString();
	}

	public static synchronized String getFilePath(ActionServlet servlet, String path) {
		if (servlet == null) {
			return null;
		}
		return getFilePath(servlet.getServletContext(), path);
	}
}
