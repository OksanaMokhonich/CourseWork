package com.mokhonich.coursework;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumPage {

	public static String getAllPageSource(String url) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver","C:\\chromedriver.exe"); 
		WebDriver driver = new ChromeDriver();
		driver.get(url); 
		JavascriptExecutor js = ((JavascriptExecutor) driver);
	    int pause = 4;
	    String rezult = "";
	    long lastH = 0;
	    int i = 0;
	    while(true) {
	    	i++;
	    	System.out.println("iteation = " + i + "  lastH = " + lastH);
	    	js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
	    	TimeUnit.SECONDS.sleep(1);
	    	long height = (Long) js.executeScript("return document.body.scrollHeight");
	    	System.out.println("height = " + height + "------------");
	    	if(lastH == height) {
	    		System.out.println("end");
	    		rezult = driver.getPageSource();
	    		driver.quit();
	    		break;
	    	}
	    	lastH = height;
	    	
	    }
	    return rezult;
	}
	
	public static String getPageSource(String url) {
		String rezult = "";
		System.setProperty("webdriver.chrome.driver","C:\\chromedriver.exe"); 
		WebDriver driver = new ChromeDriver();
		driver.get(url); 
		rezult = driver.getPageSource();
		driver.quit();
		return rezult;
		
	}
}
