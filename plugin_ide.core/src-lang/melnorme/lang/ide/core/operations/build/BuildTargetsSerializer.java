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

import static melnorme.lang.ide.core.LangCore_Actual.VAR_NAME_SdkToolPath;
import static melnorme.lang.ide.core.operations.build.VariablesResolver.variableRefString;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.utils.DocumentSerializerHelper;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Collection2;
import melnorme.utilbox.core.CommonException;

public class BuildTargetsSerializer extends DocumentSerializerHelper {

	private static final String BUILD_TARGETS_ElemName = "build_targets";
	private static final String TARGET_ElemName = "target";
	private static final String PROP_NAME = "config";
	private static final String PROP_ENABLED = "n_enabled";
	private static final String PROP_AUTO_ENABLED = "auto_enabled";
	private static final String PROP_FORMAT_VERSION2 = "version2";
	private static final String PROP_ARGUMENTS = "options";
	private static final String PROP_EXE_PATH = "exe_path";
	
	/* -----------------  ----------------- */
	
	protected final BuildManager buildManager;
	
	public BuildTargetsSerializer(BuildManager buildManager) {
		this.buildManager = buildManager;
	}
	
	public String writeProjectBuildInfo(ProjectBuildInfo projectBuildInfo) throws CommonException {
		Collection2<BuildTarget> buildTargets = projectBuildInfo.getBuildTargets();
		return writeProjectBuildInfo(buildTargets.map((elem) -> elem.getData()));
	}
	
	public String writeProjectBuildInfo(Iterable<? extends BuildTargetDataView> buildTargets) throws CommonException {
		Document doc = getDocumentBuilder().newDocument();
		writeDocument(doc, buildTargets);
		
		return documentToString(doc);
	}
	
	protected void writeDocument(Document doc, Iterable<? extends BuildTargetDataView> buildTargets) {
		Element buildTargetsElem = doc.createElementNS(LangCore.PLUGIN_ID, BUILD_TARGETS_ElemName);
		doc.appendChild(buildTargetsElem);
		
		for(BuildTargetDataView buildTarget : buildTargets) {
			buildTargetsElem.appendChild(writeBuildTargetElement(doc, buildTarget));
		}
		
	}
	
	public ArrayList2<BuildTargetData> readProjectBuildInfo(String targetsXml) throws CommonException {
		Document doc = parseDocumentFromXml(targetsXml);
		
		Node buildTargetsElem = doc.getFirstChild();
		if(buildTargetsElem == null || !buildTargetsElem.getNodeName().equals(BUILD_TARGETS_ElemName)) {
			throw new CommonException("Expected element " + BUILD_TARGETS_ElemName + ".");
		}
		
		ArrayList2<BuildTargetData> buildTargets = new ArrayList2<>();
		
		Node targetElem = buildTargetsElem.getFirstChild();
		for(; targetElem != null; targetElem = targetElem.getNextSibling() ) {
			if(targetElem.getNodeType() == Node.TEXT_NODE) {
				continue;
			}
			buildTargets.add(readBuildTargetElement(targetElem));
		}
		
		return buildTargets;
	}
	
	protected Element writeBuildTargetElement(Document doc, BuildTargetDataView btd) {
		Element targetElem = doc.createElement(TARGET_ElemName);
		
		targetElem.setAttribute(PROP_NAME, btd.getTargetName());
		targetElem.setAttribute(PROP_ENABLED, Boolean.toString(btd.isNormalBuildEnabled()));
		targetElem.setAttribute(PROP_AUTO_ENABLED, Boolean.toString(btd.isAutoBuildEnabled()));
		targetElem.setAttribute(PROP_FORMAT_VERSION2, "true");
		
		setOptionalAttribute(targetElem, PROP_ARGUMENTS, btd.getBuildArguments());
		setOptionalAttribute(targetElem, PROP_EXE_PATH, btd.getExecutablePath());
		
		return targetElem;
	}
	
	protected BuildTargetData readBuildTargetElement(Node targetElem) throws CommonException {
		String nodeName = targetElem.getNodeName();
		if(nodeName.equals(TARGET_ElemName)) {
			
			BuildTargetData buildTargetData = new BuildTargetData();
			buildTargetData.normalBuildEnabled = getBooleanAttribute(targetElem, PROP_ENABLED, false);
			buildTargetData.autoBuildEnabled = getBooleanAttribute(targetElem, PROP_AUTO_ENABLED, false);
			buildTargetData.targetName = getAttribute(targetElem, PROP_NAME, "");
			buildTargetData.buildArguments = getAttribute(targetElem, PROP_ARGUMENTS, null);
			buildTargetData.executablePath = getAttribute(targetElem, PROP_EXE_PATH, null);
			
			if(getAttribute(targetElem, PROP_FORMAT_VERSION2, null) == null) {
				// we have a version 1 format
				String buildArgs_old = getAttribute(targetElem, PROP_ARGUMENTS, null);
				if(buildArgs_old != null) {
					// Try to migrate from previous settings format
					buildTargetData.buildArguments = variableRefString(VAR_NAME_SdkToolPath) + " " + buildArgs_old; 
				}
			}
			
			return createBuildTarget(targetElem, buildTargetData);
		} else {
			throw new CommonException("XML element not recognized : " + nodeName);
		}
	}
	
	protected BuildTargetData createBuildTarget(@SuppressWarnings("unused") Node targetElem, 
			BuildTargetData buildTargetData) {
		return buildTargetData;
	}
	
}