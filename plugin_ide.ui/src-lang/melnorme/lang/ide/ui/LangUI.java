/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
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

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.BundleContext;

import melnorme.util.swt.jface.resources.ImageDescriptorRegistry;
import melnorme.util.swt.jface.text.ColorManager2;
import melnorme.utilbox.ownership.IDisposable;
import melnorme.utilbox.ownership.LifecycleObject;

// TODO: move singletons from LangUIPlugin here
public class LangUI extends LifecycleObject {
	
	protected static LangUI instance;
	
	public static LangUI getInstance() {
		return instance;
	}
	
	protected static Display getStandardDisplay() {
		return PlatformUI.getWorkbench().getDisplay();
	}
	
	/* -----------------  ----------------- */
	
	protected final BundleContext context;
	protected final ThemeHelper themeHelper;
	
	protected LangUI(BundleContext context) {
		this.context = context;
		this.themeHelper = new ThemeHelper(context);
		addOwned(themeHelper);
	}
	
	public void addOwned(IDisposable disposable) {
		owned.add(disposable);
	}
	
	public ThemeHelper getThemeHelper() {
		return themeHelper;
	}
	
	protected final ColorManager2 colorManager = new ColorManager2();
	
	public ColorManager2 getColorManager() {
		return colorManager;
	}
	
	protected ImageDescriptorRegistry imageDescriptorRegistry;
	
	public ImageDescriptorRegistry getImageDescriptorRegistry() {
		assertTrue(getStandardDisplay() != null);
		if(imageDescriptorRegistry == null) {
			imageDescriptorRegistry = new ImageDescriptorRegistry();
		}
		return imageDescriptorRegistry;
	}
	
}