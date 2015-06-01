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

import melnorme.utilbox.collections.ArrayList2;

public class OwnedArraylist extends ArrayList2<IDisposable> implements IOwnedList<IDisposable> {
	
	private static final long serialVersionUID = 8056073985418947624L;
	
}