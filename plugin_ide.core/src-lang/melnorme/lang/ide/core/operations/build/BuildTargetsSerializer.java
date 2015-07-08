/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.operations.build;

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
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.project_model.AbstractBundleInfo.BuildConfiguration;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;

public class BuildTargetsSerializer {
	
	private static final String BUILD_TARGETS_ElemName = "build_targets";
	private static final String TARGET_ElemName = "target";
	private static final String PROP_NAME = "name";
	private static final String PROP_ENABLED = "enabled";
	
	protected static final BuildConfiguration DUMMY_BUILD_CONFIG = new BuildConfiguration("", null);
	
	/* -----------------  ----------------- */
	
	protected final BuildManager buildManager;
	
	public BuildTargetsSerializer(BuildManager buildManager) {
		this.buildManager = buildManager;
	}
	
	protected DocumentBuilder getDocumentBuilder() throws CommonException {
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch(ParserConfigurationException e) {
			throw new CommonException("Error obtaining XML DocumentBuilder: ", e);
		}
	}
	
	public String writeProjectBuildInfo(ProjectBuildInfo projectBuildInfo) throws CommonException {
		return writeProjectBuildInfo(projectBuildInfo.getBuildTargets());
	}
	
	public String writeProjectBuildInfo(Iterable<BuildTarget> buildTargets) throws CommonException {
		
		Document doc = getDocumentBuilder().newDocument();
		writeDocument(doc, buildTargets);
		
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
	
	protected void writeDocument(Document doc, Iterable<BuildTarget> buildTargets) {
		Element buildTargetsElem = doc.createElementNS(LangCore.PLUGIN_ID, BUILD_TARGETS_ElemName);
		doc.appendChild(buildTargetsElem);
		
		for(BuildTarget buildTarget : buildTargets) {
			buildTargetsElem.appendChild(createTargetElement(doc, buildTarget));
		}
		
	}
	
	protected Element createTargetElement(Document doc, BuildTarget buildTarget) {
		Element targetElem = doc.createElement(TARGET_ElemName);
		targetElem.setAttribute(PROP_ENABLED, Boolean.toString(buildTarget.isEnabled()));
		
		String targetName = buildTarget.getTargetName();
		targetElem.setAttribute(PROP_NAME, targetName);
		return targetElem;
	}
	
	
	public ArrayList2<BuildTarget> readProjectBuildInfo(String targetsXml) throws CommonException {
		Document doc;
		try {
			doc = getDocumentBuilder().parse(new InputSource(new StringReader(targetsXml)));
		} catch(SAXException | IOException e) {
			throw new CommonException("Unable to parse XML", e);
		}
		
		Node buildTargetsElem = doc.getFirstChild();
		if(buildTargetsElem == null || !buildTargetsElem.getNodeName().equals(BUILD_TARGETS_ElemName)) {
			throw new CommonException("Expected element " + BUILD_TARGETS_ElemName + ".");
		}
		
		ArrayList2<BuildTarget> buildTargets = new ArrayList2<>();
		
		Node targetElem = buildTargetsElem.getFirstChild();
		for(; targetElem != null; targetElem = targetElem.getNextSibling() ) {
			if(targetElem.getNodeType() == Node.TEXT_NODE) {
				continue;
			}
			buildTargets.add(readTargetElement(targetElem));
		}
		
		return buildTargets;
	}
	
	protected BuildTarget readTargetElement(Node targetElem) throws CommonException {
		String nodeName = targetElem.getNodeName();
		if(nodeName.equals(TARGET_ElemName)) {
			boolean enabled = getBooleanAttribute(targetElem, PROP_ENABLED, false);
			String targetName = getAttribute(targetElem, PROP_NAME, null);
			
			return createBuildTarget(enabled, targetName, targetElem);
		} else {
			throw new CommonException("XML element not recognized : " + nodeName);
		}
	}
	
	protected BuildTarget createBuildTarget(boolean enabled, String targetName, 
			@SuppressWarnings("unused") Node targetElem) {
		return buildManager.createBuildTarget(targetName, DUMMY_BUILD_CONFIG, enabled);
	}
	
	protected String getAttribute(Node targetElem, String keyName, String defaultValue) {
		Node attribute = targetElem.getAttributes().getNamedItem(keyName);
		if(attribute == null) {
			return defaultValue;
		}
		return attribute.getTextContent();
	}
	
	protected boolean getBooleanAttribute(Node targetElem, String keyName, boolean defaultValue) {
		String enabledStr = getAttribute(targetElem, keyName, null);
		if(enabledStr == null) {
			return defaultValue;
		}
		return Boolean.parseBoolean(enabledStr);
	}
	
}