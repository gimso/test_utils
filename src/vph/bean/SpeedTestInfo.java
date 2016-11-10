package vph.bean;

import java.util.Date;

public class SpeedTestInfo {

	private final Date mTime;
	private final String mQuality;
	private final int mSpeed;

	public SpeedTestInfo(Date time, String mQuality, int mSpeed) {
		this.mTime = time;
		this.mQuality = mQuality;
		this.mSpeed = mSpeed;
	}

	public Date getTime() {
		return mTime;
	}

	public String getQuality() {
		return mQuality;
	}

	public int getSpeed() {
		return mSpeed;
	}

	@Override
	public String toString() {
		return String.format("Speed Test - Time:%s, Quality:%s, Speed:%d", mTime, mQuality, mSpeed);
	}

}
