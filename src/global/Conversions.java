package global;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * This Class is a utility to covert from and to types
 * 
 * @author Yehuda
 * @author Or
 */
public class Conversions {

	/**
	 * get byte array and convert it into integer value
	 * 
	 * @param value
	 * @return int
	 */
	public static int byteArraysToInt(byte[] value) {
		String temp = "";
		for (byte b : value)
			temp += String.format("%02x", b);
		return Integer.parseInt(temp, 16);
	}

	/**
	 * Get ASCII string from byte array
	 * 
	 * @param value
	 * @return
	 */
	public static String stringASCIIFromByteArray(byte[] value) {
		try {
			return new String(value, "US-ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new NullPointerException(String.valueOf(value) + " cannot parse as ASCII");
		}
	}

	/**
	 * Get numbers in String format, Convert it into integer values in the byte
	 * array.
	 * 
	 * @param string
	 * @return byte[]
	 */
	public static byte[] stringNumsToByteArray(String string) {
		char[] cs = string.toCharArray();
		byte[] rv = new byte[cs.length];
		for (int i = 0; i < cs.length; i++) {
			rv[i] = (byte) Character.getNumericValue(cs[i]);
		}
		return rv;
	}

	/**
	 * Convert from String to ASCII chars and then to byte array Receive Any
	 * ASCII characters
	 * 
	 * @param string
	 * @return byte[]
	 */
	public static byte[] stringToASCIIByteArray(String string) {
		try {
			return string.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Convert from string to ASCII chars and then to byte array Receive Any
	 * number of ASCII characters, where each character is between "0" and "9",
	 * or "." (dot) or "-" (hyphen).
	 * 
	 * @param string
	 * @return byte[]
	 */
	public static byte[] stringToASCIINumericByteArray(String string) {
		if (!string.matches("^([0-9.-])+$"))
			throw new RuntimeException("String " + string + " do not match the pattern [0-9_.]");
		try {
			return string.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Union byte arrays
	 * 
	 * @param one
	 *            or more bytes array
	 * @return combineByteArrays
	 */
	public static byte[] combineByteArrays(byte[]... bytes) {
		int size = 0;
		for (byte[] byteArray : bytes)
			if (byteArray != null)
				size = size + byteArray.length;

		ByteBuffer target = ByteBuffer.allocate(size);
		for (byte[] byteArray : bytes)
			if (byteArray != null)
				target.put(byteArray);

		return target.array();
	}

	/**
	 * Convert from int to four byte array
	 * 
	 * @param value
	 * @return byte[]
	 */
	public static byte[] intToFourBytesArray(long value) {
		long maxSize = 0xff_ff_ff_ffl;
		if (value > maxSize)
			throw new IllegalArgumentException(
					"The int value " + value + " is bigger the the buffer size " + 0xffffffffl);

		return ByteBuffer.allocate(4).putInt((int) value).array();
	}

	/**
	 * Convert from int to two byte array
	 * 
	 * @param value
	 * @return byte[]
	 */
	public static byte[] intToTwoBytesArray(int value) {
		if (value > 0xffff)
			throw new IllegalArgumentException("The int value " + value + " is bigger the the buffer size " + 0xffff);
		byte b = 0x0;
		if (value > 0xff)
			b = (byte) (value >>> 8);
		return new byte[] { b, (byte) value };
	}

	/**
	 * check if the value of bytes in the byte array is only 0-9
	 * 
	 * @param vsimId
	 */
	public static boolean isByteArrayContainsZeroToNine(byte[] vsimId) {
		for (byte b : vsimId) {
			if (b > 9) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Converts int to byte
	 * 
	 * @author Tal
	 * @param value
	 *            The int value
	 * @return The byte result
	 * @throws IllegalArgumentException
	 */
	public static byte intToByte(int value) throws IllegalArgumentException {
		if (value > 0xff) {
			throw new IllegalArgumentException();
		}
		return (byte) (value & 0xff);
	}

	/**
	 * convert int to byte array
	 * 
	 * @param data
	 *            int
	 * @return byte array
	 */
	public static byte[] intToBytes(final int data) {
		return BigInteger.valueOf(data).toByteArray();
	}

	/**
	 * get byte array and return the integer value
	 * 
	 * @param arr
	 * @return int
	 */
	public static int bytesToInt(final byte[] arr) {
		ByteBuffer wrapped = ByteBuffer.wrap(arr);
		return wrapped.getShort(); // 1
	}

	/**
	 * Convert byte arrays to string
	 * 
	 * @param array
	 * @return string byte-array
	 */
	public static String byteArrayToHexString(final byte[] byteArr) {
		String returnHex = "";
		for (byte b : byteArr)
			returnHex += (String.format("%02X", b));
		return returnHex;
	}

	/**
	 * This method get the int decimal value from hexadecimal string
	 * 
	 * @param hex
	 * @return int
	 */
	public static int hexStringToDecimalInt(String hex) {
		return Integer.parseInt(hex, 16);
	}

	/**
	 * Converts hex string (decimal value) to byte array
	 * 
	 * @param hexString
	 * @return byte array
	 */
	public static byte[] hexStringToByteArray(String hexString) {
		return new BigInteger(hexString, 16).toByteArray();
	}

	/**
	 * @return the two byte in byte array
	 */
	public static byte[] bytesToByteArray(byte one, byte two) {
		return new byte[] { one, two };
	}

}