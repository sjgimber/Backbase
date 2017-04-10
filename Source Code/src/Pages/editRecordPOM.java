package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import HelperFunctions.*;

public class editRecordPOM 
{
	WebDriver driver;

	@FindBy(xpath="//input[@type='submit' and @value='Delete this computer']") WebElement btnDelete;	// <Create this computer> button.
	@FindBy(xpath="//input[@type='submit' and @value='Save this computer']") WebElement btnSave;		// <Save this computer> button.
	@FindBy(xpath=".//*[@id='main']/form[1]/div/a") WebElement btnCancel;								// <Cancel> button.
	@FindBy(xpath=".//*[@id='main']/h1") WebElement lblPageTitle;										// The page title.
	
	@FindBy(id="name") WebElement txtComputerName;														// "Computer name" field.
	@FindBy(xpath="//*[@id='name']/../..") WebElement divComputerName;									// "Computer name" field container (used for validation checks).
	@FindBy(id="introduced") WebElement txtIntroducedDate;												// "Introduced date" field.
	@FindBy(xpath="//*[@id='introduced']/../..") WebElement divIntroducedDate;							// "Introduced Date" field container (used for validation checks).	
	@FindBy(id="discontinued") WebElement txtDiscontinuedDate;											// "Discontinued date" field.
	@FindBy(xpath="//*[@id='discontinued']/../..") WebElement divDiscontinuedDate;						// "Introduced Date" field container (used for validation checks).
	@FindBy(id="company") WebElement listCompany;														// "Company name" drop-down list.

	public editRecordPOM(WebDriver driver)
	{
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void setCompanyName(String companyName)
	{
		// Setting the "Company name" listbox selection.
		// Can only select an item from the list if a valid value is presented.
		if (companyName.isEmpty())
		{
			companyName = "-- Choose a company --";
		}
		Select companyList = new Select(listCompany);
		companyList.selectByVisibleText(companyName);
	}
	
	public String getCompanyName()
	{
		// Setting the "Company name" listbox selection.
		Select companyList = new Select(listCompany);
		return companyList.getFirstSelectedOption().getText();
	}	
	
	public void clickDelete()
	{
		// Click on the <Delete this computer> button.
		btnDelete.click();
	}

	public void clickSave()
	{
		// Click on the <Save this computer> button.
		btnSave.click();
	}
	
	public void clickCancel()
	{
		// Click on the <Cancel> button.
		btnCancel.click();
	}
	
	public void enterRecordDetails(String computerName, String introducedDate, String discontinuedDate, String companyName)
	{
		// Populate the "edit computer" fields with a specific set of data.
		System.out.println("Updating record details: (Computer: " + computerName + ", introduced: " + introducedDate + ", discontinued: " + discontinuedDate + ", company: " + companyName + ")... ");
		txtComputerName.clear();
		txtComputerName.sendKeys(computerName);
		
		txtIntroducedDate.clear();
		txtIntroducedDate.sendKeys(introducedDate);
		
		txtDiscontinuedDate.clear();
		txtDiscontinuedDate.sendKeys(discontinuedDate);
		
		setCompanyName(companyName);
	}	
	
	public boolean verifyRecordDetails(String computerName, String introducedDate, String discontinuedDate, String companyName)
	{
		String thisComputerName = txtComputerName.getAttribute("value");
		String thisIntroducedDate = txtIntroducedDate.getAttribute("value");
		String thisDiscontinuedDate = txtDiscontinuedDate.getAttribute("value");
		String thisCompanyName = getCompanyName();
		
		System.out.println("Retrieving record details: (Computer: " + thisComputerName + ", introduced: " + thisIntroducedDate + ", discontinued: " + thisDiscontinuedDate + ", company: " + thisCompanyName + ")... ");
		
		boolean resultsMatch = true;
		if (!thisComputerName.contains(computerName))
		{
			resultsMatch = false;
		}
		
		if (!thisIntroducedDate.contains(introducedDate))
		{
			resultsMatch = false;
		}
		
		if (!thisDiscontinuedDate.contains(discontinuedDate))
		{
			resultsMatch = false;
		}
		
		if (!thisCompanyName.contains(companyName))
		{
			resultsMatch = false;
		}

		// Report on whether the found details match the expected ones.
		if (resultsMatch)
		{
			System.out.println("\tRetrieved details match the requested ones.");
		}
		else
		{
			System.out.println("\tRetrieved details MISMATCH.  FAIL.");

		}
		return resultsMatch;
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
