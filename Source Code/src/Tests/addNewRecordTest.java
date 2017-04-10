package Tests;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import Pages.*;
import HelperFunctions.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class addNewRecordTest 
{
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
	// List of invalid date values to use for validation checking of each date field.
    String[] invalidDates =	{"2017", "2017-02-29", "invalidvalue"};
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception 
	{
		System.setProperty("webdriver.chrome.driver", "/home/john/eclipse/chromedriver");
		driver = new ChromeDriver();
		
		baseUrl = "http://computer-database.herokuapp.com/computers";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@After
	public void tearDown() throws Exception 
	{
		driver.close();
	}

	@Test
	public void test01_01_CreateRecord_AddValidRecord() throws InterruptedException
	{
		String[] testInfo =	{
				"Test 1.1: Create Record: Create Valid Record",
				"Summary: This test verifies that a record can be added."};
	
		utilsTestRecords utils = new utilsTestRecords(driver);
		utils.logTestStart(testInfo);
	    driver.get(baseUrl);
	    databasePOM dbPage = new databasePOM(driver);
	    addNewRecordPOM addNewRecordPage = new addNewRecordPOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);
	    
	    // Test values:
	    String testID 			= "SJG Test 1.1";
	    String introducedDate 	= "2017-04-01";
	    String discontinuedDate = "2017-04-04";
	    String companyName 		= "Amstrad";
	    
		// Ensure a clean slate.
		utils.deleteTestRecord(testID);
		
	    // Create a new record.
		utils.createTestRecord(testID, introducedDate, discontinuedDate, companyName);
	    
	    // Verify created record contents.
	    assertTrue(utils.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
	    utils.logTestEnd();
	}

	@Test
	public void test01_02_CreateRecord_AddEmptyRecord()
	{
		String[] testInfo =	{
				"Test 1.2: Create Record: Create Empty Record",
				"Summary: This test verifies that an empty record cannot be created."};

		utilsTestRecords utils = new utilsTestRecords(driver);
		utils.logTestStart(testInfo);
		driver.get(baseUrl);
	    databasePOM dbPage = new databasePOM(driver);
	    addNewRecordPOM addNewRecordPage = new addNewRecordPOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);
	    
	    // Test values:
	    String testID 			= "SJG Test 1.2";
	    String computerName		= "";
	    String introducedDate 	= "";
	    String discontinuedDate = "";
	    String companyName 		= "";

		// Ensure a clean slate.
		utils.deleteTestRecord(testID);
		
	    // Request a new record.
	    dbPage.clickAdd();
	    assertTrue(addNewRecordPage.verifyScreenShown());
	    
	    // Enter record details.
		addNewRecordPage.enterRecordDetails(computerName, introducedDate, discontinuedDate, companyName);
	    
		// Create the record and verify success.
		addNewRecordPage.clickCreate();
		
		System.out.print("Checking for validation error on the 'Computer Name' field...");
		if (addNewRecordPage.isValidationErrorShown_ComputerName())
		{
			System.out.println("PASS");
		}
		else
		{
			System.out.println("FAIL");
		}
		assertTrue(addNewRecordPage.isValidationErrorShown_ComputerName());
	    utils.logTestEnd();
	}

	@Test
	public void test01_03_CreateRecord_InvalidIntroducedDate()
	{
		String[] testInfo =	{
				"Test 1.3: Create Record: Create Invalid Record (Invalid “Introduced Date”)",
				"Summary: This test verifies that an invalid record cannot be created (invalid “Introduced Date” value)."};
	
		utilsTestRecords utils = new utilsTestRecords(driver);
		utils.logTestStart(testInfo);
	    driver.get(baseUrl);
	    databasePOM dbPage = new databasePOM(driver);
	    addNewRecordPOM addNewRecordPage = new addNewRecordPOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);
	    
	    // Test values:
	    String testID 			= "SJG Test 1.3";
	    String computerName		= "";
	    String introducedDate 	= "";
	    String discontinuedDate = "";
	    String companyName 		= "";

		// Ensure a clean slate.
		utils.deleteTestRecord(testID);
		
	    // Cycle through a list of invalid date values.
    	// For each invalid date...
	    System.out.println("Validating 'Introduced Date' field...");
	    for (int i = 0; i < invalidDates.length; i++)
	    {
		    // Request a new record.
		    dbPage.clickAdd();
		    assertTrue(addNewRecordPage.verifyScreenShown());
		    
	    	introducedDate = invalidDates[i];
	    	
	    	// Enter record details.
	    	addNewRecordPage.enterRecordDetails(testID, introducedDate, discontinuedDate, companyName);
	    	
			// Create the record and verify date field failure..
			addNewRecordPage.clickCreate();

			System.out.println("\tChecking for validation error on the 'Introduced Date' field... (" + introducedDate + ")");
			boolean errorShown = addNewRecordPage.isValidationErrorShown_IntroducedDate(); 
			if (errorShown)
			{
				System.out.println("\tPASS");
			}
			else
			{
				System.out.println("\tFAIL");
			}
			assertTrue(errorShown);
			
			// Return to the DB List page, to ensure that the field validations are reset.
			editPage.clickCancel();
		}
	    utils.logTestEnd();
	}

	@Test
	public void test01_04_CreateRecord_InvalidDiscontinuedDate()
	{
		String[] testInfo =	{
				"Test 1.4: Create Record: Create Invalid Record (Invalid “Discontinued Date”)",
				"Summary: This test verifies that an invalid record cannot be created (invalid “Discontinued Date” value)."};
	
		utilsTestRecords utils = new utilsTestRecords(driver);
		utils.logTestStart(testInfo);
	    driver.get(baseUrl);
	    databasePOM dbPage = new databasePOM(driver);
	    addNewRecordPOM addNewRecordPage = new addNewRecordPOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);
	    
	    // Test values:
	    String testID 			= "SJG Test 1.4";
	    String computerName		= "";
	    String introducedDate 	= "";
	    String discontinuedDate = "";
	    String companyName 		= "";

		// Ensure a clean slate.
		utils.deleteTestRecord(testID);
		
	    // Cycle through a list of invalid date values.
    	// For each invalid date...
	    System.out.println("Validating 'Discontinued Date' field...");
	    for (int i = 0; i < invalidDates.length; i++)
	    {
		    // Request a new record.
		    dbPage.clickAdd();
		    assertTrue(addNewRecordPage.verifyScreenShown());
		    
	    	discontinuedDate = invalidDates[i];
	    	
	    	// Enter record details.
	    	addNewRecordPage.enterRecordDetails(testID, introducedDate, discontinuedDate, companyName);
	    	
			// Create the record and verify date field failure..
			addNewRecordPage.clickCreate();

			System.out.println("\tChecking for validation error on the 'Discontinued Date' field... (" + discontinuedDate + ")");
			boolean errorShown = addNewRecordPage.isValidationErrorShown_DiscontinuedDate(); 
			if (errorShown)
			{
				System.out.println("\tPASS");
			}
			else
			{
				System.out.println("\tFAIL");
			}
			assertTrue(errorShown);
			
			// Return to the DB List page, to ensure that the field validations are reset.
			editPage.clickCancel();
		}
	    utils.logTestEnd();
	}
	
	@Test
	public void test01_05_CreateInvalidRecord_InvalidDateOrder()
	{
		String[] testInfo =	{
				"Test 1.5: Create Record: Create Invalid Record (“Introduced Date” > “Discontinued Date”)",
				"Summary: This test verifies that an invalid record cannot be created (where the “Introduced Date” is greater than the “Discontinued Date” value)."};
		
		utilsTestRecords utils = new utilsTestRecords(driver);
		utils.logTestStart(testInfo);
		driver.get(baseUrl);
	    databasePOM dbPage = new databasePOM(driver);
	    addNewRecordPOM addNewRecordPage = new addNewRecordPOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);
	    
	    // Test values:
	    String testID 			= "SJG Test 1.5";
	    String introducedDate 	= "2017-04-04";			
	    String discontinuedDate = "2017-04-01";
	    String companyName 		= "";

		// Ensure a clean slate.
		utils.deleteTestRecord(testID);
		
	    // Request a new record.
	    dbPage.clickAdd();
	    assertTrue(addNewRecordPage.verifyScreenShown());
	    
	    // Enter record details.
		addNewRecordPage.enterRecordDetails(testID, introducedDate, discontinuedDate, companyName);
	    
		// Create the record and verify validation failure (as the Introduced date is later than the Discontinued date).
		addNewRecordPage.clickCreate();
		
		System.out.println("Checking for validation error on the date order (Introduced date is later than the Discontinued date)...");
		if (addNewRecordPage.isValidationErrorShown_IntroducedDate())
		{
			System.out.println("\tPASS");
		}
		else
		{
			System.out.println("\tFAIL");
		}
		assertTrue(addNewRecordPage.isValidationErrorShown_IntroducedDate());

		System.out.println("Checking for validation error on the date order (Introduced date is later than the Discontinued date)...");
		if (addNewRecordPage.isValidationErrorShown_DiscontinuedDate())
		{
			System.out.println("\tPASS");
		}
		else
		{
			System.out.println("\tFAIL");
		}
		assertFalse(dbPage.verifyRecordAddedMessageShown(testID));
		assertTrue(addNewRecordPage.isValidationErrorShown_DiscontinuedDate());
		utils.logTestEnd();
	}

	@Test
	public void test01_06_CreateRecord_DangerousCharacersInName()
	{
		String[] testInfo =	{
				"Test 1.6: Create Record: Dangerous Characters In Name",
				"Summary: This test verifies that a valid record can be created (where the “Computer Name” contains dangerous characters (“<b> & ; ‘ “”))."};
		
		utilsTestRecords utils = new utilsTestRecords(driver);
		utils.logTestStart(testInfo);
	    driver.get(baseUrl);
	    databasePOM dbPage = new databasePOM(driver);
	    addNewRecordPOM addNewRecordPage = new addNewRecordPOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);
	    	    
	    // Test values:
	    String testID 			= "SJG Test 1.6 <b> & ; ' \"";
	    String introducedDate 	= "2016-02-29";
	    String discontinuedDate = "2017-04-04";
	    String companyName 		= "Amstrad";
	    
		// Ensure a clean slate.
		utils.deleteTestRecord(testID);
		
	    // Request a new record.
		utils.createTestRecord(testID, introducedDate, discontinuedDate, companyName);
		
	    // Verify created record contents.
	    assertTrue(utils.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
		utils.logTestEnd();
	}

	@Test
	public void test01_07_CreateInvalidRecord_DuplicateRecord()
	{
		String[] testInfo =	{
				"Test 1.7: Create Record: Create Duplicate Record",
				"Summary: This test verifies that an attempt to create a duplicate record is handled."};
		
		utilsTestRecords utils = new utilsTestRecords(driver);
		utils.logTestStart(testInfo);
	    driver.get(baseUrl);
	    databasePOM dbPage = new databasePOM(driver);
	    addNewRecordPOM addNewRecordPage = new addNewRecordPOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);
	    
	    // Test values:
	    String testID 			= "SJG Test 1.7";
	    String introducedDate 	= "2017-04-04";			
	    String discontinuedDate = "2017-04-01";
	    String companyName 		= "";

		// Ensure a clean slate.
		utils.deleteTestRecord(testID);
		
	    // Create a new base record.
		utils.createTestRecord(testID, introducedDate, discontinuedDate, companyName);
		
		// Now create a duplicate record.
	    dbPage.clickAdd();
	    assertTrue(addNewRecordPage.verifyScreenShown());
	    
	    // Enter record details.
		addNewRecordPage.enterRecordDetails(testID, introducedDate, discontinuedDate, companyName);
	    
		// Attempt to create the record (it should be blocked).
		addNewRecordPage.clickCreate();

		// TODO: Functionality here is unclear - the app currently allows for multiple duplicate records to be created,
		// but this doesn't make sense.  Verification of expected functionality is required.
		assertFalse(dbPage.verifyRecordAddedMessageShown(testID));
		utils.logTestEnd();
	}
	
	@Test
	public void test01_08_CreateRecord_CancelCreation() throws InterruptedException
	{
		String[] testInfo =	{
				"1.8: Create Record: Cancel Record Creation",
				"Summary: This test verifies that an attempt to create a duplicate record can be cancelled."};
		
		utilsTestRecords utils = new utilsTestRecords(driver);
		utils.logTestStart(testInfo);
	    driver.get(baseUrl);
	    databasePOM dbPage = new databasePOM(driver);
	    addNewRecordPOM addNewRecordPage = new addNewRecordPOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);
	    
	    // Test values:
	    String testID 			= "SJG Test 1.8";
	    String introducedDate 	= "2017-04-01";
	    String discontinuedDate = "2017-04-04";
	    String companyName 		= "";
		
	    // Ensure a clean slate.
		utils.deleteTestRecord(testID);
		
	    // Request a new record.
	    dbPage.clickAdd();
	    assertTrue(addNewRecordPage.verifyScreenShown());
	    
	    // Enter record details.
		addNewRecordPage.enterRecordDetails(testID, introducedDate, discontinuedDate, companyName);
	    
		// Cancel the request.
		addNewRecordPage.clickCancel();

		// Verify no message is displayed.
		assertFalse(dbPage.verifyRecordAddedMessageShown(testID));
		
		// Verify that the record was not created.
		dbPage.searchForComputer(testID);
		assertTrue(dbPage.getMatchCount() == 0);
		utils.logTestEnd();
	}
}
