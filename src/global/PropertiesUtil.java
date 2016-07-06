package global;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This class replacing the use of proprties.json with java properties file that
 * supply more flexible uses like getters/setters, comments in the properties
 * files and more java built-in methods.
 * 
 * @author Yehuda Ginsburg
 *
 */
public class PropertiesUtil {

	private static PropertiesUtil propertiesUtil;
	private Properties properties;
	private static String path = getCurrentProjectPropertyFileName();

	/**
	 * Get instance from no parameter CTOR
	 * 
	 * @return PropertiesUtil
	 */
	public static PropertiesUtil getInstance() {
		if (propertiesUtil == null)
			return new PropertiesUtil();
		else
			return propertiesUtil;
	}

	/**
	 * Get instance from path parameter CTOR
	 * 
	 * @param propertiesFilePath
	 * @return PropertiesUtil
	 */
	public static PropertiesUtil getInstance(String propertiesFilePath) {
		if (propertiesUtil == null)
			return new PropertiesUtil(propertiesFilePath);
		else
			return propertiesUtil;
	}

	/**
	 * CTOR getting the Properties file from current running project
	 */
	private PropertiesUtil() {
		this(PropertiesUtil.path);
	}

	/**
	 * CTOR that initialized the from any property file by path use specially
	 * when need property from different path
	 * 
	 * @param propertiesFilePath
	 */
	private PropertiesUtil(String path) {
		Properties properties = new Properties();
		try (InputStreamReader in = new InputStreamReader(new FileInputStream(path))) {
			properties.load(in);
			this.properties = properties;
			propertiesUtil = this;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get a specific property from current project
	 * 
	 * @param key
	 * @return string property
	 */
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	/**
	 * Set a specific property from current project </br>
	 * Be aware it sets the current instance only, not the file itself
	 * 
	 * @param key
	 * @param newValue
	 */
	public void setProperty(String key, String newValue) {
		properties.setProperty(key, newValue);
	}

	/**
	 * Get all properties from current project
	 * 
	 * @return map
	 */
	public Map<String, String> getAllProperties() {
		Map<String, String> rv = new HashMap<>();
		this.properties.keySet().forEach(key -> {
			rv.put((String) key, this.properties.getProperty((String) key));
		});
		return rv;
	}

	/**
	 * Convert from properties.json file to (ProjectName).properties Saved the
	 * file under Resources of Util
	 * 
	 * @param path
	 */
	public void fromPropertiesJsonToJavaProperty(String path) {
		try {
			Properties prop = new Properties();
			OutputStream output = new FileOutputStream(
					"Resources/" + getCurrentProjectPropertyFileName() + ".properties");
			for (org.json.JSONObject object : Arrays.asList(JSONReader.getJsonFromFile(path)))
				for (String key : object.keySet())
					prop.setProperty(key, (String) object.get(key));

			prop.store(output, "");
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the current project property file name from user.dir java property
	 * 
	 * @return String name
	 */
	private static String getCurrentProjectPropertyFileName() {
		String property = System.getProperty("user.dir");
		String resourcesPath = property.substring(0, property.lastIndexOf("\\")) + "\\Util\\Resources\\";
		String projectName = property.substring(property.lastIndexOf("\\") + 1);
		return resourcesPath + projectName + ".properties";
	}
}