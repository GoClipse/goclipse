/*******************************************************************************
 * Copyright (c) 2008, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.editor;

import com.googlecode.goclipse.tooling.oracle.GoFindDocOperation;
import com.googlecode.goclipse.ui.actions.GoOpenDefinitionOperation;

import melnorme.lang.ide.ui.editor.hover.AbstractDocHover;
import melnorme.lang.tooling.common.ISourceBuffer;

/**
 * Standard documentation hover.
 * (used in editor hovers extensions, and editor information provider (F2))
 */
public class GoDocTextHover extends AbstractDocHover {
	
	public GoDocTextHover() {
	}
	
	@Override
	protected OpenDocumentationOperation getOpenDocumentationOperation(ISourceBuffer sourceBuffer, int offset) {
		GoFindDocOperation goFindDocOperation = new GoFindDocOperation(
			GoOpenDefinitionOperation.getFindDefinitionOperation(sourceBuffer, offset));
		return new OpenDocumentationOperation("Get Documentation", goFindDocOperation);
	}
	
}