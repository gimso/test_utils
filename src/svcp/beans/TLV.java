package svcp.beans;

import global.Conversions;
import svcp.enums.Tag;
import svcp.util.SVCPConversion;

/**
 * Bean class for SVCP TLV's
 * 
 * @author Yehuda
 *
 */
public class TLV {

	private Tag type;
	private byte typeValue;
	private int length;
	private String stringValue;
	private byte[] value;
	private byte[] tlv;

	/**
	 * Extract TLV object from tlv byte array
	 * 
	 * @param tlv
	 */
	public TLV(byte[] tlv) {
		this.tlv = tlv;
		setType(tlv[0]);
		setLength(tlv);
		setValueFromTlv(tlv);
		setStringValue(this.type, this.value);
	}

	/**
	 * Create TLV object from ETag and value
	 * 
	 * @param type
	 * @param value
	 * @param completeTlv
	 */
	public TLV(Tag type, byte[] value) {
		setType(type);
		this.value = value;
		this.length = value.length;
		boolean isJumbo = value.length > 0xff;
		byte[] header = isJumbo 
				? new byte[] { this.typeValue, (byte) 0xff } 
				: new byte[] { this.typeValue };
		byte[] lengthByteArray = isJumbo 
				? new byte[] { (byte) (length >>> 8), (byte) length }
				: new byte[] { (byte) length };

		this.tlv = Conversions.combineByteArrays(header, lengthByteArray, value);
		setStringValue(this.type, this.value);
	}

	/**
	 * Create TLV object from ETag and value
	 * 
	 * @param type
	 * @param value
	 * @param completeTlv
	 */
	public TLV(Tag type, byte value) {
		this(type, new byte[] { value });
	}

	/**
	 * create TLV with zero value (tag only)
	 * 
	 * @param type
	 */
	public TLV(Tag type) {
		setType(type);
		this.length = 0;
		this.value = null;
		this.stringValue = "";
		this.tlv = new byte[] { this.typeValue, 0 };
	}

	public Tag getType() {
		return type;
	}

	/**
	 * Initialized the Type as a tag and as a byte
	 * 
	 * @param type
	 */
	public void setType(byte type) {
		this.type = Tag.getTag(type);
		this.typeValue = type;
	}

	/**
	 * Initialized the Type as a tag and as a byte
	 * 
	 * @param Tag
	 */
	public void setType(Tag tag) {
		this.typeValue = (byte) tag.getValue();
		this.type = tag;
	}

	public int getLength() {
		return length;
	}

	/**
	 * Set the length if it's not bigger then 2 bytes
	 * 
	 * @param length
	 */
	public void setLength(int length) {
		if (length > 0xffff/* Max value of tlv "jumbo length" */) {
			throw new IllegalArgumentException();
		}
		this.length = length;
	}

	/**
	 * set length from TLV byte array
	 * 
	 * @param tlv
	 */
	public void setLength(byte[] tlv) {
		this.length = SVCPConversion.getLengthFromTlvByteArray(tlv);
	}

	public byte[] getValue() {
		return value;
	}
	
	/**set value by value byte array*/
	public void setValue(byte[] value) {
		this.value = value;
	}
	
	/**
	 * set the value getting from TLV array
	 * 
	 * @param tlv byte array
	 */
	public void setValueFromTlv(byte[] tlv) {
		this.value = SVCPConversion.getValueFromTlvByteArray(tlv);
	}

	public byte[] getTlv() {
		return tlv;
	}

	public void setTlv(byte[] tlv) {
		this.tlv = tlv;
	}
	
	public String getStringValue() {
		return stringValue;
	}

	/**
	 * Convert the value to String according to the tag
	 * 
	 * @param value
	 */
	public void setStringValue(Tag tag, byte[] value) {
		this.stringValue = SVCPConversion.getValueAsStringFromTagAndValue(tag, value);
	}

	@Override
	public String toString() {
		return "TLV "
				+ "[Type = " + type.name() + String.format(" [0x%X]", type.getValue()) 
				+ ", Length = " + ((getLength() < 1) ? getLength() & 0xff : getLength())
				+ ", Value = " + stringValue 
				+ ", Message = " + Conversions.byteArrayToHexString(tlv) 
				+ "]";

	}
}