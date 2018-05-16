package com.mokhonich.coursework.olx;

import java.util.List;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.jsoup.select.Elements;

import com.mokhonich.coursework.SeleniumPage;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

public class HtmlCleanerOlx {
	
	private static OlxDatabaseController controller = new OlxDatabaseController();
	
	public static void main(String[] args) throws XPatherException {
		HtmlCleaner cleaner = new HtmlCleaner();
		String url = "https://www.olx.ua/moda-i-stil/odezhda/kiev/?search%5Bprivate_business%5D=private&search%5Bpaidads_listing%5D=1";
		String page = SeleniumPage.getPageSource(url);
		TagNode html = cleaner.clean(page);
		controller.openDatabaseConnection();
		getAdvertInfo(html);
		controller.closeConnection();
	}

	public static String getNextHref(TagNode html) throws XPatherException {
		String next= "";
		List<TagNode> childs = ((TagNode) html.evaluateXPath("//*[@class=\"pager rel clr\"]")[0]).getChildren();
		TagNode last = childs.get(childs.size()-1);
		next =  ((TagNode)last.evaluateXPath("/a")[0]).getAttributeByName("href").toString();
		return next.replace('"', '\'');
	}

	public static TagNode openConnection(String url) {
		HtmlCleaner cleaner = new HtmlCleaner();
		String page = SeleniumPage.getPageSource(url);
		return cleaner.clean(page);
	}

	public static void getAdvertInfo(TagNode html) throws XPatherException {
		// Elements adverts = page.getElementsByClass("wrap");
		//// *[@id="body-container"]/div[3]/div/div[4]
		// #body-container > div:nth-child(3) > div > div.pager.rel.clr
		//

		String nextHref = getNextHref(html);
		Object[] adverts = html.evaluateXPath("//tr[@class=\"wrap\"]");

		for (Object obj : adverts) {
			TagNode node = (TagNode) obj;

			String advHref = getAdvHref(node);// посиланя
			String advTitle = getAdvTitle(node);// тайтл
			String cat = getAdvCategory(node);
			String subCat = getAdvSubcategory(node);
			String advRegion = getAdvRegion(node);// район
			String advPrice = getAdvPrice(node);// ціна
			String advImg = getAdvImg(node);// фото*/

			System.out.println(advHref);
			System.out.println(advTitle);
			System.out.println(cat);
			System.out.println(subCat);
			System.out.println(advRegion);
			System.out.println(advPrice);
			System.out.println(advImg);
			controller.addPoducts(advTitle, advHref, advPrice, advImg, advRegion);
		}
		
		if(nextHref!=null) {
			System.out.println("1111111111111111111111111111111111");
			getAdvertInfo(openConnection(nextHref));
		}

	}

	private static String getAdvImg(TagNode node) throws XPatherException {
		// TODO Auto-generated method stub
		TagNode temp  = ((TagNode) node.evaluateXPath("//a/img")[0]);
		if(temp!=null){
		return temp.getAttributeByName("src").toString().replace('"', '\'');
		}
		return "";
	}

	private static String getAdvPrice(TagNode node) throws XPatherException {
		// //*[@id="offers_table"]/tbody/tr[41]/td/table/tbody/tr[1]/td[3]/div/p/strong
		return ((TagNode) node.evaluateXPath("//p/strong")[0]).getText().toString().trim().replace('"', '\'');
	}

	private static String getAdvRegion(TagNode node) throws XPatherException {
		// //*[@id="offers_table"]/tbody/tr[41]/td/table/tbody/tr[2]/td[1]/div/p[1]/small/span
		return ((TagNode) node.evaluateXPath("//p[1]/small")[1]).getText().toString().trim().replace('"', '\'');
	}

	private static String getAdvSubcategory(TagNode node) throws XPatherException {

		String catAndSubCat = ((TagNode) node.evaluateXPath("//p/small")[0]).getText().toString().trim().replace('"', '\'');
		String[] temp = catAndSubCat.split(" ");

		String subCategory = "";

		for (int i = 2; i < temp.length; i++) {
			subCategory += " " + temp[i];
		}
		return subCategory.trim().replace('"', '\'');

	}

	private static String getAdvCategory(TagNode node) throws XPatherException {
		String catAndSubCat = ((TagNode) node.evaluateXPath("//p/small")[0]).getText().toString().trim();
		return catAndSubCat.split(" ")[0].replace('"', '\'');
	}

	private static String getAdvTitle(TagNode node) throws XPatherException {
		return ((TagNode) node.evaluateXPath("//a/strong")[0]).getText().toString().replace('"', '\'');
	}

	private static String getAdvHref(TagNode node) throws XPatherException {
		return ((TagNode) node.evaluateXPath("//a")[0]).getAttributeByName("href").toString().replace('"', '\'');
	}
}
