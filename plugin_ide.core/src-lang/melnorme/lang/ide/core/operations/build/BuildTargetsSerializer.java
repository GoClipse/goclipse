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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.build.BuildTarget.BuildTargetData;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.utils.DocumentSerializerHelper;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;

public class BuildTargetsSerializer extends DocumentSerializerHelper {

	private static final String BUILD_TARGETS_ElemName = "build_targets";
	private static final String TARGET_ElemName = "target";
	private static final String PROP_NAME = "config";
	private static final String PROP_ENABLED = "enabled";
	private static final String PROP_ARGUMENTS = "options";
	private static final String PROP_EXE_PATH = "exe_path";
	
	/* -----------------  ----------------- */
	
	protected final BuildManager buildManager;
	
	public BuildTargetsSerializer(BuildManager buildManager) {
		this.buildManager = buildManager;
	}
	
	public String writeProjectBuildInfo(ProjectBuildInfo projectBuildInfo) throws CommonException {
		return writeProjectBuildInfo(projectBuildInfo.getBuildTargets());
	}
	
	public String writeProjectBuildInfo(Iterable<BuildTarget> buildTargets) throws CommonException {
		Document doc = getDocumentBuilder().newDocument();
		writeDocument(doc, buildTargets);
		
		return documentToString(doc);
	}
	
	protected void writeDocument(Document doc, Iterable<BuildTarget> buildTargets) {
		Element buildTargetsElem = doc.createElementNS(LangCore.PLUGIN_ID, BUILD_TARGETS_ElemName);
		doc.appendChild(buildTargetsElem);
		
		for(BuildTarget buildTarget : buildTargets) {
			buildTargetsElem.appendChild(createBuildTargetElement(doc, buildTarget));
		}
		
	}
	
	public ArrayList2<BuildTarget> readProjectBuildInfo(String targetsXml) throws CommonException {
		Document doc = parseDocumentFromXml(targetsXml);
		
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
			buildTargets.add(readBuildTargetElement(targetElem));
		}
		
		return buildTargets;
	}
	
	protected Element createBuildTargetElement(Document doc, BuildTarget buildTarget) {
		Element targetElem = doc.createElement(TARGET_ElemName);
		
		targetElem.setAttribute(PROP_NAME, buildTarget.getTargetName());
		targetElem.setAttribute(PROP_ENABLED, Boolean.toString(buildTarget.isEnabled()));
		setOptionalAttribute(targetElem, PROP_ARGUMENTS, buildTarget.getBuildArguments());
		setOptionalAttribute(targetElem, PROP_EXE_PATH, buildTarget.getExecutablePath());
		
		return targetElem;
	}
	
	protected BuildTarget readBuildTargetElement(Node targetElem) throws CommonException {
		String nodeName = targetElem.getNodeName();
		if(nodeName.equals(TARGET_ElemName)) {
			
			BuildTargetData buildTargetData = new BuildTargetData();
			buildTargetData.enabled = getBooleanAttribute(targetElem, PROP_ENABLED, false);
			buildTargetData.targetName = getAttribute(targetElem, PROP_NAME, "");
			buildTargetData.buildArguments = getAttribute(targetElem, PROP_ARGUMENTS, null);
			buildTargetData.executablePath = getAttribute(targetElem, PROP_EXE_PATH, null);
			
			return createBuildTarget(targetElem, buildTargetData);
		} else {
			throw new CommonException("XML element not recognized : " + nodeName);
		}
	}
	
	protected BuildTarget createBuildTarget(@SuppressWarnings("unused") Node targetElem, 
			BuildTargetData buildTargetData) {
		return buildManager.createBuildTarget(buildTargetData);
	}
	
}