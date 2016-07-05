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
package melnorme.utilbox.concurrency;

/**
 * Alias interface to FutureX<RESULT, RuntimeException>, does not add additional semantics.
 * 
 * There should not be function parameters declared with this type, 
 * instead use the more general {@code FutureX<RESULT, RuntimeException>}. 
 * 
 */
public interface Future2<RESULT> extends FutureX<RESULT, RuntimeException> {
	
}