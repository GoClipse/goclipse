/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.utils.prefs;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.osgi.service.prefs.BackingStoreException;

//BM: changed PreferenceChangeListener to not use Display
/**
 * Adapts an options {@link IEclipsePreferences} to {@link org.eclipse.jface.preference.IPreferenceStore}.
 * <p>
 * This preference store is read-only i.e. write access
 * throws an {@link java.lang.UnsupportedOperationException}.
 * </p>
 */
public class EclipsePreferencesAdapter implements IPreferenceStore {

	/**
	 * Preference change listener. Listens for events preferences
	 * fires a {@link org.eclipse.jface.util.PropertyChangeEvent}
	 * on this adapter with arguments from the received event.
	 */
	private class PreferenceChangeListener implements IEclipsePreferences.IPreferenceChangeListener {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void preferenceChange(final IEclipsePreferences.PreferenceChangeEvent event) {
			firePropertyChangeEvent(event.getKey(), event.getOldValue(), event.getNewValue());
		}
	}

	/** Listeners on on this adapter */
	private ListenerList<IPropertyChangeListener> fListeners= new ListenerList<>(ListenerList.IDENTITY);

	/** Listener on the node */
	private IEclipsePreferences.IPreferenceChangeListener fListener= new PreferenceChangeListener();

	/** wrapped node */
	private final IScopeContext fContext;
	private final String fQualifier;

	/**
	 * Initialize with the node to wrap
	 *
	 * @param context the context to access
	 * @param qualifier the qualifier
	 */
	public EclipsePreferencesAdapter(IScopeContext context, String qualifier) {
		fContext= context;
		fQualifier= qualifier;
	}

	private IEclipsePreferences getNode() {
		return fContext.getNode(fQualifier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		if (fListeners.size() == 0)
			getNode().addPreferenceChangeListener(fListener);
		fListeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		fListeners.remove(listener);
		if (fListeners.size() == 0) {
			getNode().removePreferenceChangeListener(fListener);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(String name) {
		return getNode().get(name, null) != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void firePropertyChangeEvent(String name, Object oldValue, Object newValue) {
		PropertyChangeEvent event= new PropertyChangeEvent(this, name, oldValue, newValue);
		Object[] listeners= fListeners.getListeners();
		for (int i= 0; i < listeners.length; i++)
			((IPropertyChangeListener) listeners[i]).propertyChange(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getBoolean(String name) {
		return getNode().getBoolean(name, BOOLEAN_DEFAULT_DEFAULT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getDefaultBoolean(String name) {
		return BOOLEAN_DEFAULT_DEFAULT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDefaultDouble(String name) {
		return DOUBLE_DEFAULT_DEFAULT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getDefaultFloat(String name) {
		return FLOAT_DEFAULT_DEFAULT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDefaultInt(String name) {
		return INT_DEFAULT_DEFAULT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getDefaultLong(String name) {
		return LONG_DEFAULT_DEFAULT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultString(String name) {
		return STRING_DEFAULT_DEFAULT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDouble(String name) {
		return getNode().getDouble(name, DOUBLE_DEFAULT_DEFAULT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getFloat(String name) {
		return getNode().getFloat(name, FLOAT_DEFAULT_DEFAULT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getInt(String name) {
		return getNode().getInt(name, INT_DEFAULT_DEFAULT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLong(String name) {
		return getNode().getLong(name, LONG_DEFAULT_DEFAULT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getString(String name) {
		return getNode().get(name, STRING_DEFAULT_DEFAULT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDefault(String name) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean needsSaving() {
		try {
			return getNode().keys().length > 0;
		} catch (BackingStoreException e) {
			// ignore
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putValue(String name, String value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefault(String name, double value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefault(String name, float value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefault(String name, int value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefault(String name, long value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefault(String name, String defaultObject) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefault(String name, boolean value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setToDefault(String name) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(String name, double value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(String name, float value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(String name, int value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(String name, long value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(String name, String value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(String name, boolean value) {
		throw new UnsupportedOperationException();
	}

}