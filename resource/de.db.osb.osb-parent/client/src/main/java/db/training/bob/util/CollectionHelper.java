package db.training.bob.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class CollectionHelper {
	public static <T extends Collection<String>> T separatedStringToCollection(T resultCollection,
	    String str, String delimiter) {
		if (resultCollection == null) {
			throw new IllegalArgumentException("resultCollection darf nicht NULL sein.");
		}
		if (delimiter == null) {
			throw new IllegalArgumentException("ungültiges Trennzeichen");
		}

		resultCollection.clear();

		if (str.length() > 0) {
			Scanner scanner = new Scanner(str);
			scanner.useDelimiter(delimiter);
			while (scanner.hasNext())
				resultCollection.add(scanner.next().trim());
		}

		return resultCollection;
	}

	public static <T> String toSeparatedString(Collection<T> collection, String delimiter) {
		if (collection == null)
			return null;

		if (delimiter == null) {
			throw new IllegalArgumentException("ungültiges Trennzeichen");
		}

		StringBuffer result = new StringBuffer();
		Iterator<T> it = collection.iterator();
		while (it.hasNext()) {
			result.append(it.next());
			if (it.hasNext())
				result.append(delimiter);
		}

		return result.toString();
	}

	public static <T> T getFirst(Collection<T> collection) {
		if (collection == null || collection.isEmpty())
			return null;

		return collection.iterator().next();
	}

	/**
	 * gibt ein festgelegtes Intervall einer Liste als String zurück
	 * 
	 * @param <T>
	 * @param rawIntegers
	 * @param string
	 * @param listSize
	 * @param startIndex
	 * @return
	 */
	public static <T> String toSeparatedStringListSize(List<T> list, String delimiter,
	    int startIndex, int listSize) {
		if (list == null)
			return null;

		if (delimiter == null) {
			throw new IllegalArgumentException("ungültiges Trennzeichen");
		}

		if (startIndex < 0 || startIndex >= list.size()) {
			throw new IndexOutOfBoundsException("startIndex");
		}

		if (listSize < 0) {
			throw new IndexOutOfBoundsException("listSize");
		}

		StringBuffer result = new StringBuffer();
		List<T> subList = list.subList(startIndex, Math.min(startIndex + listSize, list.size()));
		Iterator<T> it = subList.iterator();
		while (it.hasNext()) {
			result.append(it.next());
			if (it.hasNext())
				result.append(delimiter);
		}

		return result.toString();
	}
}
