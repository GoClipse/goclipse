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

import melnorme.lang.ide.ui.utils.ImageDescriptorRegistry;
import melnorme.lang.ide.ui.utils.PluginImagesHelper;
import melnorme.lang.ide.ui.utils.PluginImagesHelper.ImageHandle;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;

public abstract class LangImages {
	
	protected static final String IMAGES_BUNDLE_PREFIX = "$nl$/icons/";
	protected static final IPath IMAGES_PATH = new Path(IMAGES_BUNDLE_PREFIX);
	
	protected static final PluginImagesHelper helper = new PluginImagesHelper(
			LangUIPlugin.getInstance().getBundle(), IMAGES_PATH, true); 
	
	protected static String getKey(String prefix, String name) {
		return prefix + "/" + name;
	}
	
	protected static ImageHandle createManaged(String prefix, String name) {
		return createManaged(getKey(prefix, name));
	}
	
	protected static ImageHandle createManaged(String imagePath) {
		return helper.createManaged(imagePath);
	}
	
	protected static ImageHandle createFromPlatformSharedImage(String prefix, String name, String sharedImageName) {
		ImageDescriptor descriptor = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(sharedImageName);
		String key = getKey(prefix, name);
		return helper.putManaged(key, descriptor);
	}
	
	protected static ImageDescriptor createUnmanaged(String prefix, String name) {
		return helper.createImageDescriptor(getKey(prefix, name), false);
	}
	
	/* ----------------- Image cache indexed by ImageDescriptor ----------------- */
	
	protected static final ImageDescriptorRegistry imageCache = new ImageDescriptorRegistry();
	
	public static Image getCachedImage(ImageDescriptor imageDescriptor) {
		return imageCache.getImage(imageDescriptor);
	}
	
	protected static final String T_OBJ = "obj16";
	protected static final String T_OVR = "ovr16";
	protected static final String T_TABS = "view16";
	
	public static final String LANG_ACTIONS = "icons-lang/actions/";
	public static final String CAT_VIEWS = "views";
	public static final String CAT_LANG_OBJ = "icons-lang/obj";
	
	/* ---------------- Common Lang images ---------------- */
	
	public static final ImageHandle OBJ_CA_TEMPLATE = createManaged(CAT_LANG_OBJ, "CA_Templates.16.gif");
	
	public static final ImageHandle ACTIONS_OPEN_DEF = createManaged(LANG_ACTIONS, "OpenDef.16.png");
	
	public static final ImageHandle TOOLS_CONSOLE_ICON = createManaged(CAT_VIEWS, "ToolsConsole.16.png");
	
	public static final ImageHandle IMG_LAUNCHTAB_MAIN = createManaged(T_TABS, "main_launch_tab.png");
	public static final ImageHandle IMG_LAUNCHTAB_ARGUMENTS = createManaged(T_TABS, "arguments_tab.gif");
	
	public static final ImageHandle IMG_SCROLL_LOCK = createManaged(LANG_ACTIONS, "lock_co.png");
	public static final ImageHandle IMG_CLEAR_CONSOLE = createManaged(LANG_ACTIONS, "clear_co.png");
	public static final ImageHandle IMG_PIN_CONSOLE = createManaged(LANG_ACTIONS, "pin.png");
	
}