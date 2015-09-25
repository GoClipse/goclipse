/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.editor.text;

import melnorme.lang.ide.ui.editor.text.LangAutoEditStrategyExt;

import org.eclipse.jface.text.ITextViewer;

import com.googlecode.goclipse.core.text.GoPartitions;

public class GoAutoEditStrategy extends LangAutoEditStrategyExt {
	
	public GoAutoEditStrategy(String contentType, ITextViewer viewer) {
		super(GoPartitions.PARTITIONING_ID, contentType, viewer);
	}
	
}