package com.mokhonich.coursework.olx;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupOlx {

	private static OlxDatabaseController controller = new OlxDatabaseController();

	public static void main(String[] args) throws IOException {
		String url = "https://www.olx.ua/moda-i-stil/odezhda/kiev/?search%5Bprivate_business%5D=private&search%5Bpaidads_listing%5D=1";
		controller.openDatabaseConnection();
		Document doc = openConnection(url);
		getAdvertInfo(doc);
		controller.closeConnection();
		getAdvertInfo(doc);

	}

	public static Document openConnection(String url) throws IOException {
		Document page = Jsoup.connect(url).timeout(10000).userAgent("Chrome/66.0.3359.139").get();
		return page;

	}

	public static void getAdvertInfo(Document page) throws IOException {
		String nextPage = getNextHref(page);

		Elements adverts = page.getElementsByClass("wrap");
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
			System.out.println("***********************");
		}

		if (!nextPage.equals("")) {
			getAdvertInfo(openConnection(nextPage));
		}

	}

	public static String getNextHref(Document doc) {
		String rez = "";

		Elements elem = doc.getElementsByClass("pager").get(0).getElementsByClass("fleft");

		Elements nextHref = doc.getElementsByClass("pager").get(0).getElementsByClass("next").get(0)
				.getElementsByTag("a");
		if (!nextHref.isEmpty()) {
			rez = nextHref.get(0).attr("href");
		}
		return rez.replace('"', '\'');
	}

	private static String getAdvCategory(Element elem) {
		String catAndSubcat = elem.getElementsByTag("small").get(0).text();
		String category = catAndSubcat.split(" ")[0];
		return category.replace('"', '\'');
	}

	private static String getAdvSubcategory(Element elem) {
		String catAndSubcat = elem.getElementsByTag("small").get(0).text();
		String[] temp = catAndSubcat.split(" ");

		String subCategory = "";

		for (int i = 2; i < temp.length; i++) {
			subCategory += " " + temp[i];
		}

		return subCategory.trim().replace('"', '\'');
	}

	private static String getAdvPrice(Element elem) {
		return elem.getElementsByClass("price").get(0).text().replace('"', '\'');
	}

	private static String getAdvImg(Element elem) {
		return elem.getElementsByTag("img").get(0).attr("src").replace('"', '\'');
	}

	private static String getAdvRegion(Element elem) {
		return elem.getElementsByTag("small").get(1).text().replace('"', '\'');
	}

	private static String getAdvHref(Element elem) {
		return elem.getElementsByTag("a").get(0).attr("href").replace('"', '\'');
	}

	private static String getAdvTitle(Element elem) {
		return elem.getElementsByTag("h3").get(0).getElementsByTag("strong").get(0).text().replace('"', '\'');
	}

}
