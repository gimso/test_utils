package tcp_gateway;

import java.util.ArrayList;

/**
 * Created by tal on 12/24/15.
 *
 * Byte array list class
 */
public class ByteArray extends ArrayList<Byte> {
    public byte[] toByteArray() {
        byte[] byteArray = new byte[size()];
        int i = 0;
        for (Byte tlvByte : this) {
            byteArray[i++] = tlvByte;
        }
        return byteArray;
    }
}
