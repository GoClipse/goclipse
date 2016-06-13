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
package melnorme.lang.ide.core.text;

import org.eclipse.jface.text.IDocument;

import melnorme.lang.tooling.common.ISourceBuffer;

/**
 * BM: We might be able to refactor this class so that it is no longer necessary, 
 * by moving similar functionality to IDocument to {@link ISourceBuffer}, like change notifications.
 */
public interface ISourceBufferExt extends ISourceBuffer {
	
	IDocument getDocument();
	
	@Override
	default String getSource() {
		return getDocument().get();
	}
	
	default Object getKeyForCurrentInput() {
		if(getLocation_opt().isPresent()) {
			return getLocation_opt().get().toPathString();
		} else {
			return this;
		}
	}
	
}