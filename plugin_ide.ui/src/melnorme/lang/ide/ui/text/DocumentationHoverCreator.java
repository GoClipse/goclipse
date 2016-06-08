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
package melnorme.lang.ide.ui.text;

import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.information.IInformationProviderExtension2;

import melnorme.lang.ide.ui.editor.hover.DocumentationInformationControl.DocumentationInformationControlCreator;

public class DocumentationHoverCreator implements ITextHoverExtension, IInformationProviderExtension2 {
	
	@Override
	public IInformationControlCreator getInformationPresenterControlCreator() {
		return new DocumentationInformationControlCreator();
	}
	
	@Override
	public IInformationControlCreator getHoverControlCreator() {
		return getInformationPresenterControlCreator();
	}
	
}