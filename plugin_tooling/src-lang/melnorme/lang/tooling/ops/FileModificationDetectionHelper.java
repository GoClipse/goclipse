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
package melnorme.lang.tooling.ops;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import melnorme.utilbox.misc.Location;

public class FileModificationDetectionHelper {
	
	protected final Location fileLocation;
	protected BasicFileAttributes fileSyncAttributes;
	
	public FileModificationDetectionHelper(Location fileLocation) {
		this.fileLocation = assertNotNull(fileLocation);
	}
	
	public Path getFilePath() {
		return fileLocation.getPath();
	}
	
	public void markRead() throws IOException {
		fileSyncAttributes = Files.readAttributes(getFilePath(), BasicFileAttributes.class);
	}
	
	public void markStale() {
		fileSyncAttributes = null;
	}
	
	public boolean isModifiedSinceLastRead() {
		BasicFileAttributes newAttributes;
		try {
			newAttributes = Files.readAttributes(getFilePath(), BasicFileAttributes.class);
		} catch (IOException e) {
			return true;
		}
		
		return hasBeenModified(fileSyncAttributes, newAttributes);
	}
	
	protected boolean hasBeenModified(BasicFileAttributes originalAttributes, BasicFileAttributes newAttributes) {
		if(newAttributes.lastModifiedTime().toMillis() > System.currentTimeMillis()) {
			handleWarning_ModifiedTimeInTheFuture(newAttributes.lastModifiedTime());
		}
		
		return 
				originalAttributes == null ||
				originalAttributes.lastModifiedTime().toMillis() != newAttributes.lastModifiedTime().toMillis() ||
				originalAttributes.size() != newAttributes.size();
	}
	
	@SuppressWarnings("unused")
	protected void handleWarning_ModifiedTimeInTheFuture(FileTime lastModifiedTime) {
		// Default behavior, ignore
	}
	
}