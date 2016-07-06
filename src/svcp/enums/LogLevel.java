package svcp.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 6.6. Log Level (0x06) </br>
 * Description: Level of Log line. </br>
 * Encoding: One byte with the following encoding </br>
 * (highest first = less information): </br>
 * 
 * @author Yehuda
 *
 */
public enum LogLevel {

	VERBOSE(0X00), 
	DEBUG(0X01), 
	INFORMATION(0X02), 
	WARNING(0X03), 
	ERROR(0X04), 
	CRASH_REPORT(0xFE);

	private static Map<Integer, LogLevel> map;
	private int value;

	static {
		map = new HashMap<>();
		for (LogLevel logLevel : LogLevel.values())
			map.put(logLevel.value, logLevel);
	}
	
	LogLevel(int aValue) {
		this.value = aValue;
	}

	public byte getValue() {
		return (byte) value;
	}

	/**
	 * Gets LogLevel by value
	 * 
	 * @param value
	 * @return LogLevel
	 */
	public static LogLevel getLogLevel(int value) {
		return map.get(value);
	}
}
