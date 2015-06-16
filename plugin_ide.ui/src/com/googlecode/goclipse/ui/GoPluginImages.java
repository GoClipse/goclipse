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

import melnorme.lang.ide.ui.LangElementImages;
import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.ide.ui.utils.PluginImagesHelper.ImageHandle;

import org.eclipse.jface.resource.ImageDescriptor;

public class GoPluginImages extends LangImages {
	
	public static final ImageHandle NAVIGATOR_GOROOT_ENTRY = createManaged("", "navigator/goroot_entry.png");
	public static final ImageHandle NAVIGATOR_GOPATH_ENTRY = createManaged("", "navigator/gopath_entry.gif");
	
	public static final ImageHandle NAVIGATOR_SRC_FOLDER = createManaged("", "navigator/src-folder.png");
	public static final ImageHandle NAVIGATOR_PKG_FOLDER = createManaged("", "navigator/pkg_folder.gif");
	public static final ImageHandle NAVIGATOR_BIN_FOLDER = createManaged("", "navigator/bin_folder.gif");
	public static final ImageHandle NAVIGATOR_GO_PACKAGE_FILE = createManaged("", "navigator/package_file.png");
	public static final ImageHandle NAVIGATOR_SOURCE_PACKAGE_FOLDER = createManaged("", "navigator/source_package.gif");
	
	
	
	public static final ImageHandle SOURCE_OTHER = LangElementImages.UNKNOWN;
	public static final ImageHandle SOURCE_FUNCTION = createManaged("", "function_co.png");
	public static final ImageHandle SOURCE_PRIVATE_FUNCTION = createManaged("", "public_co.gif"); // Is this icon correct?
	public static final ImageHandle SOURCE_METHOD = createManaged("", "public_co.gif");
	
	public static final ImageHandle SOURCE_INTERFACE = LangElementImages.T_INTERFACE;
	public static final ImageHandle SOURCE_STRUCT = LangElementImages.T_STRUCT;
	public static final ImageHandle SOURCE_IMPORT = createManaged("", "imp_obj.gif");
	public static final ImageHandle SOURCE_PRIVATE_VAR = createManaged("", "field_private_obj.gif");
	public static final ImageHandle SOURCE_PUBLIC_VAR = createManaged("", "field_public_obj.gif");
	
	
	public static final ImageDescriptor OVERLAYS_WARNING = createUnmanaged("", "warning_co.gif");
	public static final ImageDescriptor OVERLAYS_ERROR = createUnmanaged("", "error_co.gif");
	
	
	public static final ImageHandle WIZARD_SOURCE_ICON = createManaged("", "sourceicon.png");
	public static final ImageHandle WIZARD_ICON = createManaged("", "go-icon-wizard.png");
	
}