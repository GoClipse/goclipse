/**
 * 
 */
package com.googlecode.goclipse.utils;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Properties;

import org.eclipse.core.resources.IProject;

/**
 * 
 * @author kevin
 */
public class ProjectProperties extends Properties {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1970944256732066617L;

	private final IProject project;

	private PropertyChangeSupport changeSupport;

	public ProjectProperties(IProject project) {
		this.project = project;
		this.changeSupport = new PropertyChangeSupport(this);
	}

	@Override
	public synchronized Object put(Object key, Object value) {
		Object oldValue = super.put(key, value);
		changeSupport.firePropertyChange(key.toString(), oldValue, value);
		return oldValue;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	public PropertyChangeListener[] getPropertyChangeListeners() {
		return changeSupport.getPropertyChangeListeners();
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(propertyName, listener);
	}

	public IProject getProject() {
		return this.project;
	}

}
