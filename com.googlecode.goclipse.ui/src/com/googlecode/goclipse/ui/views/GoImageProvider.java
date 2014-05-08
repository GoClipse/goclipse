/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.views;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import org.eclipse.swt.graphics.Image;

import com.googlecode.goclipse.go.lang.model.Node;
import com.googlecode.goclipse.go.lang.model.Type;
import com.googlecode.goclipse.go.lang.model.TypeClass;
import com.googlecode.goclipse.ui.GoPluginImages;

public class GoImageProvider {
	
	public static Image getImage(Node node) {
		switch (node.getNodeKind()) {
		case VAR: return getManagedImage(GoPluginImages.NODE_VAR);
		case MEMBER: return getManagedImage(GoPluginImages.NODE_MEMBER);
		case PACKAGE: return getManagedImage(GoPluginImages.NODE_PACKAGE);
		case FUNCTION: return getManagedImage(GoPluginImages.NODE_FUNCTION);
		case METHOD: return getManagedImage(GoPluginImages.NODE_METHOD);
		case TYPE: return getTypeImage((Type) node);
		case IMPORT: return getManagedImage(GoPluginImages.NODE_IMPORT);
		case SCOPE: return getManagedImage(GoPluginImages.NODE_SCOPE);
		case FILESCOPE: return getManagedImage(GoPluginImages.NODE_FILESCOPE);
			
		}
		throw assertFail();
	}
	
	public static Image getTypeImage(Type type) {
		TypeClass typeClass = type.getTypeClass();
		
		if(typeClass == TypeClass.STRUCT) {
			return getManagedImage(GoPluginImages.NODE_TYPE_STRUCT);
		} else if(typeClass == TypeClass.INTERFACE) {
			return getManagedImage(GoPluginImages.NODE_TYPE_INTERFACE);
		} else {
			return getManagedImage(GoPluginImages.NODE_TYPE);
		}
	}
	
	protected static Image getManagedImage(String key) {
		return GoPluginImages.getImage(key);
	}
	
}