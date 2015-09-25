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
package melnorme.lang.ide.ui.views;


import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.ui.navigator.IDescriptionProvider;

public abstract class AbstractLangLabelProvider extends LabelProvider 
		implements IStyledLabelProvider, IDescriptionProvider, StylerHelpers {
	
	public AbstractLangLabelProvider() {
		super();
	}
	
	@Override
	public String getText(Object element) {
		StyledString styledText = getStyledText(element);
		if(styledText != null) {
			return styledText.getString();
		}
		return null;
	}
	
	@Override
	public abstract StyledString getStyledText(Object element);
	
	/* ----------------- description ----------------- */
	
	// By default, use full string of styled text  
	@Override
	public String getDescription(Object anElement) {
		
		StyledString styledText = getStyledText(anElement);
		if(styledText != null) {
			return styledText.getString();
		}
		return null;
	}
	
}