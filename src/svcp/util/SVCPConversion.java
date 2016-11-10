package svcp.util;

import static global.Conversions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.Assert;

import adb.AdbUtil;
import global.Conversions;
import global.FileUtil;
import global.PropertiesUtil;
import logging.LogcatLogger;
import logging.PlugLogger;
import regex.*;
import svcp.beans.*;
import svcp.enums.*;

/**
 * Utility for converting
 * 
 * @author Yehuda
 *
 */
public class SVCPConversion {

	private static final int LENGTH_INDEX = 1;
	private static final int JUMBO = 0xff;
	private static final int JUMBO_LENGTH_INDEX_2 = 3;
	private static final int JUMBO_LENGTH_INDEX_1 = 2;
	private static final int VALUE_BEGIN_INDEX = 2;
	private static final int JUMBO_VALUE_BEGIN_INDEX = 4;
	private static final String TUNNEL_INCOMING = "adb shell am broadcast -a simgo.vsim.TUNNELING -e \"tunnel_incoming\"";
	private static final String TUNNEL_OUTGOING = "adb shell am broadcast -a simgo.vsim.TUNNELING -e \"tunnel_outgoing\"";
	private static final String TX = "Tx: ";
	private static final String RX = "Rx: ";
	private static final int MAX_HEADER_LENGTH = Integer.decode(PropertiesUtil.getInstance().getProperty("MAX_HEADER_LENGTH"));

	/**
	 * Extract the tlv's from a svcp byte array, Covert it to separate byte[]
	 * for each tlv Insert each tlv into List
	 * 
	 * @param svcp
	 * @return List of tlv byte arrays
	 */
	public static List<byte[]> extractTlvsFromSvcp(byte[] svcp) {
		List<byte[]> tlvsList = new ArrayList<>();
		
		if (svcp.length <= Header.HEADER_SIZE)
			System.err.println("svcp byte array is to small - and not includes tlv's");
		
		//Extract msg length (eliminate the need to take care of array index out of bounds exceptions...)
		byte[] lengthBA = Arrays.copyOfRange(svcp, 3, 5);
		int msgLength = Conversions.hexStringToDecimalInt(Conversions.byteArrayToHexString(lengthBA)) +  Header.HEADER_SIZE;

		// Start from the first point after Header-size (6)
		// check if index + 1 (type = length) exists in svcp
		// Increment in the and of the loop
		for (int i = Header.HEADER_SIZE; i + 1 < msgLength;) {
			//check if the length of TLV is Jumbo (2 bytes length instead of one byte)
			boolean isJumbo = svcp[i + 1] == JUMBO || svcp[i + 1] < 0;
			
			// JUMBO type included also the byte FF that it part of the header.
			byte[] type = isJumbo 
					? new byte[] { svcp[i], (byte) 0xff } 
					: new byte[] { svcp[i] };
			
			//get the length
			byte[] length = isJumbo 
					? Conversions.bytesToByteArray(svcp[i + JUMBO_LENGTH_INDEX_1], svcp[i + JUMBO_LENGTH_INDEX_2])
					: new byte[] { svcp[i + LENGTH_INDEX] };
			
			// copy value to separate byte array
			byte[] value = new byte[byteArraysToInt(length)];
			
			// src = svcp
			int srcPos = isJumbo ? i + JUMBO_VALUE_BEGIN_INDEX : i + VALUE_BEGIN_INDEX;
			
			// dest = value ;
			int destPos = 0;

			// if the remaining of the svcp (from srcPos until value length) is not big enough error will print
			if(svcp.length - srcPos < value.length) {
				System.err.println("The value length (" + value.length + ") is bigger then the svcp length (" + (svcp.length - srcPos) + ")");
				break;
			}
			
			//copy the value from the whole SVCP to TLV byte array
			System.arraycopy(svcp, srcPos, value, destPos, value.length);
			
			// create the tlv
			byte[] tlv = combineByteArrays(type, length, value);
			
			// add tlv to the list
			tlvsList.add(tlv);
			
			// moving the index to the next tlv
			i = srcPos + value.length;
		}
		return tlvsList;
	}

	/**
	 * extract the value from tlv array
	 * 
	 * @param tlv
	 */
	public static byte[] getValueFromTlvByteArray(byte[] tlv) {
		// get length and init the array
		int lengthFromTlvByteArray = getLengthFromTlvByteArray(tlv);
		
		int length = (lengthFromTlvByteArray < 1) ? lengthFromTlvByteArray & 0xff : lengthFromTlvByteArray;
		
		boolean isJumbo = length > 255;
		
		byte[] value = new byte[length];
		// copy value by length
		int srcPos = isJumbo ? 4 : 2;
		int destPos = 0;
		// length of the value byte array = length;
		System.arraycopy(tlv, srcPos, value, destPos, length);
		return value;
	}

	/**
	 * Get length of value, check if its jumbo tlv or regular and return the
	 * length
	 * 
	 * @param tlv
	 * @return
	 */
	public static int getLengthFromTlvByteArray(byte[] tlv) {
		if (tlv[1] == (byte) 0xff)
			return Conversions.byteArraysToInt(new byte[] { tlv[2], tlv[3] });
		else
			return tlv[1];
	}

	/**
	 * Convert from svcp byte array to List of TLV objects (ignore header)
	 * 
	 * @param tlvs
	 * @return List TLV's
	 */
	public static List<TLV> extractSvcpAsTlvObjs(byte[] svcp) {
		List<TLV> tlvsObjects = new ArrayList<>();
		List<byte[]> extractTlvsFromSvcp = extractTlvsFromSvcp(svcp);
		
		for (byte[] tlv : extractTlvsFromSvcp) 
			tlvsObjects.add(new TLV(tlv));
		
		return tlvsObjects;
	}

	/**
	 * Get tlv byte array, extract the value as value byte array, Convert it
	 * into string according to Tag (ascii values, numbers etc)
	 * 
	 * @param tlv
	 * @return String value
	 */
	public static String getValueAsStringFromTagAndValue(Tag tag, byte[] value) {
		return SVCPToString.TagToString(tag,value);
		
	}

	/**
	 * Calculates the header CRC
	 * 
	 * @author Tal
	 * @param input
	 *            The header size
	 * @return the CRC of the header
	 * @throws IllegalArgumentException
	 *             if the input is null or in wrong length
	 */
	public static byte calculateCRC(final byte[] input) throws IllegalArgumentException {
		if (input == null)
			throw new IllegalArgumentException();

		int length = Header.HEADER_SIZE;
		if (length > input.length)
			throw new IllegalArgumentException();

		int crc = 0;

		for (int i = 0; i < length; i++)
			crc = (int) input[i] ^ crc;

		return (byte) crc;
	}
	
	/**
	 * Gets the payload length in byte array
	 * 
	 * @author Tal
	 * @param value
	 *            The payload length
	 * @return Byte array of the length
	 */
	public static byte[] convertPayloadLengthToByteArray(int value) {
		if (value > MAX_HEADER_LENGTH) {
			throw new IllegalArgumentException();
		}
		return new byte[] { intToByte(value >> 8), intToByte(value & 0xff) };
	}

	/**
	 * Send SVCP request From AP toward the ATMEL
	 * 
	 * @param request byte[]
	 */
	public static void sendSVCPOutgoingRequest(byte[] request) {
		// convert the byte array to hex string
		String reqStr = byteArrayToHexString(request);
		sendSVCPOutgoingRequest(reqStr);
	}

	/**
	 * Send SVCP request From AP toward the ATMEL
	 * 
	 * @param request SVCPMessage
	 */
	public static void sendSVCPOutgoingRequest(SVCPMessage svcpMessage) {
		sendSVCPOutgoingRequest(svcpMessage.getSvcp());
	}

	/**
	 * Send SVCP request From AP toward the ATMEL
	 * 
	 * @param request String
	 */
	public static void sendSVCPOutgoingRequest(String request) {
		// convert the byte array to hex string
		String cmd = TUNNEL_OUTGOING + " \"" + request + "\"";
		sendTunnelMsg(cmd);
	}
	
	/**
	 * Create request from vSIM-ATMEL to AP
	 * @param svcp request 
	 */
	public static void sendSVCPIncomingRequest(byte[] request) {
		// convert the byte array to hex string
		String reqStr = byteArrayToHexString(request);
		String cmd = TUNNEL_INCOMING + " \"" + reqStr + "\"";
		sendTunnelMsg(cmd);
		
	}

	/**
	 * @param cmd command
	 */
	public static void sendTunnelMsg(String cmd) {
		System.out.println("Request command: " + cmd);

		try {
			AdbUtil.executeCommandLine(cmd).waitFor();
			// need to wait one second, in order to received something on the
			// log Thread
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}


	/**
	 * Extract the response by opcode (|&) id from logcat saved file
	 * @param strings
	 * @param id
	 * @param opcode
	 * @param optionalTlv
	 * @return response 
	 */
	public static SVCPMessage getSvcpMsg(List<String> strings, String id, String opcode, String optionalTlv) {
		Pattern pattern = VphSVCPPatterns.getSvcpPattern(id, opcode, optionalTlv);
		for (String s : strings) {
  			Matcher matcher = pattern.matcher(s);
  			if (matcher.find()) {		  
  				
					// printSvcp(sc.next());
  				List<SVCPMessage> svcpMessages = printSvcps(matcher.group(VphSVCPPatterns.SVCP_GROUP));
  				for(SVCPMessage msg : svcpMessages)
  					if(Integer.valueOf(msg.getHeader().getId()).equals(Integer.valueOf(Conversions.hexStringToDecimalInt(id))))
  						return msg;
//  				SVCPMessage svcpMessage = new SVCPMessage(matcher.group(name));
//  				System.out.println("SVCP found\n" + svcpMessage);	
//				return svcpMessage;		
  			}		  
  		}		  		
		return null;
	}
	
	public static List<SVCPMessage> printSvcps(String svcps) {
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
				//System.err.println("Wrong SVCP structure, length is too long\n"+svcps.substring(beginIndex,totalLength));
				break;
			}
			String singelSvcp = svcps.substring(beginIndex, beginIndex + packetLength);
			svcpMessages.add(new SVCPMessage(singelSvcp));

			beginIndex += packetLength;
			beginLengthIndex = beginIndex + 6;
			endLengthIndex = beginLengthIndex + 4;
			remainingLength -= packetLength;
		}
		//svcpMessages.forEach(System.out::println);
		return svcpMessages;
	}
	
	/**
	 * Extract the response by opcode (|&) id from logcat saved file
	 * @param strings
	 * @param id
	 * @param opcode
	 * @return response 
	 */
	public static SVCPMessage getSvcpMsg(List<String> strings, String id, String opcode) {
		return getSvcpMsg(strings, id, opcode, "");
	}
	
	/**
	 * Extract the response from logcat file, Check that the response
	 * extract after recognize the "sent data"
	 * 
	 * @param logcatLogger
	 *            object instance
	 * @param SVCPMessage
	 * @return SVCPMessage as response
	 */
	public static SVCPMessage extractResponseFromLogcatLogger(LogcatLogger logcatLogger, SVCPMessage requestObj, MessageTypeOpcodes opcode) {
		return getSvcpMsg(FileUtil.listFromFile(logcatLogger.getLogCatFile()), requestObj.getHeader().getHexId(), opcode.getHexResponseValue());
	}
	
	public static SVCPMessage extractResponseFromLogcatLogger(LogcatLogger logcatLogger, MessageTypeOpcodes opcode) {
		return getSvcpMsg(FileUtil.listFromFile(logcatLogger.getLogCatFile()), null, opcode.getHexResponseValue());
	}
	
	/**
	 * Extract response from logcat by Opcode
	 * @param logcatLogger
	 * @param request
	 * @param opcode 
	 * @return SVCPMessage as response
	 */
	public static SVCPMessage extractResponseFromLogcatLogger(LogcatLogger logcatLogger, String request, MessageTypeOpcodes opcode) {
		String msgId = request!=null ? request.substring(2,4) : null;
		return getSvcpMsg(FileUtil.listFromFile(logcatLogger.getLogCatFile()), msgId, opcode.getHexResponseValue());
	}
	
	/**
	 * Extract response from logcat by request message
	 * @param logcatLogger
	 * @param requestObj
	 * @return SVCPMessage
	 */
	public static SVCPMessage extractResponseFromLogcatLogger(LogcatLogger logcatLogger, SVCPMessage requestObj) {
		String msgId = null;
		String opcode = null;
		if (requestObj.getHeader() != null) {
			if (requestObj.getHeader().getHexId() != null) {
				msgId = requestObj.getHeader().getHexId();
			}
			if (requestObj.getHeader().getEopcode() != null
					&& requestObj.getHeader().getEopcode().getHexValue() != null)
				opcode = requestObj.getHeader().getEopcode().getHexValue();
		}
		return getSvcpMsg(FileUtil.listFromFile(logcatLogger.getLogCatFile()), msgId, opcode);
	}
		
	/**
	 * Compare the id from tx and tx and return the tx (response from Atmel) as SVCPMessage
	 * @param plugLogger
	 * @param idAsHexString
	 * @return SVCPMessage
	 */
	public static SVCPMessage extractResponseFromPlugLogger(PlugLogger plugLogger, String idAsHex) {
		
		List<String> strings = FileUtil.listFromFile(plugLogger.getLogFile());
		boolean checkRx = false;
		
		if (strings != null && !strings.isEmpty()) {
			for (String line : strings) {
				if (!checkRx && line.contains(RX)) {
					String rx = line.split(RX)[1];/* split by spaces */
					if (rx.substring(2, 4).equals(idAsHex))
						checkRx = true;
				} else if (checkRx && line.contains(TX)) {
					String tx = line.split(TX)[1];
					if (tx.substring(2, 4).equals(idAsHex)) 
						return new SVCPMessage(Conversions.hexStringToByteArray(tx));
					
				}

			}
		}
		System.err.println("Cannot find response with id " + idAsHex);
		return null;
	}
	
	/**
	 * Compare the id from tx and tx and return the tx (response from Atmel) as SVCPMessage
	 * @param plugLogger
	 * @param idAsHexString
	 * @return SVCPMessage
	 */
	public static SVCPMessage extractMsgFromPlugLoggerById(PlugLogger plugLogger, String idAsHex) {
		
		List<String> strings = FileUtil.listFromFile(plugLogger.getLogFile());

		if (strings != null && !strings.isEmpty()) {
			for (String line : strings) {
				if (line.contains(RX)) {
					String rx = line.split(RX)[1];/* split by spaces */
					if (rx.substring(2, 4).equals(idAsHex))
						return new SVCPMessage(Conversions.hexStringToByteArray(rx));
				} else if (line.contains(TX)) {
					String tx = line.split(TX)[1];
					if (tx.substring(2, 4).equals(idAsHex))
						return new SVCPMessage(Conversions.hexStringToByteArray(tx));
				}
			}
		}
		System.err.println("Cannot find response with id " + idAsHex);
		return null;
	}
	
	/**
	 * Extract msg by first opcode found
	 * @param plugLogger
	 * @param meControl
	 * @return
	 */
	public static SVCPMessage extractMsgFromPlugLoggerByOpcode(PlugLogger plugLogger, MessageTypeOpcodes opcode) {
		String opcodeAsHex = Conversions.byteArrayToHexString(Conversions.intToBytes(opcode.getValue()));
		
		List<String> strings = FileUtil.listFromFile(plugLogger.getLogFile());

		if (strings != null && !strings.isEmpty()) {
			for (String line : strings) {
				if (line.contains(RX)) {
					String rx = line.split(RX)[1];/* split by spaces */
					if (rx.substring(4, 6).equals(opcodeAsHex))
						return new SVCPMessage(Conversions.hexStringToByteArray(rx));
				} else if (line.contains(TX)) {
					String tx = line.split(TX)[1];
					if (tx.substring(4, 6).equals(opcodeAsHex))
						return new SVCPMessage(Conversions.hexStringToByteArray(tx));
				}
			}
		}
		System.err.println("Cannot find response with opcode " + opcode);
		return null;
	}
	
	/**
	 * get byte array of few svcp's extract each one to SVCP object that include
	 * Header and List of TLV's
	 * 
	 * @param svcps
	 *            byte array
	 * @return list of svcp's
	 */
	public static List<SVCPMessage> getAllSVCPMessages(byte[] svcps) {
		List<SVCPMessage> SVCPlist = new ArrayList<>();

		// started from 0 index, updated in each iteration
		int srcPos = 0;

		// always 0 - copy from 0 index in the temp svcp byte[]
		int destPos = 0;

		// while svcp's length is still bugger than the srcPos to copy - extract
		while (srcPos < svcps.length) {

			// get the payload (tlv's) length from the header 3 and 4 indexes
			byte[] tlvsSize = new byte[] { svcps[srcPos + Header.HEADER_POSITION_LENGTH_0],
					svcps[srcPos + Header.HEADER_POSITION_LENGTH_1] };

			// the length of the svcp includes from header and payload's
			// together
			int length = Header.HEADER_SIZE + byteArraysToInt(tlvsSize);

			// copy to temporary byte array the svcp -
			// svcps = the source array, srcPos = the source index to copy from
			// -
			// tempSVCP = the new array to copy into it, length = how many
			// indexes to copy from the srcPos
			byte[] tempSVCP = new byte[length];
			System.arraycopy(svcps, srcPos, tempSVCP, destPos, length);

			// extract header and tlvs objects and add it to the list
			Header header = new Header(svcps);
			List<TLV> tlvs = SVCPConversion.extractSvcpAsTlvObjs(tempSVCP);
			SVCPlist.add(new SVCPMessage(header, tlvs));

			// updated the index of the current position to the next svcp
			srcPos += length;
		}
		return SVCPlist;
	}

	/**
	 * Extract the result from TLV result Object, and Assert if the is Result OK
	 * 
	 * @param result
	 */
	public static void resultSuccessAssertion(SVCPMessage svcpMessage) {
		resultAssertion(svcpMessage, ResultTags.OPERATION_WAS_SUCCESSFUL_OPCODE_ACCEPTED);
	}
	
	/**
	 * Extract the result from TLV result Object and assert by expected result tag
	 * @param svcpMessage
	 * @param resultTag
	 */
	public static void resultAssertion(SVCPMessage svcpMessage, ResultTags resultTag) {	
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
	
	/**
	 * Get TLV from svcp by Tag enum 
	 * @param tag 
	 * @param svcpMessage
	 * @return TLV
	 */
	public static TLV getTlvByTag(Tag tag, SVCPMessage svcpMessage) {
		return svcpMessage.getTlvs().stream().filter(tlv -> tlv.getType().equals(tag)).findFirst().orElse(null);
	}

	/**
	 * Send SVCP outgoing request with no delay
	 * @param SVCPMessage...
	 */
	public static void sendRequsetWithNoDelay(SVCPMessage... msgObj) {
		String request = "";
		for (SVCPMessage s : msgObj)
			request += Conversions.byteArrayToHexString(s.getSvcp());
		String cmd = "adb shell am broadcast -a simgo.vsim.TUNNELING -e \"tunnel_outgoing\" \"" + request + "\"";
		System.out.println("Request command: " + cmd);
		AdbUtil.executeCommandLine(cmd);
	}

}
