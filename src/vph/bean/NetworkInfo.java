package vph.bean;

import java.util.Date;

public class NetworkInfo {

	private final Date mTime;
	private final String mType;
	private final String mApn;
	private final boolean mIsRoaming;

	public NetworkInfo(Date time, String mType, String mApn, boolean mIsRoaming) {
		this.mTime = time;
		this.mType = mType;
		this.mApn = mApn;
		this.mIsRoaming = mIsRoaming;
	}

	public Date getTime() {
		return mTime;
	}

	public String getType() {
		return mType;
	}

	public String getApn() {
		return mApn;
	}

	public boolean isIsRoaming() {
		return mIsRoaming;
	}

	@Override
	public String toString() {
		return String.format("NetworkInfo - Time:%s, Type:%s, Apn:%s, Roaming:%s", mTime, mType, mApn, mIsRoaming);
	}

}
