/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     QNX Software System
 *******************************************************************************/
package melnorme.util.swt.jface.preference;

import java.util.List;

import melnorme.utilbox.misc.ArrayUtil;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * An overlaying preference store.
 */
public class OverlayPreferenceStore implements IPreferenceStore {
	
	public static final class TypeDescriptor {
		protected TypeDescriptor() {
		}
	}
	
	public static final TypeDescriptor BOOLEAN= new TypeDescriptor();
	public static final TypeDescriptor DOUBLE= new TypeDescriptor();
	public static final TypeDescriptor FLOAT= new TypeDescriptor();
	public static final TypeDescriptor INT= new TypeDescriptor();
	public static final TypeDescriptor LONG= new TypeDescriptor();
	public static final TypeDescriptor STRING= new TypeDescriptor();
	
	public static class OverlayKey {
		final TypeDescriptor fDescriptor;
		final String fKey;
		
		public OverlayKey(TypeDescriptor descriptor, String key) {
			fDescriptor= descriptor;
			fKey= key;
		}
	}
	
	private class PropertyListener implements IPropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			OverlayKey key= findOverlayKey(event.getProperty());
			if (key != null)
				propagateProperty(fParent, key, fStore); 
		}
	}
	
	protected IPreferenceStore fParent;
	protected IPreferenceStore fStore;
	protected OverlayKey[] fOverlayKeys;
	protected boolean fLoaded;
	protected PropertyListener fPropertyListener;
	
	public OverlayPreferenceStore(IPreferenceStore parent, List<OverlayKey> overlayKeys) {
		fParent = parent;
		fOverlayKeys = ArrayUtil.createFrom(overlayKeys, OverlayKey.class) ;
		fStore = new PreferenceStore();
	}
	
	protected OverlayKey findOverlayKey(String key) {
		for (int i= 0; i < fOverlayKeys.length; i++) {
			if (fOverlayKeys[i].fKey.equals(key))
				return fOverlayKeys[i];
		}
		return null;
	}
	
	private boolean covers(String key) {
		return (findOverlayKey(key) != null);
	}
	
	protected void propagateProperty(IPreferenceStore orgin, OverlayKey key, IPreferenceStore target) {
		if (orgin.isDefault(key.fKey)) {
			if (!target.isDefault(key.fKey))
				target.setToDefault(key.fKey);
			return;
		}
		
		TypeDescriptor d= key.fDescriptor;
		if (BOOLEAN == d) {
			boolean originValue= orgin.getBoolean(key.fKey);
			boolean targetValue= target.getBoolean(key.fKey);
			if (targetValue != originValue)
				target.setValue(key.fKey, originValue);
		} else if (DOUBLE == d) {
			double originValue= orgin.getDouble(key.fKey);
			double targetValue= target.getDouble(key.fKey);
			if (targetValue != originValue)
				target.setValue(key.fKey, originValue);
		} else if (FLOAT == d) {
			float originValue= orgin.getFloat(key.fKey);
			float targetValue= target.getFloat(key.fKey);
			if (targetValue != originValue)
				target.setValue(key.fKey, originValue);
		} else if (INT == d) {
			int originValue= orgin.getInt(key.fKey);
			int targetValue= target.getInt(key.fKey);
			if (targetValue != originValue)
				target.setValue(key.fKey, originValue);
		} else if (LONG == d) {
			long originValue= orgin.getLong(key.fKey);
			long targetValue= target.getLong(key.fKey);
			if (targetValue != originValue)
				target.setValue(key.fKey, originValue);
		} else if (STRING == d) {
			String originValue= orgin.getString(key.fKey);
			String targetValue= target.getString(key.fKey);
			if (targetValue != null && originValue != null && !targetValue.equals(originValue))
				target.setValue(key.fKey, originValue);
		}
	}
	
	public void propagate() {
		for (int i= 0; i < fOverlayKeys.length; i++)
			propagateProperty(fStore, fOverlayKeys[i], fParent);
	}
	
	protected void loadProperty(IPreferenceStore orgin, OverlayKey key, IPreferenceStore target, 
			boolean forceInitialization) {
		TypeDescriptor d= key.fDescriptor;
		if (BOOLEAN == d) {
			if (forceInitialization)
				target.setValue(key.fKey, true);
			target.setValue(key.fKey, orgin.getBoolean(key.fKey));
			target.setDefault(key.fKey, orgin.getDefaultBoolean(key.fKey));
		} else if (DOUBLE == d) {
			if (forceInitialization)
				target.setValue(key.fKey, 1.0D);
			target.setValue(key.fKey, orgin.getDouble(key.fKey));
			target.setDefault(key.fKey, orgin.getDefaultDouble(key.fKey));
		} else if (FLOAT == d) {
			if (forceInitialization)
				target.setValue(key.fKey, 1.0F);
			target.setValue(key.fKey, orgin.getFloat(key.fKey));
			target.setDefault(key.fKey, orgin.getDefaultFloat(key.fKey));
		} else if (INT == d) {
			if (forceInitialization)
				target.setValue(key.fKey, 1);
			target.setValue(key.fKey, orgin.getInt(key.fKey));
			target.setDefault(key.fKey, orgin.getDefaultInt(key.fKey));
		} else if (LONG == d) {
			if (forceInitialization)
				target.setValue(key.fKey, 1L);
			target.setValue(key.fKey, orgin.getLong(key.fKey));
			target.setDefault(key.fKey, orgin.getDefaultLong(key.fKey));
		} else if (STRING == d) {
			if (forceInitialization)
				target.setValue(key.fKey, "1"); //$NON-NLS-1$
			target.setValue(key.fKey, orgin.getString(key.fKey));
			target.setDefault(key.fKey, orgin.getDefaultString(key.fKey));
		}
	}
	
	public void load() {
		for (int i= 0; i < fOverlayKeys.length; i++)
			loadProperty(fParent, fOverlayKeys[i], fStore, true);

		fLoaded= true;
	}
	
	public void loadDefaults() {
		for (int i= 0; i < fOverlayKeys.length; i++)
			setToDefault(fOverlayKeys[i].fKey);
	}
	
	public void start() {
		if (fPropertyListener == null) {
			fPropertyListener= new PropertyListener();
			fParent.addPropertyChangeListener(fPropertyListener);
		}
	}
	
	public void stop() {
		if (fPropertyListener != null)  {
			fParent.removePropertyChangeListener(fPropertyListener);
			fPropertyListener= null;
		}
	}
	
	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		fStore.addPropertyChangeListener(listener);
	}
	
	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		fStore.removePropertyChangeListener(listener);
	}
	
	@Override
	public void firePropertyChangeEvent(String name, Object oldValue, Object newValue) {
		fStore.firePropertyChangeEvent(name, oldValue, newValue);
	}

	@Override
	public boolean contains(String name) {
		return fStore.contains(name);
	}
	
	@Override
	public boolean getBoolean(String name) {
		return fStore.getBoolean(name);
	}

	@Override
	public boolean getDefaultBoolean(String name) {
		return fStore.getDefaultBoolean(name);
	}

	@Override
	public double getDefaultDouble(String name) {
		return fStore.getDefaultDouble(name);
	}

	@Override
	public float getDefaultFloat(String name) {
		return fStore.getDefaultFloat(name);
	}

	@Override
	public int getDefaultInt(String name) {
		return fStore.getDefaultInt(name);
	}

	@Override
	public long getDefaultLong(String name) {
		return fStore.getDefaultLong(name);
	}

	@Override
	public String getDefaultString(String name) {
		return fStore.getDefaultString(name);
	}

	@Override
	public double getDouble(String name) {
		return fStore.getDouble(name);
	}

	@Override
	public float getFloat(String name) {
		return fStore.getFloat(name);
	}

	@Override
	public int getInt(String name) {
		return fStore.getInt(name);
	}

	@Override
	public long getLong(String name) {
		return fStore.getLong(name);
	}

	@Override
	public String getString(String name) {
		return fStore.getString(name);
	}

	@Override
	public boolean isDefault(String name) {
		return fStore.isDefault(name);
	}

	@Override
	public boolean needsSaving() {
		return fStore.needsSaving();
	}

	@Override
	public void putValue(String name, String value) {
		if (covers(name))
			fStore.putValue(name, value);
	}

	@Override
	public void setDefault(String name, double value) {
		if (covers(name))
			fStore.setDefault(name, value);
	}

	@Override
	public void setDefault(String name, float value) {
		if (covers(name))
			fStore.setDefault(name, value);
	}

	@Override
	public void setDefault(String name, int value) {
		if (covers(name))
			fStore.setDefault(name, value);
	}

	@Override
	public void setDefault(String name, long value) {
		if (covers(name))
			fStore.setDefault(name, value);
	}

	@Override
	public void setDefault(String name, String value) {
		if (covers(name))
			fStore.setDefault(name, value);
	}

	@Override
	public void setDefault(String name, boolean value) {
		if (covers(name))
			fStore.setDefault(name, value);
	}

	@Override
	public void setToDefault(String name) {
		fStore.setToDefault(name);
	}

	@Override
	public void setValue(String name, double value) {
		if (covers(name))
			fStore.setValue(name, value);
	}

	@Override
	public void setValue(String name, float value) {
		if (covers(name))
			fStore.setValue(name, value);
	}

	@Override
	public void setValue(String name, int value) {
		if (covers(name))
			fStore.setValue(name, value);
	}

	@Override
	public void setValue(String name, long value) {
		if (covers(name))
			fStore.setValue(name, value);
	}

	@Override
	public void setValue(String name, String value) {
		if (covers(name))
			fStore.setValue(name, value);
	}

	@Override
	public void setValue(String name, boolean value) {
		if (covers(name))
			fStore.setValue(name, value);
	}

//	/**
//	 * The keys to add to the list of overlay keys.
//	 * <p>
//	 * Note: This method must be called before {@link #load()} is called. 
//	 * </p>
//	 * 
//	 * @param keys
//	 * @since 3.0
//	 */
//	public void addKeys(OverlayKey[] keys) {
//		Assert.isTrue(!fLoaded);
//		Assert.isNotNull(keys);
//		
//		fOverlayKeys = ArrayUtil.concat(fOverlayKeys, keys);
//		
//		int overlayKeysLength= fOverlayKeys.length;
//		OverlayKey[] result= new OverlayKey[keys.length + overlayKeysLength];
//
//		for (int i= 0, length= overlayKeysLength; i < length; i++)
//			result[i]= fOverlayKeys[i];
//		
//		for (int i= 0, length= keys.length; i < length; i++)
//			result[overlayKeysLength + i]= keys[i];
//		
//		fOverlayKeys= result;
//		
//		if (fLoaded)
//			load();
//	}
}

