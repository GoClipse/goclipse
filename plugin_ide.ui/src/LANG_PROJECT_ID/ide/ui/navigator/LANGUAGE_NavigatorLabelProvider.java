/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package LANG_PROJECT_ID.ide.ui.navigator;
import melnorme.lang.ide.ui.views.AbstractLangNavigatorLabelProvider;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

public class LANGUAGE_NavigatorLabelProvider extends AbstractLangNavigatorLabelProvider implements IStyledLabelProvider {
	
	@Override
	public StyledString getStyledText(Object element) {
		return null;
	}
	
	@Override
	protected Image getImageForCustomElements(Object element) {
		return null;
	}
	
}