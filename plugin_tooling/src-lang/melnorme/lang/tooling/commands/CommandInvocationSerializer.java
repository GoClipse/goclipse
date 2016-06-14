/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.commands;

import static melnorme.utilbox.misc.CollectionUtil.nullToEmpty;

import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import melnorme.lang.utils.DocumentSerializerHelper;
import melnorme.utilbox.collections.HashMap2;
import melnorme.utilbox.collections.MapAccess;
import melnorme.utilbox.core.CommonException;

public class CommandInvocationSerializer extends DocumentSerializerHelper<CommandInvocation> {
	
	public static final String PROP_COMMAND_INVOCATION = "command_invocation";
	
	private static final String PROP_ARGUMENTS = "command_arguments";
	private static final String ELEM_ENV_VARS = "env_vars";
	private static final String PROP_APPEND_ENV = "append_env";
	
	private static final String ELEM_ENTRY = "entry";
	private static final String PROP_KEY = "key";
	
	@Override
	protected void writeDocument(Document doc, CommandInvocation command) {
		writeToParent(doc, command);
	}
	
	public void writeToParent(Node parentElement, CommandInvocation command) {
		if(command != null) {
			Element buildCommandElem = getDocumentParent(parentElement).createElement(PROP_COMMAND_INVOCATION);
			parentElement.appendChild(buildCommandElem);
			writeToElement(buildCommandElem, command);
		}
	}
	
	public void writeToElement(Element targetElem, CommandInvocation buildCommand) {
		setAttribute(targetElem, PROP_ARGUMENTS, buildCommand.commandLine);
		setAttribute(targetElem, PROP_APPEND_ENV, Boolean.toString(buildCommand.appendEnvironment));
		
		Element envVars = addChild(targetElem, ELEM_ENV_VARS);
		writeEnvVars(envVars, buildCommand.environmentVars);
	}
	
	public void writeEnvVars(Element envVarsElement, MapAccess<String, String> envVars) {
		for (Entry<String, String> entry : nullToEmpty(envVars)) {
			Element entryElement = addChild(envVarsElement, ELEM_ENTRY);
			entryElement.setAttribute(PROP_KEY, entry.getKey());
			addTextChild(entryElement, entry.getValue());
		}
	}
	
	/* ----------------- read ----------------- */
	
	@Override
	public CommandInvocation doReadFromString(String targetsXml) throws CommonException {
		Document doc = parseDocumentFromXml(targetsXml);
		
		for(int ix = 0; ix < doc.getChildNodes().getLength(); ix++) {
			Node node = doc.getChildNodes().item(ix);
			if(node.getNodeName().equals(CommandInvocationSerializer.PROP_COMMAND_INVOCATION)) {
				return readFromNode(node);
			}
		}
		return null;
	}
	
	public CommandInvocation readFromNode(Node cmdElement) {
		
		String commandArguments = getAttribute(cmdElement, PROP_ARGUMENTS, "");
		boolean appendEnv = getBooleanAttribute(cmdElement, PROP_APPEND_ENV, true);
		
		HashMap2<String, String> envVars = new HashMap2<>();
		
		for(int ix = 0; ix < cmdElement.getChildNodes().getLength(); ix++) {
			Node node = cmdElement.getChildNodes().item(ix);
			if(node.getNodeName().equals(CommandInvocationSerializer.ELEM_ENV_VARS)) {
				readEnvVars(envVars, node);
			}
		}
		
		return new CommandInvocation(commandArguments, envVars, appendEnv);
	}
	
	public void readEnvVars(HashMap2<String, String> envVars, Node envVarsNode) {
		for(int ix = 0; ix < envVarsNode.getChildNodes().getLength(); ix++) {
			Node child = envVarsNode.getChildNodes().item(ix);
			if(child.getNodeType() != Node.ELEMENT_NODE || !child.getNodeName().equals(ELEM_ENTRY)) {
				continue;
			}
			
			String key = getAttribute(child, PROP_KEY, null);
			if(key == null) {
				continue;
			}
			String entryValue = child.getTextContent();
			envVars.put(key, entryValue);
		}
	}
	
}