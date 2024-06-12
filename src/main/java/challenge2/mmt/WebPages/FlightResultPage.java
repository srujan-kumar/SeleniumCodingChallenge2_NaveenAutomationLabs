package challenge2.mmt.WebPages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import challenge.mmt.Browser.Driver;
import challenge.mmt.Report.LogStatus;
import challenge.mmt.util.ElementVisibility;
import challenge.mmt.util.PageScroll;
import challenge.mmt.util.Wait;

public class FlightResultPage extends HomePage{

	//@FindBy(xpath="//span[text()='Non Stop']/preceding-sibling::span")
	@FindBy(xpath="//p[contains(text(),'Popular Filters')]/following::span[@class='commonCheckbox sizeSm primaryCheckbox'][1]")
	WebElement NonStopCheckbox;
	
	//@FindBy(xpath="//p[text()='1 Stop']/preceding-sibling::span")
	@FindBy(xpath="//p[normalize-space()='Popular Filters']/following::span[@class='commonCheckbox sizeSm primaryCheckbox'][3]")
	WebElement oneStopCheckbox;
	
	//@FindBy(xpath="//a[@class='fli-clear inlineB']")
	@FindBy(xpath="//span[@class='filterCross']")
	WebElement clearFilter;
	
	@FindBy(xpath="//p[contains(text(),'  Tue, 11 Jul')]/preceding::div[@class='listingCard ']")
	List<WebElement> departureFilghts;
	
	@FindBy(xpath="//p[contains(text(),'  Tue, 11 Jul')]/following::div[@class='listingCard ']")
	List<WebElement> ReturnFilghts;
	
	@FindBy(xpath="//p[contains(text(),'  Tue, 11 Jul')]/preceding::div[contains(@class,'splitfare')]/p[1]")
	List<WebElement> FlightPrices;

	@FindBy(xpath="//p[contains(text(),'  Tue, 11 Jul')]/following::div[contains(@class,'splitfare')]/p[1]")
	List<WebElement> returnFlightPrices;
	
	@FindBy(xpath="//span[@class='whiteText fontSize22 boldFont']")
	WebElement totalFlightPrice;
	
	@FindBy(xpath="//span[@class='slashed-price']")
	WebElement shlashedTotalPrice;
	
	By SelectedFlightPrice=By.xpath("//p[@class='actual-price']");
	
	//Constructor to initialize webelements
	public FlightResultPage()
	{
		PageFactory.initElements(Driver.driver, this);
	}
	
	//counts total departure filghts, flights on left side. Returns no flight exception if flight not found
	public int departureFilightCount() throws Exception
	{
		Thread.sleep(2000);
		//Wait.toBeclickable(NonStopCheckbox);;
		PageScroll.toBottomOfPage();
		if(departureFilghts.size()<1)
		{
			LogStatus.fail("Flights not found");
			throw new Exception("No Flight availabe");
		}
		return departureFilghts.size();
	}
	//counts return departure flights, flights on right side. Returns no flight exception if flight not found
	public int returnFilightCount() throws Exception
	{

		PageScroll.toUP();
		Thread.sleep(2000);
		if(ReturnFilghts.size()<1)
		{
			LogStatus.fail("Flights not found");
			throw new Exception("No Flight availabe");
			
		}
		return ReturnFilghts.size();
	}
	//counts flight when no filter is applied
	public int[] NoFilterFlightCount() throws Exception
	{
		
		clearFilter();
		Thread.sleep(2000);
		int[] count=new int[2];
		count[0]=departureFilightCount();
		count[1]=returnFilightCount();
		return count;
	}
	
	//Counts flight when NOn stop filter is applied
	public int[] NoStopFlightCount() throws Exception
	{
		clearFilter();
		NonStopCheckbox.click();
		int[] count=new int[2];
		count[0]=departureFilightCount();
		count[1]=returnFilightCount();
		return count;
	}
	//counts flight when one stop filter is applied
	public int[] oneStopFlightCount() throws Exception
	{
		clearFilter();
		oneStopCheckbox.click();
		int[] count=new int[2];
		count[0]=departureFilightCount();
		count[1]=returnFilightCount();
		return count;
	}
	
	//Returns the Selected flight prices, inputs are provided from test , dep stands for departure flight number and ret stands for return flight number
	//if slashed price is displayed then it will take slashed price as total price
	public Map<String, String> selectRandomFlight(int dep,int ret)
	{
		Map<String, String> Prices=new HashMap<String, String>();
		
		JavascriptExecutor js=(JavascriptExecutor)Driver.driver;
		js.executeScript("arguments[0].click();", departureFilghts.get(dep));
			
		js.executeScript("arguments[0].click();", ReturnFilghts.get(ret));
		
		String[] Flightdetails=departureFilghts.get(dep).getAttribute("innerText").split("\\r?\\n");
		Prices.put("Dep Flight", FlightPrices.get(dep).getText().trim());

		Flightdetails=ReturnFilghts.get(ret).getAttribute("innerText").split("\\r?\\n");
		Prices.put("Ret Flight", returnFlightPrices.get(ret).getText().trim());
		//System.out.println("Dep Flight Price"+Flightdetails[dep]);
		//System.out.println("Ret Flight Price"+Flightdetails[ret]);
		System.out.println("Dep Flight Price"+FlightPrices.get(dep).getText().trim());
		System.out.println("Ret Flight Price"+returnFlightPrices.get(ret).getText().trim());
		Prices.put("Dep Bottom Price", FlightPrices.get(0).getText());
		Prices.put("Ret Bottom Price", FlightPrices.get(1).getText());
		if(ElementVisibility.isVisble(shlashedTotalPrice))
		{
			Prices.put("total Price", totalFlightPrice.getText());
			System.out.println("total Flight Price"+totalFlightPrice.getText());
		}
		else
		{
			Prices.put("total Price", totalFlightPrice.getText());
			System.out.println("total Flight Price"+totalFlightPrice.getText());
		}
		return Prices;
	}
	//Clears flight filter eg: one stop, non stop
	public void clearFilter()
	{
		if(ElementVisibility.isVisble(clearFilter))
		{
			clearFilter.click();
		}
	}
}
