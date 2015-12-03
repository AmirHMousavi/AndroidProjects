/**
 * @title LNUreader the final project of course 2DV106 Android For Java Programmers
 * @author sm222cf (SeyedAmirhossein Mousavi)
 * @author sm222cf ms222jg (Mohsen Sadeghi gol)
 * @date autumn semester 2013
 * @university Linnaeus University Växjö
 */
package com.example.lnureader.rssstuff;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ChannelReader {

	private String channelURL;

	
	public ChannelReader(String channelURL) {
		this.channelURL = channelURL;
	}



	public ChannelInfo getInfo() throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		ChannelHandler handler = new ChannelHandler();

		saxParser.parse(channelURL, handler);

		return handler.getInfo();

	}

}
