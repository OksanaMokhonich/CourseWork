package com.mokhonich.coursework.catalog;

import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mokhonich.coursework.SeleniumPage;

public class JsoupCatalog {

	
	private static CatalogDatabaseController controller = new CatalogDatabaseController();
	public static void main(String[] args) throws IOException {
		Document doc = Jsoup.connect("https://sribniyvik.ua/ua/").timeout(10000).userAgent("Chrome/66.0.3359.139").get();
		controller.openDatabaseConnection();
		getCategoryInfo(doc);
		controller.closeConnection();
	}
	
	public static void getCategoryInfo(Document document) throws IOException {
		
		Elements categories = document.getElementById("nav").getElementsByClass("drop-holder");
		
		for(Element elem: categories) {
			
		String categoryName = getCategoryName(elem);
		String categoryHref = getCategoryHref(elem);
		System.out.println(categoryName + "******" + categoryHref);
		if(!categoryName.equals("Інші категорії")) {
			controller.addCategories(categoryName, categoryHref);
			getSubCategoryInfo(categoryName, categoryHref);}
		}	
	}
	
	public static String getCategoryName(Element elem) {
		return elem.getElementsByTag("a").get(0).text().replace('"', '\'');
	}
	
	public static String getCategoryHref(Element elem) {
		return elem.getElementsByTag("a").get(0).attr("href").replace('"', '\'');
	}
	
	public static void getSubCategoryInfo(String categoryName, String url) throws IOException {
		//Document doc = Jsoup.parse(SeleniumPage.getPageSource(url));
		Document doc = Jsoup.connect(url).timeout(10000).userAgent("Chrome/66.0.3359.139").get();
		Elements categories = doc.getElementsByClass("portalblocks_block");
		
		for(Element elem: categories) {
			
		String subCategoryName = getSubCategoryName(elem);
		String subCategoryHref = getSubCategoryHref(elem);
		System.out.println(subCategoryName + "------" +subCategoryHref);
		controller.addSubCategories(subCategoryName, subCategoryHref, categoryName);
		getProdInfo(subCategoryName,subCategoryHref);
		}	
	}
	
	public static String getSubCategoryName(Element elem) {
		return elem.getElementsByTag("a").get(0).text().replace('"', '\'');
	}
	
	public static String getSubCategoryHref(Element elem) {
		return elem.getElementsByTag("a").get(0).attr("href").replace('"', '\'');
	}
		
	public static String getProductName(Element elem) {
		return elem.getElementsByTag("a").attr("title").replace('"', '\'');
	}
	
	public static String getProductHref(Element elem) {
		return elem.getElementsByTag("a").attr("href").replace('"', '\'');
	}
	
	public static String getProductImageHref(Element elem) {
		return elem.getElementsByTag("a").get(0).getElementsByTag("img").attr("src").replace('"', '\'');
	}
	
	public static String getProductPrice(Element elem) {
		return elem.getElementsByClass("special-price").text().replace('"', '\'');
	}
	
	public static void getProdInfo(String subCategoryName,String url) throws IOException {
		Document doc = Jsoup.connect(url).userAgent("Chrome/66.0.3359.139").get();
		Elements productList = doc.getElementsByClass("item-inner");
		String nextHrefs = "";
		System.out.println(doc.getElementsByClass("i-next").size());
		if(doc.getElementsByClass("i-next").size()>0){
		nextHrefs = doc.getElementsByClass("i-next").attr("href");
		System.out.println(nextHrefs);
		}
		for(Element prod : productList) {
			String prodName = getProductName(prod);
			String prodHref = getProductHref(prod);
			String prodImage = getProductImageHref(prod);
			String prodPrice = getProductPrice(prod);
			if(!prodHref.equals("https://sribniyvik.ua/ua/dostavka-i-oplata/")) {
				System.out.println(prodName + " %%%%%%%%" + prodHref + "%%%%" + prodImage + "%%%%%" + prodPrice);
				controller.addPoducts(prodName, prodHref, prodPrice, prodImage, subCategoryName);
				}
		}
		
		if(!nextHrefs.equals("")) {
			System.out.println("******************************************"
					+ "******************************************"
					+ "*****************************");
			getProdInfo(subCategoryName, url);
		}
			
	}
	
}
