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
package melnorme.lang.utils;

import java.io.IOException;
import java.nio.file.attribute.FileTime;

import melnorme.utilbox.misc.Location;

/**
 * An entry caching some value, derived from a file as input. 
 * Keeps tracks of file and value timestamps, to see if current value is stale or not with regards to the file input.
 */
public abstract class FileCachingEntry<VALUE> extends FileModificationDetectionHelper {
	
	private VALUE value;
	
	public FileCachingEntry(Location location) {
		super(location);
	}
	
	public Location getFileLocation() {
		return fileLocation;
	}
	
	public synchronized VALUE getValue() {
		return value;
	}
	
	/** @return file modified time when entry was last marked as read. Non-null. */
	public synchronized FileTime getValueTimeStamp() {
		return fileSyncAttributes == null ? FileTime.fromMillis(0) : fileSyncAttributes.lastModifiedTime();
	}
	
	@Override
	public synchronized void markStale() {
		super.markStale();
	}
	
	public synchronized boolean isStale() {
		return super.isModifiedSinceLastRead();
	}
	
	public synchronized void updateValue(VALUE value) {
		try {
			markRead();
		} catch (IOException e) {
			markStale();
		}
		this.value = value;
	}
	
	@Override
	protected abstract void handleWarning_ModifiedTimeInTheFuture(FileTime lastModifiedTime);
	
}