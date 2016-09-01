package tcp_gateway;

/**
 * Created by tal on 12/20/15.
 * <p/>
 * Enum represents tags
 */
public class Tag {
    public static final byte UNKNOWN = 0x00;
    public static final byte AUTHENTICATION_REQUEST = 0x01;
    public static final byte ALLOCATION_ID = 0x02;
    public static final byte DEVICE_ID = 0x03;
    public static final byte RESULT_CODE = 0x04;
    public static final byte AUTHENTICATION_RESPONSE = 0x05;
}
