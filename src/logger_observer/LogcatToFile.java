package logger_observer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class LogcatToFile implements Subscriber {
	private Publisher logcatPub;
	private File file;

	public LogcatToFile(Publisher publisher) {
		this.logcatPub = publisher;
		this.logcatPub.register(this);
		this.file = createFileAndFolderFromPath(getPathAndTime());
		System.out.println("Logcat file saved in:\t"+this.file.getAbsolutePath());
	}

	@Override
	public void update(String line) {
		if (!line.isEmpty())
			try {
				FileUtils.writeStringToFile(this.file, line+"\n", true);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/**
	 * @param String
	 *            of folder name, path of the file has to be save
	 * @return the Path to save the file and the time to insert in the class
	 *         name
	 */
	public String getPathAndTime() {
		String folder = "Logcat Logger";
		String path = System.getProperty("user.dir");
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String time = formatter.format(date);
		String cmdToFile = path + "\\" + folder + "\\" + time + ".txt";
		return cmdToFile;
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

	public File getFile() {
		return file;
	}
}
