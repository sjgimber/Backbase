package Tests;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import Pages.*;

public class databaseTest {
	private WebDriver driver;
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
	public void setUp() throws Exception {
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
	public void test() throws InterruptedException 
	{
		// Currently there are no tests for the Database List page (though there are functions in the POM).
		
		/*
	    driver.get(baseUrl);
	    
	    databasePOM dbPage = new databasePOM(driver);
	    dbPage.searchForComputer("test");
	    System.out.println(dbPage.getMatchCount());
	    
	    Thread.sleep(5000); */
	}

}
