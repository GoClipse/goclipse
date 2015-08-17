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
package LANG_PROJECT_ID.ide.ui.navigator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.StyledString;

import melnorme.lang.ide.core.project_model.view.BundleErrorElement;
import melnorme.lang.ide.core.project_model.view.IBundleModelElement;
import melnorme.lang.ide.ui.views.LangNavigatorLabelProvider;

public class LANGUAGE_NavigatorLabelProvider extends LangNavigatorLabelProvider implements IStyledLabelProvider {
	
	@Override
	protected DefaultGetStyledTextSwitcher getStyledText_switcher() {
		return new DefaultGetStyledTextSwitcher() {
			@Override
			public StyledString visitBundleElement(IBundleModelElement bundleElement) {
				return new BundleModelGetStyledTextSwitcher() { }.switchBundleElement(bundleElement);
			}
		};
	}
	
	@Override
	protected DefaultGetImageSwitcher getBaseImage_switcher() {
		return new DefaultGetImageSwitcher() {
			@Override
			public ImageDescriptor visitBundleElement(IBundleModelElement bundleElement) {
				return new BundleModelGetImageSwitcher() {
					
					@Override
					public ImageDescriptor visitErrorElement(BundleErrorElement element) {
						/* FIXME: */
						return null;
					}
					
				}.switchBundleElement(bundleElement);
			}
		};
	}
	
}