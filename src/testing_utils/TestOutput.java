package testing_utils;
/**
 * This is java bean using to collect an result of test and the output message of it 
 * @author Yehuda Ginsburg
 *
 */
public class TestOutput {

	private Boolean result;
	private String output;

	public TestOutput(){}

	public TestOutput(Boolean result, String output) {

		this.output = output;
		this.result = result;

	}

	public Boolean getResult() {
		return result;
	}

	public String getOutput() {
		return output;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	@Override
	public String toString() {
		return "TestOutput [result=" + result + ", output=" + output + "]";
	}
}