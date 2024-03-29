package svcp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import global.Conversions;
import regex.VphSVCPPatterns;
import svcp.beans.SVCPMessage;
import svcp.beans.TLV;
import svcp.enums.FileId;
import svcp.enums.FilePath;
import svcp.enums.MessageTypeOpcodes;
import svcp.enums.Tag;

/**
 * This is a Util for SET_FILES: insert all the log include the set_files svcp
 * msgs then press exit then you will see if there is a duplication
 * 
 * @author Yehuda
 *
 */
public class AtmelFilesUtil {
	public static void main(String[] args) {
		List<SVCPMessage> svcpMessages = extractSetFilesSvcps();
		printFiles(svcpMessages);
	}

	/**
	 * @param svcpMessages
	 */
	public static void printFiles(List<SVCPMessage> svcpMessages) {
		Map<FileId, String> fileIds = new HashMap<>();
		for (SVCPMessage message : svcpMessages) {
			if (!message.getHeader().isRequest())
				continue;
			TLV fileData = extractFileDataFromSVCPMsg(message);
			TLV fileId = extractFileIdFromSVCPMsg(message);
			TLV filePath = extractFilePathFromSVCPMsg(message);
			FileId fileIdEnum = null;
			FilePath filePathEnum = null;
			String dataValue = fileData.getStringValue();

			String msg = "";
			if (fileId != null && fileId.getValue() != null && filePath != null) {
				fileIdEnum = FileId.getFileId(fileId.getValue());
				filePathEnum = FilePath.getFilePath(filePath.getValue());
				msg = "*\t" + filePathEnum + ": " + fileIdEnum + "," + dataValue;
			}
			if (fileId != null && fileIdEnum != null && !fileIds.containsKey(fileIdEnum)) {
				System.out.println(msg + " First Time Added");
			} else if (fileIds.containsKey(fileIdEnum) && fileIds.get(fileIdEnum).equals(dataValue))
				System.err.println(msg + " Same file Added again");
			else if (fileIds.containsKey(fileIdEnum) && !fileIds.get(fileIdEnum).equals(dataValue))
				System.out.println(msg + " File has changed");
			else
				continue;

			fileIds.put(fileIdEnum, dataValue);
		}
	}

	private static TLV extractFilePathFromSVCPMsg(SVCPMessage message) {
		for (TLV tlv : message.getTlvs())
			if (tlv.getType().equals(Tag.FILE_PATH))
				return tlv;
		return null;
	}

	/**
	 * @return
	 */
	public static List<SVCPMessage> extractSetFilesSvcps() {
		List<SVCPMessage> svcpMessages = new ArrayList<>();
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("Insert the HexString svcp message:\nType exit to get results");
		String hexString = "";
		String regex = "(?<date>\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\.\\d{3}).*\\s(?<svcp>(?<header>(?<version>10)(?<id>[\\da-fA-f]{2,2})(?<opcode>[\\da-fA-f]{2,2})(?<length>[\\da-fA-f]{4})(?<crc>[\\da-fA-F]{2}))(?<tlvs>[\\da-fA-f])?[\\da-fA-f]+)";
		Pattern svcppattern = Pattern.compile(regex);
		while ((hexString = sc.nextLine()) != null) {
			if (hexString.equals("exit"))
				break;
			try {
				Matcher matcher = svcppattern.matcher(hexString);
				if (matcher.find()) {
					SVCPMessage printSvcps = printSvcps(matcher.group(VphSVCPPatterns.SVCP_GROUP),
							MessageTypeOpcodes.SET_FILES);
					if (printSvcps != null)
						svcpMessages.add(printSvcps);
					System.out.println("a msg added, svcps size = " + svcpMessages.size());
				}
			} catch (Exception e) {
			}
		}
		return svcpMessages;
	}

	private static TLV extractFileIdFromSVCPMsg(SVCPMessage message) {
		for (TLV tlv : message.getTlvs())
			if (tlv.getType().equals(Tag.FILE_ID))
				return tlv;
		return null;
	}

	private static TLV extractFileDataFromSVCPMsg(SVCPMessage message) {
		for (TLV tlv : message.getTlvs())
			if (tlv.getType().equals(Tag.FILE_DATA))
				return tlv;
		return null;
	}

	public static SVCPMessage printSvcps(String svcps, MessageTypeOpcodes eopcode) {
		int headerLength = 12;
		int totalLength = svcps.length();
		int remainingLength = totalLength;

		int beginIndex = 0;
		int beginLengthIndex = 6;
		int endLengthIndex = beginLengthIndex + 4;
		while (remainingLength > 0) {

			String substring = svcps.substring(beginLengthIndex, endLengthIndex);
			int hexStringToDecimalInt = Conversions.hexStringToDecimalInt(substring) * 2;
			int packetLength = headerLength + hexStringToDecimalInt;

			if (remainingLength < packetLength) {
				System.err.println(
						"Wrong SVCP structure, length is too long\n" + svcps.substring(beginIndex, totalLength));
				break;
			}
			String singelSvcp = svcps.substring(beginIndex, beginIndex + packetLength);
			SVCPMessage msgExtract = new SVCPMessage(singelSvcp);
			if (msgExtract.getHeader().getEopcode().equals(eopcode))
				return msgExtract;

			beginIndex += packetLength;
			beginLengthIndex = beginIndex + 6;
			endLengthIndex = beginLengthIndex + 4;
			remainingLength -= packetLength;
		}
		return null;
	}
}
