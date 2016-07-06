package global;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class TypeConversions {
	
	public static byte[] convertIntToBytes(final int data) {
	
	ByteBuffer b = ByteBuffer.allocate(4);
	b.putInt(data);

	byte[] result = b.array();
	
	return result;
	
	}

	public static int convertBytesToInt  (final byte [] arr) {
	
	
	ByteBuffer wrapped = ByteBuffer.wrap(arr); 
	int num = wrapped.getShort(); // 1

	return num;
	
	}
	
	public static String byteArrayToHexString (final byte [] byteArr) {
	
		
		String returnHex = "";
		for (Byte b : byteArr) {
			returnHex+=(String.format("%02X", b));
		}
		
		return returnHex;
	
	}
	
	public static int hexStringToDecimalInt (String hex) {
	

	int response = Integer.parseInt(hex, 16 );
	return response;		
	}
	
	
	public static byte[] combineMultipleByteArrays(byte[]... bytes) {
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
	
	
    public static byte[] combineByteArrays(final byte[] byte1, final byte[] byte2)
    {
        if (byte1 == null || byte2 == null)
        {
            System.out.println("combineByteArrays FAILED because atleast one Input ByteArray is NULL");
            return (new byte[] {});
        }

        ByteBuffer target = ByteBuffer.allocate(byte1.length + byte2.length);
        target.put(byte1);
        target.put(byte2);

        return target.array();
    }
	
	
    public static byte [] decimalNumberToByteArray(String str)
    {
    	return new BigInteger(str,16).toByteArray();
    }
	
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    
    public static byte[] decimalToBCDArray(long num) {
        int digits = 0;

        long temp = num;
        while (temp != 0) {
            digits++;
            temp /= 10;
        }

        int byteLen = digits % 2 == 0 ? digits / 2 : (digits + 1) / 2;

        byte bcd[] = new byte[byteLen];

        for (int i = 0; i < digits; i++) {
            byte tmp = (byte) (num % 10);

            if (i % 2 == 0) {
                bcd[i / 2] = tmp;
            } else {
                bcd[i / 2] |= (byte) (tmp << 4);
            }

            num /= 10;
        }

        for (int i = 0; i < byteLen / 2; i++) {
            byte tmp = bcd[i];
            bcd[i] = bcd[byteLen - i - 1];
            bcd[byteLen - i - 1] = tmp;
        }

        return bcd;
    }
   
}
