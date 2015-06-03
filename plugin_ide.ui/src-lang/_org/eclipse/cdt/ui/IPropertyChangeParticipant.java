/*******************************************************************************
 * Copyright (c) 2008 Symbian Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Andrew Ferguson (Symbian) - Initial Implementation
 *******************************************************************************/
package _org.eclipse.cdt.ui;

import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * Implemented by classes which can optionally participate in property
 * change events, and report whether an event would affect them without
 * adapting to it.
 * <p>
 * Clients may implement this interface.
 * </p>
 * 
 * @since 5.0
 */
public interface IPropertyChangeParticipant {
	/**
	 * @param event
	 * @return true if the specified event will affect the participant's
	 * behaviour in a way it determines potential clients could act upon.
	 */
	public boolean affectsBehavior(PropertyChangeEvent event);
	
	/**
	 * Performs any necessary to adapt the participant to the specified event.
	 * @param event
	 */
	public void adaptToPreferenceChange(PropertyChangeEvent event);
}
