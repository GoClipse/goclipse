/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.debug.core;

import java.io.File;

import org.eclipse.cdt.debug.core.sourcelookup.AbsolutePathSourceContainer;
import org.eclipse.cdt.debug.internal.core.sourcelookup.SourceUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.sourcelookup.containers.LocalFileStorage;

public class LangAbsolutePathSourceContainer extends AbsolutePathSourceContainer {
	
	@Override
	public Object[] findSourceElements(String name) throws CoreException {
		if (name != null) {
			File file = new File(name);
			if (isValidAbsoluteFilePath(file)) {
				Object[] result = SourceUtils.findSourceElements(file, getDirector());
				
				if(result != null && result.length == 1 && result[0] == null) {
					// Fix a bug/limitation in CDT, that tries to create an ITranslationUnit for a CProject
					// which doesn't apply since project is not CProject
					return new LocalFileStorage[] { new LocalFileStorage(file) };
				}
				
				return result;
			}
		}
		return new Object[0];
	}
	
}