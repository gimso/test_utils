package selenium;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import testing_utils.TestOutput;

/**
 * 
 * @author Yehuda & Dana 
 *
 */
public interface DriverUtil {
	/**
	 * Select item found elementId and click by visible text, Surrounded with
	 * try and catch if element is not there.
	 * @param visibleText
	 * @param elementId
	 */
	public default void selectByVisibleText(String elementId, String visibleText) {
		try {
			new Select(getDriver().findElement(By.id(elementId))).selectByVisibleText(visibleText);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete in the global page (where all elements in table) A global method
	 * that after checking (selecting) an table columns id delete them
	 */
	public default void deleteSelectedItemsFromTable() {
		new Select(getDriver().findElement(By.name("action"))).selectByVisibleText("Delete selected");
		getDriver().findElement(By.name("index")).click();
	}

	/**
	 * delete an item when its on specific element page
	 */
	public default void deleteSelectedItemsFromElementPage() {
		try {
			getDriver().findElement(By.linkText("Delete")).click();
			String xpathImSure = "//input[@value=" + "\"Yes, I'm sure\"" + "]";
			getDriver().findElement(By.xpath(xpathImSure)).click();
		} catch (Exception e) {
			System.err.println("Couldn't Delete selected Item");
			e.printStackTrace();
		}
	}

	/**
	 * Delete inside component page (where all the details of items been)
	 * @param name
	 */
	public default void deleteByLinkTextName(String name) {
		try {
			getDriver().findElement(By.linkText(name)).click();
			deleteSelectedItemsFromElementPage();
		} catch (Exception e) {
			String errorMessage = "Couldn't Delete " + name + " Item";
			System.err.println(errorMessage);
			e.printStackTrace();
		}
	}

	// *******************************
	// Error Checking
	// ********************************

	/**
	 * A global method that check for all Error messages in the page if exist
	 * 
	 * @param message
	 * @return TestOutput
	 */
	public default TestOutput checkErrors(String message) {
		try {
			if ((getDriver().findElement(By.className("errornote"))) != null) {
				List<WebElement> elements = getDriver().findElements(By.xpath("//ul[@class='errorlist']/li"));
				String errorMessage = " - ";
				for (WebElement w : elements) {
					errorMessage = w.getText() + "\n";
				}

				System.err.println(message + " ," + errorMessage);
				return new TestOutput(false, message + " ," + errorMessage);
			}
		} catch (Exception e) {
		}

		return new TestOutput(true, "PASS");
	}

	/**
	 * Updating the TestOutput according to error's on the page if they exist
	 * 
	 * @param output
	 * @param pass
	 * @return TestOutput
	 */
	public default TestOutput finalCheck(String output, boolean pass) {
		if (!checkErrors(output).getOutput().equalsIgnoreCase("Pass")) {
			output = checkErrors(output).getOutput();
			pass = false;
		} else {
			output = getDriver().findElement(By.className("grp-info")).getText();
			pass = true;
		}
		return new TestOutput(pass, output);
	}

	public default String finalCheck() {
		String errorMessage = null;
		String outputMessage = null;
		try {
			if ((getDriver().findElement(By.className("errornote"))) != null) {
				List<WebElement> elements = getDriver().findElements(By.xpath("//ul[@class='errorlist']/li"));
				errorMessage = " - ";
				for (WebElement w : elements) {
					errorMessage += "\n" + w.getText();
				}
				return errorMessage;
			}
		} catch (Exception e) {
			/* if didn't find errorClass do Nothing */}
		try {
			if (getDriver().findElement(By.className("grp-info")) != null) {
				outputMessage = getDriver().findElement(By.className("grp-info")).getText();
			}
		} catch (Exception e) {
			/* if didn't find grpInfo do Nothing */}
		return outputMessage != null ? outputMessage : "";

	}

	/**
	 * 
	 * @return WebDriver instance
	 */
	public WebDriver getDriver();

	public void setDriver(WebDriver driver);

	/**
	 * Generic 'Delete all' from table</br>
	 * for every combination of delete, e.g. "delete all selected items", "delete fw versions" etc.</br>
	 * "are you sure" or "Go" buttons exist
	 */
	public default String deleteAllFromTablePage() {
		String selectAll = "//tr/th[1]/div/span/input";
		boolean exist = false;
		try {
			if (getDriver().findElement(By.xpath(selectAll)) != null) {
				exist = true;
				getDriver().findElement(By.xpath(selectAll)).click();
				Select selectOption = new Select(getDriver().findElement(By.name("action")));
				for (WebElement element : selectOption.getOptions()) {
					String textContent = element.getText().toLowerCase();
					if (textContent.contains("delete".toLowerCase())) {
						selectOption.selectByVisibleText(element.getText());
						break;
					}
				}
				//in case a "Go" button exists, click on it
				String nameGoButton = "index";
				if (getDriver().findElement(By.name(nameGoButton)) != null) {
					getDriver().findElement(By.name(nameGoButton)).click();
				}
				//in case an "I'm sure" button exists, click on it
				String xpathImSure = "//input[@value=\"Yes, I'm sure\"]";
				if (getDriver().findElement(By.xpath(xpathImSure)) != null) {
					getDriver().findElement(By.xpath(xpathImSure)).click();
				}
			}
		} catch (Exception e) {
			/* if cannot find elements skip */}

		if (exist) {
			return finalCheck();
		}
		return "The table is already empty";
	}

	/**
	 * Insert URL query
	 * 
	 * @param element
	 */
	public default void searchForElement(String element) {
		getDriver().get(getDriver().getCurrentUrl() + "?q=" + element);
	}

	/**
	 * Search all elements of headers by name and get the column index
	 * 
	 * @param coulmnName
	 * @return int index
	 */
	public default int getColumnIndexByHeaderName(String coulmnName) {

		int coulmnIndex = -1;
		List<WebElement> findElements = getDriver().findElements(By.xpath("//table/thead/tr/th"));
		int i = 0;
		for (WebElement element : findElements) {
			String text = element.getText();
			if (text.equals(coulmnName)) {
				coulmnIndex = i;
				break;
			}
			i++;
		}
		return coulmnIndex;

	}

	/**
	 * find the element by id, find if its not already checked, click if it is
	 * checked and need to uncheck, click also if it is unchecked and need to
	 * check.
	 * 
	 * @param id
	 * 
	 * @param check
	 */
	public default void clickCheckIfNeeded(String id, Boolean check) {
		WebElement allowedElement = getDriver().findElement(By.id(id));
		boolean isChecked = allowedElement.isSelected();
		if ((!isChecked && check) || (isChecked && !check)) {
			allowedElement.click();
		}
	}
}
