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

import java.util.concurrent.Future;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.utils.M_WorkerThread;
import melnorme.utilbox.collections.HashMap2;
import melnorme.utilbox.concurrency.AutoUnlockable;
import melnorme.utilbox.concurrency.AwaitResultFuture;
import melnorme.utilbox.concurrency.ReentrantLockExt;
import melnorme.utilbox.misc.ListenerListHelper;
import melnorme.utilbox.misc.Location;

public abstract class StructureModelManager {
	
	public static StructureModelManager manager = LangUIPlugin_Actual.createStructureModelManager();
	
	public static ReentrantLockExt modelLock = new ReentrantLockExt(); 

	public static StructureModelManager getDefault() {
		return manager;
	}
	
	/* -----------------  ----------------- */
	
	// TODO: ideally this should be a cache, otherwise it's a potential memory leak if structures are not removed.
	protected final HashMap2<Location, SourceFileStructure> builtStructures = new HashMap2<>();
	
	protected final ListenerListHelper<IStructureModelListener> listenerList = new ListenerListHelper<>();
	
	public abstract void rebuild(Location location, String source, M_WorkerThread reconcilerWorkerThread);
	
	public void addListener(IStructureModelListener listener) {
		listenerList.addListener(listener);
	}
	
	public void removeListener(IStructureModelListener listener) {
		listenerList.removeListener(listener);
	}
	
	public void addNewStructure(Location location, SourceFileStructure sourceFileStructure) {
		try(AutoUnlockable _ = modelLock.lock_()) {
			
			builtStructures.put(location, sourceFileStructure);
			
			for(IStructureModelListener listener : listenerList.getListeners()) {
				try {
					listener.structureChanged(location, sourceFileStructure, modelLock);
				} catch (Exception e) {
					LangCore.logInternalError(e);
				}
			}
			
		}
	}
	
	public SourceFileStructure getStructure(Location location) {
		try(AutoUnlockable _ = modelLock.lock_()) {
			return builtStructures.get(location);
		}
	}
	
	/**
	 * @return a {@link Future} that will wait until a non-null structure is available for the given location.
	 */
	public StructureModifiedSentinel getNonNullStructureSentinel(final Location location) {
		
		try(AutoUnlockable _ = modelLock.lock_()) {
			SourceFileStructure structure = builtStructures.get(location);
			final StructureModifiedSentinel sentinel = new StructureModifiedSentinel();
			
			if(structure != null) {
				sentinel.setResult(structure);
				return sentinel;
			}
			
			addListener(new IStructureModelListener() {
				@Override
				public void structureChanged(Location modifiedLocation, SourceFileStructure structure,
						Object structureModelLock) {
					if(location.equals(modifiedLocation) && structure != null) {
						sentinel.setResult(structure);
						removeListener(this);
					}
				}
			});
			return sentinel;
		}
		
	}
	
	public static class StructureModifiedSentinel extends AwaitResultFuture<SourceFileStructure> {
	}
	
}