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