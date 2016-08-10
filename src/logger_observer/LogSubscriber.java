package logger_observer;

public class LogSubscriber implements Subscriber {
	int lineNum = 0;
	private Publisher logcatPub;

	public LogSubscriber(Publisher publisher) {
		this.logcatPub = publisher;
		this.logcatPub.register(this);
	}

	@Override
	public void update(String line) {
		if (!line.isEmpty())
			System.out.println("Line: " + lineNum++ + "\t" + line);
	}
}
