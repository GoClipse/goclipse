/*******************************************************************************
 * Copyright (c) 2011 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.tests.utils;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

public class WizardDialog__Accessor extends WizardDialog {
    public WizardDialog__Accessor(Shell parentShell, IWizard newWizard) {
    	super(parentShell, newWizard);
    }
    
    // make buttons public

    @Override
    public void nextPressed() {
    	super.nextPressed();
    }
    @Override
    public void backPressed() {
    	super.backPressed();
    }
    
    @Override
    public void finishPressed() {
    	super.finishPressed();
    }
    
    @Override
    public void cancelPressed() {
    	super.cancelPressed();
    }
}