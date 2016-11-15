package svcp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import global.Conversions;
import regex.VphSVCPPatterns;
import svcp.beans.SVCPMessage;
import svcp.enums.MessageTypeOpcodes;

/**
 * This SVCP's tools is a SVCP debugging tools: you can past any lines of log,
 * the parser will check for complete SVCP's and will print them when finding
 * them Example:</br>
 * Insert the HexString svcp message:</br>
 * 916 11-13 16:57:35.718 I/DeviceUtil( 3835): Sending data to VSim:
 * 10110700181E19027FFF1A026F7E1C0BFFFFFFFF24F51028CBFF00150101</br>
 * 
 * 924 11-13 16:57:35.738 I/DeviceUtil( 3835): Sending data to VSim:
 * 10130700151119027FFF1A026F7E1C0BFFFFFFFF24F51028CBFF00</br>
 * 
 * 958 11-13 16:57:35.798 I/DeviceUtil( 3835): Sending data to VSim:
 * 10150700131119027FFF1A026F071C09084952107067140021</br>
 * 
 * 971 11-13 16:57:35.828 I/DeviceUtil( 3835): Sending data to VSim:
 * 101707001D1D19027FFF1A026F731C0EFFFFFFFFFFFFFF24F5100000FF010F01011F00</br>
 * 
 * SVCPMessage:</br>
 * Header [Version = 10, ID = 11, Node = AP, Opcode=SET_FILES [0x7], Length =
 * 24, CRC = 1E, Mode = Request, Message = 10110700181E]</br>
 * TLV [Type = FILE_PATH [0x19], Length = 2, Value = adf [0x7FFF], Message =
 * 19027FFF]</br>
 * TLV [Type = FILE_ID [0x1A], Length = 2, Value = ef.loci [0x6F7E], Message =
 * 1A026F7E]</br>
 * TLV [Type = FILE_DATA [0x1C], Length = 11, Value = FFFFFFFF24F51028CBFF00,
 * Message = 1C0BFFFFFFFF24F51028CBFF00]</br>
 * TLV [Type = SET_FILES_OPTIONS [0x15], Length = 1, Value = IN_HOME_COUNTRY,
 * Message = 150101]</br>
 * SVCP Message:
 * [10110700181E19027FFF1A026F7E1C0BFFFFFFFF24F51028CBFF00150101]</br>
 * SVCPMessage:</br>
 * Header [Version = 10, ID = 13, Node = AP, Opcode=SET_FILES [0x7], Length =
 * 21, CRC = 11, Mode = Request, Message = 101307001511]</br>
 * TLV [Type = FILE_PATH [0x19], Length = 2, Value = adf [0x7FFF], Message =
 * 19027FFF]</br>
 * TLV [Type = FILE_ID [0x1A], Length = 2, Value = ef.loci [0x6F7E], Message =
 * 1A026F7E]</br>
 * TLV [Type = FILE_DATA [0x1C], Length = 11, Value = FFFFFFFF24F51028CBFF00,
 * Message = 1C0BFFFFFFFF24F51028CBFF00]</br>
 * SVCP Message: [10130700151119027FFF1A026F7E1C0BFFFFFFFF24F51028CBFF00]</br>
 * SVCPMessage:</br>
 * Header [Version = 10, ID = 15, Node = AP, Opcode=SET_FILES [0x7], Length =
 * 19, CRC = 11, Mode = Request, Message = 101507001311]</br>
 * TLV [Type = FILE_PATH [0x19], Length = 2, Value = adf [0x7FFF], Message =
 * 19027FFF]</br>
 * TLV [Type = FILE_ID [0x1A], Length = 2, Value = ef.imsi [0x6F07], Message =
 * 1A026F07]</br>
 * TLV [Type = FILE_DATA [0x1C], Length = 9, Value = 084952107067140021, Message
 * = 1C09084952107067140021]</br>
 * SVCP Message: [10150700131119027FFF1A026F071C09084952107067140021]</br>
 * SVCPMessage:</br>
 * Header [Version = 10, ID = 17, Node = AP, Opcode=SET_FILES [0x7], Length =
 * 29, CRC = 1D, Mode = Request, Message = 101707001D1D]</br>
 * TLV [Type = FILE_PATH [0x19], Length = 2, Value = adf [0x7FFF], Message =
 * 19027FFF]</br>
 * TLV [Type = FILE_ID [0x1A], Length = 2, Value = ef.psloci [0x6F73], Message =
 * 1A026F73]</br>
 * TLV [Type = FILE_DATA [0x1C], Length = 14, Value =
 * FFFFFFFFFFFFFF24F5100000FF01, Message =
 * 1C0EFFFFFFFFFFFFFF24F5100000FF01]</br>
 * TLV [Type = SIM_GENERATION [0xF], Length = 1, Value = USIM, Message =
 * 0F0101]</br>
 * TLV [Type = APPLY_NOW [0x1F], Length = 0, Value = EMPTY_VALUE, Message =
 * 1F00]</br>
 * SVCP Message:
 * [101707001D1D19027FFF1A026F731C0EFFFFFFFFFFFFFF24F5100000FF010F01011F00]</br>
 * 
 * @author Yehuda
 *
 */
public class SVCPs {
	private static final String ANY_HEX_STRING = "[\\da-fA-f]";
	private static final String SVCP_REGEX = ""
			+ "(?<svcp>"
			+ "(?<header>"
			+ "(?<version>10)"
			+ "(?<id>"+ANY_HEX_STRING+"{2})"
			+ "(?<opcode>"+ANY_HEX_STRING+"{2})"
			+ "(?<length>"+ANY_HEX_STRING+"{4})"
			+ "(?<crc>"+ANY_HEX_STRING+"{2})"
			+ ")"
			+ "(?<tlvs>"+ANY_HEX_STRING+")?"+ANY_HEX_STRING+"+"
			+ ")";
	private static final int HEADER_LENGTH = 12;

	public static void main(String[] args) {
		boolean includeLogLines = false;
		boolean includeErrorForShortSVCPs = false;
		run(includeLogLines,includeErrorForShortSVCPs);
	}

	/**
	 * @param includeErrorForShortSVCPs 
	 * 
	 */
	public static void run(boolean includeLogLines, boolean includeErrorForShortSVCPs) {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("Insert the HexString svcp message:");
		String hexString = "";
		String regex = SVCP_REGEX;
		Pattern svcppattern = Pattern.compile(regex);
		while ((hexString = sc.nextLine()) != null) {
			try {
				Matcher matcher = svcppattern.matcher(hexString);
				if (matcher.find()) {
					String opcode = matcher.group("opcode");
					if(!opcode.equalsIgnoreCase("03"))
						printSvcps(matcher.group(VphSVCPPatterns.SVCP_GROUP),includeLogLines,includeErrorForShortSVCPs);
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * @param svcps
	 * @param includeErrorForShortSVCPs 
	 */
	private static void printSvcps(String svcps, boolean includeLogLines, boolean includeErrorForShortSVCPs) {
		int headerLength = HEADER_LENGTH;
		int totalLength = svcps.length();
		int remainingLength = totalLength;

		int beginIndex = 0;
		int beginLengthIndex = 6;
		int endLengthIndex = beginLengthIndex + 4;
		List<SVCPMessage> svcpMessages = new ArrayList<>();
		while (remainingLength > 0) {
			String substring = svcps.substring(beginLengthIndex, endLengthIndex);
			int hexStringToDecimalInt = Conversions.hexStringToDecimalInt(substring) * 2;
			int packetLength = headerLength + hexStringToDecimalInt;

			if (includeErrorForShortSVCPs && remainingLength < packetLength) {
				System.err.println(
						"Wrong SVCP structure, length is too long\n" + svcps.substring(beginIndex, totalLength));
				break;
			}
			String singelSvcp = svcps.substring(beginIndex, beginIndex + packetLength);
			SVCPMessage extractSVCP = new SVCPMessage(singelSvcp);
			if (!includeLogLines){
				if (!extractSVCP.getHeader().getEopcode().equals(MessageTypeOpcodes.LOG_LINE))
					svcpMessages.add(extractSVCP);
			}
			else 
				svcpMessages.add(extractSVCP);
			

			beginIndex += packetLength;
			beginLengthIndex = beginIndex + 6;
			endLengthIndex = beginLengthIndex + 4;
			remainingLength -= packetLength;
		}
		svcpMessages.forEach(System.out::println);
	}
}
