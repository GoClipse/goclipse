/*******************************************************************************
 * Copyright (c) 2014, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.utils.prefs;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.HashMap;


public class AbstractPreferenceHelper {
	
	protected static final HashMap<String, AbstractPreferenceHelper> instances = new HashMap<>();
	public final String key;
	
	public AbstractPreferenceHelper(String key) {
		this.key = assertNotNull(key);
		
		synchronized (instances) {
			// Allow only one instance of a preference helper per key.
			assertTrue(instances.containsKey(key) == false);
			instances.put(key, this);
		}
	}
	
}