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
package melnorme.lang.ide.core.utils.operation;

import org.eclipse.core.runtime.CoreException;

import melnorme.lang.ide.core.LangCore;
import melnorme.utilbox.core.CommonException;

public interface CoreRunnable<R> {
	
	public R execute() throws CoreException, CommonException;
	
	public static <R> R toCoreException(CoreRunnable<R> coreRunnable) throws CoreException {
		try {
			return coreRunnable.execute();
		} catch(CommonException e) {
			throw LangCore.createCoreException(e);
		}
	}
	
}