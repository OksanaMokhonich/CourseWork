package com.mokhonich.coursework.news;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.xalan.xsltc.trax.SAX2DOM;
import org.ccil.cowan.tagsoup.Parser;
import org.jdom2.input.DOMBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.mokhonich.coursework.database.NewsDatabaseController;
import com.mokhonich.coursework.selenium.SeleniumPage;

public class ParseNewsWithTagSoup {

	private static NewsDatabaseController controller = new NewsDatabaseController();

	public static void testTaSoupNews() throws Exception {
		String url = "https://www.ukr.net/news/main.html";
		Document doc = openConnection(url);
		controller.openDatabaseConnection();
		getNewsCategories(doc);
		controller.closeConnection();
		System.out.println("success");
	}

	public static Document openConnection(String url) throws ParserConfigurationException, IOException, SAXException {
		String page1 = SeleniumPage.getPageSource(url);
		Parser p = new Parser();
		InputSource inputSource = new InputSource(new StringReader(page1));
		SAX2DOM sax2dom = null;
		org.w3c.dom.Node node = null;
		sax2dom = new SAX2DOM();
		p.setContentHandler(sax2dom);
		p.parse(inputSource);
		node = sax2dom.getDOM();
		return (Document) node;
	}

	public static void getNewsCategories(Document document)
			throws ParserConfigurationException, IOException, SAXException {
		NodeList sections = document.getElementsByTagName("li");
		for (int i = 0; i < sections.getLength(); i++) {
			final Node item = sections.item(i);
			Node attr = item.getAttributes().getNamedItem("class");
			if (attr != null) {
				String attribute = item.getAttributes().getNamedItem("class").getTextContent();
				if (attribute.equals("n-m_li")) {
					String categoryName = getCategoryTitle(item).replace('"', '\'');
					String categoryHref = getCategoryHref(item).replace('"', '\'');
					controller.addCategories(categoryName, categoryHref);
					getNewsInfo(categoryName, openConnection(categoryHref));
					// System.out.println(categoryName + " " + categoryHref);
				}
			}
		}
	}

	public static void getNewsInfo(String categoryName, Document document) {
		NodeList sections = document.getElementsByTagName("section");
		for (int i = 0; i < sections.getLength(); i++) {
			final Node item = sections.item(i);
			Node attr = item.getAttributes().getNamedItem("class");
			if (attr != null) {
				String attribute = item.getAttributes().getNamedItem("class").getTextContent();
				if (attribute.equals("im")) {

					String time = getNewsTime(item);

					String newsHref = getNewsHref(item).replace('"', '\'');
					String str2 = getNewsTitle(item).replace('"', '\'');

					// System.out.println(time);System.out.println(newsHref);System.out.println(str2);
					controller.addNews(str2, time, newsHref, categoryName);

				}
			}
		}
	}

	public static String getNewsTitle(Node item) {
		return item.getChildNodes().item(3).getChildNodes().item(1).getChildNodes().item(1).getTextContent();// ссилка
	}

	public static String getNewsHref(Node item) {
		Node href = item.getChildNodes().item(3).getChildNodes().item(1).getChildNodes().item(1);// ссилка
		String badHref = href.getAttributes().getNamedItem("href").toString();
		return "https:" + badHref.substring(6, badHref.length() - 1);
	}

	public static String getNewsTime(Node item) {
		return item.getChildNodes().item(1).getTextContent().toString();// час
	}

	public static String getCategoryHref(Node item) {
		String categoryHref = item.getChildNodes().item(1).getAttributes().getNamedItem("href").toString();
		return "https:" + categoryHref.substring(6, categoryHref.length() - 1);
	}

	public static String getCategoryTitle(Node item) {
		return item.getChildNodes().item(1).getTextContent();
	}
}
