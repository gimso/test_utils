package beans;

/**
 * Simple POJO bean has TestData class and name of Phone Type (SGS3/IPHONE4
 * etc.)
 * 
 * @author Yehuda Ginsburg
 *
 */
public class PhoneType {

	private TestData testData;
	private String name;
	private String rsim;
	private OS os;

	public enum OS {
		ANDROID, IOS
	}

	public PhoneType() {}

	public PhoneType(String name, TestData testData) {
		super();
		this.testData = testData;
		this.name = name;

	}

	public PhoneType(String name, TestData testData, String rsim) {
		super();
		this.testData = testData;
		this.name = name;
		this.rsim = rsim;
	}

	public TestData getTestData() {
		return testData;
	}

	public void setTestData(TestData testData) {
		this.testData = testData;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRsim() {
		return rsim;
	}

	public void setRsim(String rsim) {
		this.rsim = rsim;
	}

	public OS getOs() {
		return os;
	}

	public void setOs(OS os) {
		this.os = os;
	}

	@Override
	public String toString() {
		if (rsim == null)
			return "PhoneType [testData=" + testData + ", name=" + name + "]";
		else
			return "PhoneType [testData=" + testData + ", name=" + ", rsim=" + rsim + ", os=" + os + "]";
	}

}