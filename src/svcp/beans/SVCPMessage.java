package svcp.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import global.Conversions;
import svcp.enums.Tag;
import svcp.util.SVCPConversion;

/**
 * This Class made for better managing of SVCPMessage protocol messages that
 * includes Header and one or more TLV
 * 
 * @author Yehuda
 *
 */
public class SVCPMessage {
	private Header header;
	private List<TLV> tlvs;
	private byte[] svcp;
	
	public SVCPMessage() {}
	
	public SVCPMessage(Header header, TLV... tlvs) {
		this(header, Arrays.asList(tlvs));
	}
	
	public SVCPMessage(Header header, List<TLV> tlvs) {
		this.header = header;
		this.tlvs = tlvs;
		byte[] temp = header.getHeader();
		for(TLV tlv : tlvs)
			if (tlv != null)
				temp = Conversions.combineByteArrays(temp, tlv.getTlv());
		this.svcp = temp;
	}
	
	public SVCPMessage(Header header){
		this.header = header;
		this.tlvs = new ArrayList<TLV>();
		this.svcp = header.getHeader();	
	}
	
	public SVCPMessage(byte[] svcp){
		this(new Header(svcp),SVCPConversion.extractSvcpAsTlvObjs(svcp));
	}
	
	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public List<TLV> getTlvs() {
		return tlvs;
	}

	public void setTlvs(List<TLV> tlvs) {
		this.tlvs = tlvs;
	}

	public byte[] getSvcp() {
		return svcp;
	}

	public void setSvcp(byte[] svcp) {
		this.svcp = svcp;
	}
	
	/**
	 * Find in tlv's the first result with specific tag
	 * @param Tag
	 * @return TLV
	 */
	public TLV getTlvByTag(Tag tag){
		return tlvs
				.stream()
				.filter(tlv -> tlv.getType().equals(tag))
				.findFirst()
				.orElse(null);
	}
	
	/**
	 *  Find in tlv's all tlv's with specific tag
	 * @param Tag
	 * @return List of TLV
	 */
	public List<TLV> getTlvsByTag(Tag tag) {
		List<TLV> rv = new ArrayList<>();
		for (TLV tlv : tlvs)
			if (tlv.getType().name().equalsIgnoreCase(tag.name()))
				rv.add(tlv);
		return rv.isEmpty() ? null : rv;
	}

	@Override
	public String toString() {
		String rv = "SVCPMessage:\n" + header;
		for (TLV tlv : tlvs)
			rv += "\n" + tlv;
		rv += "\nSVCP Message: [" + Conversions.byteArrayToHexString(svcp) + "]";
		return rv;
	}
}
