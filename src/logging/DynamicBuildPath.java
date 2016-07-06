package logging;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
/**
 * This class inserting dynamically/cross-platform a rxtx file to JAVA_HOME/bin location depended on OS type
 * FIXME needs a way to pass the administrator authority demanded by windows, not implemented yet
 * @author Yehuda Ginsburg
 *
 */
public class DynamicBuildPath {

	public static final String OS_NAME = "os.name";
	public static final String WINDOWS_OS = "Windows 7";
	public static final String MAC_OS = "Mac OS X";
	public static final String JAVA_HOME_ENV = "JAVA_HOME";
	public static final String USER_DIR = "user.dir";
	private static final String PATH_SEPARATOR = "path.separator";
	/**
	 * Add rxtx dll or jnilib file into java_home/bin folder if not exist 
	 */
	public static void setRxtxInJavaBinBuildPath() {
		if (System.getProperty(OS_NAME).equalsIgnoreCase(WINDOWS_OS)) {
			String pathInProject = System.getProperty(USER_DIR)
					+ "\\files\\rxtxSerial.dll";
			String envWitoutSeperator = System.getenv(JAVA_HOME_ENV).split(
					System.getProperty(PATH_SEPARATOR))[0];

			File source = new File(pathInProject), dest = new File(
					envWitoutSeperator + "\\bin\\rxtxSerial.dll");
			
			try {
				if (!dest.exists()) {
					Files.copy(source.toPath(), dest.toPath());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			if (System.getProperty(OS_NAME).equalsIgnoreCase(MAC_OS)) {
				String pathInProject = System.getProperty(USER_DIR)
						+ "/files/librxtxSerial.jnilib";
				String envWitoutSeperator = System.getenv(JAVA_HOME_ENV).split(
						System.getProperty(PATH_SEPARATOR))[0];

				File source = new File(pathInProject);
				File dest = new File(envWitoutSeperator + "/bin/rxtxSerial.dll");
				try {
					if (!dest.exists()) {
						Files.copy(source.toPath(), dest.toPath());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}