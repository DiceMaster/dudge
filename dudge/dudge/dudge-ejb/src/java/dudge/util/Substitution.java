/*
 * Substitution.java
 *
 * Created on 14 Май 2007 г., 18:27
 */
package dudge.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс подстановок в строки.
 *
 * @author Vitaly Kalinkin
 */
public class Substitution implements Cloneable {

	/**
	 * The first char of the substitution
	 */
	public final static char SUBST_CHAR = '$';
	/**
	 * The open bracket symbol of the substitution.
	 */
	public final static char SUBST_OPEN_BRACKET = '{';
	/**
	 * The close bracket symbol of the substitution.
	 */
	public final static char SUBST_CLOSE_BRACKET = '}';
	private Map<String, String> substs;

	/**
	 * Creates a new instance of Substitution
	 */
	public Substitution() {
		super();
		substs = new HashMap<>();
	}

	/**
	 * Adds new substitution to the object or sets it if substitution already exists
	 *
	 * @param aSubst The new substitution
	 * @param aValue The value of substitution
	 */
	public void set(String aSubst, String aValue) {
		if (aSubst != null && aValue != null) {
			substs.put(aSubst, aValue);
		}
	}

	/**
	 * Returns value of the substitution
	 *
	 * @param aSubst The substitution witch value will be returned
	 * @return The value of substitution
	 */
	public String get(String aSubst) {
		return substs.get(aSubst);
	}

	/**
	 * Removes all substitution
	 */
	public void clear() {
		substs.clear();
	}

	/**
	 * Returns count of substitutions
	 *
	 * @return The count of substitutions
	 */
	public int getSubstsCount() {
		return substs.size();
	}

	/**
	 * Compares two objects
	 *
	 * @param otherObject The other object to compare
	 * @return True if substitutions are equaly, false else
	 */
	@Override
	public boolean equals(Object otherObject) {
		if (otherObject == null) {
			return false;
		}

		if (otherObject.getClass() != this.getClass()) {
			return false;
		}

		if (otherObject == this) {
			return true;
		}

		Substitution other = (Substitution) otherObject;

		return substs.equals(other.substs);
	}

	/**
	 * Removes substitution from object
	 *
	 * @param aSubst The substitution for remove
	 */
	public void remove(String aSubst) {
		substs.remove(aSubst);
	}

	/**
	 * Clones currnet object
	 *
	 * @return The clone of current object
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		Substitution cloned = (Substitution) super.clone();

		cloned.substs = new HashMap<>();

		for (Map.Entry<String, String> i : substs.entrySet()) {
			cloned.substs.put(i.getKey(), i.getValue());
		}

		return cloned;
	}

	/**
	 * Converts object to string
	 *
	 * @return The string with data from object
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(getClass().getName());

		int count = substs.size();

		result.append("[");
		for (Map.Entry<String, String> i : substs.entrySet()) {
			count--;
			result.append(i.getKey()).append("=").append(i.getValue()).append((count == 0 ? "" : ","));
		}
		result.append("]");

		return result.toString();
	}

	/**
	 * Returns hash code of object
	 *
	 * @return Hash code of object
	 */
	@Override
	public int hashCode() {
		long hashCode = 0;

		for (Map.Entry<String, String> i : substs.entrySet()) {
			hashCode += i.getKey().hashCode();
		}

		return (int) (hashCode % Integer.MAX_VALUE);
	}

	/**
	 * Decodes string with substitution to the full string
	 *
	 * @return Decoded string
	 * @param aString The string with substitutions
	 * @throws java.lang.IllegalStateException Throws if graph of substitutions contains cycles
	 */
	public String decodeString(String aString) throws IllegalStateException {
		if (aString == null) {
			return null;
		}

		String result = aString;
		boolean flag = true;

		if (!testGraphForCycles()) {
			throw new IllegalStateException("Detected cycles in graph.");
		}

		String[] lastSubst;
		while (flag) {
			lastSubst = getAllSubstitutions(result);
			for (Map.Entry<String, String> i : substs.entrySet()) {
				String subst = "" + SUBST_CHAR + SUBST_OPEN_BRACKET + i.getKey() + SUBST_CLOSE_BRACKET;
				String value = i.getValue();
				result = result.replace(subst, value);
			}
			flag = !Arrays.equals(lastSubst, getAllSubstitutions(result));
		}

		return result;
	}

	/**
	 * Encodes normal string to the format with substitutions
	 *
	 * @param aString The wtring for coding
	 * @return The encoded string
	 */
	public String encodeString(String aString) {
		if (aString == null) {
			return null;
		}

		String result = aString;
		boolean flag = true;

		if (!testGraphForCycles()) {
			throw new IllegalStateException("Detected cycles in graph.");
		}

		String[] lastSubst;
		while (flag) {
			lastSubst = getAllSubstitutions(result);
			for (Map.Entry<String, String> i : substs.entrySet()) {
				String subst = "" + SUBST_CHAR + SUBST_OPEN_BRACKET + i.getKey() + SUBST_CLOSE_BRACKET;

				String value = i.getValue();
				result = result.replace(value, "<|" + value + "|>");
				int index = result.indexOf(value);

				while (index >= 0) {
					int shift = 1;

					if (isInSubstitution(result, index)) {
						shift = -1;
						result = result.substring(0, index - 2) + value + result.substring(index + value.length() + 2, result.length());
					}
					index = result.indexOf(value, index + shift);
				}

				result = result.replace("<|" + value + "|>", subst);
			}
			flag = !Arrays.equals(lastSubst, getAllSubstitutions(result));
		}

		return result;
	}

	/**
	 *
	 * @param aString
	 * @param position
	 * @return
	 */
	private boolean isInSubstitution(String aString, Integer position) {
		int indexOpen = aString.lastIndexOf("" + SUBST_CHAR + SUBST_OPEN_BRACKET, position);
		int indexClose = aString.indexOf("" + SUBST_CLOSE_BRACKET, position);

		if (indexOpen == -1) {
			indexOpen = aString.length();
		}

		return position < indexClose && position > indexOpen;

	}

	/**
	 *
	 * @param aString
	 * @return
	 */
	private boolean isSubstitutions(String aString) {
		if (aString == null) {
			return false;
		}

		int indexSubstChar = aString.indexOf("" + SUBST_CHAR);

		if (indexSubstChar == -1) {
			return false;
		}

		if (aString.length() < indexSubstChar + 2) {
			return false;
		}

		if (aString.charAt(indexSubstChar + 1) != SUBST_OPEN_BRACKET) {
			return false;
		}

		int indexCloseBracket = aString.indexOf("" + SUBST_CLOSE_BRACKET, indexSubstChar + 1);

		if (indexCloseBracket == -1) {
			return false;
		}

		return true;
	}

	/**
	 *
	 * @param str
	 * @return
	 */
	private String[] getAllSubstitutions(String str) {
		if (str != null && !isSubstitutions(str)) {
			return new String[0];
		}

		List<String> result = new ArrayList<>();
		String prefix = "" + SUBST_CHAR + SUBST_OPEN_BRACKET;
		String suffix = "" + SUBST_CLOSE_BRACKET;

		if (str != null) {
			int lastIndex = str.indexOf(prefix);
			while (lastIndex >= 0 && lastIndex + 2 < str.length()) {
				int index = str.indexOf(suffix, lastIndex + 1);

				if (index >= 0 && lastIndex + 2 != index) {
					result.add(str.substring(lastIndex + 2, index));
				}
				lastIndex = str.indexOf(prefix, index);
			}
		}

		return (String[]) result.toArray(new String[result.size()]);
	}

	/**
	 *
	 * @param array
	 * @param index
	 * @return
	 */
	private int evaluateColSum(boolean[][] array, int index) {
		int sum = 0;

		for (int i = 0; i < array.length; i++) {
			if (array[i][index]) {
				sum++;
			}
		}
		return sum;
	}

	/**
	 *
	 * @return
	 */
	private boolean testGraphForCycles() {
		boolean[][] graph = generateGraph();

		for (int i = 0; i < graph.length; i++) {
			if (graph[i][i]) {
				// TODO: Add warning message about cycles length 1
				return false;
			}
		}

		// Cycles detecting
		boolean flag = true;

		while (flag) {
			flag = false;
			for (int i = 0; i < graph.length; i++) {
				if (evaluateColSum(graph, i) == 0) {
					for (int j = 0; j < graph.length; j++) {
						if (graph[i][j]) {
							graph[i][j] = false;
							flag = true;
						}
					}
				}
			}
		}

		flag = true;
		for (int i = 0; i < graph.length; i++) {
			if (evaluateColSum(graph, i) != 0) {
				flag = false;
				break;
				// TODO: Add warning about cycles
			}
		}

		return flag;
	}

	/**
	 *
	 * @return
	 */
	private boolean[][] generateGraph() {
		String[] allKeys = getAllKeys();
		String[] keys = (String[]) substs.keySet().toArray(new String[0]);
		String[] values = (String[]) substs.values().toArray(new String[0]);

		boolean[][] result = new boolean[allKeys.length][allKeys.length];

		for (int i = 0; i < result.length; i++) {
			Arrays.fill(result[i], false);
		}

		String prefix = "" + SUBST_CHAR + SUBST_OPEN_BRACKET;
		String suffix = "" + SUBST_CLOSE_BRACKET;
		int i = 0;

		for (String str : allKeys) {
			int j = 0;

			for (String str1 : values) {
				if (str1.indexOf(prefix + str + suffix) >= 0) {
					int index_value = Arrays.binarySearch(allKeys, keys[j]);

					if (index_value >= 0) {
						result[index_value][i] = true;
					}
				}
				j++;
			}
			i++;
		}

		return result;
	}

	/**
	 *
	 * @return
	 */
	private String[] getAllKeys() {
		List<String> result = new ArrayList<>();
		List<String> temp = new ArrayList<>();

		for (Map.Entry<String, String> subst : substs.entrySet()) {
			temp.add(subst.getKey());
			temp.addAll(Arrays.asList(getAllSubstitutions(subst.getValue())));
		}

		Collections.sort(temp);

		if (temp.isEmpty()) {
			return null;
		}

		String lastStr = "";

		for (String str : temp) {
			if (!lastStr.equals(str)) {
				result.add(str);
				lastStr = str;
			}
		}

		return (String[]) result.toArray(new String[result.size()]);
	}
}
