package vph.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestData {
	private Date mReboot;
	private Date m3gConnection;
	private Date mRsim;
	private Date m4gConnection;
	private Date mBootCompleted;

	private List<Authentication> mAuths;
	private String mFilePath;

	private TestVPHProperties mProperties;
	private SpeedTestInfo mSpeedTestInfo;
	private NetworkInfo mNetworkInfo3G;
	private NetworkInfo mNetworkInfo4G;
	private String mAtmelVersion;

	public TestData() {
		this.mAuths = new ArrayList<>();
	}

	public Date getReboot() {
		return mReboot;
	}

	public void setReboot(Date mReboot) {
		this.mReboot = mReboot;
	}

	public Date get3gConnection() {
		return m3gConnection;
	}

	public void set3gConnection(Date m3gConnection) {
		this.m3gConnection = m3gConnection;
	}

	public Date getRsim() {
		return mRsim;
	}

	public void setRsim(Date mRsim) {
		this.mRsim = mRsim;
	}

	public Date get4gConnection() {
		return m4gConnection;
	}

	public void set4gConnection(Date m4gConnection) {
		this.m4gConnection = m4gConnection;
	}

	public List<Authentication> getAuths() {
		return mAuths;
	}

	public void setAuths(List<Authentication> mAuths) {
		this.mAuths = mAuths;
	}

	public String getFilePath() {
		return mFilePath;
	}

	public void setFilePath(String mFilePath) {
		this.mFilePath = mFilePath;
	}

	public TestVPHProperties getProperties() {
		return mProperties;
	}

	public void setProperties(TestVPHProperties mProperties) {
		this.mProperties = mProperties;
	}

	public SpeedTestInfo getSpeedTestInfo() {
		return mSpeedTestInfo;
	}

	public void setSpeedTestInfo(SpeedTestInfo mSpeedTestInfo) {
		this.mSpeedTestInfo = mSpeedTestInfo;
	}

	public NetworkInfo getNetworkInfo3G() {
		return mNetworkInfo3G;
	}

	public void setNetworkInfo3G(NetworkInfo mNetworkInfo3G) {
		this.mNetworkInfo3G = mNetworkInfo3G;
	}

	public NetworkInfo getNetworkInfo4G() {
		return mNetworkInfo4G;
	}

	public void setNetworkInfo4G(NetworkInfo mNetworkInfo4G) {
		this.mNetworkInfo4G = mNetworkInfo4G;
	}

	public String getAtmelVersion() {
		return mAtmelVersion;
	}

	public void setAtmelVersion(String mAtmelVersion) {
		this.mAtmelVersion = mAtmelVersion;
	}

	public Date getBootCompleted() {
		return mBootCompleted;
	}

	public void setBootCompleted(Date mBootCompleted) {
		this.mBootCompleted = mBootCompleted;
	}

	@Override
	public String toString() {
		return "TestData:\nReboot=" + mReboot + "\n3gConnection=" + m3gConnection + "\nRsim=" + mRsim
				+ "\n4gConnection=" + m4gConnection + "\nAuths=" + mAuths + "\nFilePath=" + mFilePath + "\nProperties="
				+ mProperties + "\nSpeedTestInfo=" + mSpeedTestInfo + "\nNetworkInfo3G=" + mNetworkInfo3G
				+ "\nNetworkInfo4G=" + mNetworkInfo4G + "\nAtmel Version" + mAtmelVersion + "\nBoot Completed"
				+ mBootCompleted;
	}

}
