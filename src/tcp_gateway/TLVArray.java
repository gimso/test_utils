package tcp_gateway;


import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by tal on 1/14/16.
 * Modified by Or on 31/08/2016
 */
public class TLVArray extends ArrayList<TLV> {
    private static final int JUMBO_LENGTH = 2;

    public TLVArray() {
        super();
    }

    public TLVArray(int size) {
        super(size);
    }

    public TLVArray(final byte[] message, int start, int length) throws BufferUnderflowException, ArrayIndexOutOfBoundsException {
        super();
        if (message != null) {
            //Gets all the message
            ByteBuffer msgByteBuffer = ByteBuffer.wrap(message, start, length);
            //Goes untill reaches the end
            while (msgByteBuffer.remaining() >= 2) {
                //TLV tag
                Byte tag = msgByteBuffer.get();
                // handles also jumbo length
                int valueLength = msgByteBuffer.get() & 0xff;
                if (valueLength == 0xff) {
                    byte[] lengthBytes = new byte[JUMBO_LENGTH];
                    msgByteBuffer.get(lengthBytes, 0, JUMBO_LENGTH);
                    valueLength = ProtocolConversions.convertBytesToInt(lengthBytes);
                }
                byte[] decodedTlvValue;
                //Gets the value
                decodedTlvValue = new byte[valueLength];
                msgByteBuffer.get(decodedTlvValue, 0, valueLength); //TLV value

                //Insert into the array
                add(new TLV(tag, decodedTlvValue));
            }
        }
    }

    /**
     * Gets the tlv by its tag
     *
     * @param aTag The tag to search
     * @return The tlv or null if not found
     */
    public TLV get(byte aTag) {
        for (TLV tlv : this) {
            if (tlv.getTag() == aTag) {
                return tlv;
            }
        }
        return null;
    }

    @Override
    public TLV get(int position) {
        return get((byte) position);
    }

    /**
     * Encodes the tlv array
     *
     * @return The encoded tlvs
     */
    public byte[] encodeTlvs() {
        byte[] result = new byte[]{};
        if (isEmpty()) {
            return result;
        }
        for (TLV tlv : this) {
            result = global.Conversions.combineByteArrays(result, tlv.encodeTlv());
        }
        return result;
    }

}
