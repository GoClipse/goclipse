/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Bruno Medeiros = removed initialize method
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;


/**
 * Interface for preference configuration blocks which can either be
 * wrapped by a {@link melnorme.lang.ide.ui.preferences.AbstractConfigurationBlockPreferencePage2.internal.ui.preferences.AbstractConfigurationBlockPreferencePage}
 * or be included some preference page.
 * <p>
 * Clients may implement this interface.
 * </p>
 *
 * @since 3.0
 */
public interface IPreferenceConfigurationBlock2 {

	/**
	 * Creates the preference control.
	 *
	 * @param parent the parent composite to which to add the preferences control
	 * @return the control that was added to <code>parent</code>
	 */
	Control createControl(Composite parent);

	/**
	 * Called when the <code>OK</code> button is pressed on the preference
	 * page. Implementations should commit the configured preference settings
	 * into their form of preference storage.
	 */
	void performOk();

	/**
	 * Called when the <code>Defaults</code> button is pressed on the
	 * preference page. Implementation should reset any preference settings to
	 * their default values and adjust the controls accordingly.
	 */
	void performDefaults();

	/**
	 * Called when the preference page is being disposed. Implementations should
	 * free any resources they are holding on to.
	 */
	void dispose();
}
