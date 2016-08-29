package tcp_gateway;




import java.nio.ByteBuffer;

/**
 * Created by tal on 12/29/15.
 */
public class TLV {
    private byte mTag;
    private byte[] mValue;

    /**
     * Constructor
     *
     * @param aTag The tag
     */
    public TLV(byte aTag) {
        this(aTag, null);
    }

    /**
     * Constructor
     *
     * @param aTag   The tag
     * @param aValue The value
     */
    public TLV(byte aTag, byte[] aValue) {
        mTag = aTag;
        mValue = aValue;
    }

    /**
     * Constructor
     *
     * @param aTag   The tag
     * @param aValue The value
     */
    public TLV(byte aTag, int aValue) {
        this(aTag, (byte) aValue);
    }

    /**
     * Constructor
     *
     * @param aTag   The tag
     * @param aValue The value
     */
    public TLV(byte aTag, byte aValue) {
        mTag = aTag;
        mValue = new byte[]{aValue};
    }

    public byte getTag() {
        return mTag;
    }

    public byte[] getValue() {
        return mValue;
    }

    /**
     * Gets the tlv length in bytes
     *
     * @return The tlv length in bytes
     */
    private byte[] getTLVLength(int value) {
        ByteArray tlvArrayList = new ByteArray();
        boolean isJumbo = value > 0xff;
        do {
            tlvArrayList.add(0, ConvertersUtil.convertIntToByte(value & 0xff));
            value = value - 0xff;
        } while (value > 0);
        if (isJumbo) {
            tlvArrayList.add(0, (byte)0xff);
        }
        return tlvArrayList.toByteArray();
    }

    public byte[] encodeTlv() {
        int valueLength = mValue != null ? mValue.length : 0;

        //Gets the tlv length bytes
        byte[] tlvLength = getTLVLength(valueLength);
        ByteBuffer tlv = ByteBuffer.allocate(1 + valueLength + tlvLength.length);
        tlv.put(mTag);
        tlv.put(tlvLength);
        if (mValue != null) {
            tlv.put(mValue);
        }
        return tlv.array();
    }
}
