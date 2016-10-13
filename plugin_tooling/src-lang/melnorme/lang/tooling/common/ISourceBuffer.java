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
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public interface ISourceBuffer {
	
	public abstract Location getLocation_orNull();
	
	public default Optional<Location> getLocation_opt() {
		return option(getLocation_orNull());
	}
	
	public default Location getLocation() throws CommonException {
		if(getLocation_orNull() == null) {
			throw new CommonException(SourceOpContext.MSG_NoFileLocationForThisOperation);
		}
		return getLocation_orNull();
	}
	
	public abstract String getSource();
	
	public abstract boolean isDirty();
	
	/**
	 * Try to save a buffer if it is dirty.
	 * 
	 * @return success if buffer is now non-dirty 
	 * (either because it was saved, or because it was never dirty in the first place), false otherwise
	 * 
	 *  Warning, it might be necessary to obtain a new SourceOpContext to update dirty status.
	 */
	default void trySaveBufferIfDirty() throws CommonException, OperationCancellation {
		if(!isDirty()) {
			return;
		}
		doTrySaveBuffer();
	}
	
	public abstract void doTrySaveBuffer() throws CommonException, OperationCancellation;
	
	public abstract ISourceBuffer getReadOnlyView();
	
	public default SourceOpContext getSourceOpContext(SourceRange range) {
		return getSourceOpContext(range.getOffset(), new SourceRange(range.getOffset(), 0));
	}
	
	public default SourceOpContext getSourceOpContext(int offset, SourceRange selection) {
		return new SourceOpContext(getLocation_opt(), offset, selection, getSource(), isDirty());
	}
	
}