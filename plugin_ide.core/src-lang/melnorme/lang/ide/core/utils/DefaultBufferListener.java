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
package melnorme.lang.ide.core.utils;

import org.eclipse.core.filebuffers.IFileBuffer;
import org.eclipse.core.filebuffers.IFileBufferListener;
import org.eclipse.core.runtime.IPath;

public class DefaultBufferListener implements IFileBufferListener {
	
	@Override
	public void underlyingFileMoved(IFileBuffer buffer, IPath path) {
	}
	
	@Override
	public void underlyingFileDeleted(IFileBuffer buffer) {
	}
	
	@Override
	public void stateValidationChanged(IFileBuffer buffer, boolean isStateValidated) {
	}
	
	@Override
	public void stateChanging(IFileBuffer buffer) {
	}
	
	@Override
	public void stateChangeFailed(IFileBuffer buffer) {
	}
	
	@Override
	public void dirtyStateChanged(IFileBuffer buffer, boolean isDirty) {
	}
	
	@Override
	public void bufferDisposed(IFileBuffer buffer) {
	}
	
	@Override
	public void bufferCreated(IFileBuffer buffer) {
	}
	
	@Override
	public void bufferContentReplaced(IFileBuffer buffer) {
	}
	
	@Override
	public void bufferContentAboutToBeReplaced(IFileBuffer buffer) {
	}
	
}