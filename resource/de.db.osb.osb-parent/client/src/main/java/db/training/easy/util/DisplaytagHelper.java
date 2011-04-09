/**
 * 
 */
package db.training.easy.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author michels
 * 
 */
public class DisplaytagHelper {

	/**
	 * filtert aus den Requestparametern alle Parameter des Displaytags heraus, und gibt diese
	 * in einer neuen Collection zurück.
	 * 
	 * @param requestParameterSet
	 * @return
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getParameterMap(Map requestParameterSet)
	    throws IllegalArgumentException {
		if (requestParameterSet == null) {
			throw new IllegalArgumentException();
		}

		Map<String, String> displaytagParamMap = new HashMap<String, String>(3);

		for (Iterator paramIterator = requestParameterSet.entrySet().iterator(); paramIterator
		    .hasNext();) {
			Map.Entry<String, String[]> paramEntry = (Map.Entry<String, String[]>) paramIterator
			    .next();
			if (paramEntry.getKey().indexOf("d-") >= 0) {
				// Parameter ist sehr wahrscheinlich ein Displaytag Parameter
				displaytagParamMap.put(paramEntry.getKey(), paramEntry.getValue()[0]);
			}
		}

		return displaytagParamMap;
	}
}
