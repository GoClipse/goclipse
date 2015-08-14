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
	
	public static final ImageHandle NAVIGATOR_GOPATH_ENTRY = createManaged("", "navigator/gopath_entry.gif");
	
	public static final ImageHandle NAVIGATOR_PKG_FOLDER = createManaged("", "navigator/pkg_folder.gif");
	public static final ImageHandle NAVIGATOR_GO_PACKAGE_FILE = createManaged("", "navigator/package_file.png");
	
	
	
	public static final ImageHandle SOURCE_IMPORT = createManaged("", "imp_obj.gif");
	public static final ImageHandle SOURCE_FUNCTION = createManaged("", "go_function.png");
	public static final ImageHandle SOURCE_METHOD = createManaged("", "go_method.gif");
	public static final ImageHandle SOURCE_VARIABLE = createManaged("", "variable2.png");
	
	public static final ImageDescriptor OVERLAYS_WARNING = createUnmanaged("", "warning_co.gif");
	public static final ImageDescriptor OVERLAYS_ERROR = createUnmanaged("", "error_co.gif");
	
	
	public static final ImageHandle WIZARD_SOURCE_ICON = createManaged("", "sourceicon.png");
	public static final ImageHandle WIZARD_ICON = createManaged("", "go-icon-wizard.png");
	
}