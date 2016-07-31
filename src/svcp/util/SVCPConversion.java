package svcp.util;

import static global.Conversions.byteArrayToHexString;
import static global.Conversions.byteArraysToInt;
import static global.Conversions.combineByteArrays;
import static global.Conversions.hexStringToByteArray;
import static global.Conversions.intToByte;
import static global.Conversions.stringASCIIFromByteArray;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.Assert;

import adb.AdbUtil;
import global.Conversions;
import global.FileUtil;
import global.PropertiesUtil;
import logging.LogcatLogger;
import logging.PlugLogger;
import svcp.beans.Header;
import svcp.beans.SVCPMessage;
import svcp.beans.TLV;
import svcp.enums.ApplyUpdate;
import svcp.enums.FileType;
import svcp.enums.LogLevel;
import svcp.enums.METype;
import svcp.enums.MessageTypeOpcodes;
import svcp.enums.Mode;
import svcp.enums.PowerSupplyFromMe;
import svcp.enums.ResultTags;
import svcp.enums.SetFiles;
import svcp.enums.SimGeneration;
import svcp.enums.Tag;
import svcp.enums.UICCRelay;
import svcp.enums.VsimType;

/**
 * Utility for converting
 * 
 * @author Yehuda
 *
 */
public class SVCPConversion {

	private static final String SENT_MESSAGE = "Sent data: ";
	private static final String RECEIVED_MESSAGE = "Received message: ";
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

		// Start from the first point after Header-size (6)
		// check if index + 1 (type = length) exists in svcp
		// Increment in the and of the loop
		for (int i = Header.HEADER_SIZE; i + 1 < svcp.length;) {
			boolean isJumbo = svcp[i + 1] == 0xff || svcp[i + 1] < 0;
			
			byte[] type = isJumbo 
					? new byte[] { svcp[i], (byte) 0xff } 
					: new byte[] { svcp[i] };
			byte[] length = isJumbo 
					? Conversions.bytesToByteArray(svcp[i + 2], svcp[i + 3])
					: new byte[] { svcp[i + 1] };
			// copy value to separate byte array
			byte[] value = new byte[byteArraysToInt(length)];
			// src = svcp
			int srcPos = isJumbo ? i + 4 : i + 2;
			// dest = value ;
			int destPos = 0;
			// length of the value byte array = length;
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
		
		if (value.length < 1)
			return "EMPTY_VALUE";
		
		switch (tag) {
			// ASCII TLV's
			case FW_VERSION:
			case HW_VERSION:
			case CONFIGURATION_NAME:
			case LOG_LINE:
			case VSIM_ID:
				return stringASCIIFromByteArray(value);
			// Binary TLV's
			case PACKET_PAYLOAD:
				return "(" + value.length + " bytes)";
			// Numbers (Hex value)
			case FILE_DATA:
			case AUTHENTICATION_DATA:
			case PHONE_NUMBER:
			case IMSI:
			case UPDATE_SIZE:
			case PACKET_BEGIN:
			case PACKET_SIZE:
			case APPLY_NOW:
			case NAA_INIT:
			case UICC_RESET:
			case BOYCOTT:
			case GET_ALL:
				return byteArrayToHexString(value);
			// default path / id on the vfs
			case FILE_PATH:
				if(value.equals(SetFiles.USIM_3G_PATH))
					return SetFiles.USIM_3G_PATH.name();
				else if(value.equals(SetFiles.SIM_2G_PATH))
					return SetFiles.SIM_2G_PATH.name();
				else
					return byteArrayToHexString(value);
			case FILE_ID:
				if(value.equals(SetFiles.IMSI_FILE_ID))
					return SetFiles.IMSI_FILE_ID.name();
				else
					return byteArrayToHexString(value);				
			// ME type
			case ME_TYPE:
				return METype.getMEType(byteArraysToInt(value)).name();
			// Results
			case RESULT_TAG:
				return ResultTags.getResultTag(value[0]).name();
			// Mode
			case MODE:
				return Mode.getMode(byteArraysToInt(value)).name();
			// Apply Update
			case APPLY_UPDATE:
				return ApplyUpdate.getApplyUpdate(byteArraysToInt(value)).name();
			// UICC Source and Destination
			case UICC_RELAY:
				if(value.length==1)
					return  " UICC relay value[]=" + UICCRelay.getUICCRelay(value[0]);
				else if (value[0] < 0 && value[1] > -1)
					return  " UICC relay value [0] < 0,  value[1]=" + UICCRelay.getUICCRelay(value[1]);
				else if (value[1] < 0 && value[0] > -1)
					return  " UICC relay value [1] < 0,  value[0]=" + UICCRelay.getUICCRelay(value[0]);
				else if (value[0] < 0 && value[1] < 0)
					return  " UICC relay value[0] and  value[1] is < 0";
				else	{
				UICCRelay uiccRelay1 = UICCRelay.getUICCRelay(value[0]);
				UICCRelay uiccRelay2 = UICCRelay.getUICCRelay(value[1]);
				String src =  uiccRelay1 != null 
						? 	uiccRelay1.name()
						:	String.format("%X",value[0]);
				String dest = uiccRelay2 != null
						?	uiccRelay2.name()
						:	String.format("%X",value[1]);
				return "Source: " +src+"\t,Destination: " +dest;
				}
			// File Type
			case FILE_TYPE:
				return FileType.getFileType(byteArraysToInt(value)).name();
			// Log Level TLV
			case LOG_LEVEL:
				return LogLevel.getLogLevel(byteArraysToInt(value)).name();
			// vSim type
			case VSIM_TYPE:
				return VsimType.getVsim(byteArraysToInt(value)).name();	
			// SIM generation 3g/4g
			case SIM_GENERATION:
				SimGeneration simGeneration = SimGeneration.getSimGeneration(byteArraysToInt(value));
				return simGeneration != null ? simGeneration.name() : String.format("%X", simGeneration);
			// Is power supply from me on/off
			case POWER_SUPPLY_FROM_ME:
				return PowerSupplyFromMe.getPowerSupplyMode(byteArraysToInt(value)).name();
			// Is power supply from me on/off
			case CLOUD_CONNECTION:
				return PowerSupplyFromMe.getPowerSupplyMode(byteArraysToInt(value)).name();
			default:
				break;
		}
		return new String(value);
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
		String cmd = "adb shell am broadcast -a simgo.vsim.TUNNELING -e \"tunnel_outgoing\" \"" + request + "\"";
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
	 * Create request from vSIM-ATMEL to AP
	 * @param svcp request 
	 */
	public static void sendSVCPIncomingRequest(byte[] request) {
		// convert the byte array to hex string
		String reqStr = byteArrayToHexString(request);
		String cmd = "adb shell am broadcast -a simgo.vsim.TUNNELING -e \"tunnel_incoming\" \""+reqStr+"\"";
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
	 * Extract the first response from logcat saved file
	 * 
	 * @param logcatLogger
	 *            object instance
	 * @return response byte array
	 */
	public static SVCPMessage extractResponseFromLogcatLogger(LogcatLogger logcatLogger) {
		// extractFirstResponseFromLogcatLogger
		List<String> strings = FileUtil.listFromFile(logcatLogger.getLogCatFile());
		if (strings != null && !strings.isEmpty())
			for (String line : strings)
				if (line.contains(RECEIVED_MESSAGE)) {
					String[] tempMessageLine = line.split(RECEIVED_MESSAGE);
					if (tempMessageLine.length > 1)
						return new SVCPMessage(Conversions.hexStringToByteArray(tempMessageLine[1]));
				}
		System.err.println("Cannot find any response in " + logcatLogger.getLogCatFile().getAbsoluteFile());
		return null;
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
	public static SVCPMessage extractResponseFromLogcatLogger(LogcatLogger logcatLogger, SVCPMessage requestObj, MessageTypeOpcodes opcode) {//extractResponseFromLogcatLoggerByRequest
		String request = Conversions.byteArrayToHexString(requestObj.getSvcp());
		return extractResponseFromLogcatLogger(logcatLogger, request, opcode);
	}
	
	/**
	 * Extract response from logcat by Opcode
	 * @param logcatLogger
	 * @param request
	 * @param opcode
	 * @return SVCPMessage as response
	 */
	public static SVCPMessage extractResponseFromLogcatLogger(LogcatLogger logcatLogger, String request,
			MessageTypeOpcodes opcode) {
		// extractResponseFromLogcatByOpcode
		List<String> strings = FileUtil.listFromFile(logcatLogger.getLogCatFile());
		boolean checkRx = false;
		
		if (strings != null && !strings.isEmpty()) {
			for (String line : strings) {
				if (!checkRx && line.contains(SENT_MESSAGE)) {
					checkRx = isLineContainsRequest(request, line);
				} else if (checkRx && line.contains(RECEIVED_MESSAGE)) {
					String[] tempMessageLine = line.split(RECEIVED_MESSAGE);
					if (tempMessageLine.length > 1) {
						String hexString = tempMessageLine[1];
						byte[] hexStringToByteArray = Conversions.hexStringToByteArray(hexString);
						SVCPMessage temp = new SVCPMessage(hexStringToByteArray);
						if (temp.getHeader().getEopcode().equals(opcode))
							return temp;
					}
				}
			}
		}
		System.err.println("Cannot find any response in " + logcatLogger.getLogCatFile().getAbsoluteFile());
		return null;

	}
	
	/**
	 * Extract response from logcat by request message
	 * @param logcatLogger
	 * @param requestObj
	 * @return SVCPMessage
	 */
	public static SVCPMessage extractResponseFromLogcatLogger(LogcatLogger logcatLogger, SVCPMessage requestObj) {
		//	Get logcat logger output file as list of string
		List<String> strings = FileUtil.listFromFile(logcatLogger.getLogCatFile());
				
		String requestHex = Conversions.byteArrayToHexString(requestObj.getSvcp());
		String svcpVersion = requestHex.substring(0, 2);
		String msgId = requestHex.substring(2,4);
		
		Integer opcode = Integer.parseInt(requestHex.substring(4, 6), 16) | 0x80;
		String hexString = Integer.toHexString(opcode);
		String returnHex = hexString.length() == 2 ? hexString : "0" + hexString;
		String headerIdAndOpcode = svcpVersion + msgId + returnHex;

		boolean checkRx = false;		
		
		if (strings != null && !strings.isEmpty()) {
			for (String line : strings) {
				if (!checkRx && line.contains(SENT_MESSAGE))
					checkRx = isLineContainsRequest(requestHex, line);
				else if (checkRx && line.toLowerCase().contains(headerIdAndOpcode.toLowerCase())) {
					return getSvcpById(RECEIVED_MESSAGE, msgId, line);
				}
			}
		}
		System.err.println("Cannot find response with " + headerIdAndOpcode);
		return null;
	}	
	
	/**
	 * Extract the responses from logcat log file
	 * 
	 * @return a list of svcp byte array
	 */
	public static List<SVCPMessage> extractResponseFromLogcatLoggerReturnList(LogcatLogger logcatLogger) {
		List<SVCPMessage> responses = new ArrayList<>();
		List<String> strings = FileUtil.listFromFile(logcatLogger.getLogCatFile());
		
		if (!strings.isEmpty())
			for (String line : strings)
				if (line.contains(RECEIVED_MESSAGE))
					if (line.split(RECEIVED_MESSAGE).length > 1)
						responses.add(new SVCPMessage(hexStringToByteArray(line.split(RECEIVED_MESSAGE)[1])));
		return responses;
	}
	
	/**
	 * Compare the id from tx and tx and return the tx (response from Atmel) as SVCPMessage
	 * @param plugLogger
	 * @param idAsHexString
	 * @return SVCPMessage
	 */
	public static SVCPMessage extractResponseFromPlugLogger(PlugLogger plugLogger, String idAsHex) {
		
		String rx_ = "Rx: ";
		String tx_ = "Tx: ";
		
		List<String> strings = FileUtil.listFromFile(plugLogger.getLogFile());
		boolean checkRx = false;
		
		if (strings != null && !strings.isEmpty()) {
			for (String line : strings) {
				if (!checkRx && line.contains(rx_)) {
					String rx = line.split(rx_)[1];/* split by spaces */
					if (rx.substring(2, 4).equals(idAsHex))
						checkRx = true;
				} else if (checkRx && line.contains(tx_)) {
					String tx = line.split(tx_)[1];
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
		String rx_ = "Rx: ";
		String tx_ = "Tx: ";
		List<String> strings = FileUtil.listFromFile(plugLogger.getLogFile());

		if (strings != null && !strings.isEmpty()) {
			for (String line : strings) {
				if (line.contains(rx_)) {
					String rx = line.split(rx_)[1];/* split by spaces */
					if (rx.substring(2, 4).equals(idAsHex))
						return new SVCPMessage(Conversions.hexStringToByteArray(rx));
				} else if (line.contains(tx_)) {
					String tx = line.split(tx_)[1];
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
		String rx_ = "Rx: ";
		String tx_ = "Tx: ";
		List<String> strings = FileUtil.listFromFile(plugLogger.getLogFile());

		if (strings != null && !strings.isEmpty())
			for (String line : strings)
				if (line.contains(rx_)) {
					String rx = line.split(rx_)[1];/* split by spaces */
					if (rx.substring(4, 6).equals(opcodeAsHex))
						return new SVCPMessage(Conversions.hexStringToByteArray(rx));
				} else if (line.contains(tx_)) {
					String tx = line.split(tx_)[1];
					if (tx.substring(4, 6).equals(opcodeAsHex))
						return new SVCPMessage(Conversions.hexStringToByteArray(tx));
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
	
	/**
	 * Check if line contains the Request
	 * @param request
	 * @param checkRx
	 * @param line
	 * @return boolean
	 */
	private static boolean isLineContainsRequest(String request, String line) {
		String sent = line.split(SENT_MESSAGE)[1];/* split by spaces */
		if (sent.toLowerCase().contains(request.toLowerCase()))
			return true;
		return false;
	}

	/**
	 * Get response msg by ID
	 * 
	 * @param receivedMessage
	 * @param msgId
	 * @param line
	 * @return SVCPMessage
	 */
	private static SVCPMessage getSvcpById(String receivedMessage, String msgId, String line) {
		byte[] hexStringToByteArray = Conversions.hexStringToByteArray(line.split(receivedMessage)[1]);
		
		Header tempHeader = new Header(hexStringToByteArray);
		int firstLength = Header.HEADER_SIZE + tempHeader.getLength();
		int length = hexStringToByteArray.length - firstLength;
		
		byte[] tempFirst = new byte[firstLength];
		System.arraycopy(hexStringToByteArray, 0, tempFirst, 0, firstLength);
		
		SVCPMessage tempA = new SVCPMessage(tempFirst);
		boolean byteArrayContainsTwoMsgs = firstLength < hexStringToByteArray.length;

		if (byteArrayContainsTwoMsgs) {
			byte[] tempSec = new byte[length];
			System.arraycopy(hexStringToByteArray, firstLength, tempSec, 0, length);

			SVCPMessage tempB = new SVCPMessage(tempSec);

			if (tempA.getHeader().getHexId().equalsIgnoreCase(msgId))
				return tempA;
			else
				return tempB;
		} else
			return tempA;
	}
}