package com.mokhonich.coursework.catalog;

import java.io.IOException;
import java.net.MalformedURLException;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import com.mokhonich.coursework.database.CatalogDatabaseController;
import com.mokhonich.coursework.selenium.SeleniumPage;

public class HtmlCleanerCatalog {

	private static CatalogDatabaseController controller = new CatalogDatabaseController();

	public static void testHtmlCleanerCatalog() throws XPatherException, InterruptedException, IOException {
		String url = "https://sribniyvik.ua/ua/";
		controller.openDatabaseConnection();
		getCategoryInfo(openConnection(url));
		controller.closeConnection();
	}

	public static TagNode openConnection(String url) {
		HtmlCleaner cleaner = new HtmlCleaner();
		String page = SeleniumPage.getPageSource(url);
		return cleaner.clean(page);
	}

	public static void getCategoryInfo(TagNode html) throws XPatherException, InterruptedException, IOException {
		Object[] categories = html.evaluateXPath("//*[@id=\"nav\"]/li");
		for (Object obj : categories) {
			TagNode node = (TagNode) obj;
			//// *[@id="nav"]/li[1]/div/div[1]/a
			String categoryName = getCategoryName(node);
			String categoryHref = getCategoryHref(node);
			// System.out.println(categoryName + "....." + categoryHref);

			if (!categoryName.equals("Інші категорії")) {
				controller.addCategories(categoryName, categoryHref);
				getSubCategoryInfo(categoryName, categoryHref);
			}

		}
	}

	public static String getSubcategoryName(TagNode node) throws XPatherException {
		return ((TagNode) node.evaluateXPath("//a")[0]).getText().toString().trim().replace('"', '\'');
	}

	public static String getSubcategoryHref(TagNode node) throws XPatherException {
		return ((TagNode) node.evaluateXPath("//a")[0]).getAttributeByName("href").replace('"', '\'');
	}

	public static void getSubCategoryInfo(String categoryName, String url) throws IOException, XPatherException {
		System.out.println("start");

		TagNode html = openConnection(url);

		Object[] subcategories = html.evaluateXPath("//li[@class=\"portalblocks_block\"]");
		System.out.println(subcategories.length);
		System.out.println(subcategories);
		for (Object obj : subcategories) {
			TagNode node = (TagNode) obj;
			String subCategoryName = getSubcategoryName(node);
			String subCategoryHref = getSubcategoryHref(node);
			System.out.println(subCategoryName + " " + subCategoryHref);
			controller.addSubCategories(subCategoryName, subCategoryHref, categoryName);
			getProdInfo(subCategoryName, subCategoryHref);
		}
	}

	public static void getProdInfo(String subCategoryName, String url) throws IOException, XPatherException {
		System.out.println("start");
		HtmlCleaner cleaner = new HtmlCleaner();
		String page = SeleniumPage.getPageSource(url);
		TagNode html = cleaner.clean(page);

		String nextHrefs = "";/// html/body/div[1]/main/div/div/div[3]/div[2]/form/fieldset/div[2]/div[2]/div/div/ul/li[7]/a
		System.out.println(html.evaluateXPath("//a[@class = \"i-next\"]").length);
		if (html.evaluateXPath("//a[@class = \"i-next\"]").length > 0) {
			nextHrefs = ((TagNode) html.evaluateXPath("//a[@class = \"i-next\"]")[0]).getAttributeByName("href")
					.replace('"', '\'');
		}

		Object[] productlist = html.evaluateXPath("//div[@class=\"item-inner\"]");
		for (Object obj : productlist) {
			TagNode node = (TagNode) obj;
			String productName = getProductName(node);
			String productHref = getProductHref(node);
			String productImage = getProductImage(node);
			if (!productHref.equals("https://sribniyvik.ua/ua/dostavka-i-oplata/")) {
				String productPrice = getProductPrice(node);
				System.out.println(productName + "..." + productHref + "..." + productImage + "....." + productPrice);
			}
			/// html/body/div[1]/main/div/div/div[3]/div[2]/form/fieldset/div[2]/ul/li[11]/div/a
		}

		if (!("").equals(nextHrefs)) {
			// System.out.println("****")
			getProdInfo(subCategoryName, url);
		}
	}

	public static String getProductHref(TagNode node) throws XPatherException {
		return ((TagNode) node.evaluateXPath("/a")[0]).getAttributeByName("href").replace('"', '\'');
	}

	public static String getProductName(TagNode node) throws XPatherException {
		return ((TagNode) node.evaluateXPath("/a")[0]).getAttributeByName("title").replace('"', '\'');
	}

	public static String getProductImage(TagNode node) throws XPatherException {
		return ((TagNode) node.evaluateXPath("/a/img")[0]).getAttributeByName("src").replace('"', '\'');
	}

	public static String getProductPrice(TagNode node) throws XPatherException {
		return (node.evaluateXPath("/div[@class=\"price-box\"]/span[@class = \"special-price\"]/text()")[0]).toString()
				.trim().replace('"', '\'');
	}

	public static String getCategoryName(TagNode node) throws XPatherException {
		return ((TagNode) node.evaluateXPath("/div/div[1]/a")[0]).getText().toString().replace('"', '\'');
	}

	public static String getCategoryHref(TagNode node) throws XPatherException {
		return ((TagNode) node.evaluateXPath("/div/div[1]/a")[0]).getAttributeByName("href").replace('"', '\'');
	}
}
