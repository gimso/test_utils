package tcp_gateway;

import java.nio.ByteBuffer;
import java.util.regex.Pattern;

/**
 * @author Or
 * Provides necceary type conversions for the Simgo Protocol
 */
public class ProtocolConversions {


    private static final int BYTE_SIZE = 8;
    private static final int NUMBER_OF_BYTES_IN_INT = 4;
    private static final int NUMBER_OF_BYTES_IN_INT_8 = 2;


    /**
     * Converts int to byte
     *
     * @param value The int value
     * @return The byte result
     * @throws IllegalArgumentException
     */
    public static byte convertIntToByte(int value) throws IllegalArgumentException {
        if (value > 0xff) {
            throw new IllegalArgumentException();
        }
        return (byte) (value & 0xff);
    }

    /**
     * Converts int to bytes array
     *
     * @param value The int value
     * @return The bytes array
     */
    public static byte[] convertIntToBytes(int value) {
        ByteBuffer b = ByteBuffer.allocate(NUMBER_OF_BYTES_IN_INT);
        b.putInt(value);
        return unPadByteArray(b.array());
    }

    /**
     * Converts int to bytes array with size int32
     *
     * @param value The int value
     * @return The bytes array
     */
    public static byte[] convertIntToBytesWithPaddingInt32(int value) {
        return convertIntToBytesWithPadding(value, NUMBER_OF_BYTES_IN_INT);

    }

    /**
     * Converts int to bytes array with size int8
     *
     * @param value The int value
     * @return The bytes array
     */
    public static byte[] convertIntToBytesWithPaddingInt8(int value) {
        return convertIntToBytesWithPadding(value, NUMBER_OF_BYTES_IN_INT_8);
    }

    /**
     * Converts int to bytes array
     *
     * @param value          The int value
     * @param aNumberOfBytes Number of bytes to pad
     * @return The bytes array
     */
    public static byte[] convertIntToBytesWithPadding(int value, int aNumberOfBytes) {
        ByteBuffer b = ByteBuffer.allocate(NUMBER_OF_BYTES_IN_INT);
        b.putInt(value);
        byte[] intValue = b.array();
        try {
            byte[] targetArray = new byte[aNumberOfBytes];
            System.arraycopy(intValue, NUMBER_OF_BYTES_IN_INT - aNumberOfBytes, targetArray, 0, aNumberOfBytes);
            return targetArray;
        } catch (IndexOutOfBoundsException e) {
            return intValue;
        }
    }



    /**
     * Converts a byte[] to integer
     *
     * @param rawData The data to convert
     * @return returns an integer represents the array
     * @throws IllegalArgumentException If byte array is too long or null
     */
    public static int convertBytesToInt(final byte[] rawData) throws IllegalArgumentException {

        if (rawData == null || rawData.length > NUMBER_OF_BYTES_IN_INT) {
            throw new IllegalArgumentException();
        }

        int result = 0;
        for (byte dataByte : rawData) {
            result = result << BYTE_SIZE;
            result += (dataByte & 0xff);
        }

        return result;
    }

    /**
     * Remove leading zeros byte from bytes array
     *
     * @param initialByteArray The byte array to remove the leading zeros from
     * @return Byte array without leading zeros
     */
    private static byte[] unPadByteArray(final byte[] initialByteArray) {
        if (initialByteArray == null || initialByteArray.length < 1) {
            return (new byte[]{});
        }

        int index = 1;

        try {
            while (index < NUMBER_OF_BYTES_IN_INT && initialByteArray[index] == 0) {
                index++;
            }
            int length = NUMBER_OF_BYTES_IN_INT - index;
            if (length == 0) {
                length = 1;
                index = NUMBER_OF_BYTES_IN_INT - 1;
            }
            byte[] targetArray = new byte[length];

            System.arraycopy(initialByteArray, index, targetArray, 0, length);

            return targetArray;

        } catch (IndexOutOfBoundsException e) {
            return (new byte[]{});
        }
    }
   

}
