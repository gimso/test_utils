package beans;

/**
 * this class represent phone type data
 * @author Tamir Sagi
 *
 */
public class PhoneTypeData {
	
	private String mPlatform;
	private String mVersion;
	private String mModel;
	private String mDeviceUDID;
	
	
	
	public String getmPlatform() {
		return mPlatform;
	}
	public void setPlatform(String platform) {
		this.mPlatform = platform;
	}
	public String getVersion() {
		return mVersion;
	}
	public void setVersion(String version) {
		this.mVersion = version;
	}
	public String getModel() {
		return mModel;
	}
	public void setModel(String model) {
		this.mModel = model;
	}
	public String getDeviceUDID() {
		return mDeviceUDID;
	}
	public void setDeviceUDID(String deviceUDID) {
		this.mDeviceUDID = deviceUDID;
	}
	
	
	@Override
	public String toString() {
		return "PhoneTypeData [mPlatform=" + mPlatform + ", mversion="
				+ mVersion + ", mModel=" + mModel + ", mDeviceUDID="
				+ mDeviceUDID + "]";
	}
	
	
	
	
	
	


}