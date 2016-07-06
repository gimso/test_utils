package global;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FTPUtil {

	private FTPClient ftpClient;
	public static final String FTP_INNER_FOLDER = 
			PropertiesUtil.getInstance().getProperty("FTP_INNER_FOLDER");
	public static final String FTP_SERVER = 
			PropertiesUtil.getInstance().getProperty("FTP_SERVER");
	public static final int FTP_PORT = Integer.parseInt(
			PropertiesUtil.getInstance().getProperty("FTP_PORT"));
	public static final String FTP_PASSWORD = 
			PropertiesUtil.getInstance().getProperty("FTP_PASSWORD");
	public static final String FTP_USER = 
			PropertiesUtil.getInstance().getProperty("FTP_USER");
	public static final String USER_DIR = System.getProperty("user.dir")
			+ "\\files\\";

	public FTPUtil() {
		this.ftpClient = connect();
	}

	/**
	 * Determines whether a file exists in the ftp server or not
	 * 
	 * @param filePath
	 * @return true if exists, false otherwise
	 * @throws IOException
	 *             thrown if any I/O error occurred.
	 */
	public boolean isFileExistInFtp(String ftpFileName) {
		// check if the directory exist
		if (!isDirectoryExists()) {
			return false;
		}
		// inside the dir check if file exist
		try {
			FTPFile[] listFiles = getFtpClient().listFiles(FTP_INNER_FOLDER);
			for (FTPFile file : listFiles) {
				if (file.getName().toLowerCase()
						.equalsIgnoreCase(ftpFileName.toLowerCase()))
					return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * upload file to ftp server from local file in USER_DIR
	 * 
	 * @param localFileName
	 * @return
	 */
	public boolean uploadFileFromLocal(String localFileName) {
		// check if file exist in USER_DIR
		String ftpFileName = shorterFileName(localFileName);
		File uploadingFile = new File(USER_DIR + localFileName);
		if (!uploadingFile.exists()) {
			System.err.println("File " + localFileName + " is not exist in "
					+ uploadingFile.getAbsolutePath() );
			return false;
		}

		String firstRemoteFile = "/" + FTP_INNER_FOLDER + "/" + ftpFileName;

		// check if file exist in ftp server
		if (isFileExistInFtp(ftpFileName)) {
			System.err
					.println("File is already exist in ftp server - not uploaded");
			return false;
		}
		try {
			return uploadFileFromLocal(uploadingFile, firstRemoteFile);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * get from google drive a sharable file in this format:<br>
	 * https://drive.google.com/uc?export=download&id=< the id > <br>
	 * the id is something like '0BxmvpMvKSLJ_ckJ1T1VNd0NtaDg'
	 * 
	 * @param driveURL
	 * @param fileName
	 * @return true is success
	 */
	public boolean uploadFileFromGoogleDriveToFTP(String driveURL,
			String fileName) {
		fileName = shorterFileName(fileName);
		// check that the file is not already in the ftp server
		if (isFileExistInFtp(fileName)) {
			System.err.println("the file " + fileName
					+ " is already exist in ftp server");
			return false;
		}

		// create a local file (in the project folder) to save into the
		// file-download from google-drive
		File fileToDownloadPath = createLocalFileIfNotExist(fileName);

		// check the url if web exist
		URL url = isUrlSitelValid(driveURL);

		try {
			// copy from the google drive url to local path
			FileUtils.copyURLToFile(url, fileToDownloadPath);
			// the full ftp path
			String remoteFTPFilePathAndNewName = "/" + FTP_INNER_FOLDER + "/"
					+ fileToDownloadPath.getName();

			boolean done = uploadFileFromLocal(fileToDownloadPath,
					remoteFTPFilePathAndNewName);

			if (done) {
				System.out.println("The file uploaded successfully.");
				return isFileExistInFtp(fileName);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Download file from ftp server into USER_DIR
	 * 
	 * @param ftpFileName
	 * @return
	 * @throws IOException
	 */
	public boolean downloadFileIntoUserPath(String ftpFileName) {

		String remoteFile = "/" + FTP_INNER_FOLDER + "/" + ftpFileName;

		// create local file
		File downloadFile = createLocalFileIfNotExist(ftpFileName);

		// open output stream
		try (OutputStream outputStream = new BufferedOutputStream(
				new FileOutputStream(downloadFile))) {

			// download the file
			this.ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			return this.ftpClient.retrieveFile(remoteFile, outputStream);

		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * get instance of FTPClient, if the connection is broken - connect again
	 * 
	 * @return FTPClient
	 */
	public FTPClient getFtpClient() {
		if (ftpClient.isAvailable() && ftpClient.isConnected())
			return ftpClient;
		else {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return connect();
		}
	}
	

	/**
	 * "FTP_SERVER":"o.qa.gimso.net", "FTP_PORT":"21",
	 * "FTP_PASSWORD":"SimgoFiles", "FTP_USER":"ftp_simgo", "FTP_INNER_FOLDER"
	 * 
	 * @return
	 */
	private FTPClient connect() {
		try {
			this.ftpClient = new FTPClient();
			this.ftpClient.connect(FTP_SERVER, FTP_PORT);
			this.ftpClient.login(FTP_USER, FTP_PASSWORD);

			return this.ftpClient;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Determines whether a directory exists or not
	 * 
	 * @param dirPath
	 * @param ftpClient
	 * @return true if exists, false otherwise
	 * @throws IOException
	 *             thrown if any I/O error occurred.
	 */
	private boolean isDirectoryExists() {
		try {
			FTPFile[] listFiles = getFtpClient().listFiles();
			for (FTPFile file : listFiles) {
				if (file.getName().toLowerCase()
						.equalsIgnoreCase(FTP_INNER_FOLDER.toLowerCase()))
					return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		try {
			unitTesting();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @throws IOException
	 */
	private static void unitTesting() throws IOException {
		FTPUtil ftpUtil = new FTPUtil();
		
		boolean fileExist = ftpUtil.isFileExistInFtp("test_ftp_02.sgo");
		System.out.println(fileExist);
		
		boolean uploadFileFromLocal = ftpUtil.uploadFileFromLocal("properties.json");
		System.out.println(uploadFileFromLocal);
		
		String ftpFileName = "plug_v0.7.0_delayed_connection_qa_sgs6.sgo";
		boolean downloadFileIntoUserPath = ftpUtil.downloadFileIntoUserPath(ftpFileName);
		System.out.println(downloadFileIntoUserPath);
		
		String driveURL = "https://drive.google.com/uc?export=download&id=0BxmvpMvKSLJ_ckJ1T1VNd0NtaDg";
		String fileName = "name";
		boolean uploadFileFromGoogleDriveToFTP = ftpUtil
				.uploadFileFromGoogleDriveToFTP(driveURL, fileName);
		System.out.println(uploadFileFromGoogleDriveToFTP);
	}

	/**
	 * upload file
	 * 
	 * @param localFilePath
	 * @param newFtpFileNameAndPath
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private boolean uploadFileFromLocal(File localFilePath,
			String newFtpFileNameAndPath) throws FileNotFoundException,
			IOException {
		InputStream inputStream = new FileInputStream(localFilePath);
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		boolean done = this.ftpClient.storeUniqueFile(newFtpFileNameAndPath,
				inputStream);
		inputStream.close();
		return done;
	}

	/**
	 * check if url site is valid (cannot check the path in the site,<br>
	 * it will be checked when trying to upload/download )
	 * 
	 * @param driveURL
	 * @return
	 */
	private URL isUrlSitelValid(String driveURL) {
		URL url = null;
		try {
			url = new URL(driveURL);
			URLConnection conn = url.openConnection();
			conn.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return url;
	}

	/**
	 * create local file if its not exist
	 * 
	 * @param fileName
	 * @return
	 */
	private File createLocalFileIfNotExist(String fileName) {
		fileName = shorterFileName(fileName);
		File fileToDownloadPath = new File(USER_DIR + fileName);
		if (fileToDownloadPath.exists()) {
			System.out.println("File " + fileName + " is alreday exist in "
					+ fileToDownloadPath);
			return fileToDownloadPath;
		}
		try {
			fileToDownloadPath.createNewFile();
			return fileToDownloadPath;
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		return null;
	}

	/**
	 * when uploading sgo file with name that is length is more then 38 char's the
	 * persist didn't except it as a valid URL
	 * 
	 * @param fileName
	 */
	public static String shorterFileName(String fileName) {
		if (fileName.length() < 38)return fileName;
		System.out.println("sgo file name before \""+fileName+"\", the length is "+fileName.length());
		StringBuilder sb = new StringBuilder();

		for (String s : fileName.split("_")) {
			switch (s) {
			case "plug":
				s = "p";
				break;
			case "global":
				s = "glbl";
				break;
			case "delayed":
				s = "dlyd";
				break;
			case "connection":
				s = "";
				break;
			case "staging":
				s = "stg";
				break;

			default:
				break;
			}
			if (!s.isEmpty())
				sb.append(s);
			if (!s.contains(".sgo") && !s.isEmpty())
				sb.append("_");
		}

		String after = sb.toString();
		System.out.println("sgo file name after \"" + after
				+ "\", the length is " + after.length());
		if (after.length() < 38)
			return after;		
		else if (after.length() < 38)
			throw new RuntimeException(
					"The file name is to long, must be less then 38 characters");
		return null;
	
	}

}
