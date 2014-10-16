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
package com.googlecode.goclipse.ui;

import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.ide.ui.utils.PluginImagesHelper.ImageHandle;

import org.eclipse.jface.resource.ImageDescriptor;

public class GoPluginImages extends LangImages {
	
	public static final ImageHandle COMPLETION_GO = createManaged("", "go.png");
	
	public static final ImageHandle GO_ICON = createManaged("", "go-icon16.png");
	
	public static final ImageHandle GO_CONSOLE_ICON = createManaged("", "go-icon16.png");
	
	public static final ImageHandle NAVIGATOR_SRC_FOLDER = createManaged("", "src-folder.png");
	public static final ImageHandle NAVIGATOR_PKG_FOLDER = createManaged("", "open_artifact_obj.gif");
	public static final ImageHandle NAVIGATOR_BIN_FOLDER = createManaged("", "cf_obj.gif");
	public static final ImageHandle NAVIGATOR_SOURCE_CONTAINER = createManaged("", "source-folder.gif");
	public static final ImageHandle NAVIGATOR_GO_PACKAGE_FILE = createManaged("", "package_file.png");
	public static final ImageHandle NAVIGATOR_GO_PATH_ELEMENT = createManaged("", "jar_l_obj-1.gif");
	
	
	public static final ImageHandle ELEMENT_IMPORT_CONTAINER = createManaged("", "impc_obj.gif");
	public static final ImageHandle ELEMENT_SOURCE_FOLDER = createManaged("", "source-folder.gif");
	public static final ImageHandle ELEMENT_PACKAGE = createManaged("", "package.gif");
	
	public static final ImageHandle NODE_PACKAGE = createManaged("", "package.gif");
	public static final ImageHandle NODE_IMPORT = createManaged("", "imp_obj.gif");
	
	public static final ImageHandle NODE_TYPE = createManaged("", "type.png");
	public static final ImageHandle NODE_STRUCT = createManaged("", "struct.png");
	public static final ImageHandle NODE_INTERFACE = createManaged("", "interface.gif");
	
	public static final ImageHandle NODE_VAR = createManaged("", "private_co.gif");
	public static final ImageHandle NODE_MEMBER = createManaged("", "private_co.gif");
	
	public static final ImageHandle NODE_FUNCTION = createManaged("", "function_co.png");
	public static final ImageHandle NODE_METHOD = createManaged("", "public_co.gif");
	
	public static final ImageHandle NODE_SCOPE = createManaged("", "function_co.png");
	public static final ImageHandle NODE_FILESCOPE = createManaged("", "function_co.png");
	
	
	public static final ImageHandle SOURCE_OTHER = createManaged("", "orange_cube16.png");
	public static final ImageHandle SOURCE_FUNCTION = createManaged("", "function_co.png");
	public static final ImageHandle SOURCE_PRIVATE_FUNCTION = createManaged("", "public_co.gif"); // Is this icon correct?
	public static final ImageHandle SOURCE_INTERFACE = NODE_INTERFACE;
	public static final ImageHandle SOURCE_STRUCT = NODE_STRUCT;
	public static final ImageHandle SOURCE_IMPORT = NODE_IMPORT;
	public static final ImageHandle SOURCE_PRIVATE_VAR = createManaged("", "field_private_obj.gif");
	public static final ImageHandle SOURCE_PUBLIC_VAR = createManaged("", "field_public_obj.gif");
	public static final ImageHandle SOURCE_LOCAL_VAR = createManaged("", "variable_local_obj.gif");
	
	
	public static final ImageDescriptor OVERLAYS_WARNING = createUnmanaged("", "warning_co.gif");
	public static final ImageDescriptor OVERLAYS_ERROR = createUnmanaged("", "error_co.gif");
	
	
	public static final ImageHandle WIZARD_SOURCE_ICON = createManaged("", "sourceicon.png");
	public static final ImageHandle WIZARD_ICON = createManaged("", "go-icon-wizard.png");
	
}