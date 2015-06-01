/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.ownership;

import java.util.List;

/**
 * The owner of this list owns all the elements in the list. 
 */
public interface IOwnedList<T extends IDisposable> extends List<T> {
	
	default void disposeAll() {
		for(IDisposable disposable : this) {
			disposable.dispose();
		}
		this.clear();
	}
	
}