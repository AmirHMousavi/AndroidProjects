/**
 * @title LNUreader the final project of course 2DV106 Android For Java Programmers
 * @author sm222cf (SeyedAmirhossein Mousavi)
 * @author sm222cf ms222jg (Mohsen Sadeghi gol)
 * @date autumn semester 2013
 * @university Linnaeus University Växjö
 */
package com.example.lnureader.rssstuff;

import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class RssReader {

	private String rssUrl;

	public RssReader(String rssUrl) {
		this.rssUrl = rssUrl;

	}

	public List<RssItem> getItems() throws Exception {

		List<RssItem> temp = null;
		// SAX parse RSS data
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		RssParseHandler handler = new RssParseHandler();

		saxParser.parse(rssUrl, handler);
		temp = handler.getItems();
		return temp;

	}

	public ChannelInfo getInfo() throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		RssParseHandler handler = new RssParseHandler();

		saxParser.parse(rssUrl, handler);

		return handler.getInfo();

	}

}
