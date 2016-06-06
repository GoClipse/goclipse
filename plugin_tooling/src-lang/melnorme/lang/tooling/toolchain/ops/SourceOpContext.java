/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.toolchain.ops;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.Optional;

import melnorme.lang.tooling.ast.SourceRange;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public class SourceOpContext {
	
	public static final String MSG_NoFileLocationForThisOperation = "No file location for this operation";
	
	protected final Location fileLocation; // can be null
	public final SourceRange sourceRange;
	public final String source;
	
	public SourceOpContext(Location fileLocation, int offset, String source) {
		this(fileLocation, new SourceRange(offset, 0),source);
	}
	
	public SourceOpContext(Location fileLocation, SourceRange sourceRange, String source) {
		this.fileLocation = fileLocation;
		this.sourceRange = assertNotNull(sourceRange);
		this.source = assertNotNull(source);
	}
	
	public int getOffset() {
		return sourceRange.getOffset();
	}
	
	public Optional<Location> getOptionalFileLocation() {
		return Optional.ofNullable(fileLocation);
	}
	
	public Location getFileLocation() throws CommonException {
		if(fileLocation == null) {
			throw new CommonException(MSG_NoFileLocationForThisOperation);
		}
		return fileLocation;
	}
	
	public String getSource() {
		return source;
	}
	
	public SourceRange getSourceRange() {
		return sourceRange;
	}
	
}