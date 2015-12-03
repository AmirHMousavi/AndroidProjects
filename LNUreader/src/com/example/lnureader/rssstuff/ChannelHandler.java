/**
 * @title LNUreader the final project of course 2DV106 Android For Java Programmers
 * @author sm222cf (SeyedAmirhossein Mousavi)
 * @author sm222cf ms222jg (Mohsen Sadeghi gol)
 * @date autumn semester 2013
 * @university Linnaeus University Växjö
 */
package com.example.lnureader.rssstuff;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ChannelHandler extends DefaultHandler {

	private ChannelInfo currentChannel;
	private boolean parsingTitle;
	private boolean parsingDescription;
	private boolean parsingLogo;
	private boolean parsingLastBuiltDate;
	private boolean dontParseAnyMore;

	public ChannelHandler() {
		currentChannel = new ChannelInfo();
	}

	public ChannelInfo getInfo() {
		return currentChannel;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if ("channel".equals(qName)) {
			currentChannel = new ChannelInfo();
		} else if ("title".equals(qName)) {
			parsingTitle = true;
		} else if ("description".equals(qName)) {
			parsingDescription = true;
		} else if ("url".equals(qName)) {
			parsingLogo = true;
		} else if ("lastBuildDate".equals(qName) || "pubDate".equals(qName)) {
			parsingLastBuiltDate = true;
		} else if ("item".equals(qName)) {
			dontParseAnyMore = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if ("channel".equals(qName)) {
		} else if ("title".equals(qName)) {
			parsingTitle = false;
		} else if ("description".equals(qName)) {
			parsingDescription = false;
		} else if ("url".equals(qName)) {
			parsingLogo = false;
		} else if ("lastBuildDate".equals(qName)) {
			parsingLastBuiltDate = false;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (!dontParseAnyMore) {
			if (parsingTitle) {
				if (currentChannel != null) {
					currentChannel
							.setChannelName(new String(ch, start, length));
				}
			} else if (parsingDescription) {
				if (currentChannel != null) {
					currentChannel
							.setDescription(new String(ch, start, length));
				}
			} else if (parsingLogo) {
				if (currentChannel != null) {
					currentChannel.setLogoLink(new String(ch, start, length));
				}
			} else if (parsingLastBuiltDate) {
				if (currentChannel != null) {
					currentChannel.setLastModification(new String(ch, start,
							length));
				}
			}
		}
	}

}
