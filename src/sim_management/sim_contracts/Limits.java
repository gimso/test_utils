package sim_management.sim_contracts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import selenium.SimDriverUtil;
import selenium.SimPageSelect;
/**
 * 
 * @author Dana
 *
 */
public class Limits {

	private static final String NAME_SAVE = "_save";
	private static final String CLASS_NAME_ADD = "addlink";
	private static final String ID_DURATION = "id_billing_duration";
	private static final String ID_BILLING_TYPE = "id_billing_type";
	private static final String ID_AUTO_TOPUP = "id_auto_topup";
	private static final String ID_DATA_PLAN = "id_data_plan";
	private static final String ID_CALLS_PLAN = "id_calls_plan";
	private static final String ID_SMS_PLAN = "id_sms_plan";
	private static final String ID_PRICE_DATA = "id_data_price_0";
	private static final String ID_PRICE_DATA_CURRENCY = "id_data_price_1";
	private static final String ID_PRICE_CALL = "id_calls_price_0";
	private static final String ID_PRICE_CALL_CURRENCY = "id_calls_price_1";
	private static final String ID_PRICE_SMS = "id_sms_price_0";
	private static final String ID_PRICE_SMS_CURRENCY = "id_sms_price_1";
	private static final String ID_PRICE_LINE = "id_line_price_0";
	private static final String ID_PRICE_LINE_CURRENCY = "id_line_price_1";
	private static final String ID_PRICE_DATA_OVERUSE = "id_data_overuse_0";
	private static final String ID_PRICE_DATA_OVERUSE_CURRENCY = "id_data_overuse_1";
	private static final String ID_PRICE_CALL_OVERUSE = "id_calls_overuse_0";
	private static final String ID_PRICE_CALL_OVERUSE_CURRENCY = "id_calls_overuse_1";
	private static final String ID_PRICE_SMS_OVERUSE = "id_sms_overuse_0";
	private static final String ID_PRICE_SMS_OVERUSE_CURRENCY = "id_sms_overuse_1";
	private static final String ID_PRICE_LINE_OVERUSE = "id_line_overuse_0";
	private static final String ID_PRICE_LINE_OVERUSE_CURRENCY = "id_line_overuse_1";
	private static final String ID_COMMENTS = "id_comments";
	private SimDriverUtil simDriverUtil;
	private SimPageSelect select;
	private WebDriver driver;

	public Limits() {
		simDriverUtil = SimDriverUtil.getInstance();
		this.select = simDriverUtil.getSelect();
		this.driver = simDriverUtil.getDriver();
	}

	/**
	 * Add a limit with default values
	 * 
	 * @param duration
	 * @param billingType
	 * @param dataPlan
	 * @param callsPlan
	 * @param smsPlan
	 * @return message
	 */
	public String add(
			String duration, 
			String billingType, 
			String dataPlan, 
			String callsPlan, 
			String smsPlan
			) {
		//go to limits page
		select.selectLimits();
		//click on add
		driver.findElement(By.className(CLASS_NAME_ADD)).click();
		//insert duration
		if (duration != null) {
			driver.findElement(By.id(ID_DURATION)).clear();
			driver.findElement(By.id(ID_DURATION)).sendKeys(duration);
		}
		//select billing type
		if (billingType != null) {
			simDriverUtil.selectByVisibleText(ID_BILLING_TYPE, billingType);
		}
		//insert data plan
		if (dataPlan != null) {
			driver.findElement(By.id(ID_DATA_PLAN)).clear();
			driver.findElement(By.id(ID_DATA_PLAN)).sendKeys(dataPlan);
		}
		//insert calls plan
		if (callsPlan != null) {
			driver.findElement(By.id(ID_CALLS_PLAN)).clear();
			driver.findElement(By.id(ID_CALLS_PLAN)).sendKeys(dataPlan);
		}
		//insert sms plan
		if (smsPlan != null) {
			driver.findElement(By.id(ID_SMS_PLAN)).clear();
			driver.findElement(By.id(ID_SMS_PLAN)).sendKeys(smsPlan);
		}
		//click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}

	/**
	 * Add limit with all values
	 * 
	 * @param duration
	 * @param billingType
	 * @param autoTopup
	 * @param dataPlan
	 * @param callsPlan
	 * @param smsPlan
	 * @param pricePerData
	 * @param pricePerDataCurrency
	 * @param pricePerCalls
	 * @param pricePerCallsCurrency
	 * @param pricePerSms
	 * @param pricePerSmsCurrency
	 * @param pricePerLine
	 * @param pricePerLineCurrency
	 * @param pricePerDataOveruse
	 * @param pricePerDataOveruseCurrency
	 * @param pricePerCallsOveruse
	 * @param pricePerCallsOveruseCurrency
	 * @param pricePerSmsOveruse
	 * @param pricePerSmsOveruseCurrency
	 * @param pricePerLineOveruse
	 * @param pricePerLineOveruseCurrency
	 * @param comment
	 * @return message
	 */
	public String add(
			String duration, 
			String billingType, 
			Boolean autoTopup, 
			String dataPlan, 
			String callsPlan,
			String smsPlan, 
			String pricePerData,
			String pricePerDataCurrency,
			String pricePerCalls,
			String pricePerCallsCurrency,
			String pricePerSms,
			String pricePerSmsCurrency,
			String pricePerLine,
			String pricePerLineCurrency,
			String pricePerDataOveruse,
			String pricePerDataOveruseCurrency,
			String pricePerCallsOveruse,
			String pricePerCallsOveruseCurrency,
			String pricePerSmsOveruse,
			String pricePerSmsOveruseCurrency,
			String pricePerLineOveruse,
			String pricePerLineOveruseCurrency,
			String comment
			) {
		//go to limits page
		select.selectLimits();
		//click on the name of the limit plan
		driver.findElement(By.className(CLASS_NAME_ADD)).click();
		//insert duration
		if (duration != null) {
			driver.findElement(By.id(ID_DURATION)).clear();
			driver.findElement(By.id(ID_DURATION)).sendKeys(duration);
		}
		//select billing type
		if (billingType != null) {
			simDriverUtil.selectByVisibleText(ID_BILLING_TYPE, billingType);
		}
		//check the auto topup is needed
		if (autoTopup != null && autoTopup) {
			driver.findElement(By.id(ID_AUTO_TOPUP)).click();
		}
		//insert data plan
		if (dataPlan != null) {
			driver.findElement(By.id(ID_DATA_PLAN)).clear();
			driver.findElement(By.id(ID_DATA_PLAN)).sendKeys(dataPlan);
		}
		//insert calls plan
		if (callsPlan != null) {
			driver.findElement(By.id(ID_CALLS_PLAN)).clear();
			driver.findElement(By.id(ID_CALLS_PLAN)).sendKeys(dataPlan);
		}
		//insert sms plan
		if (smsPlan != null) {
			driver.findElement(By.id(ID_SMS_PLAN)).clear();
			driver.findElement(By.id(ID_SMS_PLAN)).sendKeys(smsPlan);
		}
		//insert price per data
		if (pricePerData != null) {
			driver.findElement(By.id(ID_PRICE_DATA)).clear();
			driver.findElement(By.id(ID_PRICE_DATA)).sendKeys(pricePerData);
		}
		//select currency from list
		if (pricePerDataCurrency != null) {
			simDriverUtil.selectByVisibleText(ID_PRICE_DATA_CURRENCY, pricePerDataCurrency);
		}
		//insert price per calls
		if (pricePerCalls != null) {
			driver.findElement(By.id(ID_PRICE_CALL)).clear();
			driver.findElement(By.id(ID_PRICE_CALL)).sendKeys(pricePerCalls);
		}
		//select currency from list
		if (pricePerCallsCurrency != null) {
			simDriverUtil.selectByVisibleText(ID_PRICE_CALL_CURRENCY, pricePerCallsCurrency);
		}
		//insert price per sms
		if (pricePerSms != null) {
			driver.findElement(By.id(ID_PRICE_SMS)).clear();
			driver.findElement(By.id(ID_PRICE_SMS)).sendKeys(pricePerSms);
		}
		//select currency from list
		if (pricePerSmsCurrency != null) {
			simDriverUtil.selectByVisibleText(ID_PRICE_SMS_CURRENCY, pricePerSmsCurrency);
		}
		//insert price per line (a fixed price that may/may not include other prices - data, calls, sms).
		if (pricePerLine != null) {
			driver.findElement(By.id(ID_PRICE_LINE)).clear();
			driver.findElement(By.id(ID_PRICE_LINE)).sendKeys(pricePerLine);
		}
		//select currency from list
		if (pricePerLineCurrency != null) {
			simDriverUtil.selectByVisibleText(ID_PRICE_LINE_CURRENCY, pricePerLineCurrency);
		}
		//insert price per data overuse
		if (pricePerDataOveruse != null) {
			driver.findElement(By.id(ID_PRICE_DATA_OVERUSE)).clear();
			driver.findElement(By.id(ID_PRICE_DATA_OVERUSE)).sendKeys(pricePerDataOveruse);
		}
		//select currency from list
		if (pricePerDataOveruseCurrency != null) {
			simDriverUtil.selectByVisibleText(ID_PRICE_DATA_OVERUSE_CURRENCY, pricePerDataOveruseCurrency);
		}
		//insert price per calls overuse
		if (pricePerCallsOveruse != null) {
			driver.findElement(By.id(ID_PRICE_CALL_OVERUSE)).clear();
			driver.findElement(By.id(ID_PRICE_CALL_OVERUSE)).sendKeys(pricePerCallsOveruse);
		}
		//select currency from list
		if (pricePerCallsOveruseCurrency != null) {
			simDriverUtil.selectByVisibleText(ID_PRICE_CALL_OVERUSE_CURRENCY, pricePerCallsOveruseCurrency);
		}
		//insert price per sms overuse
		if (pricePerSmsOveruse != null) {
			driver.findElement(By.id(ID_PRICE_SMS_OVERUSE)).clear();
			driver.findElement(By.id(ID_PRICE_SMS_OVERUSE)).sendKeys(pricePerSmsOveruse);
		}
		//select currency from list
		if (pricePerSmsOveruseCurrency != null) {
			simDriverUtil.selectByVisibleText(ID_PRICE_SMS_OVERUSE_CURRENCY, pricePerSmsOveruseCurrency);
		}
		//insert price per line overuse
		if (pricePerLineOveruse != null) {
			driver.findElement(By.id(ID_PRICE_LINE_OVERUSE)).clear();
			driver.findElement(By.id(ID_PRICE_LINE_OVERUSE)).sendKeys(pricePerLineOveruse);
		}
		//select currency from list
		if (pricePerLineOveruseCurrency != null) {
			simDriverUtil.selectByVisibleText(ID_PRICE_LINE_OVERUSE_CURRENCY, pricePerLineOveruseCurrency);
		}
		//insert a comment
		if (comment != null) {
			driver.findElement(By.id(ID_COMMENTS)).clear();
			driver.findElement(By.id(ID_COMMENTS)).sendKeys(comment);
		}
		//click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}
	
	/**
	 * Modify a limit
	 * 
	 * @param name
	 * @param newDuration
	 * @param newBillingType
	 * @param newAutoTopup
	 * @param newDataPlan
	 * @param newCallsPlan
	 * @param newSmsPlan
	 * @param newPricePerData
	 * @param newPricePerDataCurrency
	 * @param newPricePerCalls
	 * @param newPricePerCallsCurrency
	 * @param newPricePerSms
	 * @param newPricePerSmsCurrency
	 * @param newPricePerLine
	 * @param newPricePerLineCurrency
	 * @param newPricePerDataOveruse
	 * @param newPricePerDataOveruseCurrency
	 * @param newPricePerCallsOveruse
	 * @param newPricePerCallsOveruseCurrency
	 * @param newPricePerSmsOveruse
	 * @param newPricePerSmsOveruseCurrency
	 * @param newPricePerLineOveruse
	 * @param newPricePerLineOveruseCurrency
	 * @param newComment
	 * @return message
	 */
	public String modifyByName(
			String name,
			String newDuration, 
			String newBillingType, 
			Boolean newAutoTopup, 
			String newDataPlan, 
			String newCallsPlan,
			String newSmsPlan, 
			String newPricePerData,
			String newPricePerDataCurrency,
			String newPricePerCalls,
			String newPricePerCallsCurrency,
			String newPricePerSms,
			String newPricePerSmsCurrency,
			String newPricePerLine,
			String newPricePerLineCurrency,
			String newPricePerDataOveruse,
			String newPricePerDataOveruseCurrency,
			String newPricePerCallsOveruse,
			String newPricePerCallsOveruseCurrency,
			String newPricePerSmsOveruse,
			String newPricePerSmsOveruseCurrency,
			String newPricePerLineOveruse,
			String newPricePerLineOveruseCurrency,
			String newComment
			) {
		//go to limits page
		select.selectLimits();
		//click on the limit name to change
		driver.findElement(By.linkText(name)).click();
		//insert duration
		if (newDuration != null) {
			driver.findElement(By.id(ID_DURATION)).clear();
			driver.findElement(By.id(ID_DURATION)).sendKeys(newDuration);
		}
		//select billing type
		if (newBillingType != null) {
			simDriverUtil.selectByVisibleText(ID_BILLING_TYPE, newBillingType);
		}
		//click on auto topup if needed
		if (newAutoTopup != null && !newAutoTopup) {
			driver.findElement(By.id(ID_AUTO_TOPUP)).click();
		}
		//insert new data plan
		if (newDataPlan != null) {
			driver.findElement(By.id(ID_DATA_PLAN)).clear();
			driver.findElement(By.id(ID_DATA_PLAN)).sendKeys(newDataPlan);
		}
		//insert calls plan
		if (newCallsPlan != null) {
			driver.findElement(By.id(ID_CALLS_PLAN)).clear();
			driver.findElement(By.id(ID_CALLS_PLAN)).sendKeys(newDataPlan);
		}
		//insert sms plan
		if (newSmsPlan != null) {
			driver.findElement(By.id(ID_SMS_PLAN)).clear();
			driver.findElement(By.id(ID_SMS_PLAN)).sendKeys(newSmsPlan);
		}
		//insert price per data
		if (newPricePerData != null) {
			driver.findElement(By.id(ID_PRICE_DATA)).clear();
			driver.findElement(By.id(ID_PRICE_DATA)).sendKeys(newPricePerData);
		}
		//select currency from list
		if (newPricePerDataCurrency != null) {
			simDriverUtil.selectByVisibleText(ID_PRICE_DATA_CURRENCY, newPricePerDataCurrency);
		}
		//insert price per calls
		if (newPricePerCalls != null) {
			driver.findElement(By.id(ID_PRICE_CALL)).clear();
			driver.findElement(By.id(ID_PRICE_CALL)).sendKeys(newPricePerCalls);
		}
		//select currency from list
		if (newPricePerCallsCurrency != null) {
			simDriverUtil.selectByVisibleText(ID_PRICE_CALL_CURRENCY, newPricePerCallsCurrency);
		}
		//insert price per sms
		if (newPricePerSms != null) {
			driver.findElement(By.id(ID_PRICE_SMS)).clear();
			driver.findElement(By.id(ID_PRICE_SMS)).sendKeys(newPricePerSms);
		}
		//select currency from list
		if (newPricePerSmsCurrency != null) {
			simDriverUtil.selectByVisibleText(ID_PRICE_SMS_CURRENCY, newPricePerSmsCurrency);
		}
		//insert price per line
		if (newPricePerLine != null) {
			driver.findElement(By.id(ID_PRICE_LINE)).clear();
			driver.findElement(By.id(ID_PRICE_LINE)).sendKeys(newPricePerLine);
		}
		//select currency from list
		if (newPricePerLineCurrency != null) {
			simDriverUtil.selectByVisibleText(ID_PRICE_LINE_CURRENCY, newPricePerLineCurrency);
		}
		//insert price per data overuse
		if (newPricePerDataOveruse != null) {
			driver.findElement(By.id(ID_PRICE_DATA_OVERUSE)).clear();
			driver.findElement(By.id(ID_PRICE_DATA_OVERUSE)).sendKeys(newPricePerDataOveruse);
		}
		//select currency from list
		if (newPricePerDataOveruseCurrency != null) {
			simDriverUtil.selectByVisibleText(ID_PRICE_DATA_OVERUSE_CURRENCY, newPricePerDataOveruseCurrency);
		}
		//insert price per calls overuse
		if (newPricePerCallsOveruse != null) {
			driver.findElement(By.id(ID_PRICE_CALL_OVERUSE)).clear();
			driver.findElement(By.id(ID_PRICE_CALL_OVERUSE)).sendKeys(newPricePerCallsOveruse);
		}
		//select currency from list
		if (newPricePerCallsOveruseCurrency != null) {
			simDriverUtil.selectByVisibleText(ID_PRICE_CALL_OVERUSE_CURRENCY, newPricePerCallsOveruseCurrency);
		}
		//insert price per sms overuse
		if (newPricePerSmsOveruse != null) {
			driver.findElement(By.id(ID_PRICE_SMS_OVERUSE)).clear();
			driver.findElement(By.id(ID_PRICE_SMS_OVERUSE)).sendKeys(newPricePerSmsOveruse);
		}
		//select currency from list
		if (newPricePerSmsOveruseCurrency != null) {
			simDriverUtil.selectByVisibleText(ID_PRICE_SMS_OVERUSE_CURRENCY, newPricePerSmsOveruseCurrency);
		}
		//insert price per line overuse
		if (newPricePerLineOveruse != null) {
			driver.findElement(By.id(ID_PRICE_LINE_OVERUSE)).clear();
			driver.findElement(By.id(ID_PRICE_LINE_OVERUSE)).sendKeys(newPricePerLineOveruse);
		}
		//select currency from list
		if (newPricePerLineOveruseCurrency != null) {
			simDriverUtil.selectByVisibleText(ID_PRICE_LINE_OVERUSE_CURRENCY, newPricePerLineOveruseCurrency);
		}
		//insert comment
		if (newComment != null) {
			driver.findElement(By.id(ID_COMMENTS)).clear();
			driver.findElement(By.id(ID_COMMENTS)).sendKeys(newComment);
		}
		//click on save
		driver.findElement(By.name(NAME_SAVE)).click();
		return simDriverUtil.finalCheck();
	}
	
	/**
	 * Delete limit by its name 
	 * 
	 * @param name
	 * @return message
	 */
	public String deleteByName(String name){
		select.selectLimits();
		simDriverUtil.deleteByLinkTextName(name);
		return simDriverUtil.finalCheck();
	}
	
	/**
	 * Delete all limits
	 * 
	 * @return message
	 */
	public String deleteAll(){
		select.selectLimits();
		simDriverUtil.deleteAllFromTablePage();
		return simDriverUtil.finalCheck();
	}
}
