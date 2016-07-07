package global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import beans.FilePacket;

/**
 * Utility to get info from Files
 * 
 * @author Yehuda Ginsburg
 *
 */
public class FileUtil {

	/**
	 * read from File and return it as a String
	 * 
	 * @param file
	 * @return file as string
	 */
	public static synchronized String readFromFile(File file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * using Apache org.apache.commons.io.FileUtils get list from file
	 * 
	 * @param file
	 * @return List of String
	 */
	public static List<String> listFromFile(File file) {
		boolean isFileExistAndHasDataInit = false;
		try {
			isFileExistAndHasDataInit = file != null && file.exists()
					&& !FileUtils.readFileToString(file).trim().isEmpty();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (isFileExistAndHasDataInit) {
			try {
				return FileUtils.readLines(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * write to file from Process (from windows CMD in most cases) in path given
	 * as a parameter
	 * 
	 * @param process
	 * @param pathWithDate
	 * @param timeInMillis
	 * @return a File
	 */
	public static File writeToFileFromProcess(Process process, String pathWithDate) {
		String line;
		File file = createFileAndFolderFromPath(pathWithDate);
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			while ((line = bufferedReader.readLine()) != null) {
				// if line is empty do not print new line
				String data = line.isEmpty() ? "" : line + "\n";
				FileUtils.writeStringToFile(file, data, true);
			}
			return file;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * get a path A. check if folders are exist if not create them. B.create the
	 * file if its not exist
	 * 
	 * @param pathAndTime
	 * @return
	 */
	public static File createFileAndFolderFromPath(String pathAndTime) {
		Path pathToFile = Paths.get(pathAndTime);
		try {
			Files.createDirectories(pathToFile.getParent());
			return Files.createFile(pathToFile).toFile();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Convert local sgo file into byte[]
	 * 
	 * @return byte[]
	 */
	public static byte[] fileToByteArray(File file) {
		try {
			// from File to byte[]
			return Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get a File and split it into byte array accordance to the split
	 * size.</br>
	 * Insert all splitting data into {@link beans.FilePacket} objects and them
	 * to a list
	 * 
	 * @param originalFile
	 * @param splitSize
	 * @return List of FilePacket
	 */
	public static List<FilePacket> splitFile(File originalFile, int splitSize, boolean isSGO) {
		byte[] src = fileToByteArray(originalFile);
		List<FilePacket> filePackets = new ArrayList<>();
		int index = 0;
		// if it's sgo file it should contains 16 bytes in the first packets
		if (isSGO) {
			byte[] sgoHeader = new byte[16];
			System.arraycopy(src, 0, sgoHeader, 0, sgoHeader.length);
			FilePacket sgoHeaderFP = new FilePacket(src.length, sgoHeader.length, 0, sgoHeader,
					originalFile.getAbsolutePath());
			filePackets.add(sgoHeaderFP);
			index = 16;
		}

		while (index < src.length) {
			if (src.length - index < splitSize)
				splitSize = src.length - index;
			byte[] dest = new byte[splitSize];
			System.arraycopy(src, index, dest, 0, splitSize);
			FilePacket ffp = new FilePacket(src.length, dest.length, index, dest, originalFile.getAbsolutePath());
			filePackets.add(ffp);
			index = index + splitSize;
		}

		return filePackets;
	}

	/**
	 * Check if String exist in file - return boolean and print error message if
	 * needed
	 * 
	 * @param file
	 * @param string
	 * @return boolean
	 */
	public static boolean isStringExistInFile(File file, String string) {
		List<String> listFromFile = listFromFile(file);
		for (String line : listFromFile) {
			if (line.toLowerCase().contains(string.toLowerCase())) {
				System.out.println("String " + string + " Found in Line: \n" + line);
				return true;
			}
		}
		System.err.println(string + " was not found in\n" + file.getAbsolutePath());
		return false;
	}
}