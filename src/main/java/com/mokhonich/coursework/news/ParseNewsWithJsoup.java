package com.mokhonich.coursework.news;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;

import com.mokhonich.coursework.database.NewsDatabaseController;
import com.mokhonich.coursework.selenium.SeleniumPage;

public class ParseNewsWithJsoup {
	static NewsDatabaseController controller = new NewsDatabaseController();

	public static void testJsoupNews() throws IOException, InterruptedException {
		controller.openDatabaseConnection();
		String url = "https://www.ukr.net/news/main.html";
		Document doc = Jsoup.parse(SeleniumPage.getPageSource(url));
		getNewsCategories(doc);
		controller.closeConnection();

	}

	public static void getNewsCategories(Document document) throws InterruptedException {
		Elements li = document.getElementsByClass("n-m_li");
		System.out.println(li.text());
		for (Element temp : li) {
			String categoryTitle = getCategoryTitle(temp).replace('"', '\'');
			;
			String categoryHref = getCategoryHref(temp);
			// System.out.println(categoryHref+" ++++++");
			String goodCategoryHref = "https:" + categoryHref;
			if (!categoryTitle.equals("В регіоні<i class=\"r-r\"></i>")) {
				Document doc = Jsoup.parse(SeleniumPage.getAllPageSource(goodCategoryHref));
				controller.addCategories(categoryTitle, goodCategoryHref);
				getNewsInfo(categoryTitle, doc);
			}
		}
	}

	private static String getCategoryTitle(Element elem) {
		return elem.getElementsByClass("n-m_li_a").get(0).text();
	}

	private static String getCategoryHref(Element elem) {
		return elem.getElementsByClass("n-m_li_a").get(0).attr("href");
	}

	public static void getNewsInfo(String categoryTitle, Document document) {
		Elements sections = document.getElementsByClass("im");

		for (Element temp : sections) {
			String time = getNewsTime(temp);
			String newsText = getNewsTitle(temp);
			String newsText1 = newsText.replace('"', '\'');
			String newsHref = getNewsHref(temp);
			String goodNewsHref = newsHref;
			// System.out.println(categoryTitle);
			controller.addNews(newsText1, time, goodNewsHref, categoryTitle);
			System.out.println(time + "---" + newsText + "---" + goodNewsHref);
			controller.closeConnection();

		}
	}

	public static String getNewsTime(Element elem) {
		return elem.getElementsByClass("im-tm").text();
	}

	public static String getNewsTitle(Element elem) {
		return elem.getElementsByClass("im-tl_a").text();
	}

	public static String getNewsHref(Element elem) {
		return elem.getElementsByClass("im-tl_a").get(0).attr("href");
	}

}