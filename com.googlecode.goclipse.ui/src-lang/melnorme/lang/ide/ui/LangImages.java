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
package melnorme.lang.ide.ui;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public abstract class LangImages {
	
	protected static final String IMAGES_BUNDLE_PREFIX = "$nl$/icons/";
	protected static final IPath IMAGES_PATH = new Path(IMAGES_BUNDLE_PREFIX);
	
	protected static final PluginImagesHelper helper = new PluginImagesHelper(
			LangUIPlugin.getInstance().getBundle(), IMAGES_PATH, true); 
	
	protected static final String T_OBJ = "obj16";
	protected static final String T_OVR = "ovr16";
	protected static final String T_TABS = "view16/";
	
	protected static String createManaged(String prefix, String name) {
		return helper.createManaged(prefix, name);
	}
	
	protected static ImageDescriptor createUnmanaged(String prefix, String name) {
		return helper.createUnmanaged(prefix, name);
	}
	
	/** Gets the managed {@link Image} associated with the given key. */
	public static Image getImage(String key) {
		return helper.getImage(key);
	}
	
	/** Gets the managed {@link ImageDescriptor} associated with the given key. */
	public static ImageDescriptor getDescriptor(String key) {
		return helper.getImageDescriptor(key);
	}
	
	/* ---------------- Common Lang images ---------------- */
	
	public static String IMG_VIEW_ARGUMENTS_TAB = createManaged(T_TABS, "arguments_tab.gif");
	
}