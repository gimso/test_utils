package logger_observer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import adb.AdbUtil;

public class LogPublisher implements Publisher , Runnable{
	
	private List<Subscriber> subscribers;
	private String line;
	private String filter;
	
	public LogPublisher(String... filters) {
		String temp = "";
		for (String filter : filters) {
			temp += filter+"|";
		}
		temp = temp.substring(0, temp.length()-1);
		this.filter = temp;
		this.subscribers = new ArrayList<>();
	}

	@Override
	public void register(Subscriber subscriber) {
		subscribers.add(subscriber);
	}

	@Override
	public void unregister(Subscriber subscriber) {
		subscribers.remove(subscribers.indexOf(subscriber));
	}

	@Override
	public void notifySubscriber() {
		subscribers.forEach(subscriber -> subscriber.update(line));
	}
	
	private void setLine(String line){
		this.line = line;
		notifySubscriber();
	}

	@Override
	public void run() {
		String command = "adb logcat -c && adb shell \"logcat -v time | grep -E '" + this.filter + "'\"";

		System.out.println("Logcat filter: " + command);
		// this.pathToFile = getPathAndTime(LOGCAT);
		Process process = AdbUtil.executeCommandLine(command);

		try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			// read the output from the command
			String s = null;
			while ((s = stdInput.readLine()) != null)
				setLine(s);
		} catch (Exception e) {
			//e.printStackTrace();
		}

	}
}
