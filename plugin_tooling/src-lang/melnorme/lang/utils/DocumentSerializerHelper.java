/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import melnorme.utilbox.core.CommonException;

public abstract class DocumentSerializerHelper<ELEMENT> {
	
	protected DocumentBuilder getDocumentBuilder() throws CommonException {
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch(ParserConfigurationException e) {
			throw new CommonException("Error obtaining XML DocumentBuilder: ", e);
		}
	}
	
	public String writeToString(ELEMENT buildTargets) throws CommonException {
		Document doc = getDocumentBuilder().newDocument();
		writeDocument(doc, buildTargets);
		return documentToString(doc);
	}
	
	protected abstract void writeDocument(Document doc, ELEMENT buildTargets);
	
	/* ----------------- read ----------------- */
	
	public abstract ELEMENT readFromString(String targetsXml) throws CommonException;
	
	public String documentToString(Document doc) throws TransformerFactoryConfigurationError, CommonException {
		Transformer transformer;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch(TransformerConfigurationException e) {
			throw new CommonException("Error obtaining XML Transformer: ", e);
		}
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		StringWriter writer = new StringWriter();
		try {
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
		} catch(TransformerException e) {
			throw new CommonException("Error writing XML: ", e);
		}
		return writer.toString();
	}
	
	protected static void setOptionalAttribute(Element targetElem, String name, String value) {
		if(value != null) {
			targetElem.setAttribute(name, value);
		}
	}
	
	/* -----------------  ----------------- */
	
	protected Document parseDocumentFromXml(String xmlSource) throws CommonException {
		try {
			return getDocumentBuilder().parse(new InputSource(new StringReader(xmlSource)));
		} catch(SAXException | IOException e) {
			throw new CommonException("Unable to parse XML", e);
		}
	}
	
	public static String getAttribute(Node targetElem, String keyName, String defaultValue) {
		Node attribute = targetElem.getAttributes().getNamedItem(keyName);
		if(attribute == null) {
			return defaultValue;
		}
		return attribute.getTextContent();
	}
	
	public static boolean getBooleanAttribute(Node targetElem, String keyName, boolean defaultValue) {
		String enabledStr = getAttribute(targetElem, keyName, null);
		if(enabledStr == null) {
			return defaultValue;
		}
		return Boolean.parseBoolean(enabledStr);
	}
	
}