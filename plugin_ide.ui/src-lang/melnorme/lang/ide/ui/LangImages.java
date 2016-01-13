/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import melnorme.lang.ide.ui.utils.PluginImagesHelper;
import melnorme.lang.ide.ui.utils.PluginImagesHelper.ImageHandle;
import melnorme.util.swt.jface.resources.ImageDescriptorRegistry;

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
	
	public static ImageHandle createManaged(String prefix, String name) {
		return createManaged(getKey(prefix, name));
	}
	
	public static ImageHandle createManaged(String imagePath) {
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
	
	
	/* ---------------- Common Lang images ---------------- */
	
	public static final String LANG_ACTIONS = "icons-lang/actions/";
	public static final String LANG_OVERLAYS = "icons-lang/overlays/";
	public static final String LANG_NAVIGATOR = "icons-lang/navigator/";
	
	public static final String CAT_VIEWS = "views";
	protected static final String T_TABS = "view16";
	
	public static final ImageHandle ACTIONS_OPEN_DEF = createManaged(LANG_ACTIONS, "OpenDef.16.png");
	
	public static final ImageHandle BUILD_CONSOLE_ICON = createManaged(CAT_VIEWS, "ToolsConsole.16.png");
	public static final ImageHandle ENGINE_TOOLS_CONSOLE_ICON = BUILD_CONSOLE_ICON;
	public static final ImageHandle SOURCE_EDITOR_ICON = createManaged(CAT_VIEWS, "SourceEditor.png");
	
	public static final ImageHandle BUILD_TARGET = createManaged(CAT_VIEWS, "target.png");
	public static final ImageHandle BUILD_TARGETS_ELEM = createManaged(CAT_VIEWS, "targets_elem.png");
	
	public static final ImageHandle IMG_LAUNCHTAB_MAIN = createManaged(T_TABS, "main_launch_tab.png");
	public static final ImageHandle IMG_LAUNCHTAB_ARGUMENTS = createManaged(T_TABS, "arguments_tab.gif");
	
	public static final ImageHandle IMG_SCROLL_LOCK = createManaged(LANG_ACTIONS, "lock_co.png");
	public static final ImageHandle IMG_CLEAR_CONSOLE = createManaged(LANG_ACTIONS, "clear_co.png");
	public static final ImageHandle IMG_PIN_CONSOLE = createManaged(LANG_ACTIONS, "pin.png");
	
	
	/* ----------------- overlay decorations ----------------- */
	
	public static final ImageDescriptor DESC_OVR_ERROR = createUnmanaged(LANG_OVERLAYS, "error.png");
	public static final ImageDescriptor DESC_OVR_WARNING = createUnmanaged(LANG_OVERLAYS, "warning.png");
	
	public static final ImageDescriptor DESC_OVR_PRIVATE = createUnmanaged(LANG_OVERLAYS, "prot_private.png");
	public static final ImageDescriptor DESC_OVR_PROTECTED = createUnmanaged(LANG_OVERLAYS, "prot_protected.png");
	public static final ImageDescriptor DESC_OVR_DEFAULT = createUnmanaged(LANG_OVERLAYS, "prot_default.png");
	
	public static final ImageDescriptor DESC_OVR_PRIVATE_SMALL = createUnmanaged(LANG_OVERLAYS, "prot_private.2.png");
	public static final ImageDescriptor DESC_OVR_PROTECTED_SMALL = createUnmanaged(LANG_OVERLAYS, "prot_protected.2.png");
	public static final ImageDescriptor DESC_OVR_DEFAULT_SMALL = createUnmanaged(LANG_OVERLAYS, "prot_default.2.png");
	
	public static final ImageDescriptor DESC_OVR_FINAL = createUnmanaged(LANG_OVERLAYS, "ovr_final.png");
	public static final ImageDescriptor DESC_OVR_STATIC = createUnmanaged(LANG_OVERLAYS, "ovr_static.png");
	public static final ImageDescriptor DESC_OVR_ABSTRACT = createUnmanaged(LANG_OVERLAYS, "ovr_abstract.png");
	
	public static final ImageDescriptor DESC_OVR_CONST = createUnmanaged(LANG_OVERLAYS, "ovr_const.png");
	public static final ImageDescriptor DESC_OVR_IMMUTABLE = createUnmanaged(LANG_OVERLAYS, "ovr_immutable.png");
	
	public static final ImageDescriptor DESC_OVR_TEMPLATED = createUnmanaged(LANG_OVERLAYS, "ovr_templated.png");
	public static final ImageDescriptor DESC_OVR_ALIAS = createUnmanaged(LANG_OVERLAYS, "ovr_alias_arrow.png");
	
	
	public static final ImageDescriptor OVR_CHECKED = createUnmanaged(LANG_OVERLAYS, "ovr_checked.png");
	
	/* ----------------- Navigator ----------------- */
	
	public static final ImageDescriptor NAV_Error = createUnmanaged(LANG_NAVIGATOR, "Error.png");
	
	public static final ImageDescriptor NAV_Library = createUnmanaged(LANG_NAVIGATOR, "Library.png");
	public static final ImageDescriptor NAV_LibraryNative = createUnmanaged(LANG_NAVIGATOR, "LibraryNative.png");
	
	public static final ImageDescriptor NAV_OutputFolder = createUnmanaged(LANG_NAVIGATOR, "OutputFolder.png");
	public static final ImageDescriptor NAV_OutputFolder2 = createUnmanaged(LANG_NAVIGATOR, "OutputFolder2.png");
	
	public static final ImageDescriptor NAV_Package = createUnmanaged(LANG_NAVIGATOR, "Package.png");
	public static final ImageDescriptor NAV_PackageManifest = createUnmanaged(LANG_NAVIGATOR, "PackageManifest.png");
	
	public static final ImageDescriptor NAV_SourceFolder = createUnmanaged(LANG_NAVIGATOR, "SourceFolder.png");
	public static final ImageDescriptor NAV_SourceFolderTests = createUnmanaged(LANG_NAVIGATOR, "SourceFolder_Tests2.png");
	
	/* -----------------  Debug  ----------------- */
	
	public static final ImageDescriptor BREAKPOINT_ENABLED = createUnmanaged("icons-lang", "brkp_obj.png");
	public static final ImageDescriptor BREAKPOINT_DISABLED = createUnmanaged("icons-lang", "brkpd_obj.png");
	
	/* ----------------- Image cache keyed by ImageDescriptor ----------------- */
	
	public static ImageDescriptorRegistry getImageDescriptorRegistry() {
		assertTrue(LangUIPlugin.getStandardDisplay() != null);
		return LangUIPlugin.getDefault().getImageDescriptorRegistry();
	}
	
	public static Image getManagedImage(ImageDescriptor imageDescriptor) {
		return getImageDescriptorRegistry().get(imageDescriptor);
	}
	
}