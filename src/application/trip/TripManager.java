package application.trip;

import application.config.ConfigurationManager.CancelTripStages;

/**
 * Class defines 
 * @author Tamir Sagi
 *
 */
public abstract class TripManager{
	
	protected static TripManager mTripManager;

	/**
	 * Get 
	 * @return Trip Manager instance
	 * @throws Exception if instance has not yet initialized
	 */
	public static TripManager gerInstance() throws Exception{
		if(mTripManager == null)
			throw new Exception("Instance is not initialized yet");
		return mTripManager;
	}

	
	public abstract String createTrip(String deviceId);
	
	public abstract String cancelTrip(CancelTripStages stageNumber);
	
	public abstract String enableCallForwarding();
}
