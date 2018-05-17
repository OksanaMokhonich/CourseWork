package com.mokhonich.coursework.news;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.htmlcleaner.ContentNode;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import com.mokhonich.coursework.SeleniumPage;

import net.htmlparser.jericho.Element;


public class ParseNewsWithHtmlCleaner {
	
	private static NewsDatabaseController controller = new NewsDatabaseController();
	
	public static void main(String[] args) throws MalformedURLException, IOException, XPatherException, InterruptedException {
		HtmlCleaner cleaner = new HtmlCleaner();
		String url = "https://www.ukr.net/news/main.html";
		String page = SeleniumPage.getPageSource(url);
		TagNode html = cleaner.clean(page);
		controller.openDatabaseConnection();
		getNewsCategories(html);
		
	}
	
	public static void getNewsCategories(TagNode html) throws XPatherException, InterruptedException {
		Object[] sections = html.evaluateXPath("//*[@id=\"nav\"]/div/div/ul/li");
		for(Object obj: sections) {
			TagNode node = (TagNode)obj;
			String categoryHref = getCategoryHref(node).replace('"', '\'');
			String categoryName = getCategoryTitle(node);
			if(!categoryName.equals("В регіоні")) {
				String page = SeleniumPage.getAllPageSource(categoryHref);
				HtmlCleaner cleaner = new HtmlCleaner();
				TagNode html1 = cleaner.clean(page);
				controller.addCategories(categoryName,categoryHref);
				getNewsInfo(categoryName, html1);
			}
		}
	}
	
	public static void getNewsInfo(String categoryName, TagNode html) throws XPatherException {
		Object []sections = html.evaluateXPath("//*[@id=\"main\"]/div/article/section");
		for(Object obj: sections) {
			TagNode node = (TagNode)obj;
			String newsTime = getNewsTime(node);
			String newsHref = 	getNewsHref(node).replace('"', '\'');
			String newsTitle = getNewsTitle(node).replace('"', '\'');
			controller.addNews(newsTitle, newsTime, newsHref, categoryName);
			//System.out.println(newsTime + "......." + newsTitle + "...." + newsHref);
		}
	}
	
	
	public static String getCategoryTitle(TagNode node) {
		return node.getChildTagList().get(0).getText().toString();
	}
	
	public static String getCategoryHref(TagNode node) {
		return "https:" + node.getChildTagList().get(0).getAttributeByName("href").toString();//ссилка на товар
	}
	
	
	public static String getNewsTime(TagNode node) {
		return node.getChildTagList().get(0).getText().toString();
	}
	
	public static String getNewsTitle(TagNode node) throws XPatherException {
		return ((TagNode) node.evaluateXPath("//div[1]/div/a")[0]).getText().toString();
	}
	
	public static String getNewsHref(TagNode node) throws XPatherException {
		return ((TagNode) node.evaluateXPath("//div[1]/div/a")[0]).getAttributeByName("href").toString();
		
	}

}
