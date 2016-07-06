package application.login;

public abstract class LoginManager {

	protected static LoginManager mLoginManager;

		
	/**
	 * get Login instance
	 * @return
	 * @throws Exception
	 */
	public static LoginManager gerInstance() throws Exception{
		if(mLoginManager == null)
			throw new Exception("Login Manager is not initialized yet");
		return mLoginManager;
	}
	
	/**
	 * Login to Simgo APP
	 * @param country
	 * @param phoneNumber
	 * @param manulVerificationCode
	 * @return String whether succeed or not
	 */
	public abstract String login(String country,String phoneNumber,boolean manulVerificationCode);
	
	
	/**
	 * CLick login Button
	 */
	public abstract void clickLoginButton();
	
	/**
	 * Find Country by name
	 * @param country
	 * @return true or false correspondingly 
	 */
	public abstract boolean findCountry(String country);
	
	/**
	 * enter Verification Code 
	 * @param verificationCode
	 */
	public abstract void enterVerificationCode(String verificationCode);
	
	/**
	 * Log out from Simgo App
	 * @return String whether succeed or not
	 */
	public abstract String logout(); 
}


