package com.mokhonich.coursework.olx;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.cyberneko.html.HTMLConfiguration;
import org.cyberneko.html.parsers.DOMParser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mokhonich.coursework.database.OlxDatabaseController;


public class NekoOlx {
	
	private static OlxDatabaseController controller = new OlxDatabaseController();
	
	public static void testNekoOLx()
			throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
		String url = "https://www.olx.ua/moda-i-stil/odezhda/kiev/?search%5Bprivate_business%5D=private&search%5Bpaidads_listing%5D=1";
		Node node = openConnection(url);
		controller.openDatabaseConnection();
		getAdvertInfo(node);
		controller.closeConnection();

		
	}
	
	public static Node openConnection(String url) throws SAXException, IOException {
		 
		DOMParser parser = new DOMParser();
		  parser.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
		    parser.setProperty("http://cyberneko.org/html/properties/names/attrs", "lower");
		parser.parse(new org.xml.sax.InputSource(url));
		System.out.println(parser.getDocument().getFirstChild());
		return (org.w3c.dom.Node)parser.getDocument();
	}
	
	
	
	public static String getNextHref(Node node) throws XPathExpressionException {
		XPathFactory xpathFac = XPathFactory.newInstance();
		XPath theXpath = xpathFac.newXPath();
		NodeList nodes = (NodeList) theXpath.evaluate("//*[@class=\"pager rel clr\"]/*", node, XPathConstants.NODESET);
		System.out.println(nodes);
		int len = nodes.getLength();
		Node nextNode = nodes.item(len-1);
		System.out.println(nextNode.getChildNodes().item(1).getNodeName());
		String nodeName = nextNode.getChildNodes().item(1).getNodeName();
		if(nodeName.equals("A")) {
		return nextNode.getChildNodes().item(1).getAttributes().getNamedItem("href").getTextContent();
		}
		return null;
		
	}
	
	static void getAdvertInfo(Node node)
			throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
		String nextHref = getNextHref(node);
		System.out.println("next = " + nextHref);
		XPathFactory xpathFac = XPathFactory.newInstance();
		XPath theXpath = xpathFac.newXPath();
		NodeList nodes = (NodeList) theXpath.evaluate("//*[@class=\"wrap\"]/*/*/*", node, XPathConstants.NODESET);
	
		System.out.println(nodes.getLength());
		for (int i = 0; i < nodes.getLength(); i++) {
			Node temp = nodes.item(i);
			System.out.println(temp);
			/*String region = getRegion(temp);
			String title = getTitle(temp);
			String catSubCat = getSubCat(temp);
			String cat = getCat(temp);
			String href = getHref(temp);
			String imageHref = getImgHref(temp);
			String price = getPrice(temp);
			System.out.println(title);
			System.out.println(href);
			System.out.println(imageHref);
			System.out.println(price);
			System.out.println(catSubCat);
			System.out.println(cat);
			controller.addPoducts(title, href, price, imageHref, region);*/
		}
		}
		

	public static String getPrice(Node node) {
		String rez = "";
		Node firstTr = node.getFirstChild();
		Node top = firstTr.getChildNodes().item(1);

		String badPrice = top.getNextSibling().getChildNodes().item(1).getChildNodes().item(1).getTextContent();
		String goodPrice = "";
		int len = top.getNextSibling().getChildNodes().item(1).getChildNodes().getLength();
		if (len > 3) {
			goodPrice = "" + top.getNextSibling().getChildNodes().item(1).getChildNodes().item(3).getTextContent();
			badPrice = goodPrice;
		}
		boolean pr = badPrice.equals("DIV");
		if (pr) {
			return goodPrice;
		}
		return badPrice.trim().replace('"', '\'');
	}

	private static String getSubCat(Node node) {
		Node firstTr = node.getFirstChild();
		Node top = firstTr.getChildNodes().item(1);
		Node topDiv = top.getChildNodes().item(1);

		String catAndSubCat = topDiv.getChildNodes().item(3).getTextContent().trim();
		return catAndSubCat.split(" ")[0].replace('"', '\'');
	}

	private static String getCat(Node node) {
		Node firstTr = node.getFirstChild();
		Node top = firstTr.getChildNodes().item(1);
		Node topDiv = top.getChildNodes().item(1);

		String catAndSubCat = topDiv.getChildNodes().item(3).getTextContent().trim();
		String[] temp = catAndSubCat.split(" ");

		String subCategory = "";

		for (int i = 2; i < temp.length; i++) {
			subCategory += " " + temp[i];
		}
		return subCategory.trim().replace('"', '\'');
	}

	public static String getImgHref(Node node) {
		String imageHref = node.getFirstChild().getFirstChild().getChildNodes().item(1).getChildNodes().item(1)
				.getAttributes().getNamedItem("SRC").getTextContent().toString(); // img
		return imageHref.replace('"', '\'');
	}

	public static String getHref(Node node) {
		Node firstTr = node.getFirstChild();
		String href = firstTr.getFirstChild().getChildNodes().item(1).getAttributes().getNamedItem("HREF")
				.getTextContent().toString().trim();
		return href.replace('"', '\'');
	}

	public static String getTitle(Node node) {
		Node firstTr = node.getFirstChild();
		Node top = firstTr.getChildNodes().item(1);
		Node topDiv = top.getChildNodes().item(1);
		String title = topDiv.getChildNodes().item(1).getTextContent().trim();
		return title.replace('"', '\'');
	}

	public static String getRegion(Node node) {

		Node bottom = node.getChildNodes().item(1).getChildNodes().item(0);
		Node bottomDiv = bottom.getChildNodes().item(1);
		String region = bottomDiv.getChildNodes().item(1).getChildNodes().item(1).getTextContent().toString();
		return region.trim().replace('"', '\'');

	}

	
}