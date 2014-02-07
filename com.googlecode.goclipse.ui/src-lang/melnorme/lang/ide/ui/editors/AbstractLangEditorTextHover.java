/*******************************************************************************
 * Copyright (c) 2002, 2008 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     QNX Software Systems - Initial API and implementation
 *     IBM Corporation
 *     Anton Leherbauer (Wind River Systems)
 *     Bruno Medeiros - lang modifications
 *******************************************************************************/
package melnorme.lang.ide.ui.editors;

import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.information.IInformationProviderExtension2;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;

public abstract class AbstractLangEditorTextHover<T> implements ILangEditorTextHover<T>, IInformationProviderExtension2 {
	
	protected IEditorPart fEditor;
	
	@Override
	public void setEditor(IEditorPart editor) {
		fEditor = editor;
	}
	
	protected IEditorPart getEditor() {
		return fEditor;
	}
	
	@Override
	public abstract IRegion getHoverRegion(ITextViewer textViewer, int offset);
	
	@Deprecated
	@Override
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		return null;
	}
	
	@Override
	public final Object getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion) {
		return getHoverInfo2_do(textViewer, hoverRegion);
	}
	
	@Override
	public abstract T getHoverInfo2_do(ITextViewer textViewer, IRegion hoverRegion);
	
	@Override
	public abstract IInformationControlCreator getHoverControlCreator();
	
	
	@Override
	public IInformationControlCreator getInformationPresenterControlCreator() {
		return new IInformationControlCreator() {
			@Override
			public IInformationControl createInformationControl(Shell shell) {
				return new DefaultInformationControl(shell, true);
			}
		};
	}
	
}