package com.mokhonich.coursework.catalog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.xalan.xsltc.trax.SAX2DOM;
import org.ccil.cowan.tagsoup.Parser;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.openqa.selenium.remote.NewSessionPayload;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mokhonich.coursework.SeleniumPage;

public class TagSoupCatalog {

	private static CatalogDatabaseController controller = new CatalogDatabaseController();
	
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		String url = "https://sribniyvik.ua/ua/";
		Document doc = openConnection(url);
		System.out.println(doc == null);
		controller.openDatabaseConnection();
		getCategoryInfo(doc);
		controller.closeConnection();
		//getProdInfo("https://sribniyvik.ua/ua/kolca/pomolvochnye/");

	}
	
	public static Document openConnection(String url) throws IOException, SAXException, ParserConfigurationException {
		Parser p = new Parser();
		//String page = readFile("cat.txt");
		String page = SeleniumPage.getPageSource(url);
		InputSource inputSource = new InputSource(new StringReader(page));
		SAX2DOM sax2dom = null;
		org.w3c.dom.Node node = null;
		sax2dom = new SAX2DOM();
		p.setContentHandler(sax2dom);
		p.parse(inputSource);
		node = sax2dom.getDOM();
		return (Document) node;
	}
	
	public static void getCategoryInfo(Document document) throws IOException, SAXException, ParserConfigurationException {
		NodeList categories = document.getElementsByTagName("div");

		for (int i = 0; i < categories.getLength(); i++) {
			final Node item = categories.item(i);
			Node attr = item.getAttributes().getNamedItem("class");
			
			if (attr != null) {
				String attribute = item.getAttributes().getNamedItem("class").getTextContent();
				if (attribute.equals("drop-holder")) {
					if (item.getParentNode().getParentNode().getParentNode().getAttributes().getNamedItem("class")
							.toString().substring(7, 10).equals("nav")) {
						String categoryHref = getCategoryHref(item);
						String categoryName = getCategoryTitle(item);
						if(!categoryName.equals("Інші категорії")) {
						System.out.println(categoryHref);
						System.out.println(categoryName);
						controller.addCategories(categoryName, categoryHref);
						getSubCategoryInfo(categoryName, categoryHref);
						}
					}
				}
			}
		}

	}

	private static void getSubCategoryInfo(String categoryName, String url) throws IOException, SAXException, ParserConfigurationException {
		System.out.println("start");
		Document document = openConnection(url);
		
		NodeList categories = document.getElementsByTagName("li");
		// System.out.println(categories);
		for (int i = 0; i < categories.getLength(); i++) {
			final Node item = categories.item(i);
			Node attr = item.getAttributes().getNamedItem("class");
			// System.out.println(categories.item(i).toString());
			if (attr != null) {
				String attribute = item.getAttributes().getNamedItem("class").getTextContent();
				if (attribute.equals("portalblocks_block")) {
						String subCategoryHref = getSubCategoryHref(item);
						String subCategoryName = getSubCategoryName(item);
						System.out.println(subCategoryHref + "...." + subCategoryName);
						controller.addSubCategories(subCategoryName, subCategoryHref, categoryName);
						getProdInfo(subCategoryName, subCategoryHref);
				}
			}
		}
		
		
	}
		
	private static void getProdInfo(String subCategoryName,String url) throws IOException, SAXException, ParserConfigurationException {
		System.out.println("start with url" + url);
		Document document = openConnection(url);
		String nextHrefs = "";
		NodeList a = document.getElementsByTagName("a");
		nextHrefs = getiNextLength(a);
		if(nextHrefs.length()>6) {
		nextHrefs = nextHrefs.substring(6, nextHrefs.length()-1);
		}
		
		NodeList categories = document.getElementsByTagName("div");
		// System.out.println(categories);
		for (int i = 0; i < categories.getLength(); i++) {
			final Node item = categories.item(i);
			Node attr = item.getAttributes().getNamedItem("class");
			// System.out.println(categories.item(i).toString());
			if (attr != null) {
				String attribute = item.getAttributes().getNamedItem("class").getTextContent();
				if (attribute.equals("item-inner")) {
						String productHref = getProductHref(item);
						
						if(!productHref.equals("https://sribniyvik.ua/ua/dostavka-i-oplata/")) {
							String productImage = getProductImage(item);
							String productName = getProductName(item);
							String productPrice = getProductPrice(item);
							controller.addPoducts(productName, productHref, productPrice, productImage, subCategoryName);
							System.out.println(productName + "..." + productHref + "..." + productImage + "....." + productPrice);
							}
				}
			}
		}
		
		
		if(!nextHrefs.equals("")) {
			System.out.println("******************************************"
					+ "******************************************"
					+ "*****************************");
			getProdInfo(subCategoryName,nextHrefs);
		}
	}
	
	private static String getiNextLength(NodeList a) {
//		NodeList categories = document.getElementsByTagName("a");
		String rez = "";
		// System.out.println(categories);
		for (int i = 0; i < a.getLength(); i++) {
			final Node item = a.item(i);
			Node attr = item.getAttributes().getNamedItem("class");
			// System.out.println(categories.item(i).toString());
			if (attr != null) {
				String attribute = item.getAttributes().getNamedItem("class").toString();
				String cl = attribute.substring(7,attribute.length()-1);
				if (cl.equals("i-next")) {
						return item.getAttributes().getNamedItem("href").toString();
			}
		}
		}
		return rez;
	}

	private static String getProductPrice(Node item) {
		
		return item.getChildNodes().item(5).getChildNodes().item(1).getTextContent().toString().trim().replace('"', '\'');
		
	}

	private static String getProductImage(Node item) {
		String rez = item.getChildNodes().item(1).getChildNodes().item(1).getAttributes().getNamedItem("src").getTextContent().toString();
		return  rez.replace('"', '\'');
	}

	private static String getProductHref(Node item) {
		String rez = item.getChildNodes().item(1).getAttributes().getNamedItem("href").toString();
		return  rez.substring(6, rez.length() - 1).replace('"', '\'');
	}

	private static String getProductName(Node item) {
		String rez = item.getChildNodes().item(1).getAttributes().getNamedItem("title").toString();
		return rez.substring(7, rez.length()-1).replace('"', '\'');
	}

	private static String getSubCategoryHref(Node item) {
		String temp =  item.getChildNodes().item(1).getAttributes().getNamedItem("href").toString();
		return temp.substring(6, temp.length()-1).replace('"', '\'');
		
	}

	private static String getSubCategoryName(Node item) {
		String temp =  item.getChildNodes().item(1).getTextContent().toString().trim();
		return temp.replace('"', '\'');
		
	}
	

	private static String getCategoryHref(Node item) {
		String temp = item.getChildNodes().item(0).getAttributes().getNamedItem("href").toString();

		return temp.substring(6, temp.length() - 1).replace('"', '\'');
	}

	private static String getCategoryTitle(Node item) {
		String temp = item.getChildNodes().item(0).getTextContent().toString();
		return temp.replace('"', '\'');
	}

}
