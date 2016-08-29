package tcp_gateway;

/**
 * Created by tal on 8/24/16.
 */
public class Opcode {
    public static final byte KEEP_ALIVE = 0x01;
    public static final byte AUTHENTICATION = 0x03;
    public static final byte UNKNOWN = (byte)0xff;

    /**
     * Gets the opcode by its value
     *
     * @param value The value of the opcode
     * @return Opcode the opcode
     * @throws IllegalArgumentException in case the opcode wasn't find
     */
    public static int getOpcode(int value) {
        switch (value) {
            case AUTHENTICATION:
            case KEEP_ALIVE:
                return value;
            case UNKNOWN:
            default:
                return UNKNOWN;
        }
    }

    public static String getName(int value) {
        switch (value) {
            case KEEP_ALIVE:
                return "KEEP_ALIVE";
            case AUTHENTICATION:
                return "AUTHENTICATION";
            case UNKNOWN:
            default:
                return "UNKNOWN";
        }
    }
}
