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
package melnorme.utilbox.concurrency;

import java.util.concurrent.CancellationException;

/**
 * Similar to {@link CancellationException} or {@link org.eclipse.core.runtime.OperationCanceledException}, 
 * but it is a checked exception, because checked exceptions save lives and prevent forest fires! :)
 */
public class OperationCancellation extends Exception {
	
	private static final long serialVersionUID = -8709454826700411533L;
	
	public OperationCancellation() {
	}
	
}