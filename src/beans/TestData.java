package beans;

import java.util.List;

/**
 * Simple POJO bean used to received test data from a DAO.
 * 
 * @author Yehuda Ginsburg
 */
public class TestData {
	
	private String userName;
	private String userGroupId;
	private String accessNumberGroup;
	private String outgoingAccessNumber;
	private String testPlan;
	private String homeNumber;
	private String plugId;
	private String incomingAccessNumber;
	private List<String> dialedDigits;
	private String operatingSystem;
	private String plugComPortId;
	private String deviceModel;
	private String deviceSN;
	private String deviceName;

	public TestData(String userName, String userGroupId,
			String accessNumberGroup, String outgoingAccessNumber,
			String testPlan, String homeNumber, String plugId,
			String incomingAccessNumber, List<String> dialedDigits,
			String operatingSystem, String plugComPortId, String deviceModel,
			String deviceSN, String deviceName) {
		super();
		this.userName = userName;
		this.userGroupId = userGroupId;
		this.accessNumberGroup = accessNumberGroup;
		this.outgoingAccessNumber = outgoingAccessNumber;
		this.testPlan = testPlan;
		this.homeNumber = homeNumber;
		this.plugId = plugId;
		this.incomingAccessNumber = incomingAccessNumber;
		this.dialedDigits = dialedDigits;
		this.operatingSystem = operatingSystem;
		this.plugComPortId = plugComPortId;
		this.deviceModel = deviceModel;
		this.deviceSN = deviceSN;
		this.deviceName = deviceName;
	}

	public TestData() {
		super();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(String userGroupId) {
		this.userGroupId = userGroupId;
	}

	public String getAccessNumberGroup() {
		return accessNumberGroup;
	}

	public void setAccessNumberGroup(String accessNumberGroup) {
		this.accessNumberGroup = accessNumberGroup;
	}

	public String getOutgoingAccessNumber() {
		return outgoingAccessNumber;
	}

	public void setOutgoingAccessNumber(String outgoingAccessNumber) {
		this.outgoingAccessNumber = outgoingAccessNumber;
	}

	public String getTestPlan() {
		return testPlan;
	}

	public void setTestPlan(String testPlan) {
		this.testPlan = testPlan;
	}

	public String getHomeNumber() {
		return homeNumber;
	}

	public void setHomeNumber(String homeNumber) {
		this.homeNumber = homeNumber;
	}

	public String getPlugId() {
		return plugId;
	}

	public void setPlugId(String plugId) {
		this.plugId = plugId;
	}

	public String getIncomingAccessNumber() {
		return incomingAccessNumber;
	}

	public void setIncomingAccessNumber(String incomingAccessNumber) {
		this.incomingAccessNumber = incomingAccessNumber;
	}

	public List<String> getDialedDigits() {
		return dialedDigits;
	}

	public void setDialedDigits(List<String> dialedDigits) {
		this.dialedDigits = dialedDigits;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String getPlugComPortId() {
		return plugComPortId;
	}

	public void setPlug_com_port_id(String plugComPortId) {
		this.plugComPortId = plugComPortId;
	}

	public String getDeviceSN() {
		return deviceSN;
	}

	public void setDeviceSN(String deviceSN) {
		this.deviceSN = deviceSN;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	@Override
	public String toString() {
		return "TestData [userName=" + userName + ", userGroupId="
				+ userGroupId + ", accessNumberGroup=" + accessNumberGroup
				+ ", outgoingAccessNumber=" + outgoingAccessNumber
				+ ", testPlan=" + testPlan + ", homeNumber=" + homeNumber
				+ ", plugId=" + plugId + ", incomingAccessNumber="
				+ incomingAccessNumber + ", dialedDigits=" + dialedDigits
				+ ", operatingSystem=" + operatingSystem + ", plugComPortId="
				+ plugComPortId + ", deviceModel=" + deviceModel
				+ ", deviceSN=" + deviceSN + ", deviceName=" + deviceName + "]";
	}

}
