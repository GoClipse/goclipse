/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.common;

import static melnorme.utilbox.core.CoreUtil.option;

import java.util.Optional;

import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.toolchain.ops.SourceOpContext;
import melnorme.utilbox.misc.Location;

public interface ISourceBuffer {
	
	public default Optional<Location> getLocation_opt() {
		return option(getLocation_orNull());
	}
	
	public abstract Location getLocation_orNull();
	
	public abstract String getSource();
	
	public abstract boolean isDirty();
	
	/**
	 * Try to save a buffer if it is dirty.
	 * 
	 * @return success if buffer is now non-dirty 
	 * (either because it was saved, or because it was never dirty in the first place), false otherwise 
	 */
	default boolean trySaveBufferIfDirty() {
		if(!isDirty()) {
			return true;
		}
		return doTrySaveBuffer();
	}
	
	public abstract boolean doTrySaveBuffer();
	
	public abstract ISourceBuffer getReadOnlyView();
	
	public default SourceOpContext getSourceOpContext(SourceRange range) {
		return new SourceOpContext(getLocation_opt(), range.getOffset(), getSource(), isDirty());
	}
	
}