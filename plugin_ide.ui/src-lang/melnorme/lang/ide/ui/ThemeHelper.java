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

import static melnorme.utilbox.core.CoreUtil.blindCast;

import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.e4.ui.css.swt.theme.ITheme;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.e4.ui.css.swt.theme.IThemeManager;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import melnorme.utilbox.ownership.IDisposable;
import melnorme.utilbox.ownership.IOwner;
import melnorme.utilbox.ownership.LifecycleObject;

@SuppressWarnings("restriction")
public class ThemeHelper extends LifecycleObject implements IDisposable {
	
	protected final BundleContext context;
	protected final ServiceReference<?> serviceRef;
	protected final IThemeManager themeManager;
	
	public ThemeHelper(BundleContext context) {
		this.context = context;
		this.serviceRef = context.getServiceReference(IThemeManager.class.getName());
		
		this.themeManager = blindCast(context.getService(serviceRef));
	}
	
	@Override
	public void dispose() {
		super.dispose();
		context.ungetService(serviceRef);
	}
	
	public ITheme getActiveThemeForCurrentDisplay() {
		IThemeEngine engine = themeManager.getEngineForDisplay(Display.getCurrent());
		return engine.getActiveTheme();
	}
	
	public String getIdOfActiveThemeForCurrentDisplay() {
		ITheme activeTheme = getActiveThemeForCurrentDisplay();
		return activeTheme == null ? "" : activeTheme.getId();
	}
	
	public abstract class ThemeChangeListener implements EventHandler, IDisposable {
		
		protected final ServiceRegistration<?> svcRegistration;
		
		public ThemeChangeListener() {
			this(owned);
		}
		
		public ThemeChangeListener(IOwner owner) {
			Dictionary<String, String> properties = new Hashtable<>();
			properties.put(EventConstants.EVENT_TOPIC, IThemeEngine.Events.THEME_CHANGED);
			svcRegistration = context.registerService(EventHandler.class, this, properties);
			
			owner.bind(this);
		}
		
		@Override
		public void dispose() {
			svcRegistration.unregister();
		}
		
	}
	
}