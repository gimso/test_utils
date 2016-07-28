package testrail.enums;

/**
 * Result status id's
 * 
 * @author Yehuda Ginsburg
 *
 */
public enum Status {
	PASSED(1), BLOCKED(2), UNTESTED(3), NOT_TESTABLE(4), FAILED(5), SKIPPED(6);

	private int value;

	Status(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static Status getStatus(int value) {
		for (Status status : Status.values())
			if (status.getValue() == value)
				return status;

		System.err.println("Unknown value (" + value + ") for Status, return null");
		return null;
	}

}
