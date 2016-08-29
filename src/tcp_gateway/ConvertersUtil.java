package tcp_gateway;

import java.nio.ByteBuffer;
import java.util.regex.Pattern;

public class ConvertersUtil {

    private static final String PREFIX_LOCAL_CALL = "81";
    private static final String PREFIX_INTERNATIONAL_CALL = "91";
    private static final String PREFIX_ASTERISK = "*";
    private static final String PREFIX_POUND = "#";
    private static final String PREFIX_PLUS = "+";
    private static final String SUFFIX_ODD_NUMBER = "F";
    private static final String PREFIX_ASTERISK_REPLACEMENT = "A";
    private static final String PREFIX_POUND_REPLACEMENT = "B";
    private static final int NIBBLE_INTERVAL = 2;
    private static final int PREFIX_MAGIC_END_INDEX = 2;

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
     * Union byte arrays
     *
     * @param bytes one or more bytes array
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

    /**
     * Converts byte array to hex string
     *
     * @param bytes The data
     * @return String represents the data in HEX string
     */
    public static String byteArrayToHexString(final byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes){
            sb.append(String.format("%02x", b&0xff));
        }
        return sb.toString();
    }

    /**
     * Converts hex string to byte array
     *
     * @param s The data in string
     * @return byte represents the string in bytes
     */
    public static byte[] hexStringToByteArray(final String s) {
        if (s == null) {
            return (new byte[]{});
        }

        if (s.length() % 2 != 0 || s.length() == 0) {
            return (new byte[]{});
        }

        byte[] data = new byte[s.length() / 2];
        for (int i = 0; i < s.length(); i += 2) {
            try {
                data[i / 2] = (Integer.decode("0x" + s.charAt(i) + s.charAt(i + 1))).byteValue();
            } catch (NumberFormatException e) {
                return (new byte[]{});
            }
        }
        return data;
    }

    /**
     * convert phone number which receives from the network into human format.
     * its done by replacing between each 2 nibbles
     * 81 will be append if the call is local, 81 will be removed, prefix 91 is replaced with +
     * A is changed to * and B is changed to #
     * in case the number length is odd 'F' will appear the end, after swapping its removed
     *
     * @param aOutgoingNumber - outgoing number received from Atmel
     * @return
     */
    public static String convertPhoneNumberFromNetworkToHuman(String aOutgoingNumber) {
        if (aOutgoingNumber == null || aOutgoingNumber.isEmpty()) {
            return new String();
        }
        //replace all occurrences of A and B with * and #
        String outgoingNumberRequest = aOutgoingNumber.replaceAll(PREFIX_ASTERISK_REPLACEMENT, PREFIX_ASTERISK)
                .replaceAll(PREFIX_POUND_REPLACEMENT, PREFIX_POUND);
        StringBuilder number = new StringBuilder(outgoingNumberRequest);
        char temp;
        //swap every 2 nibbles
        for (int i = PREFIX_MAGIC_END_INDEX; (i + 1) < number.length(); i += NIBBLE_INTERVAL) {
            temp = number.charAt(i);
            number.setCharAt(i, number.charAt(i + 1));
            number.setCharAt(i + 1, temp);
        }
        String prefix = number.substring(0, PREFIX_MAGIC_END_INDEX);
        //remove appearance of 81 if exist as prefix
        if (prefix.equals(PREFIX_LOCAL_CALL)) {
            number.delete(0, PREFIX_MAGIC_END_INDEX);
        } else if (prefix.equals(PREFIX_INTERNATIONAL_CALL)) {  //if it begins with 91 it shall be replaced with +
            number.replace(0, PREFIX_MAGIC_END_INDEX, PREFIX_PLUS);
        }
        //in case outgoing number length is odd , suffix 'F'  shall be removed
        if (number.charAt(number.length() - 1) == SUFFIX_ODD_NUMBER.charAt(0)) {
            number.deleteCharAt(number.length() - 1);
        }
        return number.toString();
    }

    /**
     * convert phone number which receives from the cloud  into network format.
     * its done by replacing between each 2 nibbles
     * if + appears it is replaced by 91, all * and # are replaced with A and B
     * 81 is inserted in case 91 was not inserted
     *
     * @param aOutgoingNumber - outgoing number received from Atmel
     * @return
     */
    public static String convertPhoneNumberFromHumanToNetwork(String aOutgoingNumber) {
        if (aOutgoingNumber == null || aOutgoingNumber.isEmpty()) {
            return new String();
        }
        //replace all occurrences of * and # with A and B
        String outgoingNumberResponse = aOutgoingNumber.
                replaceAll(Pattern.quote(PREFIX_ASTERISK), PREFIX_ASTERISK_REPLACEMENT).
                replaceAll(Pattern.quote(PREFIX_POUND), PREFIX_POUND_REPLACEMENT);
        StringBuilder number = new StringBuilder(outgoingNumberResponse);
        if (PREFIX_PLUS.charAt(0) == number.charAt(0)) {
            number.replace(0, 1, PREFIX_INTERNATIONAL_CALL);
        } else {
            number.insert(0, PREFIX_LOCAL_CALL);
        }
        //in case the number is odd add F at the end
        if ((number.length() - PREFIX_MAGIC_END_INDEX) % 2 == 1) {
            number.append(SUFFIX_ODD_NUMBER);
        }
        char temp;
        //replace every 2 nibbles(start from 2 to skip the prefix)
        for (int i = PREFIX_MAGIC_END_INDEX; (i + 1) < number.length(); i += NIBBLE_INTERVAL) {
            temp = number.charAt(i);
            number.setCharAt(i, number.charAt(i + 1));
            number.setCharAt(i + 1, temp);
        }
        return number.toString();
    }

}
