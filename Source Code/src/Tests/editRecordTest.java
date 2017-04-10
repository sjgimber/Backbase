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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import Pages.*;
import HelperFunctions.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class editRecordTest 
{
	private WebDriver driver;		// Standard instance used for most tests.
	private WebDriver driver2;		// Extra instance used for concurrency tests.
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
	public void test03_01_UpdateRecord_UpdateValidRecord()
	{
		String[] testInfo =	{
				"Test 3.1: Update Record: Update Valid Record (Verify Changes)",
				"Summary: This test verifies that a record can be updated."};
		
		utilsTestRecords utils = new utilsTestRecords(driver);
		utils.logTestStart(testInfo);
	    driver.get(baseUrl);
	    databasePOM dbPage = new databasePOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);
	    
	    // Test values:
	    String testID 			= "SJG Test 3.1 v1";
	    String introducedDate 	= "2017-04-01";
	    String discontinuedDate = "2017-04-04";
	    String companyName 		= "Samsung Electronics";

	    // Update values:
	    String testID2				= "SJG Test 3.1 v2";
	    String introducedDate2		= "2016-05-12";
	    String discontinuedDate2 	= "2017-06-13";
	    String companyName2			= "";

	    // Ensure a clean slate.
		utils.deleteTestRecord(testID);
		utils.deleteTestRecord(testID2);

		// Create the record needed for the test.
		utils.createTestRecord(testID, introducedDate, discontinuedDate, companyName);

		// Verify details are as entered.
		dbPage.searchForComputer(testID);
	    dbPage.viewRecord(testID);
	    assertTrue(editPage.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
	    
	    // Update the details.
	    editPage.enterRecordDetails(testID2, introducedDate2, discontinuedDate2, companyName2);
	    editPage.clickSave();
	    
	    // Verify the successful save.
	    assertTrue(dbPage.verifyRecordUpdatedMessageShown(testID2));
	    
	    // Search for the original record in the DB list.  (It should no longer be present).
	    dbPage.searchForComputer(testID);
	    assert(dbPage.getMatchCount() == 0);	    
	    
	    // Search for the updated record in the list.  (It should be present, and contain the new details).
	    assertTrue(utils.verifyRecordDetails(testID2, introducedDate2, discontinuedDate2, companyName2));
	    utils.logTestEnd();
	}

	@Test
	public void test03_02_UpdateRecord_RemoveComputerName()
	{
		String[] testInfo =	{
				"Test 3.2: Update Record: Remove Computer Name",
				"Summary: This test verifies that a record cannot be updated when the mandatory computer name is blank."};
		
		utilsTestRecords utils = new utilsTestRecords(driver);
		utils.logTestStart(testInfo);
	    driver.get(baseUrl);
	    databasePOM dbPage = new databasePOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);
	    
	    // Test values:
	    String testID 			= "SJG Test 3.2";
	    String introducedDate 	= "2017-04-01";
	    String discontinuedDate = "2017-04-04";
	    String companyName 		= "Samsung Electronics";

	    // Ensure a clean slate.
		utils.deleteTestRecord(testID);

		// Create the record needed for the test.
		utils.createTestRecord(testID, introducedDate, discontinuedDate, companyName);

		// Verify details are as entered.
	    assertTrue(utils.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
	    
	    // Update the details.
	    System.out.println("Updating the record details to remove the computer name...");
	    editPage.enterRecordDetails("", introducedDate, discontinuedDate, companyName);
	    editPage.clickSave();

	    // Verify that a validation error is shown (as a record MUST have a "computer name").
	    assertTrue(editPage.isValidationErrorShown_ComputerName());
	    utils.logTestEnd();
	}
	
	@Test
	public void test03_03_UpdateRecord_DangerousCharactersInName()
	{
		String[] testInfo =	{
				"Test 3.3: Update Record (Dangerous name (containing “<b> & ; ‘ “”))",
				"Summary: This test verifies that a record can be updated so that the mandatory computer name contains dangerous characters."};
		
		utilsTestRecords utils = new utilsTestRecords(driver);
		utils.logTestStart(testInfo);
	    driver.get(baseUrl);
	    databasePOM dbPage = new databasePOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);
	    
	    // Test values:
	    String testID 			= "SJG Test 3.3 v1";
	    String testID2			= "SJG Test 3.3 <b> & ; ' \"";
	    String introducedDate 	= "2017-04-01";
	    String discontinuedDate = "2017-04-04";
	    String companyName 		= "Samsung Electronics";

	    // Ensure a clean slate.
		utils.deleteTestRecord(testID);

		// Create the record needed for the test.
		utils.createTestRecord(testID, introducedDate, discontinuedDate, companyName);

		// Verify details are as entered.
	    assertTrue(utils.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
	    
	    // Update the details.
	    System.out.println("Updating the record details to include dangerous characters in the computer name...");
	    editPage.enterRecordDetails(testID2, introducedDate, discontinuedDate, companyName);
	    editPage.clickSave();

	    // Verify that the record is saved.
	    dbPage.verifyRecordUpdatedMessageShown(testID2);
	    
	    // Check that the old record is no longer present.
	    assertFalse(utils.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
	    
	    // Check the presence / details of the udpated record. 
	    assertTrue(utils.verifyRecordDetails(testID2, introducedDate, discontinuedDate, companyName));
	    utils.logTestEnd();
	}

	@Test
	public void test03_04_UpdateRecord_InvalidIntroducedDate()
	{
		String[] testInfo =	{
				"Test 3.4: Update Record: Invalid “Introduced Date”",
				"Summary: This test verifies that a record can be updated so that the mandatory computer name contains dangerous characters."};
		
		utilsTestRecords utils = new utilsTestRecords(driver);
		utils.logTestStart(testInfo);
	    driver.get(baseUrl);
	    databasePOM dbPage = new databasePOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);
	    
	    // Test values:
	    String testID 			= "SJG Test 3.4";
	    String introducedDate 	= "2017-04-01";
	    String discontinuedDate = "2017-04-04";
	    String companyName 		= "Samsung Electronics";
	    String newDate 			= "";

	    // Ensure a clean slate.
		utils.deleteTestRecord(testID);

		// Create the record needed for the test.
		utils.createTestRecord(testID, introducedDate, discontinuedDate, companyName);

	    // Cycle through a list of invalid date values.
    	// For each invalid date...
	    System.out.println("Validating 'Introduced Date' field...");
	    for (int i = 0; i < invalidDates.length; i++)
	    {
			// Verify details are as entered.
		    assertTrue(utils.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
		    
	    	newDate = invalidDates[i];
	    	
	    	// Enter record details.
	    	editPage.enterRecordDetails(testID, newDate, discontinuedDate, companyName);
	    	
			// Create the record and verify date field failure..
			editPage.clickSave();

			System.out.println("\tChecking for validation error on the 'Introduced Date' field... (" + newDate + ")");
			boolean errorShown = editPage.isValidationErrorShown_IntroducedDate(); 
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
	    
	    // Check the presence / details of the unchanged record. 
	    assertTrue(utils.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
	    utils.logTestEnd();
	}

	@Test
	public void test03_05_UpdateRecord_InvalidDiscontinuedDate()
	{
		String[] testInfo =	{
				"Test 3.5: Update Record: Invalid “Discontinued Date”",
				"Summary: This test verifies that a record cannot be updated when the Discontinued Date is invalid."};
		
		utilsTestRecords utils = new utilsTestRecords(driver);
		utils.logTestStart(testInfo);
	    driver.get(baseUrl);
	    databasePOM dbPage = new databasePOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);
	    
	    // Test values:
	    String testID 			= "SJG Test 3.5";
	    String introducedDate 	= "2017-04-01";
	    String discontinuedDate = "2017-04-04";
	    String companyName 		= "Samsung Electronics";
	    String newDate 			= "";

	    // Ensure a clean slate.
		utils.deleteTestRecord(testID);

		// Create the record needed for the test.
		utils.createTestRecord(testID, introducedDate, discontinuedDate, companyName);

	    // Cycle through a list of invalid date values.
    	// For each invalid date...
	    System.out.println("Validating 'Discontinued Date' field...");
	    for (int i = 0; i < invalidDates.length; i++)
	    {
			// Verify details are as entered.
		    assertTrue(utils.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
		    
	    	newDate = invalidDates[i];
	    	
	    	// Enter record details.
	    	editPage.enterRecordDetails(testID, introducedDate, newDate, companyName);
	    	
			// Create the record and verify date field failure..
			editPage.clickSave();

			System.out.println("\tChecking for validation error on the 'Discontinued Date' field... (" + newDate + ")");
			boolean errorShown = editPage.isValidationErrorShown_DiscontinuedDate(); 
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
	    
	    // Check the presence / details of the unchanged record. 
	    assertTrue(utils.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
	    utils.logTestEnd();
	}

	@Test
	public void test03_06_UpdateRecord_InvalidDateOrder()
	{
		String[] testInfo =	{
				"Test 3.6: Update Record: “Introduced Date” > “Discontinued Date”",
				"Summary: This test verifies that a record cannot be updated when the Introduced Date comes after the Discontinued Date."};
		
		utilsTestRecords utils = new utilsTestRecords(driver);
		utils.logTestStart(testInfo);
	    driver.get(baseUrl);
	    databasePOM dbPage = new databasePOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);
	    
	    // Test values:
	    String testID 			= "SJG Test 3.6";
	    String introducedDate 	= "2017-04-01";
	    String discontinuedDate = "2017-04-04";
	    String companyName 		= "Samsung Electronics";

	    // Ensure a clean slate.
		utils.deleteTestRecord(testID);

		// Create the record needed for the test.
		utils.createTestRecord(testID, introducedDate, discontinuedDate, companyName);

		// Verify details are as entered.
	    assertTrue(utils.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
	    
	    // Update the details.
	    System.out.println("Checking for validation error on the date order (Introduced date is later than the Discontinued date)...");
	    editPage.enterRecordDetails(testID, discontinuedDate, introducedDate, companyName);
	    editPage.clickSave();

	    // A validation error should be shown.
	    // TODO: The site currently allows for records to be created with the dates in any order.  Functionality to be verified.
	    assertFalse(dbPage.verifyRecordUpdatedMessageShown(testID));
		assertTrue(editPage.isValidationErrorShown_DiscontinuedDate());
	    utils.logTestEnd();
	}
	
	@Test
	public void test03_07_UpdateRecord_UpdateNonExistentRecord()
	{
		String[] testInfo =	{
				"Test 3.7: Update Record: Update Non-Existent Record",
				"Summary: This test verifies that an attempt to update a non-existent record is handled correctly."};
		
		utilsTestRecords utils = new utilsTestRecords(driver);
		utils.logTestStart(testInfo);
	    driver.get(baseUrl);
	    databasePOM dbPage = new databasePOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);
	    
	    // Need an extra instance of the browser for concurrent testing.
		driver2 = new ChromeDriver();
		driver2.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		utilsTestRecords utils2 = new utilsTestRecords(driver2);
	    databasePOM dbPage2 	= new databasePOM(driver2);
	    editRecordPOM editPage2 = new editRecordPOM(driver2);
	    driver2.get(baseUrl);
	    
	    // Test values:
	    String testID 			= "SJG Test 3.7 v1";
	    String introducedDate 	= "2017-04-01";
	    String discontinuedDate = "2017-04-04";
	    String companyName 		= "Samsung Electronics";

	    // Update values:
	    String testID2				= "SJG Test 3.7 v2";
	    String introducedDate2		= "2016-05-12";
	    String discontinuedDate2 	= "2016-06-13";
	    String companyName2			= "";

	    // Browser #1
	    // Ensure a clean slate.
		utils.deleteTestRecord(testID);
		utils.deleteTestRecord(testID2);

		// Create the record needed for the test.
		utils.createTestRecord(testID, introducedDate, discontinuedDate, companyName);

		// Verify details are as entered.
	    assertTrue(utils.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
	    
	    // Browser #2
	    // Navigate to the record's screen.
	    assertTrue(utils2.verifyRecordDetails(testID, introducedDate2, discontinuedDate2, companyName2));
	    
	    // Browser #1
	    // Delete the record.
	    utils.deleteTestRecord(testID);
	    
	    // Browser #2
	    // Edit / save the non-existent record.
	    editPage2.enterRecordDetails(testID2, introducedDate2, discontinuedDate2, companyName2);
	    editPage2.clickSave();
	    
	    // TODO: Functionality is not clear at present.
	    // However, when updating the nonexistent record, an exception error is shown on the website.
	    // Causing a hard failure until the correct outcome can be defined and coded for.
	    driver2.close();
	    assert(false);
	    utils.logTestEnd();
	}

	@Test
	public void test03_08_UpdateRecord_CancelUpdate()
	{
		String[] testInfo =	{
				"Test 3.8: Update Record: Cancel Record Update",
				"Summary: This test verifies that a record update can be cancelled."};
		
		utilsTestRecords utils = new utilsTestRecords(driver);
		utils.logTestStart(testInfo);
	    driver.get(baseUrl);
	    databasePOM dbPage = new databasePOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);
	    
	    // Test values:
	    String testID 			= "SJG Test 3.8 v1";
	    String introducedDate 	= "";
	    String discontinuedDate = "";
	    String companyName 		= "";

	    // Update values:
	    String testID2				= "SJG Test 3.8 v2";
	    String introducedDate2		= "2017-04-01";
	    String discontinuedDate2 	= "2017-04-04";
	    String companyName2			= "Samsung Electronics";

	    // Ensure a clean slate.
		utils.deleteTestRecord(testID);
		utils.deleteTestRecord(testID2);

		// Create the record needed for the test.
		utils.createTestRecord(testID, introducedDate, discontinuedDate, companyName);

		// Verify details are as entered.
	    assertTrue(utils.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
	    
	    // Update the details.
	    editPage.enterRecordDetails(testID2, introducedDate2, discontinuedDate2, companyName2);
	    editPage.clickCancel();
	    
	    // Verify the save didn't happen.
	    assertFalse(dbPage.verifyRecordUpdatedMessageShown(testID2));
	    
	    // Search for the original record in the DB list.  (It should be present and contain the original details).
	    assertTrue(utils.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
	    
	    // Search for the updated record in the list.  (It should NOT be present as it was cancelled).
	    assertFalse(utils.verifyRecordDetails(testID2, introducedDate2, discontinuedDate2, companyName2));
	    utils.logTestEnd();
	}
}
