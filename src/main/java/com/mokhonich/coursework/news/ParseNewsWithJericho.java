package com.mokhonich.coursework.news;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;

import com.mokhonich.coursework.SeleniumPage;

public class ParseNewsWithJericho {

	private static NewsDatabaseController controller = new NewsDatabaseController();
	
	public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException  {
		String page = SeleniumPage.getPageSource("https://www.ukr.net/news/main.html");
		Source source = new Source(page);
		controller.openDatabaseConnection();
		getNewsCategories(source);
		controller.closeConnection();
		
	}
	
	public static void getNewsCategories(Source source) throws InterruptedException {
		List<Element> elements = source.getAllElementsByClass("n-m_li");
		System.out.println(elements.size());
		for (Element element : elements) {
			// System.out.println(element);
			String categoryTitle = getCategoryTitle(element).replace('"', '\'');
			String categoryHref = getCategoryHref(element);
			String goodCategoryHref = "https:" +categoryHref;
			if(!categoryTitle.equals("В регіоні<i class=\"r-r\"></i>")) {
			System.out.println(goodCategoryHref + "---" + categoryTitle);
			String page = SeleniumPage.getAllPageSource(goodCategoryHref);
			Source source1 = new Source(page);
			controller.addCategories(categoryTitle, goodCategoryHref);
			getNewsInfo(categoryTitle, source1);
			}
		}
	}
	
	public static void getNewsInfo(String categoryTitle, Source source ) {
		
		List<Element> sections = source.getAllElementsByClass("im");
		for (Element temp : sections) {
			String time = getNewsTime(temp);
			String newsText = getNewsTitle(temp);
			String newsText1 = newsText.replace('"', '\'');
			String newsHref = getNewsHref(temp);
			String goodNewsHref = "https:" + newsHref;
			controller.addNews(newsText1, time, goodNewsHref, categoryTitle);
			System.out.println(time + "---" + newsText1 + "---" +goodNewsHref);

		}
	}
	
	public static String getCategoryTitle(Element elem) {
		return elem.getAllElementsByClass("n-m_li_a").get(0).getContent().toString();
	}
	
	public static String getCategoryHref(Element elem) {
		return elem.getAllElementsByClass("n-m_li_a").get(0).getAttributeValue("href");
	}
	
	
	public static String getNewsTime(Element elem) {
		return elem.getAllElementsByClass("im-tm").get(0).getContent().toString();
	}
	
	public static String getNewsTitle(Element elem) {
		return elem.getAllElementsByClass("im-tl_a").get(0).getContent().toString();
	}
	
	public static String getNewsHref(Element elem) {
		return elem.getAllElementsByClass("im-tl_a").get(0).getAttributeValue("href");
		
	}
}
