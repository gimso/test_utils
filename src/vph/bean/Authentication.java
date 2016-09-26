package vph.bean;

import java.util.Date;

public class Authentication {
	private Date mAtmelAuthRequestTime;
	private Date mVsimAuthRequestTime;
	private Date mServersAuthRequestTime;

	private Date mServersAuthResponseTime;
	private Date mVsimAuthResponseTime;
	private Date mAtmelAuthResponseTime;

	private long mServersAuthRoundTripTime;
	private long mAtmelAuthRoundTripTime;

	public Authentication() {
	}
	
	public static Date getValidAuthRequest(Authentication authentication) {
		if (authentication.getAtmelAuthRequest() != null)
			return authentication.getAtmelAuthRequest();
		else if (authentication.getVsimAuthRequest() != null)
			return authentication.getVsimAuthRequest();
		else if (authentication.getServersAuthRequest() != null)
			return authentication.getServersAuthRequest();
		else
			return null;
	}

	public Date getAtmelAuthRequest() {
		return mAtmelAuthRequestTime;
	}

	public void setAtmelAuthRequest(Date mAtmelAuthRequest) {
		this.mAtmelAuthRequestTime = mAtmelAuthRequest;
	}

	public Date getVsimAuthRequest() {
		return mVsimAuthRequestTime;
	}

	public void setVsimAuthRequest(Date mVsimAuthRequest) {
		this.mVsimAuthRequestTime = mVsimAuthRequest;
	}

	public Date getServersAuthRequest() {
		return mServersAuthRequestTime;
	}

	public void setServersAuthRequest(Date mServersAuthRequest) {
		this.mServersAuthRequestTime = mServersAuthRequest;
	}

	public Date getServersAuthResponse() {
		return mServersAuthResponseTime;
	}

	public void setServersAuthResponse(Date mServersAuthResponse) {
		this.mServersAuthResponseTime = mServersAuthResponse;
	}

	public Date getVsimAuthResponse() {
		return mVsimAuthResponseTime;
	}

	public void setVsimAuthResponse(Date mVsimAuthResponse) {
		this.mVsimAuthResponseTime = mVsimAuthResponse;
	}

	public Date getAtmelAuthResponse() {
		return mAtmelAuthResponseTime;
	}

	public void setAtmelAuthResponse(Date mAtmelAuthResponse) {
		this.mAtmelAuthResponseTime = mAtmelAuthResponse;
	}

	public long getServersAuthRoundTrip() {
		return mServersAuthRoundTripTime;
	}

	public void setServersAuthRoundTrip(long mServersAuthRoundTrip) {
		this.mServersAuthRoundTripTime = mServersAuthRoundTrip;
	}

	public long getAtmelAuthRoundTrip() {
		return mAtmelAuthRoundTripTime;
	}

	public void setAtmelAuthRoundTrip(long mAtmelAuthRoundTrip) {
		this.mAtmelAuthRoundTripTime = mAtmelAuthRoundTrip;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n Auth:");
		sb.append("\n AtmelAuthRequest=" + mAtmelAuthRequestTime);
		sb.append("\n VsimAuthRequest=" + mVsimAuthRequestTime);
		sb.append("\n ServersAuthRequest=" + mServersAuthRequestTime);
		sb.append("\n ServersAuthResponse=" + mServersAuthResponseTime);
		sb.append("\n VsimAuthResponse=" + mVsimAuthResponseTime);
		sb.append("\n AtmelAuthResponse=" + mAtmelAuthResponseTime);
		sb.append("\n ServersAuthRoundTrip=" + mServersAuthRoundTripTime);
		sb.append("\n AtmelAuthRoundTrip=" + mAtmelAuthRoundTripTime);
		return sb.toString();
	}

}
