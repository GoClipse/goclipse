/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor.text;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.ui.texteditor.ITextEditor;

import _org.eclipse.jdt.internal.ui.text.CompositeReconcilingStrategy;

public class LangReconciler extends MonoReconciler {
	
	protected final ITextEditor fTextEditor;
	
	public LangReconciler(CompositeReconcilingStrategy strategy, boolean isIncremental, ITextEditor editor) {
		super(strategy, isIncremental);
		this.fTextEditor = assertNotNull(editor);
	}
	
}