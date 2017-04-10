package HelperFunctions;

import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import Pages.addNewRecordPOM;
import Pages.databasePOM;
import Pages.editRecordPOM;

public class utilsTestRecords 
{
	WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
	public utilsTestRecords(WebDriver driver)
	{
		this.driver = driver;
	}
	
	public boolean verifyRecordDetails(String computerName, String introducedDate, String discontinuedDate, String companyName)
	{
		// Helper function to locate a record, view it, and verify that the requested details are present.
		System.out.println("Verifying record details: (Computer: " + computerName + ", introduced: " + introducedDate + ", discontinued: " + discontinuedDate + ", company: " + companyName + ")... ");
		
		System.setProperty("webdriver.chrome.driver", "/home/john/eclipse/chromedriver");
		baseUrl = "http://computer-database.herokuapp.com/computers";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	    driver.get(baseUrl);

	    databasePOM dbPage = new databasePOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);
	    boolean verified = true;
	    
	    dbPage.searchForComputer(computerName);
	    if (dbPage.getMatchCount() == 0)
	    {
	    	System.out.println("\tRecord doesn't exist.");
	    	verified = false;
	    }
	    else
	    {
	    	dbPage.viewRecord(computerName);
	    	verified = editPage.verifyRecordDetails(computerName, introducedDate, discontinuedDate, companyName);
	    }
	    return verified;
	}
	
	public boolean createTestRecord(String computerName, String introducedDate, String discontinuedDate, String companyName)
	{
		// Helper function to create a new computer record, and verify that it has been created.
		System.out.println("Creating new test record: " + computerName);
		
		System.setProperty("webdriver.chrome.driver", "/home/john/eclipse/chromedriver");
		baseUrl = "http://computer-database.herokuapp.com/computers";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	    driver.get(baseUrl);

	    databasePOM dbPage = new databasePOM(driver);
	    addNewRecordPOM addNewRecordPage = new addNewRecordPOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);
	    
	    // Request a new record.
	    dbPage.clickAdd();
	    assertTrue(addNewRecordPage.verifyScreenShown());
	    
	    // Enter record details.
		addNewRecordPage.enterRecordDetails(computerName, introducedDate, discontinuedDate, companyName);
	    
		// Create the record and verify success.
		addNewRecordPage.clickCreate();

		return dbPage.verifyRecordAddedMessageShown(computerName);
	}
	
	public boolean deleteTestRecord(String computerName)
	{
		// Helper function to delete a new computer record, and verify that it has been deleted.
		System.out.println("Deleting test record: " + computerName);
		System.setProperty("webdriver.chrome.driver", "/home/john/eclipse/chromedriver");
		boolean allRecordsDeleted = true;
		
		baseUrl = "http://computer-database.herokuapp.com/computers";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
	    driver.get(baseUrl);
	    databasePOM dbPage = new databasePOM(driver);
	    addNewRecordPOM addNewRecordPage = new addNewRecordPOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);

	    // From the Database screen, search for and view the record.
	    // NOTE: There may be multiple instances of a search record with the given name.
	    dbPage.searchForComputer(computerName);
	    int recordCount = dbPage.getMatchCount();
	    System.out.println("\t" + recordCount + " matching records found for deletion...");
	    for (int i = 0; i < recordCount; i++)
	    {
	    	System.out.println("\tDeleting instance #" + (i + 1) + " of " + recordCount);
	    	// View and delete the record.
	    	dbPage.viewRecord(computerName);
	    	editPage.clickDelete();
	    	
	    	// Verify that it deleted.
	    	if (!dbPage.verifyRecordDeletedMessageShown())
	    	{
	    		allRecordsDeleted = false;
	    		break;
	    	}
	    	
	    	// Refresh the search list to show the remaining matches.
		    dbPage.searchForComputer(computerName);
	    }
	    
	    return allRecordsDeleted;
	}	
	
	public boolean validationFailureShown(WebElement element)
	{
		// Check the specified field, and determine if a validation error is displayed.
		// Currently used for date fields on the Add Record and Edit Record screens.
		
		if (element.getAttribute("class").contains("error"))
		{
			// The "clearfix error" class is being used for this field container.
			// Therefore, a validation error is being shown.
			// (The class is the only way to detect the error, as it changes the field background).
			System.out.println("\tValidation error detected.");
			return true;
		}
		else
		{
			// The standard "clearfix" class is being used for the field.
			System.out.println("\tValidation error not detected.");
			return false;
		}
	}
	
	public void logTestStart(String[] lines)
	{
		// Place info about the current test being initiated into the debug trace.
		System.out.println("--------------------------------------------------------------------------------");
		for (int i = 0; i < lines.length; i++)
		{
			System.out.println("- " + lines[i]);
		}
		System.out.println("-");

		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println("- Test started: " + timeStamp);
		System.out.println("--------------------------------------------------------------------------------");
	}
	
	public void logTestEnd()
	{
		System.out.println("--------------------------------------------------------------------------------");
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println("- Test completed: " + timeStamp);
		System.out.println("--------------------------------------------------------------------------------");
	}
}