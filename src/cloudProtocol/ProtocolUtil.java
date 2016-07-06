package cloudProtocol;

import global.TypeConversions;

import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author orshachar
 *	This class provides the necessary methods for Simgo's Cloud protocol
 *  Util, such as creating a UDP socket and sending reqeusts, or constructing
 *  the TLV's. 
 */
public class ProtocolUtil {
	
	// UDP Properties
	
	private static final int UDP_TIMEOUT = 6000; // ms
	private static final int DEFAULT_UDP_PORT = 5150;
	private static final int MIN_PAYLOAD_SIZE = 4;
	private static final int MAX_RERTRANSMISSION = 6;


	// Conventions
	
	static final byte [] PLUG_ID_PREFIX = {0x00,0x00};
	
	// Members
	
	private byte[] sessionId;
	private byte  sequenceNumber;
	private int m_udpPort = 5150;
	 InetAddress cloudUrl;
	
	
	
	public ProtocolUtil (String url){
		setCloudAddress(url);
	}
	
	
	/**
	 * @return a byte that contains an increasing index per session ID, starting from 0
	 */
	public byte getSequenceNumber() {
		sequenceNumber = (byte) ((sequenceNumber + 1) & 0xf);
		return sequenceNumber;
	}
	
	
	
	/**
	 * @param The opcode of the parameter
	 * @param value
	 * @return a byte array constructing the complete TLV messsage with length
	 */
	public byte[] generateTlv(final int type, final byte[] value) {
		
		byte[] tlv = new byte[] { (byte) (type & 0xff),
				(byte) (value.length & 0xff) };
		return TypeConversions.combineByteArrays(tlv, value);
	}
		
	byte[] executeUdpQuery(final byte[] dataToSend) {
		byte[] returnData = new byte[] {};


		
		// validate magic number ** Maybe deprecated if construct header
		
		if (dataToSend == null || dataToSend.length < MIN_PAYLOAD_SIZE
				|| ((dataToSend[0] & 0xf0) >> 4) != Enums.MAGIC_NUMBER) {
			System.out.println(
					 "executeDnsRequest Initiated with INVALID dataToSend");
			return returnData;
		}
		
		// Create a UDP Socket
		
		DatagramSocket clientSocket;
		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e1) {
			System.out.println("UDP Request FAILED - SocketException");
			return returnData;
		}

		// Set Socket connection properties
		try {
			clientSocket.setSoTimeout(UDP_TIMEOUT);

			byte[] receivedData = new byte[1024];

			DatagramPacket sendPacket = new DatagramPacket(dataToSend,
					dataToSend.length, cloudUrl, m_udpPort);

			int retryCounter = 0;
			boolean receiveOk = false;
			long startTime = System.currentTimeMillis();
			
		// fire a udp query request, re-transmit until receiving a response or max retrasmissions is reached

			while (retryCounter < MAX_RERTRANSMISSION && !receiveOk) {
				try {

					clientSocket.send(sendPacket);

					DatagramPacket receivedPacket = new DatagramPacket(
							receivedData, receivedData.length);
					clientSocket.receive(receivedPacket);

					if (receivedPacket.getData() != null
							&& receivedPacket.getData().length > 0 &&
							// check magic & sequence numbers
							receivedPacket.getData()[0] == dataToSend[0] &&
							// check message type opcode
							(((byte) (dataToSend[1] | 0x80)) == receivedPacket
									.getData()[1])) {
						receiveOk = true;
						returnData = receivedPacket.getData();

					} else {
						System.out.println("Received INVALID UDP Response: "
								+ TypeConversions
										.byteArrayToHexString(unPadByteArray(receivedPacket
														.getData())));
					}
				} catch (IOException e) {

					System.out
							.println("UDP Request FAILED - Cloud Connectivity Error");
					e.printStackTrace();
				}
				retryCounter++;
			}
		} catch (SocketException e1) {
			System.out.println("UDP Request FAILED - SocketException");
			e1.printStackTrace();
		}

		finally {

			clientSocket.close();
		}

		return returnData;
	}

    private static byte[] unPadByteArray(final byte[] initialByteArray)
    {
        if (initialByteArray == null || initialByteArray.length < 1)
        {
            System.out.println("unPadByteArray FAILED because Input ByteArray is INVALID");
            return (new byte[] {});
        }

        int index = initialByteArray.length - 1;

        try
        {
            //find last non zero value
            while (initialByteArray[index] == 0 && index > 0)
            {
                index--;
            }

            byte[] targetArray = new byte[index + 1];

            System.arraycopy(initialByteArray, 0, targetArray, 0, index + 1);

            return targetArray;

        }
        catch (IndexOutOfBoundsException e)
        {
            System.out.println("IndexOutOfBoundsException in unPadByteArray");
            e.printStackTrace();
            return (new byte[] {});
        }
    }
	byte [] plugAddPrefix (String plugid) {
	
		byte[] plugIdInBytes = TypeConversions.decimalNumberToByteArray(plugid);		
		byte [] plugIdFinal = TypeConversions.combineByteArrays(PLUG_ID_PREFIX, plugIdInBytes);
		return plugIdFinal;
		
	}
	
	// process single TLV element
	byte[] decodeTlvRecord(final int tlvSubType,
				final ByteBuffer msgByteBuffer) {
			byte[] decodedTlvValue = null;

			if (msgByteBuffer.remaining() > 0 && msgByteBuffer.get() == tlvSubType) // TLV
																					// sub
																					// type
			{
				int valueLength = msgByteBuffer.get();
				if (msgByteBuffer.remaining() >= valueLength) {
					decodedTlvValue = new byte[valueLength];
					msgByteBuffer.get(decodedTlvValue, 0, valueLength);
				} else {
					System.out.println("Unexpected TLV length");
				}
			} else {
				System.out.println("Unexpected TLV element");
				msgByteBuffer.position(msgByteBuffer.position() - 1);
			}

			return decodedTlvValue;

		}
	
    byte[] removeCommonHeader(final byte[] replyFromCloud) {
		if (replyFromCloud != null && replyFromCloud.length > 2) {
			byte[] payload = new byte[replyFromCloud.length - 2];
			System.arraycopy(replyFromCloud, 2, payload, 0, payload.length);
			return payload;
		} else {
			return null;
		}
	}
    
	private void setCloudAddress(final String url) {
		if (url != null) {
			String dstIpAddress;
			m_udpPort = DEFAULT_UDP_PORT;

			if (url == null || url.length() < 7) {
				System.out
						.println("MessageConstructor Initiated with INVALID Destination IP Address");
				return;
			}

			// check if Destination Address contains custom UDP port
			int portIndex = url.indexOf(":");
			if (portIndex != -1) {
				// parse custom UDP port
				dstIpAddress = url.substring(0, portIndex);

				if (portIndex + 1 < url.length()) // make sure ":" is not
														// the last character
				{
					try {
						m_udpPort = Integer.parseInt(url
								.substring(portIndex + 1));
					} catch (NumberFormatException e) {
						System.out.println("Illegal DNS Port");
						cloudUrl = null;
					} catch (IndexOutOfBoundsException e1) {
						System.out
								.println("MessageConstructor Failed to Parse UDP Port");
						cloudUrl = null;
					}
				}
			} else {
				// use default UDP port
				m_udpPort = DEFAULT_UDP_PORT;
				dstIpAddress = url;
			}

			try {
				cloudUrl = InetAddress.getByName(dstIpAddress);
			} catch (UnknownHostException e) {
				System.out
						.println("MessageConstructor Failed to Parse IP Address");
			}
		}
	}
    
	public Map<Byte, byte[]> decodeTlvRecords(final byte[] message) {
		Map<Byte, byte[]> tlvMap = new HashMap<Byte, byte[]>();

		if (message != null) {
			ByteBuffer msgByteBuffer = ByteBuffer.wrap(message);

			while (msgByteBuffer != null && msgByteBuffer.remaining() > 0) {
				Byte key = msgByteBuffer.get(); // TLV sub type
				int valueLength = (int) msgByteBuffer.get() & 0xff; // TLV
																	// length
				if (msgByteBuffer.remaining() >= valueLength && valueLength > 0) {
					byte[] decodedTlvValue = new byte[valueLength];
					msgByteBuffer.get(decodedTlvValue, 0, valueLength); // TLV
																		// value
					tlvMap.put(key, decodedTlvValue);
				} else {
					// System.out.println("Unexpected TLV length");
					break;
				}
			}
		}
		return tlvMap;
		
	}

}