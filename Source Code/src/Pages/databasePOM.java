package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class databasePOM 
{
	WebDriver driver;

	@FindBy(id="searchbox") WebElement txtSearchField;						// "Search" field.
	@FindBy(id="searchsubmit") WebElement btnSubmit;						// <Filter by name> button.
	@FindBy(xpath=".//*[@id='main']/h1") WebElement lblSearchResult;		// The <h1> field containing the search result count.
	@FindBy(id="add") WebElement btnAdd;									// <Add a new computer> button.
	@FindBy(xpath=".//*[@id='main']/div[1]") WebElement lblActionResult;	// Success message div.
	
	public void setSearchField(String searchValue)
	{
		// Set the database search field value.
		txtSearchField.clear();
		txtSearchField.sendKeys(searchValue);
	}
	
	public void searchForComputer(String searchValue)
	{
		// Set the database search field value, and click on the <Submit> button.
		setSearchField(searchValue);
		btnSubmit.click();
	}
	
	public void clickAdd()
	{
		// Click the <Add a new computer> button.
		btnAdd.click();
	}
	
	public int getMatchCount()
	{
		// Get the number of matches found to the database search.
		// The response is either "No computers found" or "<x> computers found".
		
		String matches = lblSearchResult.getText().toUpperCase();
		int numMatches;
		
		if (matches.equals("NO COMPUTERS FOUND"))
		{
			// No matches found.
			numMatches = 0;
		}
		else if (matches.contains("ONE"))
		{
			// Only a single match found - and it's put in text format, different to multiple matches!
			numMatches = 1;
		}
		else
		{
			// Multiple matches found.  The format is "x matches found".
			// This filters all characters out of the result, leaving the integer value.
			numMatches = Integer.parseInt(matches.replaceAll("[^0-9]", ""));
		}
		
		System.out.println(numMatches + " matching computers detected in the list.");
		return numMatches;
	}
	
	public void viewRecord(String computerName)
	{
		// Click on the requested computer name link, to access the Edit Record screen.
		driver.findElement(By.linkText(computerName)).click();
	}
	
	
	public boolean verifyRecordAddedMessageShown(String computerName)
	{
		// Check for the presence of the "record added" message.
		// Format: "Done! Computer <computer name> has been created"
		
		String message = "Done! Computer " + computerName + " has been created";
		
		System.out.println("Action message found: '" + lblActionResult.getText() + "'");
		
		if (lblActionResult.getText().toUpperCase().contains(message.toUpperCase()))
		{
			System.out.println("\tSuccess message found.");
			return true;
		}
		else
		{
			System.out.println("\tSuccess message NOT found.");
			return false;
		}
	}
	
	public boolean verifyRecordDeletedMessageShown()
	{
		// Check for the presence of the "record deleted" message.
		// Format: "Done! Computer has been deleted"
		
		String message = "Done! Computer has been deleted";
		
		System.out.println("Action message found: '" + lblActionResult.getText() + "'");
		
		if (lblActionResult.getText().toUpperCase().contains(message.toUpperCase()))
		{
			System.out.println("\tSuccess message found.");
			return true;
		}
		else
		{
			System.out.println("\tSuccess message NOT found.");
			return false;
		}
	}

	public boolean verifyRecordUpdatedMessageShown(String computerName)
	{
		// Check for the presence of the "record updated" message.
		// Format: "Done! Computer <computer name> has been updated"
		
		String message = "Done! Computer " + computerName + " has been updated";
		
		System.out.println("Action message found: '" + lblActionResult.getText() + "'");
		
		if (lblActionResult.getText().toUpperCase().contains(message.toUpperCase()))
		{
			System.out.println("\tSuccess message found.");
			return true;
		}
		else
		{
			System.out.println("\tSuccess message NOT found.");
			return false;
		}
	}
	
	public databasePOM(WebDriver driver)
	{
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
}
