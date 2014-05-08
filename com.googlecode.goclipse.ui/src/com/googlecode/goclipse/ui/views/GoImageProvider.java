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
		case VAR: return GoPluginImages.NODE_VAR.getImage();
		case MEMBER: return GoPluginImages.NODE_MEMBER.getImage();
		case PACKAGE: return GoPluginImages.NODE_PACKAGE.getImage();
		case FUNCTION: return GoPluginImages.NODE_FUNCTION.getImage();
		case METHOD: return GoPluginImages.NODE_METHOD.getImage();
		case TYPE: return getTypeImage((Type) node);
		case IMPORT: return GoPluginImages.NODE_IMPORT.getImage();
		case SCOPE: return GoPluginImages.NODE_SCOPE.getImage();
		case FILESCOPE: return GoPluginImages.NODE_FILESCOPE.getImage();
			
		}
		throw assertFail();
	}
	
	public static Image getTypeImage(Type type) {
		TypeClass typeClass = type.getTypeClass();
		
		if(typeClass == TypeClass.STRUCT) {
			return GoPluginImages.NODE_TYPE_STRUCT.getImage();
		} else if(typeClass == TypeClass.INTERFACE) {
			return GoPluginImages.NODE_TYPE_INTERFACE.getImage();
		} else {
			return GoPluginImages.NODE_TYPE.getImage();
		}
	}
	
}