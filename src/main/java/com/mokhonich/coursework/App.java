package com.mokhonich.coursework;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.htmlcleaner.XPatherException;
import org.xml.sax.SAXException;

import com.mokhonich.coursework.olx.*;
import com.mokhonich.coursework.news.*;
import com.mokhonich.coursework.catalog.*;

/**
 * @author Mokhonich Oksana
 *
 */
public class App 
{
	public static void main(String[] args)
			throws Exception
    {
		//test parsers of news
		ParseNewsWithJsoup.testJsoupNews();
		ParseNewsWithJericho.testJerichoNews();
		ParseNewsWithHtmlCleaner.testHtmlCLeanerNews();
		ParseNewsWithTagSoup.testTaSoupNews();
		ParsingNewsWithNeko.testNekoNews();
		
		//test parsers of olx
    	JerichoOlx.testJerichoOlx();
    	JsoupOlx.testJsoupOlx();
    	HtmlCleanerOlx.testHtmlCleanerOlx();
        NekoOlx.testNekoOLx();
        TagSoupOlx.testTagSoupOlx();
        
        //test parsers of catalog
        JerichoCatalog.testJerichoCatalog();
        JsoupCatalog.testJsoupCatalog();
        HtmlCleanerCatalog.testHtmlCleanerCatalog();
        TagSoupCatalog.testTagsoupCataog();
    }
}
