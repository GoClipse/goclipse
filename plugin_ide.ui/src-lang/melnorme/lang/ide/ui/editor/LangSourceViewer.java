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
package melnorme.lang.ide.ui.editor;


import melnorme.lang.ide.ui.text.AbstractLangSourceViewerConfiguration;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

public class LangSourceViewer extends JavaSourceViewer_Mod {
	
	public LangSourceViewer(Composite parent, IVerticalRuler verticalRuler, IOverviewRuler overviewRuler,
			boolean showAnnotationsOverview, int styles, IPreferenceStore store) {
		super(parent, verticalRuler, overviewRuler, showAnnotationsOverview, styles, store);
	}
	
	@Override
	protected void configure_beforeViewerColors(SourceViewerConfiguration configuration) {
		super.configure_beforeViewerColors(configuration);
		
		if (configuration instanceof AbstractLangSourceViewerConfiguration) {
			AbstractLangSourceViewerConfiguration langConfig = (AbstractLangSourceViewerConfiguration) configuration;
			
			StyledText textWidget = getTextWidget();
			if (textWidget != null) {
				textWidget.setFont(JFaceResources.getFont(langConfig.getFontPropertyPreferenceKey()));
			}
		}
		
	}
	
}