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

public class deleteRecordTest 
{
	private WebDriver driver;		// Standard instance used for most tests.
	private WebDriver driver2;		// Extra instance used for concurrency tests.
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

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
		baseUrl = "http://computer-database.herokuapp.com/computers";

		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@After
	public void tearDown() throws Exception 
	{
		driver.close();
	}

	@Test
	public void test04_01_DeleteRecord_DeleteValidRecord()
	{
		String[] testInfo =	{
				"Test 4.1: Delete Record: Delete Valid Record",
				"Summary: This test verifies that a record can be deleted."};
		
		utilsTestRecords utils = new utilsTestRecords(driver);
		utils.logTestStart(testInfo);

		String testID 			= "SJG Test 4.1";
		String introducedDate 	= "2017-04-01";
		String discontinuedDate = "2017-04-04";
		String companyName 		= "Amstrad";
				
		// Ensure a clean slate.
		utils.deleteTestRecord(testID);
		
		// Now create a single clean record.
		assertTrue(utils.createTestRecord(testID, introducedDate, discontinuedDate, companyName));
		
		// Delete the record.  Verify it has been deleted.
		assertTrue(utils.deleteTestRecord(testID));
		assertFalse(utils.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
	    utils.logTestEnd();
	}
	
	@Test
	public void test04_02_DeleteRecord_DeleteInvalidRecord()
	{
		String[] testInfo =	{
				"Test 4.2: Delete Record (Delete Invalid Record)",
				"Summary: This test verifies that an attempt to delete a non-existent record is handled correctly."};
		
		utilsTestRecords utils 	= new utilsTestRecords(driver);
		utils.logTestStart(testInfo);
	    databasePOM dbPage 		= new databasePOM(driver);
	    editRecordPOM editPage 	= new editRecordPOM(driver);

	    // Need an extra instance of the browser for concurrent testing.
		driver2 = new ChromeDriver();
		driver2.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		utilsTestRecords utils2 = new utilsTestRecords(driver2);
	    databasePOM dbPage2 	= new databasePOM(driver2);
	    editRecordPOM editPage2 = new editRecordPOM(driver2);
		
		String testID 			= "SJG Test 4.2";
		String introducedDate 	= "";
		String discontinuedDate = "";
		String companyName 		= "";
				
		System.out.println("Browser #1: Setup");
		// Browser #1:
		// Ensure a clean slate.
		utils.deleteTestRecord(testID);

		// Now create a single clean record.
		assertTrue(utils.createTestRecord(testID, introducedDate, discontinuedDate, companyName));
		
		// Navigate to the created record.
		assertTrue(utils.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
		
		// Browser #2:
		// Navigate to the created record.
		assertTrue(utils2.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
		
		// Browser #1: Delete the record.
		System.out.println("Browser #1: Delete record...");
		assertTrue(utils.deleteTestRecord(testID));
		
		// Browser #2: Attempt to delete the non-existent record.
		// Delete the record.  Verify it has been deleted.
		System.out.println("Browser #2: Delete non-existent record...");
		editPage2.clickDelete();
		assertFalse(dbPage2.verifyRecordDeletedMessageShown());
		
		// TODO: Functionality is not clear here.  
		// Currently the test is coded to not accept a successful deletion of a non-existent record.
		
		driver2.close();
	    utils.logTestEnd();
	}
}
