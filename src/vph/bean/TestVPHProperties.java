package vph.bean;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TestVPHProperties {

	private static final String DEVICE_ID = "DeviceId";
	private static final String SERIAL_ID = "SerialId";
	private static final String SITE = "Site";
	private static final String OS_VERSION = "OsVersion";
	private static final String SERVERS_VERSION = "ServersVersion";
	private static final String VSIM_VERSION = "VSimVersion";
	private static final String ATMEL_VERSION = "AtmelVersion";
	private static final String PACKAGE_VERSION = "PackageVersion";
	private static final String CONFIG_VERSION = "ConfigVersion";
	private static final String REBOOT_TIME = "RebootTime";
	private static final String REASON = "Reason";
	private static final String TEST_RESULT = "TestResult";

	private String mDeviceId;
	private String mSerialId;
	private String mSite;
	private String mServersControlVersion;
	private String mVSimControlVersion;
	private String mOSVersion;
	private String mAtmelVersion;
	private String mPackageVersion;
	private String mConfigVersion;
	private String mRebootTime;
	private String mReason;
	private boolean mTestResult;

	private JSONObject mVPHPropertiesJson;
	private File mPropertiesFile;

	private TestVPHProperties() {
	}

	/**
	 * Build from json file contains like the following, a TestVPHProperties
	 * bean {</br>
	 * "DeviceId":"000040001021",</br>
	 * "SerialId":"1cc9d4fa",</br>
	 * "Site":"Israel",</br>
	 * "ServersVersion":"1.2.1",</br>
	 * "VSimVersion":"1.2.1",</br>
	 * "OsVersion":"4.3.1.201608221126",</br>
	 * "AtmelVersion":"SIMGO_5cf7461_20160826180622",</br>
	 * "TestResult":false</br>
	 * }
	 * 
	 * @param file
	 * @return TestVPHProperties
	 */
	public static TestVPHProperties extractTestVPHProperties(File file) {
		TestVPHProperties properties = new TestVPHProperties();
		properties.mPropertiesFile = file;
		try {
			JSONParser parser = new JSONParser();
			properties.mVPHPropertiesJson = (JSONObject) parser.parse(new FileReader(file));
			properties.mDeviceId = String.valueOf(properties.mVPHPropertiesJson.get(DEVICE_ID));
			properties.mSerialId = String.valueOf(properties.mVPHPropertiesJson.get(SERIAL_ID));
			properties.mSite = String.valueOf(properties.mVPHPropertiesJson.get(SITE));
			properties.mServersControlVersion = String.valueOf(properties.mVPHPropertiesJson.get(SERVERS_VERSION));
			properties.mVSimControlVersion = String.valueOf(properties.mVPHPropertiesJson.get(VSIM_VERSION));
			properties.mOSVersion = String.valueOf(properties.mVPHPropertiesJson.get(OS_VERSION));
			properties.mAtmelVersion = String.valueOf(properties.mVPHPropertiesJson.get(ATMEL_VERSION));
			properties.mPackageVersion = String.valueOf(properties.mVPHPropertiesJson.get(PACKAGE_VERSION));
			properties.mConfigVersion = String.valueOf(properties.mVPHPropertiesJson.get(CONFIG_VERSION));
			properties.mTestResult = (Boolean.valueOf(String.valueOf(properties.mVPHPropertiesJson.get(TEST_RESULT))));
			properties.mReason = String.valueOf(properties.mVPHPropertiesJson.get(REASON));
			properties.mRebootTime = String.valueOf(properties.mVPHPropertiesJson.get(REBOOT_TIME));
			return properties;
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getDeviceId() {
		return mDeviceId;
	}

	public String getSerialId() {
		return mSerialId;
	}

	public String getSite() {
		return mSite;
	}

	public String getServersControlVersion() {
		return mServersControlVersion;
	}

	public String getVSimControlVersion() {
		return mVSimControlVersion;
	}

	public String getOSVersion() {
		return mOSVersion;
	}

	public String getAtmelVersion() {
		return mAtmelVersion;
	}

	public String getRebootTime() {
		return mRebootTime;
	}

	public String getReason() {
		return mReason;
	}

	public boolean isTestResult() {
		return mTestResult;
	}

	public JSONObject getVPHPropertiesJson() {
		return mVPHPropertiesJson;
	}

	public File getPropertiesFile() {
		return mPropertiesFile;
	}

	public String getPackageVersion() {
		return mPackageVersion;
	}

	public String getConfigVersion() {
		return mConfigVersion;
	}

	@Override
	public String toString() {
		return "DeviceId=" + mDeviceId + ", SerialId=" + mSerialId + ", Site=" + mSite + ", ServersControlVersion="
				+ mServersControlVersion + ", VSimControlVersion=" + mVSimControlVersion + ", OSVersion=" + mOSVersion
				+ ", AtmeVersion=" + mAtmelVersion + ", PackageVersion=" + mPackageVersion + ", ConfigVersion="
				+ mConfigVersion + ", RebootTime=" + mRebootTime + ", Reason=" + mReason + ", TestResult="
				+ mTestResult;
	}
}
