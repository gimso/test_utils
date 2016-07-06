package beans;

public class FromTo {
	private String from;
	private String to;

	@Override
	public String toString() {
		return "FromTo [from=" + from + ", to=" + to + "]";
	}

	public FromTo(String from, String to) {
		super();
		this.from = from;
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

}