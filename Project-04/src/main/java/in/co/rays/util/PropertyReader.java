package in.co.rays.util;

import java.util.ResourceBundle;

/**
 * PropertyReader is a utility class that reads values from a resource bundle.
 * <p>
 * It provides methods to retrieve property values by key and supports
 * parameterized messages with single or multiple replacements.
 * </p>
 * 
 * Example usage:
 * 
 * <pre>
 * String message = PropertyReader.getValue("error.require");
 * String messageWithParam = PropertyReader.getValue("error.require", "loginId");
 * String[] params = { "Roll No", "Student Name" };
 * String messageWithParams = PropertyReader.getValue("error.multipleFields", params);
 * </pre>
 * 
 * @author Akbar
 * @version 1.0
 */
public class PropertyReader {

	/** Resource bundle to read system properties */
	private static ResourceBundle rb = ResourceBundle.getBundle("in.co.rays.bundle.system");

	/**
	 * Returns the value associated with the specified key from the resource bundle.
	 * 
	 * @param key the key to look up
	 * @return the value corresponding to the key, or the key itself if not found
	 */
	public static String getValue(String key) {
		String val = null;
		try {
			val = rb.getString(key); // {0} is required
		} catch (Exception e) {
			val = key;
		}
		return val;
	}

	/**
	 * Returns the value associated with the specified key from the resource bundle
	 * and replaces the placeholder {0} with the provided parameter.
	 * 
	 * @param key   the key to look up
	 * @param param the parameter to replace in the message
	 * @return the formatted message string
	 */
	public static String getValue(String key, String param) {
		String msg = getValue(key); // {0} is required
		msg = msg.replace("{0}", param);
		return msg;
	}

	/**
	 * Returns the value associated with the specified key from the resource bundle
	 * and replaces multiple placeholders {0}, {1}, ... with the provided
	 * parameters.
	 * 
	 * @param key    the key to look up
	 * @param params an array of parameters to replace in the message
	 * @return the formatted message string
	 */
	public static String getValue(String key, String[] params) {
		String msg = getValue(key);
		for (int i = 0; i < params.length; i++) {
			msg = msg.replace("{" + i + "}", params[i]);
		}
		return msg;
	}

	/**
	 * Main method to test PropertyReader methods.
	 * 
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		System.out.println("\nSingle parameter replacement example:");
		System.out.println(PropertyReader.getValue("error.require", "loginId"));

//		System.out.println("Single key example:");
//		System.out.println(PropertyReader.getValue("error.require"));

//		System.out.println("\nMultiple parameter replacement example:");
//		String[] params = { "Roll No", "Student Name" };
//		System.out.println(PropertyReader.getValue("error.multipleFields", params));
//	
	}

}
