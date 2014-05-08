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

import org.eclipse.swt.graphics.Image;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.go.lang.model.Node;
import com.googlecode.goclipse.go.lang.model.Type;
import com.googlecode.goclipse.go.lang.model.TypeClass;

public class GoImageProvider {

	public static Image getImage(Node node) {
		switch (node.getNodeKind()) {
		case VAR: return Activator.getImage("icons/private_co.gif");
		case MEMBER: return Activator.getImage("icons/private_co.gif");
		case PACKAGE: return Activator.getImage("icons/package.gif");
		case FUNCTION: return Activator.getImage("icons/function_co.png");
		case METHOD: return Activator.getImage("icons/public_co.gif");
		case TYPE: return getImage((Type) node);
		case IMPORT: return Activator.getImage("icons/imp_obj.gif");
		case SCOPE: return Activator.getImage("icons/function_co.png");
		case FILESCOPE: return Activator.getImage("icons/function_co.png");
			
		}
		
		return Activator.getImage("icons/private_co.gif");
	}
	
	public static Image getImage(Type type) {
		TypeClass typeClass = type.getTypeClass();
		
		if(typeClass == TypeClass.STRUCT) {
			return Activator.getImage("icons/struct.png");
		} else if(typeClass == TypeClass.INTERFACE) {
			return Activator.getImage("icons/interface.gif");
		} else {
			return Activator.getImage("icons/type.png");
		}
	}
	
}