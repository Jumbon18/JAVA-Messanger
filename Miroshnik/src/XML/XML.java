package XML;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class XML {
	
	
	public static XMLResponse parse(String XML) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		Handler handler = new Handler();
		try {
		SAXParser parser = factory.newSAXParser();
		XMLReader xmlReader = parser.getXMLReader();
		xmlReader.setContentHandler(handler);	
		xmlReader.parse(new InputSource(new ByteArrayInputStream(XML.getBytes("utf-8"))));
		} catch (Exception e) {}
		return handler.getResponse();
	}
	
	public static String login(String name) {
		try 
		{
			StringWriter stringOut = new StringWriter();
			XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
			out.writeStartDocument();
			out.writeStartElement("xml");
			out.writeStartElement("type");
			out.writeCharacters("login");
			out.writeEndElement();
			out.writeStartElement("login");
			out.writeCharacters(name);
			out.writeEndElement();
			out.writeEndElement();
			out.writeEndDocument();
			out.close();
			return stringOut.toString();
		} catch (Exception e) {}
		return "";
	}
	
	public static String getUsers() {
		try 
		{
			StringWriter stringOut = new StringWriter();
			XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
			out.writeStartDocument();
			out.writeStartElement("xml");
			out.writeStartElement("type");
			out.writeCharacters("getUsers");
			out.writeEndElement();
			out.writeEndElement();
			out.writeEndDocument();
			out.close();
			return stringOut.toString();
		} catch (Exception e) {}
		return "";
	}
	
	public static String logout(String name) {
		try 
		{
			StringWriter stringOut = new StringWriter();
			XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
			out.writeStartDocument();
			out.writeStartElement("xml");
			out.writeStartElement("type");
			out.writeCharacters("logout");
			out.writeEndElement();
			out.writeStartElement("login");
			out.writeCharacters(name);
			out.writeEndElement();
			out.writeEndElement();
			out.writeEndDocument();
			out.close();
			return stringOut.toString();
		} catch (Exception e) {}
		return "";
	}
	
	public static String send(String from, String to, String message) {
		try 
		{
			StringWriter stringOut = new StringWriter();
			XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
			out.writeStartDocument();
			out.writeStartElement("xml");
			out.writeStartElement("type");
			out.writeCharacters("send");
			out.writeEndElement();
			out.writeStartElement("message");
			out.writeStartElement("who");
			out.writeCharacters(from);
			out.writeEndElement();
			out.writeStartElement("whom");
			out.writeCharacters(to);
			out.writeEndElement();
			out.writeStartElement("text");
			out.writeCharacters(message);
			out.writeEndElement();
			out.writeEndElement();
			out.writeEndElement();
			out.writeEndDocument();
			out.close();
			return stringOut.toString();
		} catch (Exception e) {}
		return "";
	}
	
	public static String users(List<String> allUsers) {
		try 
		{
			StringWriter stringOut = new StringWriter();
			XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
			out.writeStartDocument();
			out.writeStartElement("xml");
			out.writeStartElement("type");
			out.writeCharacters("users");
			out.writeEndElement();
			for(String user : allUsers) {
				out.writeStartElement("message");
				out.writeStartElement("who");
				out.writeCharacters(user);
				out.writeEndElement();
				out.writeEndElement();
			}
			out.writeEndElement();
			out.writeEndDocument();
			out.close();
			return stringOut.toString();
		} catch (Exception e) {}
		return "";
	}
	
	public static String getMessages(String from, String to) {
		try 
		{
			StringWriter stringOut = new StringWriter();
			XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
			out.writeStartDocument();
			out.writeStartElement("xml");
			out.writeStartElement("type");
			out.writeCharacters("getMessages");
			out.writeEndElement();
			out.writeStartElement("message");
			out.writeStartElement("who");
			out.writeCharacters(from);
			out.writeEndElement();
			out.writeStartElement("whom");
			out.writeCharacters(to);
			out.writeEndElement();
			out.writeEndElement();
			out.writeEndElement();
			out.writeEndDocument();
			out.close();
			return stringOut.toString();
		} catch (Exception e) {}
		return "";
	}
	
	public static String messages(List<String> allMessages) {
		try 
		{
			StringWriter stringOut = new StringWriter();
			XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
			out.writeStartDocument();
			out.writeStartElement("xml");
			out.writeStartElement("type");
			out.writeCharacters("users");
			out.writeEndElement();
			for(String message : allMessages) {
				out.writeStartElement("message");
				out.writeStartElement("text");
				out.writeCharacters(message);
				out.writeEndElement();
				out.writeEndElement();
			}
			out.writeEndElement();
			out.writeEndDocument();
			out.close();
			return stringOut.toString();
		} catch (Exception e) {}
		return "";
	}	
	
private static class Handler extends DefaultHandler {
		private Format format;
		private XMLResponse xml;
		private XMLMessage message;
		private String currentTag;
		@Override
		public void startElement(String uri, String localName, String tag, Attributes attributes) throws SAXException {
			currentTag = tag;
			switch(currentTag) {
			case "xml": {
				xml = new XMLResponse();
				xml.ourMessage = new ArrayList<>();
			} break;
			case "message": {
				message = new XMLMessage();
			} break;
			default: {}
			}
		}
		@Override
		public void characters(char[] characters, int start, int lenght) throws SAXException {
			String text = new String (characters, start, lenght);
			switch (currentTag) {
			case "type": {
				xml.type = text;
			} break;
			case "who": {
				message.from = text;
			} break;
			case "whom": {
				message.to = text;
			} break;
			case "text": {
				message.message = text;
			} break;
			case "status": {
				xml.status = text;
			} break;
			case "login": {
				xml.name = text;
			} break;
			default: {}
			}
		}
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			switch(qName) {
			case "message": {
				xml.ourMessage.add(message);
			} break;
			}
		}
		
		XMLResponse getResponse() {
			return xml;
		}
	}
}
