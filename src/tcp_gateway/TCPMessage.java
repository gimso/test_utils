package tcp_gateway;

import java.nio.BufferUnderflowException;

/**
 * Created by tal on 12/27/15.
 * <p/>
 * The message data passed by the handler
 */
public class TCPMessage {
    private static final int HEADER_POSITION_OPCODE = 0;
    private static final int HEADER_POSITION_LENGTH = 1;
    public static final int HEADER_SIZE = 2;
    private static final int MAX_PAYLOAD_SIZE = 0xffff;

    private static final int RESPONSE_BYTE = 0x80;
    private static final byte RESPONSE_TURN_OFF_BYTE = (byte) 0x7F;
    private int mOpcode = Opcode.UNKNOWN;
    private TLVArray mTLVArray;
    private byte mId = 0x00;
    private boolean isResponse;
    private byte[] mMessage;
    private byte mResultCode;

    /**
     * Constructor for the class
     *
     * @param message The message
     */
    public TCPMessage(byte[] message) throws ArrayIndexOutOfBoundsException, BufferUnderflowException {
        mMessage = message;
        mOpcode = getOpcode(message);
        isResponse = isResponseOpcode(message);
        int payloadSize = message[HEADER_POSITION_LENGTH] & 0xff;
        mTLVArray = new TLVArray(message, HEADER_SIZE, payloadSize);

        if (isResponse) {
            TLV responseCode = mTLVArray.get(Tag.RESULT_CODE);
            if (responseCode == null) {
                throw new IllegalArgumentException("Received response with no result code");
            }
            mResultCode = (byte)ConvertersUtil.convertBytesToInt(responseCode.getValue());
        }
    }

    /**
     * Constructor for the class
     *
     * @param message The message
     */
    public TCPMessage(byte[] message, byte aId) throws ArrayIndexOutOfBoundsException, BufferUnderflowException {
        this(message);
        mId = aId;
    }

    //Getters
    public int getOpcode() {
        return mOpcode;
    }

    public byte getResultCode() {
        return mResultCode;
    }

    public byte getId() {
        return mId;
    }

    public TLVArray getTLVArray() {
        return mTLVArray;
    }

    public boolean isResponse() {
        return isResponse;
    }

    public byte[] getMessage() {
        return mMessage;
    }

    @Override
    public String toString() {
        return String.format("SVCP message id(0x%02x) opcode(%s) %s",
                mId, Opcode.getName(mOpcode), isResponse ? "Response:" + ResultCodes.getName(mResultCode) : "Request");
    }

    /**
     * Return if the opcode of the message is response opcode.
     *
     * @param message The message
     * @return if its a response opcode
     */
    private static boolean isResponseOpcode(byte[] message) {
        return ((message[HEADER_POSITION_OPCODE] & RESPONSE_BYTE) != 0);
    }

    /**
     * Gets the opcode from the message
     *
     * @param message The message
     * @return the opcode from the message
     */
    private static int getOpcode(byte[] message) {
        return Opcode.getOpcode(message[HEADER_POSITION_OPCODE] & RESPONSE_TURN_OFF_BYTE);
    }


    /**
     * Creates a SVCP message from the opcode and tlvs
     *
     * @param aOpcodeValue The value of the opcode of the message
     * @param encodedTlvs  The encoded tlv of the message
     * @return A byte[] of the message
     */
    public static byte[] encodeMessage(int aOpcodeValue, byte[] encodedTlvs) {
        int tlvsLength = encodedTlvs.length;
        if (tlvsLength > MAX_PAYLOAD_SIZE) {
            throw new IllegalArgumentException();
        }
        byte[] header = createHeader(aOpcodeValue, tlvsLength);
        if (tlvsLength > 0) {
            return ConvertersUtil.combineByteArrays(header, encodedTlvs);
        } else {
            return header;
        }
    }

    /**
     * Creates a SVCP message from the opcode and tlvs
     *
     * @param aOpcodeValue The value of the opcode of the message
     * @return A byte[] of the message
     */
    public static byte[] encodeMessage(int aOpcodeValue, TLVArray aTLVArray) {
        return encodeMessage(aOpcodeValue, aTLVArray.encodeTlvs());
    }

    /**
     * Creates a header for a message
     *
     * @param aEOpcode The value of the opcode of the message
     * @param length   The length of the payload
     * @return The header of the message
     */
    private static byte[] createHeader(int aEOpcode, int length) {
        byte[] header = new byte[HEADER_SIZE];
        header[HEADER_POSITION_OPCODE] = (byte) aEOpcode;
        header[HEADER_POSITION_LENGTH] = (byte)length;
        return header;
    }
}
