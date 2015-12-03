/**
 * @title LNUreader the final project of course 2DV106 Android For Java Programmers
 * @author sm222cf (SeyedAmirhossein Mousavi)
 * @author sm222cf ms222jg (Mohsen Sadeghi gol)
 * @date autumn semester 2013
 * @university Linnaeus University Växjö
 */
package com.example.lnureader.rssstuff;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RssParseHandler extends DefaultHandler {

	private List<RssItem> rssItems;

	// Used to reference item while parsing
	private RssItem currentItem;
	private ChannelInfo currentChannel;

	private boolean parsingTitle;
	private boolean parsingLink;
	private boolean parsingDescription;
	private boolean parsingDate;
	private boolean parsingThumbnail;
	private boolean parsingLBD;

	public RssParseHandler() {
		rssItems = new ArrayList<RssItem>();
	}

	public List<RssItem> getItems() {
		return rssItems;
	}

	public ChannelInfo getInfo() {
		return currentChannel;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if ("item".equals(qName)) {
			currentItem = new RssItem();
		} else if ("channel".equals(qName)) {
			currentChannel = new ChannelInfo();
		} else if ("title".equals(qName)) {
			parsingTitle = true;
		} else if ("link".equals(qName)) {
			parsingLink = true;
		} else if ("description".equals(qName)) {
			parsingDescription = true;
		} else if ("pubDate".equals(qName)) {
			parsingDate = true;
		} else if (localName.trim().equals("thumbnail")) {
			parsingThumbnail = true;
			if (currentItem != null)
				currentItem.setThumbnailLink(attributes.getValue("url"));
			else if (currentChannel != null)
				currentChannel.setLogoLink(attributes.getValue("url"));
		} else if ("lastBuildDate".equals(qName)) {
			parsingLBD = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if ("item".equals(qName)) {
			rssItems.add(currentItem);
			currentItem = null;
		} else if ("title".equals(qName)) {
			parsingTitle = false;
		} else if ("link".equals(qName)) {
			parsingLink = false;
		} else if ("description".equals(qName)) {
			parsingDescription = false;
		} else if ("pubDate".equals(qName)) {
			parsingDate = false;
		} else if (localName.trim().equals("thumbnail")) {
			parsingThumbnail = false;
		} else if ("lastBuildDate".equals(qName)) {
			parsingLBD = false;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (parsingTitle) {
			if (currentItem != null)
				currentItem.setTitle(new String(ch, start, length));
			else
				currentChannel.setChannelName(new String(ch, start, length));
		} else if (parsingLink) {
			if (currentItem != null) {
				currentItem.setLink(new String(ch, start, length));
				parsingLink = false;
			}
		} else if (parsingDescription) {
			if (currentItem != null) {
				Character c = new Character('<');
				if (c.compareTo(ch[start]) == 0)
					currentItem
							.setDescription(" ");
				else
					currentItem.setDescription(new String(ch, start, length));
				parsingDescription = false;
			}
		} else if (parsingDate) {
			if (currentItem != null) {
				currentItem.setDate(new String(ch, start, length));
				parsingDate = false;
			}
		} else if (parsingLBD) {
			if (currentChannel != null) {
				currentChannel
						.setLastModification(new String(ch, start, length));
				parsingLBD = false;
			}
		}
	}

}
