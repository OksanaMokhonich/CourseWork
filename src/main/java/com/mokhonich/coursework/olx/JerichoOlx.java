package com.mokhonich.coursework.olx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.jsoup.select.Elements;

import com.mokhonich.coursework.database.OlxDatabaseController;
import com.mokhonich.coursework.selenium.SeleniumPage;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

public class JerichoOlx {

	private static OlxDatabaseController controller = new OlxDatabaseController();

	public static void testJerichoOlx() throws IOException {
		String url = "https://www.olx.ua/moda-i-stil/odezhda/kiev/?search%5Bprivate_business%5D=private&search%5Bpaidads_listing%5D=1";

		Source source = new Source(new URL(url));

		controller.openDatabaseConnection();
		getAdvertInfo(source);
		controller.closeConnection();

	}

	public static String getNextHref(Source source) {
		String rez = "";
		List<Element> elem = source.getAllElementsByClass("pager").get(0).getAllElementsByClass("fleft");
		List<Element> nextHref = source.getAllElementsByClass("pager").get(0).getAllElementsByClass("next").get(0)
				.getAllElementsByClass("pageNextPrev");
		if (!nextHref.isEmpty()) {
			rez = nextHref.get(0).getAttributeValue("href");
		}
		return rez.replace('"', '\'');
	}

	public static void getAdvertInfo(Source source) throws IOException {
		String nextPage = "";
		nextPage = getNextHref(source);
		// System.out.println("next = " + nextPage);
		List<Element> adverts = source.getAllElementsByClass("wrap");
		for (Element elem : adverts) {
			String advHref = getAdvHref(elem);// посиланя
			String advTitle = getAdvTitle(elem);// тайтл
			String cat = getAdvCategory(elem);
			String subCat = getAdvSubcategory(elem);
			String advRegion = getAdvRegion(elem);// район
			String advPrice = getAdvPrice(elem);// ціна
			String advImg = getAdvImg(elem);// фото

			/*
			 * System.out.println(advHref); System.out.println(advTitle);
			 * System.out.println(cat); System.out.println(subCat);
			 * System.out.println(advRegion); System.out.println(advPrice);
			 * System.out.println(advImg);
			 */
			controller.addPoducts(advTitle, advHref, advPrice, advImg, advRegion);
		}

		if (nextPage != null) {
			getAdvertInfo(openConnection(nextPage));
		}

	}

	private static Source openConnection(String url) throws MalformedURLException, IOException {
		return new Source(new URL(url));
	}

	private static String getAdvImg(Element elem) {

		return elem.getAllElementsByClass("fleft").get(0).getAttributeValue("src").toString().replace('"', '\'');
	}

	private static String getAdvPrice(Element elem) {

		return elem.getAllElementsByClass("price").get(0).getChildElements().get(0).getContent().toString().replace('"',
				'\'');
	}

	private static String getAdvRegion(Element elem) {
		String region = elem.getAllElementsByClass("breadcrumb x-normal").get(1).getChildElements().get(0).getContent()
				.toString().trim();
		return region.replace('"', '\'');
	}

	private static String getAdvSubcategory(Element elem) {
		String catAndSubCat = elem.getAllElementsByClass("space").get(0).getAllElementsByClass("breadcrumb").get(0)
				.getContent().toString().trim();
		String[] temp = catAndSubCat.split(" ");

		String subCategory = "";

		for (int i = 2; i < temp.length; i++) {
			subCategory += " " + temp[i];
		}
		return subCategory.trim().replace('"', '\'');

	}

	private static String getAdvCategory(Element elem) {
		String catAndSubCat = elem.getAllElementsByClass("space").get(0).getAllElementsByClass("breadcrumb").get(0)
				.getContent().toString().trim();
		return catAndSubCat.split(" ")[0].replace('"', '\'');
	}

	private static String getAdvTitle(Element elem) {

		return elem.getAllElementsByClass("link").get(0).getChildElements().get(0).getContent().toString().trim()
				.replace('"', '\'');
	}

	private static String getAdvHref(Element elem) {

		return elem.getAllElementsByClass("link").get(0).getAttributeValue("href").replace('"', '\'');
	}
}