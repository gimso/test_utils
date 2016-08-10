package logger_observer;

public interface Publisher {
	public void register(Subscriber subscriber);
	public void unregister(Subscriber subscriber);
	public void notifySubscriber();
}
