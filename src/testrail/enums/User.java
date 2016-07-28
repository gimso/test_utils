package testrail.enums;

/**
 * User id's
 * 
 * @author Yehuda Ginsburg
 *
 */
public enum User {
	OR_SHACHAR(1), 
	YEHUDA_GINSBURG(4), 
	GIMSO_REPORTS(5), 
	TAMIR_SAGI(7), 
	DANA_TAWIL(8), 
	TZVIKA_SHNEIDER(9), 
	AUTOMATION(10), 
	OMRI_DEUTSCH(11), 
	BEN_KATZ(12);

	private Long value;

	User(int value) {
		this.value = Long.valueOf(value);
	}

	public Long getValue() {
		return value;
	}

	public static User getUser(Long value) {
		for (User user : User.values())
			if (user.getValue() == value)
				return user;

		System.err.println("Unknown value (" + value + ") for User, return null");
		return null;
	}

}
