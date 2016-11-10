package unit_tests;

import static global.Conversions.hexStringToByteArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.Assert;

import global.Conversions;
import regex.VphSVCPPatterns;
import svcp.beans.Header;
import svcp.beans.SVCPMessage;
import svcp.beans.TLV;
import svcp.enums.ResultTags;
import svcp.enums.Tag;
import svcp.util.SVCPConversion;

public class SVCPs {
	public static void main(String[] args) {
//		System.err.close();
		Scanner sc = new Scanner(System.in);
		System.out.println("Insert the HexString svcp message:");
		String hexString = "";
		String regex = "(?<date>\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\.\\d{3}).*\\s(?<svcp>(?<header>(?<version>10)(?<id>[\\da-fA-f]{2,2})(?<opcode>[\\da-fA-f]{2,2})(?<length>[\\da-fA-f]{4})(?<crc>[\\da-fA-F]{2}))(?<tlvs>[\\da-fA-f])?[\\da-fA-f]+)";
		Pattern svcppattern = Pattern.compile(regex );
		while ((hexString = sc.nextLine()) != null) {

			try {
				boolean matchesNumbers = hexString.matches("[\\d].{6,}");
				boolean startsWith = hexString.matches("^[Tx:|Rx:|data:|message:].*");
				// boolean hasSvcp =
				Matcher matcher = svcppattern.matcher(hexString);
				if (matcher.find()) {
					// printSvcp(sc.next());
					printSvcps(matcher.group(VphSVCPPatterns.SVCP_GROUP));

				} else if (matchesNumbers && hexString.startsWith("10"))
					printSvcps(hexString);
			} catch (Exception e) {
			
			}
		}

	}

	private static void printSvcp(String hexString) {
		try {
			//System.err.close();
			byte[] hexStringToByteArray = Conversions.hexStringToByteArray(hexString);
			Header tempHeader = new Header(hexStringToByteArray);
			int firstLength = Header.HEADER_SIZE + tempHeader.getLength();
			int length = hexStringToByteArray.length - firstLength;
			byte[] tempFirst = new byte[firstLength];
			System.arraycopy(hexStringToByteArray, 0, tempFirst, 0, firstLength);
			byte[] tempSec = new byte[length];
			boolean b = firstLength < hexStringToByteArray.length;
			if (b) {
				System.out.println("Parse 2 messages");
				System.out.println(new SVCPMessage(tempFirst));
				System.arraycopy(hexStringToByteArray, firstLength, tempSec, 0, length);
				System.out.println(new SVCPMessage(tempSec));
			} else {
				System.out.println(new SVCPMessage(tempFirst));
			}
			// SVCPMessage rv = new SVCPMessage(hexStringToByteArray);
			// System.out.println(rv);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param svcps
	 */
	public static void printSvcps(String svcps) {
		int headerLength = 12;
		int totalLength = svcps.length();
		int remainingLength = totalLength;

		int beginIndex = 0;
		int beginLengthIndex = 6;
		int endLengthIndex = beginLengthIndex + 4;
		List<SVCPMessage> svcpMessages = new ArrayList<>();
		while (remainingLength>0) {
			//System.err.println("remaining:\t"+svcps.substring(beginIndex,totalLength));
			String substring = svcps.substring(beginLengthIndex, endLengthIndex);
			int hexStringToDecimalInt = Conversions.hexStringToDecimalInt(substring) * 2;
			//System.out.println(substring);
			int packetLength = headerLength + hexStringToDecimalInt;

			if (remainingLength < packetLength){
				System.err.println("Wrong SVCP structure, length is too long\n"+svcps.substring(beginIndex,totalLength));
				break;
			}
			String singelSvcp = svcps.substring(beginIndex, beginIndex + packetLength);
			svcpMessages.add(new SVCPMessage(singelSvcp));

			beginIndex += packetLength;
			beginLengthIndex = beginIndex + 6;
			endLengthIndex = beginLengthIndex + 4;
			remainingLength -= packetLength;
		}
		svcpMessages.forEach(System.out::println);
	}
	public static List<SVCPMessage> getSvcpMsgs(String strings) {
		List<SVCPMessage> svcpMessages = new ArrayList<>();
		String defaultHexStr = "\\da-fA-f";
		String idGroup = //(idGroup == null) 
			//?
					"([" + defaultHexStr + "]{2,2})"; 
			//: "(" + idGroup + ")";
		String opcodeGroup = //(opcodeGroup == null) 
			//?
					"([08]{1,1}[" + defaultHexStr + "]{1,1})";
			//: opcodeGroup.length() < 2 ? "(0" + opcodeGroup + ")" : "(" + opcodeGroup + ")";
		String lengthGroup = "([\\da-fA-f]{4,4})";
		String svcpMsgGroup = "(10" + idGroup + opcodeGroup + lengthGroup + ".*)";
		
		System.out.println(svcpMsgGroup+svcpMsgGroup+svcpMsgGroup);//.substring(0, svcpMsgGroup.length()-1)+svcpMsgGroup+svcpMsgGroup+svcpMsgGroup+svcpMsgGroup);
	
		Pattern pattern = Pattern.compile(svcpMsgGroup, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(strings);
			while (matcher.find()) {
				String group = matcher.group(1);
				System.out.println(group);
				System.err.println("SVCP Extract: "+new SVCPMessage(group));
				svcpMessages.add(new SVCPMessage(group));
			}	
		
		return svcpMessages;
	}

	public static void main3(String[] args) {
		String header = "100181004DDD";
		String tlv1 = "230107";//
		String svcpst = "100181004DDD230107010C4646464646464646464646460212332E302E302E323031363032303731373238030B3235352E3235352E3235350416676C6F62616C5F73746167696E675F6970686F6E65368001";
		System.out.println(new Header(hexStringToByteArray(svcpst)));
	}

	public static void main2(String[] args) {
		String x = "xxx";
		String y = "123";
		String z = "0x100181004DDD230107010C4646464646464646464646460212332E302E302E323031363032303731373238030B3235352E3235352E3235350416676C6F62616C5F73746167696E675F6970686F6E6536800101";
		System.out.println(hexStringToByteArray(z));
		System.out.println(hexStringToByteArray(x));

		try {
			int xx = Integer.valueOf(x);
		} catch (NumberFormatException e) {
			System.err.println(x + " is not a number");
		}
		System.out.println(Integer.valueOf(y));

		String s = "100101000011";

		extractAndPrintSVCPFromHexString(s);

	}

	public static void extractAndPrintSVCPFromHexString(String string) {

		byte[] response = Conversions.hexStringToByteArray(string);
		SVCPMessage svcpMessage = SVCPConversion.getAllSVCPMessages(response).get(0);
		System.out.println(svcpMessage);
	}

	/**
	 * 
	 */
	public static void x() {

		String stringSvcps = "100181000696230107800101" + "100181000696230107800101"
				+ "100181001181010C464646464646464646464646800101";
		byte[] svcps = Conversions.hexStringToByteArray(stringSvcps);

		new SVCPs().getAllSVCPs(svcps).forEach(System.out::println);

	}

	public List<SVCPMessage> getAllSVCPs(byte[] svcps) {
		List<SVCPMessage> SVCPlist = new ArrayList<>();
		int srcPos = 0;
		int destPos = 0;
		while (srcPos < svcps.length) {
			Header header;
			List<TLV> tlvs;

			byte[] tlvsSize = new byte[] { svcps[srcPos + Header.HEADER_POSITION_LENGTH_0],
					svcps[srcPos + Header.HEADER_POSITION_LENGTH_1] };
			int length = Header.HEADER_SIZE + Conversions.byteArraysToInt(tlvsSize);

			byte[] tempSVCP = new byte[length];
			System.arraycopy(svcps, srcPos, tempSVCP, destPos, length);

			header = new Header(tempSVCP);
			tlvs = SVCPConversion.extractSvcpAsTlvObjs(tempSVCP);

			SVCPlist.add(new SVCPMessage(header, tlvs));
			srcPos += length;
		}
		return SVCPlist;
	}
	public static List<SVCPMessage> allMsgFromFile;
	
		public static void resultAssertion(File logCatFile, ResultTags resultTag) {
			List<SVCPMessage> allMsgsFromFile = getAllMsgsFromFile(logCatFile);
			for (SVCPMessage svcpMessage : allMsgsFromFile){
				Assert.assertNotNull(svcpMessage, "Response Message is null");
				Assert.assertTrue(!svcpMessage.getTlvs().isEmpty(),"TLV list is empty");
				TLV result = svcpMessage.getTlvs()
										.stream()
										.filter(tlv -> tlv.getType().equals(Tag.RESULT_TAG))
										.findFirst()
										.orElse(null);
				Assert.assertNotNull(result);
				
				String actual = svcpMessage.getTlvByTag(Tag.RESULT_TAG).getStringValue();
				String expected = resultTag.name();
				String reason = "Result value doesn't much the expected result ("+resultTag.name()+")";
				MatcherAssert.assertThat(reason,actual, Matchers.containsString(expected));
				
			}
		}
	
		public static List<SVCPMessage> getAllMsgsFromFile(File file) {
			String hexString;
			allMsgFromFile = new ArrayList<>();
			try (Scanner scanner = new Scanner(file);) {
				while ((hexString = scanner.next()) != null) {
					boolean matchesNumbers = hexString.matches("[\\d].{6,}");
					boolean startsWith = hexString.matches("^[Tx:|Rx:|data:|message:].*");
					
					if (startsWith)
						initSvcpList(scanner.next());
					else if (matchesNumbers && hexString.startsWith("10"))
						initSvcpList(hexString);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return allMsgFromFile.isEmpty()?null:allMsgFromFile;
		}
		private static void initSvcpList(String hexString) {
			try {
				byte[] hexStringToByteArray = Conversions.hexStringToByteArray(hexString);
				Header tempHeader = new Header(hexStringToByteArray);
				int firstLength = Header.HEADER_SIZE + tempHeader.getLength();
				int length = hexStringToByteArray.length - firstLength;
				byte[] tempFirst = new byte[firstLength];
				System.arraycopy(hexStringToByteArray, 0, tempFirst, 0, firstLength);
				byte[] tempSec = new byte[length];
				boolean b = firstLength < hexStringToByteArray.length;
				if (b) {
					allMsgFromFile.add(new SVCPMessage(tempFirst));
					System.arraycopy(hexStringToByteArray, firstLength, tempSec, 0, length);
					allMsgFromFile.add(new SVCPMessage(tempSec));
				} else {
					allMsgFromFile.add(new SVCPMessage(tempFirst));
				}
			} catch (Exception e) {
				//DO NOTHING;
			}
			
		}
}

// // System.out.println(x.substring(0, x2*2));
// [5088] Simgo-Monitor: 000000
// SVCP: Tx: 10008D00039E800117
// E/simgo:VSimControl( 525): Cannot parse vsim data:
// 1001810006962301078001011001810006962301078001011001810056C6230107010C4646464646464646464646460212332E302E302E323031363032303731373238030B3235352E3235352E3235350416676C6F62616C5F73746167696E675F6970686F6E65360E0100100408091010800101
// Message:SVCP message id(1) sanityRe
// E/simgo:VSimControl( 525): Cannot parse vsim data:
// 1001810006962301078001011001810056C6230107010C4646464646464646464646460212332E302E302E323031363032303731373238030B3235352E3235352E3235350416676C6F62616C5F73746167696E675F6970686F6E65360E0100100408091010800101
// byte[] svcps = hexStringToByteArray(svcpst);
// System.out.println(SVCPConversion.getAllSVCPMessages(svcps));
// Message:SVCP message id(1) sanityResult(ERROR_MESSAGE_LENGT
// [6568] Simgo-Monitor: 000000 SVCP: Tx: 10008D00039E800117

// String hexString = "100181000393800101";
// String hexString = sc.next();
// String nextHex = sc.next();
// String theNext = sc.next();
// String regex = "^([0-9a-fA-F])+$";
// Sent data:
// Received message:

// Sent data: 1001010002120E00
// 03-28 17:03:49.751 D/simgo:VSimControl( 527): Sent data:
// 101B0100040E10000E00
// 03-28 17:03:49.793 V/simgo:VSimControl( 527): Received message:
// 1001810006968001010E0100101B8100119B8001010E01001009080910100000000000
// {6,}
// temp += sc.next();
// p // System.out.println("Length:"+stringSvcps.length()/2);
// System.out.println("Length_1:
// "+"100181000696230107800101".length()/2);
// System.out.println("Length_3:
// "+"100181000696230107800101".length()/2);
// System.out.println("Length_2:
// "+"100181001181010C464646464646464646464646800101".length()/2);
//
//
//
// System.out.println(stringSvcps);
// System.out.println("Total bytes "+svcps.length);
// // Header header = new Header(svcps);
// // header.getLength();
// // int x2 = Header.HEADER_SIZE + header.getLength() ;
//
// // int beginIndex = 0;
// // int endIndex = x2*2;
// // String y = stringSvcps.substring(beginIndex,endIndex);
// // byte[] svcp_1 = SVCPConversion.hexStringToByteArray(y);
// // Main.printSvcp(svcp_1);
//
// int srcPos = 0;
// int destPos = 0;
// int headerPositionLength0 = Header.HEADER_POSITION_LENGTH_0;
// int headerPositionLength1 = Header.HEADER_POSITION_LENGTH_1;
// int index0 = srcPos + headerPositionLength0;
// int index1 = srcPos + headerPositionLength1;
// byte b = svcps[index0/* 3 */];
// byte c = svcps[index1/* 4 */];
// System.out.println("srcPos "+srcPos);
// int length = SVCPConversion.byteArraysToInt(new byte[] { b, c }) +
// Header.HEADER_SIZE;
//
// byte[] svcp_1 = new byte[length];
// System.arraycopy(svcps, srcPos, svcp_1, destPos, length);
// Main.printSvcp(svcp_1);
//
// srcPos += length;// +1 ?
// System.out.println("first svcp length "+svcp_1.length);
// System.out.println("srcPos "+srcPos);
// int index3 = srcPos + headerPositionLength0;
// int index4 = srcPos + headerPositionLength1;
// b = svcps[index3/* 3 */];
// c = svcps[index4/* 4 */];
// length = SVCPConversion.byteArraysToInt(new byte[] { b, c }) +
// Header.HEADER_SIZE;
//
// byte[] svcp_2 = new byte[length];
// System.arraycopy(svcps, srcPos, svcp_2, destPos, length);
// Main.printSvcp(svcp_2);
//
// srcPos += length;// +1 ?
// System.out.println("second svcp length "+svcp_2.length);
// System.out.println("srcPos "+srcPos);
// int index5 = srcPos + headerPositionLength0;
// int index6 = srcPos + headerPositionLength1;
// b = svcps[index5/* 3 */];
// c = svcps[index6/* 4 */];
// length = SVCPConversion.byteArraysToInt(new byte[] { b, c }) +
// Header.HEADER_SIZE;
// System.out.println(length);
// byte[] svcp_3 = new byte[length];
// System.out.println("svcps, srcPos, svcp_3, destPos,
// length\t"+svcps.length+", "+ srcPos+", "+ svcp_3.length+", "+
// destPos+", "+ length);
//
// System.arraycopy(svcps, srcPos, svcp_3, destPos, length);
// Main.printSvcp(svcp_3);
//
// // int srcPos = 0;
// // byte[] svcp = new byte[x2];
// // System.arraycopy(svcps, srcPos , svcp, 0, x2);
// //// srcPos = x;
// // Main.printSvcp(svcp);
// // System.out.println(x2);
// rintSvcp(temp);
//
// while (sc.next()!=null){// (sc.next() != "q") {
// String hexString = sc.next();
// String nextHex = sc.next();
// String theNext = sc.next();
// String regex = "^([0-9a-fA-F])+$";
// if (hexString.matches(regex))
// {
// printSvcp(hexString);
//
// temp += sc.next();
// printSvcp(temp);

// }

// else
// printSvcp(hexString);
// }
// else{
/// printSvcp(hexString);
// printSvcp(temp);
// }//System.out.println(new
// SVCPMessage(hexStringToByteArray(hexString)));
// [6568] Simgo-Monitor: 000010 SVCP: Tx:
// 1001810052C2230107010C4646464646464646464646460212332E302E302E323

// String s = ""// private static void printSvcp(String hexString) {
// List<SVCPMessage> allSVCPMessages = null;
// try {
// allSVCPMessages =
// SVCPConversion.getAllSVCPMessages(hexStringToByteArray(hexString));
// //allSVCPMessages.forEach // "100101000010";
// "100181000393800101";
// "1001860013848001140D0202040D0200050D02FF030D02FF06";
// "1001860013848001010D0202040D0200050D02FF030D02FF06";
// "1001060010070D0200040D0200050D0200060D020003";
// 1001060004130D0204000D0205000D0206000D020300
// "1001860013848001010D0202040D02FF050D0200 // String s =
// "100181004DDD230107010C4646464646464646464646460212332"
// + "E302E302E323031363032303731373238030B3235352E3235352E32"
// + "35350416676C6F62616C5F73746167696E675F6970686F6E6536800101";
// String s = "1001060004130D020003";
// String s =
// "100181004DDD230107010C4646464646464646464646460212332E302E"
// + "302E323031363032303731373238030B3235352E3235352E323535041667"
// + "6C6F62616C5F73746167696E675F6970686F6E6536800101";
// String s =
// "100181004DDD230107010C4646464646464646464646460212332E302E302E323031363032303731373238030B3235352E3235352E3235350416676C6F62616C5F73746167696E675F6970686F6E6536800101";030D02FF06";(x->{
// // System.out.println("\n"+x.getHeader().getEopcode()+"\n====\n");
// //});
// allSVCPMessages.forEach(System.out::println);
// for (SVCPMessage message : allSVCPMessages)
// if
// (message.getHeader().getEopcode().equals(MessageTypeOpcodes.LOG_LINE)){
// String print = "";
// for(TLV tlv : message.getTlvs()){
//
// if(tlv.getType().equals(Tag.LOG_LEVEL))print+="["+tlv.getStringValue();
// if(tlv.getType().equals(Tag.LOG_LINE))print+="] "+tlv.getStringValue();
//
// }
// print.replace("\n", "");
// print.replace("\r", "");
// System.out.println(print);
// }//else
// //System.out.println(message);
// } catch (Exception e) {
// //1001810056C6230107010C4646464646464646464646460212332E302E302E323031363033313631333236030B3235352E3235352E3235350416676C6F62616C5F73746167696E675F6970686F6E65360E0100100408091010800101
//
//
// //1001810056C6230107010C4646464646464646464646460212332E302E302E323031363033313631333236030B3235352E3235352E3235350416676C6F62616C5F73746167696E675F6970686F6E65360E0100100408091010800101
//// // TODO Auto-generated catch block
// //e.printStackTrace();
// }
// }
// + "100181000696__230107800101"
// + "100181000696230107800101"
// + "100181000696230107800101"
// + "100181000696230107800101"
// + "100181000696230107800101"
// + "100181000696230107800101"
// + "100181000696230107800101"
// + "100181000393010100";
// + "100101000313000100";
// + "100181000696230107800101";//100181000393800101";
// + "100181000393800101";//"100181000393800101";
// Main.printSvcp((SVCPConversion.hexStringToByteArray(s)));
// List<TLV> tlvs =
// SVCPConversion.extractSvcpAsTlvObjs(SVCPConversion.hexStringToByteArray(s));
// for (TLV tlv : tlvs) {
// System.out.println(tlv);
// }// sc.close();// "100181000393800101";