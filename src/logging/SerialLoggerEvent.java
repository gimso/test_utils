package logging;

import java.io.IOException;

/**
 * Getting event from serial logger
 * 
 * @author Yehuda
 *
 */
public interface SerialLoggerEvent {
	/**
	 * When catching IOException handle it
	 * 
	 * @param exception
	 */
	public void onSerialException(IOException exception);
}
