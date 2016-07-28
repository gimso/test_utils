package testrail.listener;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import testrail.api.TestRailUtil;
import testrail.enums.Status;
import testrail.enums.User;
import testrail.refrences.Result;
import testrail.refrences.Test;

/**
 * This class has global listener that update the testrail results
 * 
 * @author Yehuda Ginsburg
 *
 */
public class Listener implements ITestListener {

	@Override
	public void onTestStart(ITestResult result) {
		String methodName = getMethodName(result);
		System.out.println("Test " + methodName + " Started");
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		updateTestRail(result);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		updateTestRail(result);
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		updateTestRail(result);
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		updateTestRail(result);
	}

	@Override
	public void onStart(ITestContext context) {
	}

	@Override
	public void onFinish(ITestContext context) {
	}

	/**
	 * Update TestRail get the name status output and test id and send result to testrail 
	 * @param iTestResult
	 */
	public void updateTestRail(ITestResult iTestResult) {
		String methodName = getMethodName(iTestResult);
		Status status = getStatus(iTestResult);
		String output = getOutput(iTestResult);
		Test test = getTest(methodName);

		if (test != null) {
			new Result((Long) test.getId(), status, User.GIMSO_REPORTS, output == null ? "" : output);
			System.out.println("Method " + methodName + " result is " + status.name());
		} else {
			System.err.println("cannot find a name " + methodName + " equals to any method in \n");
		}
	}

	/**
	 * Get method name using iTestResult reflection
	 * @param iTestResult
	 * @return String
	 */
	public String getMethodName(ITestResult iTestResult) {
		return iTestResult.getMethod().getMethodName();
	}

	/**
	 * Get the Test object by method name and titles list
	 * @param methodName
	 * @return Test
	 */
	public Test getTest(String methodName) {
		methodName = methodName.toLowerCase().replaceAll("\\s", "");
		for (testrail.refrences.Test tempTest : TestRailUtil.getInstance().getPlan().getTests()) {
			String title = String.valueOf(tempTest.getTitle()).toLowerCase().replaceAll("\\s", "");
			String custom_method_name = String.valueOf(tempTest.getCustom_method_name()).toLowerCase();
			boolean equalsTitle = title.equals(methodName);
			boolean equalsCustomMethod = custom_method_name != null && custom_method_name.equals(methodName);
			if (equalsTitle || equalsCustomMethod) {
				return tempTest;
			}
		}
		return null;
	}

	/**
	 * Get output if output member exist in class using reflection 
	 * @param iTestResult
	 * @return String
	 */
	public String getOutput(ITestResult iTestResult) {
		Object instance = iTestResult.getInstance();
		try {
			return (String) instance.getClass().getDeclaredField("output").get(instance);
		} catch (Exception e) {
			System.err.println("Cannot get output from Class " + instance.getClass().getName());
			return null;
		}
	}

	/**
	 * Retrieve Status object(enum) from iTestResult
	 * @param iTestResult
	 * @return
	 */
	public Status getStatus(ITestResult iTestResult) {
		Status status;
		switch (iTestResult.getStatus()) {

		case ITestResult.FAILURE:
			status = Status.FAILED;
			break;
		case ITestResult.SKIP:
			status = Status.SKIPPED;
			break;
		case ITestResult.SUCCESS:
			status = Status.PASSED;
			break;
		default:
			status = Status.BLOCKED;
			break;
		}
		return status;
	}

}