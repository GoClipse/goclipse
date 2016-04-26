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
package melnorme.lang.ide.core.operations.build;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import melnorme.lang.utils.DocumentSerializerHelper;
import melnorme.utilbox.core.CommonException;

public class CommandInvocationSerializer extends DocumentSerializerHelper<CommandInvocation> {
	
	public static final String PROP_COMMAND_INVOCATION = "command_invocation";
	
	private static final String PROP_ARGUMENTS = "command_arguments";
	
	public void writeToElement(Element targetElem, CommandInvocation buildCommand) {
		setOptionalAttribute(targetElem, PROP_ARGUMENTS, buildCommand.commandArguments);
	}
	
	@Override
	protected void writeDocument(Document doc, CommandInvocation command) {
		writeToParent(doc, command);
	}
	
	public void writeToParent(Node parentElement, CommandInvocation command) {
		if(command != null) {
			Element buildCommandElem = parentElement.getOwnerDocument().createElement(PROP_COMMAND_INVOCATION);
			writeToElement(buildCommandElem, command);
			parentElement.appendChild(buildCommandElem);
		}
	}
	
	/* ----------------- read ----------------- */
	
	@Override
	public CommandInvocation readFromString(String targetsXml) throws CommonException {
		if(targetsXml == null) {
			return null;
		}
		Document doc = parseDocumentFromXml(targetsXml);
		return readFromNode(doc.createElement(PROP_COMMAND_INVOCATION));
	}
	
	public CommandInvocation readFromNode(Node node) {
		String commandArguments = getAttribute(node, PROP_ARGUMENTS, null);
		if(commandArguments != null) {
			return new CommandInvocation(commandArguments);
		}
		return null;
	}
	
}