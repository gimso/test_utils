package tcp_gateway;

/**
 * Created by tal on 12/20/15.
 * <p/>
 * Enum represents tags
 */
public class ResultCodes {
    public static final byte SUCCESS = 0x00;
    public static final byte GENERIC_ERROR = 0x01;
    public static final byte BAD_REQUEST = 0x02;
    public static final byte AUTHENTICATION_FAILED_ON_SIM_UNIT = 0x03;
    public static final byte RESOURCE_UNAVAILABLE = 0x04;
    public static final byte SIM_UNIT_FAILURE = 0x05;
    public static final byte UNKNOWN = (byte) 0xff;

    public static String getName(int value) {
        switch (value) {
            case SUCCESS:
                return "SUCCESS";
            case GENERIC_ERROR:
                return "GENERIC_ERROR";
            case BAD_REQUEST:
                return "BAD_REQUEST";
            case AUTHENTICATION_FAILED_ON_SIM_UNIT:
                return "AUTHENTICATION_FAILED_ON_SIM_UNIT";
            case RESOURCE_UNAVAILABLE:
                return "RESOURCE_UNAVAILABLE";
            case SIM_UNIT_FAILURE:
                return "SIM_UNIT_FAILURE";
            default:
            case UNKNOWN:
                return "UNKNOWN";
        }
    }
}
