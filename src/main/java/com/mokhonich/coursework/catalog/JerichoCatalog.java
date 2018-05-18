package com.mokhonich.coursework.catalog;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.mokhonich.coursework.database.CatalogDatabaseController;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

public class JerichoCatalog {

	private static CatalogDatabaseController controller = new CatalogDatabaseController();
	
	public static void testJerichoCatalog() throws MalformedURLException, IOException {
		Source page = new Source(new URL("https://sribniyvik.ua/ua/"));
		controller.openDatabaseConnection();
		getGoodsCategoryInfo("https://sribniyvik.ua/ua/podveski/podveski-s-tsepochkoy/");
		controller.closeConnection();
	}

	public static void getGoodsCategoryInfo(String url) throws IOException {

		Source page = new Source(new URL(url));
		List<Element> itemInner = page.getElementById("nav").getAllElementsByClass("drop-holder");
		for (Element elem : itemInner) {
			String categoryName = getCategoryName(elem);
			String categoryHref = getCategoryHref(elem);
			//System.out.println(categoryName + "******" + categoryHref);
			if (!categoryName.equals("Інші категорії")) {
				controller.addCategories(categoryName, categoryHref);
				getSubCategoryInfo(categoryName, categoryHref);
			}
		}
	}

	public static String getCategoryName(Element elem) {
		return elem.getAllElements(HTMLElementName.A).get(0).getAllElements(HTMLElementName.SPAN).get(0).getContent().toString().replace('"', '\'');
	}

	public static String getCategoryHref(Element elem) {
		return elem.getAllElements(HTMLElementName.A).get(0).getAttributeValue("href").replace('"', '\'');
	}
	
	public static String getSubCategoryName(Element elem) {
		return elem.getAllElements(HTMLElementName.A).get(0).getAllElements(HTMLElementName.SPAN).get(0).getContent().toString().replace('"', '\'');
	}
	
	public static String getSubCategoryHref(Element elem) {
		return elem.getAllElements(HTMLElementName.A).get(0).getAttributeValue("href").replace('"', '\'');
	}
	
	
	public static String getProductName(Element elem) {
		return elem.getAllElements(HTMLElementName.A).get(0).getAttributeValue("title").replace('"', '\'');
	}
	
	public static String getProductHref(Element elem) {
		return elem.getAllElements(HTMLElementName.A).get(0).getAttributeValue("href").replace('"', '\'');
	}
	
	public static String getProductImageHref(Element elem) {
		return elem.getAllElements(HTMLElementName.A).get(0).getAllElements(HTMLElementName.IMG).get(0).getAttributeValue("src").replace('"', '\'');
	}
	
	public static String getProductPrice(Element elem) {
		String temp = elem.getAllElementsByClass("special-price").get(0).getAllElementsByClass("price").get(0).getAllElements(HTMLElementName.SPAN).get(1).getContent().toString();
		String temp1 = elem.getAllElementsByClass("special-price").get(0).getAllElementsByClass("price").get(0).getAllElements(HTMLElementName.SPAN).get(0).getContent().toString();
		String str = temp1.substring(0,temp1.length()-16).replace('"', '\'');
		return str + temp;
	}
	
	public static void getSubCategoryInfo(String categoryName, String url) throws IOException {
		
		
		Source page = new Source(new URL(url));
		List<Element> categories = page.getAllElementsByClass("portalblocks_block");
		for(Element elem: categories) {
			
			String subCategoryName = getSubCategoryName(elem);
			String subCategoryHref = getSubCategoryHref(elem);
			System.out.println(subCategoryName + "------" +subCategoryHref);
			controller.addSubCategories(categoryName, subCategoryHref, categoryName);
			getProdInfo(subCategoryName,subCategoryHref);
	}
	}
	
	public static void getProdInfo(String subCategoryName, String url) throws MalformedURLException, IOException {
		
		Source page = new Source(new URL(url));
		String nextHrefs = "";
		//System.out.println(doc.getElementsByClass("i-next").size());
		System.out.println(page.getAllElementsByClass("i-next").size());
		if(page.getAllElementsByClass("i-next").size()>0) {
			nextHrefs = page.getAllElementsByClass("i-next").get(0).getAttributeValue("href");
			System.out.println(nextHrefs);
		}
		List<Element> categories = page.getAllElementsByClass("item-inner");
		for(Element elem: categories) {
			String prodHref = getProductHref(elem);
			if(!prodHref.equals("https://sribniyvik.ua/ua/dostavka-i-oplata/")) {
				String prodName = getProductName(elem);
				String prodImage = getProductImageHref(elem);
				//System.out.println(prodName);
				//System.out.println(prodHref);
				String prodPrice = getProductPrice(elem);
				//System.out.println(prodPrice);
				controller.addPoducts(prodName, prodHref,prodPrice, prodImage,  subCategoryName);
			}
	}
		if(!nextHrefs.equals("")) {
			//System.out.println("********");
			getProdInfo(subCategoryName, url);
		}
		
	}
}
