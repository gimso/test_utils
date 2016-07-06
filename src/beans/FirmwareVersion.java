package beans;

public class FirmwareVersion {
	private String config;
	private String version;
	private String url;
	private String comment;

	public FirmwareVersion(String config, String version, String url) {
		super();
		this.config = config;
		this.version = version;
		this.url = url;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "FirmwareVersion [config=" + config + ", version=" + version
				+ ", url=" + url + ", comment=" + comment + "]";
	}
}
