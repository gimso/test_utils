package svcp.beans;

import java.util.List;

import global.Conversions;
import global.PropertiesUtil;
import svcp.enums.MessageTypeOpcodes;
import svcp.util.SVCPConversion;

/**
 * Bean class for SVCPMessage Header
 * 
 * @author Yehuda
 */
public class Header {

	private static final String AP_NODE = "AP";
	private static final String VSIM_NODE = "vSim";
	
	private static final int OPCODE_MAX_VALUE = Integer.decode(PropertiesUtil.getInstance().getProperty("OPCODE_MAX_VALUE"));// 0x7f
	private static final int OPCODE_MSB = Integer.decode(PropertiesUtil.getInstance().getProperty("OPCODE_MSB"));// 0x80;
	private static final int MAX_HEADER_LENGTH = Integer.decode(PropertiesUtil.getInstance().getProperty("MAX_HEADER_LENGTH"));
	
	public static final int HEADER_POSITION_VERSION = 0;
	public static final int HEADER_POSITION_ID = 1;
	public static final int HEADER_POSITION_OPCODE = 2;
	public static final int HEADER_POSITION_LENGTH_0 = 3;
	public static final int HEADER_POSITION_LENGTH_1 = 4;
	public static final int HEADER_POSITION_CRC = 5;
	public static final int HEADER_SIZE = 6;
	
	private static final byte SVCP_VERSION = 0x10;

	private byte opcodeValue;
	private MessageTypeOpcodes eopcode;
	private int length;
	private byte crc;
	private boolean isRequest;
	private byte version;
	private byte id;
	private byte[] header;
	
	/**
	 * Used to convert to Header obj from svcp (or just header byte array) byte array 
	 * @param svcp
	 */
	public Header(byte[] svcp) {
		this.version = svcp[HEADER_POSITION_VERSION];
		this.id = svcp[HEADER_POSITION_ID];
		setOpcode(svcp[HEADER_POSITION_OPCODE]);
		setLength(new byte[] { 
				svcp[HEADER_POSITION_LENGTH_0], 
				svcp[HEADER_POSITION_LENGTH_1] });
		this.crc = svcp[HEADER_POSITION_CRC];	
		this.header = new byte[] { 
        		   svcp[HEADER_POSITION_VERSION],
        		   svcp[HEADER_POSITION_ID],
        		   svcp[HEADER_POSITION_OPCODE],
        		   svcp[HEADER_POSITION_LENGTH_0],
        		   svcp[HEADER_POSITION_LENGTH_1],
        		   svcp[HEADER_POSITION_CRC]};
	}
	
	/**
	 * CTOR with default id (0x01)
	 * getting length by the tlv's
	 * @param opcode
	 * @param tlvs TLV...
	 */
	public Header(MessageTypeOpcodes opcode, TLV... tlvs) {
		this.version = SVCP_VERSION;
		this.id = generateOddId();
		//the length is the length of all tlvs (as byte array)
		setLength(tlvs);	
		setOpcode((byte)opcode.getValue());
		initializeHeader();
	}
	
	/**
	 * CTOR with default id (0x01)
	 * getting length by the tlv's
	 * @param opcode
	 * @param tlvs List of TLV
	 */
	public Header(MessageTypeOpcodes opcode, List<TLV> tlvs) {
		this(opcode, tlvs.toArray(new TLV[tlvs.size()]));
	}

	/**
	 * Create header byte array 
	 * @param version
	 * @param id
	 * @param opcode
	 * @param length_0
	 * @param length_1
	 * @param crc
	 * @return header
	 */
	private void initializeHeader() {
		
		byte[] lengthArray = SVCPConversion.convertPayloadLengthToByteArray(this.length);
		byte length_0 = lengthArray[0];
		byte length_1 = lengthArray[1];
		
		byte[] header = new byte[HEADER_SIZE];
		header[HEADER_POSITION_VERSION] = (byte) version;
		header[HEADER_POSITION_ID] = (byte) id;
		header[HEADER_POSITION_OPCODE] = (byte) opcodeValue;
		header[HEADER_POSITION_LENGTH_0] = length_0;
		header[HEADER_POSITION_LENGTH_1] = length_1;
		header[HEADER_POSITION_CRC] = 0x0;
		this.crc = SVCPConversion.calculateCRC(header);
		header[HEADER_POSITION_CRC] = (byte)this.crc;
		this.header = header;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
	}

	public int getId() {
		return this.id;
	}
	
	public String getHexId() {
		return String.format("%02X", this.id);
	}

	public void setId(byte id) {
		this.id = id;
		initializeHeader();
	}
	
	public void setId(int id) {
		setId((byte)id);
	}
	
	public int getOpcodeValue() {
		return opcodeValue;
	}

	/**
	 * set the opcode value check if opcode is response or request (assign it
	 * into isRequest)
	 * 
	 * @param opcode
	 */
	public void setOpcode(byte opcode) {
		// check if MSB is on or off (1/0) bitwise and
		int msb = opcode & OPCODE_MSB;
		// if MSB is set to 0 opcode it is Request opcode, if it is 1 it is
		// Response opcode
		this.isRequest = msb == 0 ? true : false;
		// 0x7f is the max value of opcode
		this.opcodeValue = (byte) (opcode & OPCODE_MAX_VALUE);
		setEopcode(opcode);
		initializeHeader();
	}

	public int getLength() {
		return this.length;
	}

	public void setLength(TLV... tlvs) {
		int tempLength = 0;
		if (tlvs != null) {
			for (TLV tlv : tlvs) {
				// check if length is not more then max size
				if (tlv == null || tlv.getTlv() == null ) {
					// do nothing
				} else if (tlv.getTlv().length > MAX_HEADER_LENGTH)
					throw new IllegalArgumentException();
				else
					tempLength += tlv.getTlv().length;
			}
		}
		this.length = tempLength;
	}

	public void setLength(byte[] length) {
		//get the length as int;
		this.length = Conversions.byteArraysToInt(length);
	}

	public int getCrc() {
		return this.crc;
	}

	public void setCrc(byte crc) {
		this.crc = crc;
		this.header[HEADER_POSITION_CRC] =  crc;
	}
	public void setCrc(int crc) {
		setCrc((byte)crc);
	}

	/**
	 * The Device (Host) id's will be Odd numbers. The vSim id's will be Even
	 * numbers Check if the Id is odd or even number if its an odd number
	 * 
	 * @return node
	 */
	public String getNode() {
		if (this.id % 2 == 0)
			return VSIM_NODE;
		else
			return AP_NODE;
	}

	public boolean isRequest() {
		return this.isRequest;
	}

	public void setRequest(boolean isRequest) {
		this.isRequest = isRequest;
	}

	public MessageTypeOpcodes getEopcode() {
		return this.eopcode;
	}

	public void setEopcode(MessageTypeOpcodes eopcode) {
		this.eopcode = eopcode;
	}
	
	public void setEopcode(byte eopcode) {
		//get the opcode enum object from int id
		this.eopcode = MessageTypeOpcodes.getMessageTypeOpcode(eopcode);
	}

	public byte[] getHeader() {
		return this.header;
	}
	
	public void setHeader(byte[] header) {
		this.header = header;
	}
	
	/**
	 * Generate odd id for request from AP to cloud header
	 * 
	 * @return id as byte
	 */
	private byte generateOddId() {
		byte id = (byte) (Math.random() * 256);
		id += (id % 2 == 0 ? 1 : 0);
		return id;
	}
	
	@Override
	public String toString() {	
		StringBuilder builder = new StringBuilder();
		builder.append("Header ");
		builder.append("[Version = " + this.version);
		builder.append(", ID = " + this.id);
		builder.append(", Node = " + getNode());
		builder.append(", Opcode=");
		builder.append(this.eopcode != null ? this.eopcode.name() + String.format("[0x%X] ", this.opcodeValue) : this.opcodeValue);
		builder.append(", Length = " + length);
		builder.append(", CRC = " + String.format("%X", this.crc));
		builder.append(", Mode = ");
		builder.append(this.isRequest ? "Request" : "Response");
		builder.append(", Message = " + Conversions.byteArrayToHexString(this.header) + "]");

		return    builder.toString();
	}

}