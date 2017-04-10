package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import HelperFunctions.utilsTestRecords;

public class addNewRecordPOM 
{
	WebDriver driver;

	@FindBy(xpath="//input[@type='submit' and @value='Create this computer']") WebElement btnCreate;	// <Create this computer> button.
	@FindBy(xpath=".//*[@id='main']/form[1]/div/a") WebElement btnCancel;								// <Cancel> button.
	@FindBy(xpath=".//*[@id='main']/h1") WebElement lblPageTitle;										// The page title.
	
	@FindBy(id="name") WebElement txtComputerName;														// "Computer name" field.
	@FindBy(xpath="//*[@id='name']/../..") WebElement divComputerName;									// "Computer name" field container (used for validation checks).
	@FindBy(id="introduced") WebElement txtIntroducedDate;												// "Introduced date" field.
	@FindBy(xpath="//*[@id='introduced']/../..") WebElement divIntroducedDate;							// "Introduced Date" field container (used for validation checks).	
	@FindBy(id="discontinued") WebElement txtDiscontinuedDate;											// "Discontinued date" field.
	@FindBy(xpath="//*[@id='discontinued']/../..") WebElement divDiscontinuedDate;						// "Introduced Date" field container (used for validation checks).
	@FindBy(id="company") WebElement listCompany;														// "Company name" drop-down list.
	
	public addNewRecordPOM(WebDriver driver)
	{
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public void clickCancel()
	{
		// Click on the <Cancel> button.
		btnCancel.click();
	}
	
	public void clickCreate()
	{
		// Click on the <Create this computer> button.
		btnCreate.click();
	}
	
	public void setCompanyName(String companyName)
	{
		// Setting the "Company name" listbox selection.
		if (!companyName.isEmpty())
		{
			// Can only select an item from the list if a valid value is presented.
			Select companyList = new Select(listCompany);
			companyList.selectByVisibleText(companyName);
		}
	}
	
	public void enterRecordDetails(String computerName, String introducedDate, String discontinuedDate, String companyName)
	{
		// Populate the "add a computer" fields with a specific set of data.
		System.out.println("Entering record details: (Computer: " + computerName + ", introduced: " + introducedDate + ", discontinued: " + discontinuedDate + ", company: " + companyName + ")... ");
		txtComputerName.sendKeys(computerName);
		txtIntroducedDate.sendKeys(introducedDate);
		txtDiscontinuedDate.sendKeys(discontinuedDate);
		setCompanyName(companyName);
	}
	
	
	
	public boolean verifyScreenShown()
	{
		// This verifies that the "Add a computer" screen is shown.
		// Presently, the URL is "/new" and the <h1> tag contains "Add a computer".
		String thisPageTitle = lblPageTitle.getText();
		String expectedPageTitle = "Add a computer";
		
		System.out.print("Checking this is the '" + expectedPageTitle + "' page... ");
		if (thisPageTitle.contains(expectedPageTitle))
		{
			// Correct page.
			System.out.println("OK");
			return true;
		}
		else
		{
			// Incorrect page OR (correct page, incorrect title).
			System.out.println("FAIL - detected page is '" + thisPageTitle + "'");
			return false;
		}			
	}

	public boolean isValidationErrorShown_ComputerName()
	{
		// Detects whether a validation error is shown for the Computer Name field.
		utilsTestRecords utils = new utilsTestRecords(driver);
		return utils.validationFailureShown(divComputerName);
	}
	
	public boolean isValidationErrorShown_IntroducedDate()
	{
		// Detects whether a validation error is shown for the "Introduced Date" field.
		utilsTestRecords utils = new utilsTestRecords(driver);
		return utils.validationFailureShown(divIntroducedDate);
	}
	
	public boolean isValidationErrorShown_DiscontinuedDate()
	{
		// Detects whether a validation error is shown for the "Discontinued Date" field.
		utilsTestRecords utils = new utilsTestRecords(driver);
		return utils.validationFailureShown(divDiscontinuedDate);
	}	
}


