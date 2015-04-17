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
package melnorme.lang.ide.ui.editor.structure;

import org.eclipse.jface.text.IDocument;

import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.utilbox.misc.Location;

public abstract class StructureModelManager {
	
	public static StructureModelManager manager = LangUIPlugin_Actual.createStructureModelManager();

	public static StructureModelManager getDefault() {
		return manager;
	}
	
	public abstract void rebuild(Location location, IDocument document);
	
}