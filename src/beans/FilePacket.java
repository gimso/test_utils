package beans;

import java.util.Arrays;

/**
 * This is an utility class for managing file packets information.</br>
 * The information includes:
 * <li>long originalFileSize - The original file size.</li>
 * <li>long packetSize - The current packet size.</li>
 * <li>int packetBegin - The current packet index in the whole file data.</li>
 * <li>byte[] packet - The actual data of current packet.</li>
 * <li>String originalFilePath - The Absolute Path of the original file.</li>
 * @author Yehuda Ginsburg
 */
public class FilePacket {

	private long originalFileSize;
	private long packetSize;
	private int packetBegin;
	private byte[] packet;
	private String originalFilePath;

	public FilePacket(long originalFileSize, long packetSize, int packetBegin, byte[] packet, String originalFilePath) {
		super();
		this.originalFileSize = originalFileSize;
		this.packetSize = packetSize;
		this.packetBegin = packetBegin;
		this.packet = packet;
		this.originalFilePath = originalFilePath;
	}

	public long getOriginalFileSize() {
		return originalFileSize;
	}

	public void setOriginalFileSize(long originalFileSize) {
		this.originalFileSize = originalFileSize;
	}

	public long getPacketSize() {
		return packetSize;
	}

	public void setPacketSize(long packetSize) {
		this.packetSize = packetSize;
	}

	public int getPacketBegin() {
		return packetBegin;
	}

	public void setPacketBegin(int packetBegin) {
		this.packetBegin = packetBegin;
	}

	public byte[] getPacket() {
		return packet;
	}

	public void setPacket(byte[] packet) {
		this.packet = packet;
	}

	public String getOriginalFilePath() {
		return originalFilePath;
	}

	public void setOriginalFilePath(String originalFilePath) {
		this.originalFilePath = originalFilePath;
	}

	@Override
	public String toString() {
		return "FilePacket [originalFileSize=" + originalFileSize + ", packetSize=" + packetSize + ", packetBegin="
				+ packetBegin + ", packet=" + Arrays.toString(packet) + ", originalFilePath=" + originalFilePath + "]";
	}

}