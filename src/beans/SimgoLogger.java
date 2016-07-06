package beans;

import logging.CloudLogger;
import logging.PlugLogger;

public class SimgoLogger {
	public SimgoLogger() {
		super();
	}

	private CloudLogger cloudLogger;
	private PlugLogger plugLogger;

	public CloudLogger getCloudLogger() {
		return cloudLogger;
	}

	public SimgoLogger(PhoneType phoneType) {
		super();
		this.cloudLogger = new CloudLogger();
		this.plugLogger = new PlugLogger(phoneType.getTestData()
				.getPlugComPortId());
	}

	public void setCloudLogger(CloudLogger cloudLogger) {
		this.cloudLogger = cloudLogger;
	}

	public PlugLogger getPlugLogger() {
		return plugLogger;
	}

	public void setPlugLogger(PlugLogger plugLogger) {
		this.plugLogger = plugLogger;
	}

}
