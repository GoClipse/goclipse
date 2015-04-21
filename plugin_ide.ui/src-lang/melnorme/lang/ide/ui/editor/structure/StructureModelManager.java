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

import java.util.Hashtable;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.utilbox.misc.ListenerListHelper;
import melnorme.utilbox.misc.Location;

import org.eclipse.jface.text.IDocument;

public abstract class StructureModelManager {
	
	public static StructureModelManager manager = LangUIPlugin_Actual.createStructureModelManager();

	public static StructureModelManager getDefault() {
		return manager;
	}
	
	/* -----------------  ----------------- */
	
	protected final Hashtable<Location, SourceFileStructure> builtStructures = new Hashtable<>();
	protected final ListenerListHelper<IStructureModelListener> listenerList = new ListenerListHelper<>();
	
	public abstract void rebuild(Location location, IDocument document);
	
	public void addListener(IStructureModelListener listener) {
		listenerList.addListener(listener);
	}
	
	public void removeListener(IStructureModelListener listener) {
		listenerList.removeListener(listener);
	}
	
	/* FIXME: concurrency */
	protected void addNewStructure(Location location, SourceFileStructure moduleStructure) {
		builtStructures.put(location, moduleStructure);
		
		for(IStructureModelListener listener : listenerList.getListeners()) {
			try {
				listener.newStructureBuild(location, moduleStructure);
			} catch (Exception e) {
				LangCore.logInternalError(e);
			}
		}
	}
	
}