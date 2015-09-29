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
package LANG_PROJECT_ID.ide.core.text;

import org.eclipse.jface.text.ITextViewer;

import melnorme.lang.ide.core.TextSettings_Actual;
import melnorme.lang.ide.core.text.format.LangAutoEditStrategyExt;

public class LANGUAGE_AutoEditStrategy extends LangAutoEditStrategyExt {
	
	public LANGUAGE_AutoEditStrategy(String contentType, ITextViewer viewer, 
			ILangAutoEditsPreferencesAccessExt preferences) {
		super(TextSettings_Actual.PARTITIONING_ID, contentType, viewer, preferences);
	}
	
}