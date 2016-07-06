package persist.usage;

import global.PersistException;
import global.PersistUtil;
import global.SelectPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * This class is Added Modified and Delete Plan under persist.usage.Plan page
 * 
 * @author Yehuda Ginsburg
 *
 */
public class Plans {
	private static final String ID_OWNER = "id_owner";
	private static final String ID_CREATOR = "id_creator";
	private static final String ID_CURRENCY = "id_currency";
	private static final String ID_HOME_OVERUSE_UNITS_FEE = "id_home_overuse_units_fee";
	private static final String ID_HOME_OVERUSE_ADDITIONAL_UNITS = "id_home_overuse_additional_units";
	private static final String ID_HOME_OVERUSE_MB_FEE = "id_home_overuse_mb_fee";
	private static final String ID_HOME_OVERUSE_ADDITIONAL_MB = "id_home_overuse_additional_mb";
	private static final String ID_HOME_OVERUSE_MINUTES_FEE = "id_home_overuse_minutes_fee";
	private static final String ID_HOME_OVERUSE_ADDITIONAL_MINUTES = "id_home_overuse_additional_minutes";
	private static final String ID_OVERUSE_UNITS_FEE = "id_overuse_units_fee";
	private static final String ID_OVERUSE_ADDITIONAL_UNITS = "id_overuse_additional_units";
	private static final String ID_OVERUSE_MB_FEE = "id_overuse_mb_fee";
	private static final String ID_OVERUSE_ADDITIONAL_MB = "id_overuse_additional_mb";
	private static final String ID_OVERUSE_MINUTES_FEE = "id_overuse_minutes_fee";
	private static final String ID_OVERUSE_ADDITIONAL_MINUTES = "id_overuse_additional_minutes";
	private static final String ID_PERIOD_REPETITION = "id_period_repetition";
	private static final String ID_SUBSCRIPTION_FEE = "id_subscription_fee";
	private static final String ID_SERVICE_FEE = "id_service_fee";
	private static final String ID_PERIOD = "id_period";
	private static final String ID_ACTIVATION_FEE = "id_activation_fee";
	private static final String ID_HOME_UNIT_LIMIT = "id_home_unit_limit";
	private static final String ID_HOME_DATA_LIMIT = "id_home_data_limit";
	private static final String ID_HOME_CALLS_LIMIT = "id_home_calls_limit";
	private static final String ID_UNIT_LIMIT = "id_unit_limit";
	private static final String ID_DATA_LIMIT = "id_data_limit";
	private static final String ID_CALLS_LIMIT = "id_calls_limit";
	private static final String LINK_TEXT_ADD_PLAN = "Add plan";
	private static final String ID_NAME = "id_name";
	private static final String NAME_SAVE = "_save";

	private WebDriver driver;
	private SelectPage select;
	private PersistUtil persistUtil;

	public Plans() {
		persistUtil = PersistUtil.getInstance();
		this.driver = persistUtil.getDriver();
		this.select = persistUtil.getSelect();
	}

	/**
	 * Add plan with all parameters available
	 * 
	 * @param name
	 * @param callsLimitMinutes
	 * @param dataLimitMb
	 * @param unitLimit
	 * @param homeCountryCallsLimitMinutes
	 * @param homeCountryDataLimitMb
	 * @param homeCountryUnitLimit
	 * @param activationFee
	 * @param planPeriodDays
	 * @param serviceFee
	 * @param subscriptionFee
	 * @param planPeriodRepetition
	 * @param overuseAdditionalCalls
	 * @param overuseCallsFee
	 * @param overuseAdditionalData
	 * @param overuseDataFee
	 * @param overuseAdditionalUnit
	 * @param overuseUnitsFee
	 * @param homeCountryOveruseAdditionalCalls
	 * @param homeCountryOveruseCallsFee
	 * @param homeCountryOveruseAdditionalData
	 * @param homeCountryOveruseDataFee
	 * @param homeCountryOveruseAdditionalUnits
	 * @param homeCountryOveruseUnitsFee
	 * @param chargeCurrency
	 * @param creator
	 * @param owner
	 * @return String result message
	 * @throws PersistException
	 */
	public String add(String name, String callsLimitMinutes,
			String dataLimitMb, String unitLimit,
			String homeCountryCallsLimitMinutes, String homeCountryDataLimitMb,
			String homeCountryUnitLimit, String activationFee,
			String planPeriodDays, String serviceFee, String subscriptionFee,
			String planPeriodRepetition, String overuseAdditionalCalls,
			String overuseCallsFee, String overuseAdditionalData,
			String overuseDataFee, String overuseAdditionalUnit,
			String overuseUnitsFee, String homeCountryOveruseAdditionalCalls,
			String homeCountryOveruseCallsFee,
			String homeCountryOveruseAdditionalData,
			String homeCountryOveruseDataFee,
			String homeCountryOveruseAdditionalUnits,
			String homeCountryOveruseUnitsFee, String chargeCurrency,
			String creator, String owner) {
		// go to plan page
		select.selectUsagePlans();

		// click on Add plan
		driver.findElement(By.linkText(LINK_TEXT_ADD_PLAN)).click();

		// insert name
		if (name != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(name);
		}
		
		// insert callsLimitMinutes
		if (callsLimitMinutes != null) {
			driver.findElement(By.id(ID_CALLS_LIMIT)).clear();
			driver.findElement(By.id(ID_CALLS_LIMIT)).sendKeys(
					callsLimitMinutes);
		}

		// insert dataLimitMb
		if (dataLimitMb != null) {
			driver.findElement(By.id(ID_DATA_LIMIT)).clear();
			driver.findElement(By.id(ID_DATA_LIMIT)).sendKeys(dataLimitMb);
		}
		
		// insert unitLimit
		if (unitLimit != null) {
			driver.findElement(By.id(ID_UNIT_LIMIT)).clear();
			driver.findElement(By.id(ID_UNIT_LIMIT)).sendKeys(unitLimit);
		}

		// insert homeCountryCallsLimitMinutes
		if (homeCountryCallsLimitMinutes != null) {
			driver.findElement(By.id(ID_HOME_CALLS_LIMIT)).clear();
			driver.findElement(By.id(ID_HOME_CALLS_LIMIT)).sendKeys(homeCountryCallsLimitMinutes);
		}
		
		// insert homeCountryDataLimitMb
		if (homeCountryDataLimitMb != null) {
			driver.findElement(By.id(ID_HOME_DATA_LIMIT)).clear();
			driver.findElement(By.id(ID_HOME_DATA_LIMIT)).sendKeys(homeCountryDataLimitMb);
		}
		
		// insert homeCountryUnitLimit
		if (homeCountryUnitLimit != null) {
			driver.findElement(By.id(ID_HOME_UNIT_LIMIT)).clear();
			driver.findElement(By.id(ID_HOME_UNIT_LIMIT)).sendKeys(homeCountryUnitLimit);
		}
		
		// insert activationFee
		if (activationFee != null) {
			driver.findElement(By.id(ID_ACTIVATION_FEE)).clear();
			driver.findElement(By.id(ID_ACTIVATION_FEE)).sendKeys(activationFee);
		}
		
		// select planPeriodDays
		if (planPeriodDays != null) {
			persistUtil.selectByVisibleText(ID_PERIOD, planPeriodDays);
		}

		// insert serviceFee
		if (serviceFee != null) {
			driver.findElement(By.id(ID_SERVICE_FEE)).clear();
			driver.findElement(By.id(ID_SERVICE_FEE)).sendKeys(serviceFee);
		}

		// insert subscriptionFee
		if (subscriptionFee != null) {
			driver.findElement(By.id(ID_SUBSCRIPTION_FEE)).clear();
			driver.findElement(By.id(ID_SUBSCRIPTION_FEE)).sendKeys(subscriptionFee);
		}
		
		// insert planPeriodRepetition
		if (planPeriodRepetition != null) {
			driver.findElement(By.id(ID_PERIOD_REPETITION)).clear();
			driver.findElement(By.id(ID_PERIOD_REPETITION)).sendKeys(planPeriodRepetition);
		}
		
		// insert overuseAdditionalCalls
		if (overuseAdditionalCalls != null) {
			driver.findElement(By.id(ID_OVERUSE_ADDITIONAL_MINUTES)).clear();
			driver.findElement(By.id(ID_OVERUSE_ADDITIONAL_MINUTES)).sendKeys(overuseAdditionalCalls);
		}
		
		// insert overuseCallsFee
		if (overuseCallsFee != null) {
			driver.findElement(By.id(ID_OVERUSE_MINUTES_FEE)).clear();
			driver.findElement(By.id(ID_OVERUSE_MINUTES_FEE)).sendKeys(overuseCallsFee);
		}
		
		// insert overuseAdditionalData
		if (overuseAdditionalData != null) {
			driver.findElement(By.id(ID_OVERUSE_ADDITIONAL_MB)).clear();
			driver.findElement(By.id(ID_OVERUSE_ADDITIONAL_MB)).sendKeys(overuseAdditionalData);
		}
		
		// insert overuseDataFee
		if (overuseDataFee != null) {
			driver.findElement(By.id(ID_OVERUSE_MB_FEE)).clear();
			driver.findElement(By.id(ID_OVERUSE_MB_FEE)).sendKeys(overuseDataFee);
		}
		
		// insert overuseAdditionalUnit
		if (overuseAdditionalUnit != null) {
			driver.findElement(By.id(ID_OVERUSE_ADDITIONAL_UNITS)).clear();
			driver.findElement(By.id(ID_OVERUSE_ADDITIONAL_UNITS)).sendKeys(overuseAdditionalUnit);
		}
		
		// insert overuseUnitsFee
		if (overuseUnitsFee != null) {
			driver.findElement(By.id(ID_OVERUSE_UNITS_FEE)).clear();
			driver.findElement(By.id(ID_OVERUSE_UNITS_FEE)).sendKeys(overuseUnitsFee);
		}
		
		// insert homeCountryOveruseAdditionalCalls
		if (homeCountryOveruseAdditionalCalls != null) {
			driver.findElement(By.id(ID_HOME_OVERUSE_ADDITIONAL_MINUTES)).clear();
			driver.findElement(By.id(ID_HOME_OVERUSE_ADDITIONAL_MINUTES)).sendKeys(homeCountryOveruseAdditionalCalls);
		}
		
		// insert homeCountryOveruseCallsFee
		if (homeCountryOveruseCallsFee != null) {
			driver.findElement(By.id(ID_HOME_OVERUSE_MINUTES_FEE)).clear();
			driver.findElement(By.id(ID_HOME_OVERUSE_MINUTES_FEE)).sendKeys(homeCountryOveruseCallsFee);
		}
		
		// insert homeCountryOveruseAdditionalData
		if (homeCountryOveruseAdditionalData != null) {
			driver.findElement(By.id(ID_HOME_OVERUSE_ADDITIONAL_MB)).clear();
			driver.findElement(By.id(ID_HOME_OVERUSE_ADDITIONAL_MB)).sendKeys(homeCountryOveruseAdditionalData);
		}
		
		// insert homeCountryOveruseDataFee
		if (homeCountryOveruseDataFee != null) {
			driver.findElement(By.id(ID_HOME_OVERUSE_MB_FEE)).clear();
			driver.findElement(By.id(ID_HOME_OVERUSE_MB_FEE)).sendKeys(homeCountryOveruseDataFee);
		}
		
		// insert homeCountryOveruseAdditionalUnits
		if (homeCountryOveruseAdditionalUnits != null) {
			driver.findElement(By.id(ID_HOME_OVERUSE_ADDITIONAL_UNITS)).clear();
			driver.findElement(By.id(ID_HOME_OVERUSE_ADDITIONAL_UNITS)).sendKeys(homeCountryOveruseAdditionalUnits);
		}
		
		// insert homeCountryOveruseUnitsFee
		if (homeCountryOveruseUnitsFee != null) {
			driver.findElement(By.id(ID_HOME_OVERUSE_UNITS_FEE)).clear();
			driver.findElement(By.id(ID_HOME_OVERUSE_UNITS_FEE)).sendKeys(homeCountryOveruseUnitsFee);
		}
		
		// select chargeCurrency
		if (chargeCurrency != null) {
			persistUtil.selectByVisibleText(ID_CURRENCY, chargeCurrency);
		}
		
		// select creator
		if (creator != null) {
			persistUtil.selectByVisibleText(ID_CREATOR, creator);
		}
		
		// select owner
		if (owner  != null) {
			persistUtil.selectByVisibleText(ID_OWNER, owner);
		}
		
		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * * Add plan with the requires parameters only, the rest will fill with
	 * default values
	 * 
	 * @param name
	 * @param owner
	 * @return String result message
	 * @throws PersistException
	 */
	public String add(String name, String owner) {
		return add(name, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, owner);
	}

	/**
	 * * Modify and update the plan details by previous/old name
	 * 
	 * @return String result message
	 * @throws PersistException
	 */
	public String modifyByName(String name, String newName,
			String callsLimitMinutes, String dataLimitMb, String unitLimit,
			String homeCountryCallsLimitMinutes, String homeCountryDataLimitMb,
			String homeCountryUnitLimit, String activationFee,
			String planPeriodDays, String serviceFee, String subscriptionFee,
			String planPeriodRepetition, String overuseAdditionalCalls,
			String overuseCallsFee, String overuseAdditionalData,
			String overuseDataFee, String overuseAdditionalUnit,
			String overuseUnitsFee, String homeCountryOveruseAdditionalCalls,
			String homeCountryOveruseCallsFee,
			String homeCountryOveruseAdditionalData,
			String homeCountryOveruseDataFee,
			String homeCountryOveruseAdditionalUnits,
			String homeCountryOveruseUnitsFee, String chargeCurrency,
			String creator, String owner) {
		// go to plan page
		select.selectUsagePlans();

		// find plan by name and click on it
		driver.findElement(By.linkText(name)).click();

		// insert name
		if (newName != null) {
			driver.findElement(By.id(ID_NAME)).clear();
			driver.findElement(By.id(ID_NAME)).sendKeys(newName);
		}
		
		// insert callsLimitMinutes
		if (callsLimitMinutes != null) {
			driver.findElement(By.id(ID_CALLS_LIMIT)).clear();
			driver.findElement(By.id(ID_CALLS_LIMIT)).sendKeys(
					callsLimitMinutes);
		}

		// insert dataLimitMb
		if (dataLimitMb != null) {
			driver.findElement(By.id(ID_DATA_LIMIT)).clear();
			driver.findElement(By.id(ID_DATA_LIMIT)).sendKeys(dataLimitMb);
		}
		
		// insert unitLimit
		if (unitLimit != null) {
			driver.findElement(By.id(ID_UNIT_LIMIT)).clear();
			driver.findElement(By.id(ID_UNIT_LIMIT)).sendKeys(unitLimit);
		}

		// insert homeCountryCallsLimitMinutes
		if (homeCountryCallsLimitMinutes != null) {
			driver.findElement(By.id(ID_HOME_CALLS_LIMIT)).clear();
			driver.findElement(By.id(ID_HOME_CALLS_LIMIT)).sendKeys(homeCountryCallsLimitMinutes);
		}
		
		// insert homeCountryDataLimitMb
		if (homeCountryDataLimitMb != null) {
			driver.findElement(By.id(ID_HOME_DATA_LIMIT)).clear();
			driver.findElement(By.id(ID_HOME_DATA_LIMIT)).sendKeys(homeCountryDataLimitMb);
		}
		
		// insert homeCountryUnitLimit
		if (homeCountryUnitLimit != null) {
			driver.findElement(By.id(ID_HOME_UNIT_LIMIT)).clear();
			driver.findElement(By.id(ID_HOME_UNIT_LIMIT)).sendKeys(homeCountryUnitLimit);
		}
		
		// insert activationFee
		if (activationFee != null) {
			driver.findElement(By.id(ID_ACTIVATION_FEE)).clear();
			driver.findElement(By.id(ID_ACTIVATION_FEE)).sendKeys(activationFee);
		}
		
		// select planPeriodDays
		if (planPeriodDays != null) {
			persistUtil.selectByVisibleText(ID_PERIOD, planPeriodDays);
		}

		// insert serviceFee
		if (serviceFee != null) {
			driver.findElement(By.id(ID_SERVICE_FEE)).clear();
			driver.findElement(By.id(ID_SERVICE_FEE)).sendKeys(serviceFee);
		}

		// insert subscriptionFee
		if (subscriptionFee != null) {
			driver.findElement(By.id(ID_SUBSCRIPTION_FEE)).clear();
			driver.findElement(By.id(ID_SUBSCRIPTION_FEE)).sendKeys(subscriptionFee);
		}
		
		// insert planPeriodRepetition
		if (planPeriodRepetition != null) {
			driver.findElement(By.id(ID_PERIOD_REPETITION)).clear();
			driver.findElement(By.id(ID_PERIOD_REPETITION)).sendKeys(planPeriodRepetition);
		}
		
		// insert overuseAdditionalCalls
		if (overuseAdditionalCalls != null) {
			driver.findElement(By.id(ID_OVERUSE_ADDITIONAL_MINUTES)).clear();
			driver.findElement(By.id(ID_OVERUSE_ADDITIONAL_MINUTES)).sendKeys(overuseAdditionalCalls);
		}
		
		// insert overuseCallsFee
		if (overuseCallsFee != null) {
			driver.findElement(By.id(ID_OVERUSE_MINUTES_FEE)).clear();
			driver.findElement(By.id(ID_OVERUSE_MINUTES_FEE)).sendKeys(overuseCallsFee);
		}
		
		// insert overuseAdditionalData
		if (overuseAdditionalData != null) {
			driver.findElement(By.id(ID_OVERUSE_ADDITIONAL_MB)).clear();
			driver.findElement(By.id(ID_OVERUSE_ADDITIONAL_MB)).sendKeys(overuseAdditionalData);
		}
		
		// insert overuseDataFee
		if (overuseDataFee != null) {
			driver.findElement(By.id(ID_OVERUSE_MB_FEE)).clear();
			driver.findElement(By.id(ID_OVERUSE_MB_FEE)).sendKeys(overuseDataFee);
		}
		
		// insert overuseAdditionalUnit
		if (overuseAdditionalUnit != null) {
			driver.findElement(By.id(ID_OVERUSE_ADDITIONAL_UNITS)).clear();
			driver.findElement(By.id(ID_OVERUSE_ADDITIONAL_UNITS)).sendKeys(overuseAdditionalUnit);
		}
		
		// insert overuseUnitsFee
		if (overuseUnitsFee != null) {
			driver.findElement(By.id(ID_OVERUSE_UNITS_FEE)).clear();
			driver.findElement(By.id(ID_OVERUSE_UNITS_FEE)).sendKeys(overuseUnitsFee);
		}
		
		// insert homeCountryOveruseAdditionalCalls
		if (homeCountryOveruseAdditionalCalls != null) {
			driver.findElement(By.id(ID_HOME_OVERUSE_ADDITIONAL_MINUTES)).clear();
			driver.findElement(By.id(ID_HOME_OVERUSE_ADDITIONAL_MINUTES)).sendKeys(homeCountryOveruseAdditionalCalls);
		}
		
		// insert homeCountryOveruseCallsFee
		if (homeCountryOveruseCallsFee != null) {
			driver.findElement(By.id(ID_HOME_OVERUSE_MINUTES_FEE)).clear();
			driver.findElement(By.id(ID_HOME_OVERUSE_MINUTES_FEE)).sendKeys(homeCountryOveruseCallsFee);
		}
		
		// insert homeCountryOveruseAdditionalData
		if (homeCountryOveruseAdditionalData != null) {
			driver.findElement(By.id(ID_HOME_OVERUSE_ADDITIONAL_MB)).clear();
			driver.findElement(By.id(ID_HOME_OVERUSE_ADDITIONAL_MB)).sendKeys(homeCountryOveruseAdditionalData);
		}
		
		// insert homeCountryOveruseDataFee
		if (homeCountryOveruseDataFee != null) {
			driver.findElement(By.id(ID_HOME_OVERUSE_MB_FEE)).clear();
			driver.findElement(By.id(ID_HOME_OVERUSE_MB_FEE)).sendKeys(homeCountryOveruseDataFee);
		}
		
		// insert homeCountryOveruseAdditionalUnits
		if (homeCountryOveruseAdditionalUnits != null) {
			driver.findElement(By.id(ID_HOME_OVERUSE_ADDITIONAL_UNITS)).clear();
			driver.findElement(By.id(ID_HOME_OVERUSE_ADDITIONAL_UNITS)).sendKeys(homeCountryOveruseAdditionalUnits);
		}
		
		// insert homeCountryOveruseUnitsFee
		if (homeCountryOveruseUnitsFee != null) {
			driver.findElement(By.id(ID_HOME_OVERUSE_UNITS_FEE)).clear();
			driver.findElement(By.id(ID_HOME_OVERUSE_UNITS_FEE)).sendKeys(homeCountryOveruseUnitsFee);
		}
		
		// select chargeCurrency
		if (chargeCurrency != null) {
			persistUtil.selectByVisibleText(ID_CURRENCY, chargeCurrency);
		}
		
		// select creator
		if (creator != null) {
			persistUtil.selectByVisibleText(ID_CREATOR, creator);
		}
		
		// select owner
		if (owner  != null) {
			persistUtil.selectByVisibleText(ID_OWNER, owner);
		}

		// save
		driver.findElement(By.name(NAME_SAVE)).click();

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	/**
	 * Delete plan by name
	 * 
	 * @param name
	 * @return String result message
	 * @throws PersistException
	 */
	public String deleteByName(String name) {
		// go to plan page
		select.selectUsagePlans();

		// find plan by name and click on it
		persistUtil.deleteByLinkTextName(name);

		// check and get the message from page
		return persistUtil.finalCheck();
	}

	public static void main(String[] args) {
		System.out.println(new Plans().deleteAll());//unitTesting();
	}

	public static void unitTesting() {
		Plans plans = new Plans();
		String name = "a";
		String newName = "aa";
		String callsLimitMinutes = "2";
		String dataLimitMb = "2";
		String unitLimit = "";
		String homeCountryCallsLimitMinutes = "2";
		String homeCountryDataLimitMb = "2";
		String homeCountryUnitLimit = "";
		String activationFee = "2";
		String planPeriodDays = "7";// 1 / 7/ Month
		String serviceFee = "2";
		String subscriptionFee = "2";
		String planPeriodRepetition = "2";
		String overuseAdditionalCalls = "2";
		String overuseCallsFee = "2";
		String overuseAdditionalData = "2";
		String overuseDataFee = "2";
		String overuseAdditionalUnit = "2";
		String overuseUnitsFee = "2";
		String homeCountryOveruseAdditionalCalls = "2";
		String homeCountryOveruseCallsFee = "2";
		String homeCountryOveruseAdditionalData = "2";
		String homeCountryOveruseDataFee = "2";
		String homeCountryOveruseAdditionalUnits = "2";
		String homeCountryOveruseUnitsFee = "2";
		String chargeCurrency = "EUR";
		String creator = "Simgo";
		String owner = "Simgo";
		
		System.out.println(plans.add(name, callsLimitMinutes, dataLimitMb,
							unitLimit, homeCountryCallsLimitMinutes,
							homeCountryDataLimitMb, homeCountryUnitLimit,
							activationFee, planPeriodDays, serviceFee,
							subscriptionFee, planPeriodRepetition,
							overuseAdditionalCalls, overuseCallsFee,
							overuseAdditionalData, overuseDataFee,
							overuseAdditionalUnit, overuseUnitsFee,
							homeCountryOveruseAdditionalCalls,
							homeCountryOveruseCallsFee,
							homeCountryOveruseAdditionalData,
							homeCountryOveruseDataFee,
							homeCountryOveruseAdditionalUnits,
							homeCountryOveruseUnitsFee, chargeCurrency,
							creator, owner));
		

		
		System.out.println(plans.add("b", "Simgo"));
		

		System.out.println(plans.modifyByName(name, newName,
							callsLimitMinutes, dataLimitMb, unitLimit,
							homeCountryCallsLimitMinutes,
							homeCountryDataLimitMb, homeCountryUnitLimit,
							activationFee, planPeriodDays, serviceFee,
							subscriptionFee, planPeriodRepetition,
							overuseAdditionalCalls, overuseCallsFee,
							overuseAdditionalData, overuseDataFee,
							overuseAdditionalUnit, overuseUnitsFee,
							homeCountryOveruseAdditionalCalls,
							homeCountryOveruseCallsFee,
							homeCountryOveruseAdditionalData,
							homeCountryOveruseDataFee,
							homeCountryOveruseAdditionalUnits,
							homeCountryOveruseUnitsFee, chargeCurrency,
							creator, owner));
		

	
			System.out.println(plans.deleteByName(newName) + "\n"
					+ plans.deleteByName("b"));
			plans.driver.close();
		
		plans.driver.close();
	}
	
	/**
	 * Delete All plans
	 */
	public String deleteAll(){
		select.selectUsagePlans();
		return persistUtil.deleteAllFromTablePage();	
	}	
}