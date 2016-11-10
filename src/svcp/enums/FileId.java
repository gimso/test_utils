package svcp.enums;

import java.util.Arrays;

import global.Conversions;

/**
 * Utility to get file id and SIM generation path for SET_FILES on the VFS
 * 
 * @author Yehuda
 *
 */
public enum FileId {

	/* ef.dir 2F00 */
	EF_DIR("ef.dir", "2F00"),
	/* ef.pl 2F05 */
	EF_PL("ef.pl", "2F05"),
	/* ef.arr 2F06 */
	EF_ARR_1("ef.arr", "2F06"),
	/* ef.iccid 2FE2 */
	EF_ICCID("ef.iccid", "2FE2"),
	/* ef.tunnel_data 2FFA */
	EF_TUNNEL_DATA("ef.tunnel_data", "2FFA"),
	/* ef.tunnel_status 2FFB */
	EF_TUNNEL_STATUS_1("ef.tunnel_status", "2FFB"),
	/* ef.tunnel_status 2FFC */
	EF_TUNNEL_STATUS_2("ef.tunnel_status", "2FFC"),
	/* ef.sms 6F3C */
	EF_SMS("ef.sms", "6F3C"),
	/* ef.arr 6F06 */
	EF_ARR_2("ef.arr", "6F06"),
	/* ef.smsp 6F42 */
	EF_SMSP("ef.smsp", "6F42"),
	/* ef.smss 6F43 */
	EF_SMSS("ef.smss", "6F43"),
	/* ef.adn */
	EF_ADN("ef.adn", "6F3A"),
	/* ef.lp 6F05 */
	EF_LP("ef.lp", "6F05"),
	/* ef.arr 6F06 */
	EF_ARR("ef.arr", "6F06"),
	/* ef.kc 6F20 */
	EF_KC("ef.kc", "6F20"),
	/* ef.sst 6F38 */
	EF_SST("ef.sst", "6F38"),
	/* ef.bcch 6F74 */
	EF_BCCH("ef.bcch", "6F74"),
	/* EF.ACC 6F78 */
	EF_ACC("ef.acc", "6F78"),
	/* ef.fplmn 6F7B */
	EF_FPLMN("ef.fplmn", "6F7B"),
	/* ef.loci 6F7E */
	EF_LOCI("ef.loci", "6F7E"),
	/* ef.locigprs 6F53 */
	EF_LOCIGPRS("ef.locigprs", "6F53"),
	/* ef.ad 6FAD */
	EF_AD("ef.ad", "6FAD"),
	/* ef.phase 6FAE */
	EF_PHASE("ef.phase", "6FAE"),
	/* ef.plmnwact 6F60 */
	EF_PLMNWACT("ef.plmnwact", "6F60"),
	/* ef.oplmnwact 6F61 */
	EF_OPLMNWACT("ef.oplmnwact", "6F61"),
	/* ef.hplmnwact 6F62 */
	EF_HPLMNWACT("ef.hplmnwact", "6F62"),
	/* ef.plmnsel 6F30 */
	EF_PLMNSEL("ef.hplmnwact", "6F30"),
	/* ef.li 6F05 */
	EF_LI("ef.li", "6F05"),
	/* ef.ecc 6FB7 */
	EF_ECC("ef.ecc", "6FB7"),
	/* ef.keys 6F08 */
	EF_KEYS("ef.keys", "6F08"),
	/* ef.ust 6F38 */
	EF_UST("ef.ust", "6F38"),
	/* ef.est 6F56 */
	EF_EST("ef.est", "6F56"),
	/* ef.imsi 6F07 */
	EF_IMSI("ef.imsi", "6F07"),
	/* ef.netpar 6FC4 */
	EF_NETPAR("ef.netpar", "6FC4"),
	/* ef.psloci 6F73 */
	EF_PSLOCI("ef.psloci", "6F73"),
	/* ef.hpplmn 6F31 */
	EF_HPPLMN("ef.hpplmn", "6F31"),
	/* ef.keyps 6F09 */
	EF_KEYPS("ef.keyps", "6F09"),
	/* ef.start-hfn 6F5B */
	EF_START_HFN("ef.start-hfn", "6F5B"),
	/* ef.threshold 6F5C */
	EF_THRESHOLD("ef.threshold", "6F5C"),

	/* ef.ehplmn 6FD9 */
	EF_EHPLMN("ef.ehplmn", "6FD9"),
	/* ef.ehplmnpi 6FDB */
	EF_EHPLMNPI("ef.ehplmnpi", "6FDB");

	private byte[] value;
	private String name;

	FileId(String name,byte[] value) {
		this.value = value;
		this.name = name;
	}

	FileId(String name, String value) {
		byte[] hexStringToByteArray = Conversions.hexStringToByteArray(value);
		this.value = hexStringToByteArray;
		this.name = name;
	}

	public byte[] getValue() {
		return value;
	}

	public String getName() {
		return name + String.format(" [0x%X]", Conversions.bytesToInt(value));
	}

	/**
	 * Gets the FileId by value The byte array contains one or more values
	 * 
	 * @param value
	 * @return FileId
	 */
	public static FileId getFileId(byte[] value) {
		if (value != null) {
			for (FileId fileId : FileId.values()) {
				if (Arrays.equals(fileId.getValue(), value))
					return fileId;
			}
		}
		System.err.println("Unknown value (" + value + ") for FileId , return null");
		return null;
	}

	public static FileId getFileId(String value) {
		return getFileId(Conversions.hexStringToByteArray(value));
	}
	
}
