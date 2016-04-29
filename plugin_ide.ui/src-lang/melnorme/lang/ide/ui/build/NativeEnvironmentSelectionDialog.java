/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.build;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;

import _org.eclipse.debug.internal.ui.SWTFactory;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.ide.ui.LangUIPlugin;

/**
 * This class provides the framework for a general selection dialog class.
 * 
 * @see AbstractDebugCheckboxSelectionDialog
 * 
 * @since 3.3
 */
abstract class AbstractDebugSelectionDialog extends SelectionDialog {
	
	public static String EMPTY_STRING = "";

	protected StructuredViewer fViewer = null;
	
	/**
	 * Constructor
	 * @param parentShell the parent shell
	 */
	public AbstractDebugSelectionDialog(Shell parentShell) {
		super(parentShell);
		setDialogBoundsSettings(getDialogBoundsSettings(), Dialog.DIALOG_PERSISTSIZE);
	}
	
	/**
	 * returns the dialog settings area id
	 * @return the id of the dialog settings area
	 */
	protected abstract String getDialogSettingsId();
	
	/**
	 * Returns the object to use as input for the viewer
	 * @return the object to use as input for the viewer
	 */
	protected abstract Object getViewerInput();
	
	/**
	 * Create and return a viewer to use in this dialog.
	 * 
	 * @param parent the composite the viewer should be created in
	 * @return the viewer to use in the dialog
	 */
	protected abstract StructuredViewer createViewer(Composite parent);
	
	/**
	 * Returns if the dialog and/or current selection is/are valid.
	 * This method is polled when selection changes are made to update the enablement
	 * of the OK button by default 
	 * @return true if the dialog is in a valid state, false otherwise
	 * 
	 * @since 3.4
	 */
	protected abstract boolean isValid();
	
	/**
	 * Returns the content provider for the viewer
	 * @return the content provider for the viewer
	 */
	protected IContentProvider getContentProvider() {
		//by default return a simple array content provider
		return new ArrayContentProvider();
	}
	
	protected abstract IBaseLabelProvider getLabelProvider();
	
	/**
	 * Returns the help context id for this dialog
	 * @return the help context id for this dialog
	 */
	abstract protected String getHelpContextId();
	
	/**
	 * This method allows listeners to be added to the viewer after it
	 * is created.
	 */
	/**
	 * This method allows listeners to be added to the viewer.  Called
	 * after the viewer has been created and its input set.
	 * 
	 * @param viewer the viewer returned by createViewer()
	 */
	protected void addViewerListeners(StructuredViewer viewer){
		//do nothing by default
	}
	
	/**
	 * This method allows custom controls to be added before the viewer
	 * @param parent the parent composite to add these custom controls to
	 */
	protected void addCustomHeaderControls(Composite parent) {
		//do nothing by default
	}
	
	/**
	 * This method allows custom controls to be added after the viewer
	 * @param parent the parent composite to add these controls to
	 */
	protected void addCustomFooterControls(Composite parent) {
		//do nothing by default
	}
	
	/**
	 * This method allows the newly created controls to be initialized.
	 * This method is called only once all controls have been created from the 
	 * <code>createContents</code> method.
	 * 
	 * By default this method initializes the OK button control.
	 */
	protected void initializeControls() {
		getButton(IDialogConstants.OK_ID).setEnabled(isValid());
	}
	
	/**
	 * Returns the viewer used to display information in this dialog.
	 * Can be <code>null</code> if the viewer has not been created.
	 * @return viewer used in this dialog
	 */
	protected Viewer getViewer(){
    	return fViewer;
    }
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite comp = (Composite) super.createContents(parent);
		initializeControls();
		return comp;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		initializeDialogUnits(parent);
		Composite comp = (Composite) super.createDialogArea(parent);
		addCustomHeaderControls(comp);
		String label = getMessage();
		if(label != null && !EMPTY_STRING.equals(label)) {
			SWTFactory.createWrapLabel(comp, label, 1);
		}
		label = getViewerLabel();
		if(label != null && !EMPTY_STRING.equals(label)) {
			SWTFactory.createLabel(comp, label, 1);
		}
		fViewer = createViewer(comp);
		fViewer.setLabelProvider(getLabelProvider());
		fViewer.setContentProvider(getContentProvider());
		fViewer.setInput(getViewerInput());
		List<?> selectedElements = getInitialElementSelections();
		if (selectedElements != null && !selectedElements.isEmpty()){
			fViewer.setSelection(new StructuredSelection(selectedElements));
		}
		addViewerListeners(fViewer);
		addCustomFooterControls(comp);
		Dialog.applyDialogFont(comp);
		String help = getHelpContextId();
		if(help != null) {
			PlatformUI.getWorkbench().getHelpSystem().setHelp(comp, help);
		}
		return comp;
	}
	
	/**
	 * This method returns the label describing what to do with the viewer. Typically this label
	 * will include the key accelerator to get to the viewer via the keyboard
	 * @return the label for the viewer
	 */
	abstract protected String getViewerLabel();
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.SelectionDialog#getDialogBoundsSettings()
	 */
	@Override
	protected IDialogSettings getDialogBoundsSettings() {
		IDialogSettings settings = LangUIPlugin.getDefault().getDialogSettings();
		IDialogSettings section = settings.getSection(getDialogSettingsId());
		if (section == null) {
			section = settings.addNewSection(getDialogSettingsId());
		} 
		return section;
	}
}

/*******************************************************************************
 * Copyright (c) 2006, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/


/**
 * This class provides selection dialog using a check box table viewer. 
 * 
 * @since 3.4
 */
abstract class AbstractDebugCheckboxSelectionDialog extends AbstractDebugSelectionDialog {
	
	/**
	 * Whether to add Select All / De-select All buttons to the custom footer controls.
	 */
	private boolean fShowSelectButtons = false;
	
	/**
	 * Constructor
	 * @param parentShell the parent shell
	 */
	public AbstractDebugCheckboxSelectionDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}
	
	/**
	 * Returns the viewer cast to the correct instance.  Possibly <code>null</code> if
	 * the viewer has not been created yet.
	 * @return the viewer cast to CheckboxTableViewer
	 */
	protected CheckboxTableViewer getCheckBoxTableViewer() {
		return (CheckboxTableViewer) fViewer;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.launchConfigurations.AbstractDebugSelectionDialog#initializeControls()
	 */
	@Override
	protected void initializeControls() {
		List<?> selectedElements = getInitialElementSelections();
		if (selectedElements != null && !selectedElements.isEmpty()){
			getCheckBoxTableViewer().setCheckedElements(selectedElements.toArray());
			getCheckBoxTableViewer().setSelection(StructuredSelection.EMPTY);
		}
		super.initializeControls();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.launchConfigurations.AbstractDebugSelectionDialog#createViewer(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected StructuredViewer createViewer(Composite parent){
		//by default return a checkbox table viewer
		Table table = new Table(parent, SWT.BORDER | SWT.SINGLE | SWT.CHECK);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 150;
		gd.widthHint = 250;
		table.setLayoutData(gd);
		return new CheckboxTableViewer(table);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.launchConfigurations.AbstractDebugSelectionDialog#addViewerListeners(org.eclipse.jface.viewers.StructuredViewer)
	 */
	@Override
	protected void addViewerListeners(StructuredViewer viewer) {
		getCheckBoxTableViewer().addCheckStateListener(new DefaultCheckboxListener());
	}
	
	/**
	 * A checkbox state listener that ensures that exactly one element is checked
	 * and enables the OK button when this is the case.
	 *
	 */
	private class DefaultCheckboxListener implements ICheckStateListener{
		@Override
		public void checkStateChanged(CheckStateChangedEvent event) {
			getButton(IDialogConstants.OK_ID).setEnabled(isValid());
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.AbstractDebugSelectionDialog#isValid()
	 */
	@Override
	protected boolean isValid() {
		return getCheckBoxTableViewer().getCheckedElements().length > 0;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		Object[] elements =  getCheckBoxTableViewer().getCheckedElements();
		setResult(Arrays.asList(elements));
		super.okPressed();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.launchConfigurations.AbstractDebugSelectionDialog#addCustomFooterControls(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void addCustomFooterControls(Composite parent) {
		if (fShowSelectButtons){
			Composite comp = SWTFactory.createComposite(parent, 2, 1, GridData.FILL_HORIZONTAL);
			GridData gd = (GridData) comp.getLayoutData();
			gd.horizontalAlignment = SWT.END;
			Button button = SWTFactory.createPushButton(comp, "&Select All", null);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					getCheckBoxTableViewer().setAllChecked(true);
					getButton(IDialogConstants.OK_ID).setEnabled(isValid());
				}
			});
			button = SWTFactory.createPushButton(comp, "&Deselect All", null);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					getCheckBoxTableViewer().setAllChecked(false);
					getButton(IDialogConstants.OK_ID).setEnabled(isValid());
				}
			});
		}
	}
	
	/**
	 * If this setting is set to true before the dialog is opened, a Select All and 
	 * a De-select All button will be added to the custom footer controls.  The default
	 * setting is false.
	 * 
	 * @param setting whether to show the select all and de-select all buttons
	 */
	protected void setShowSelectAllButtons(boolean setting){
		fShowSelectButtons = setting;
	}
	    
}

/**
 * This dialog allows users to select one or more known native environment variables from a list.
 */
class NativeEnvironmentSelectionDialog extends AbstractDebugCheckboxSelectionDialog {
	
	private Object fInput;
	
	public NativeEnvironmentSelectionDialog(Shell parentShell, Object input) {
		super(parentShell);
		fInput = input;
		setShellStyle(getShellStyle() | SWT.RESIZE);
		setShowSelectAllButtons(true);
	}
	
	@Override
	protected String getDialogSettingsId() {
		return LangCore.PLUGIN_ID + ".ENVIRONMENT_TAB.NATIVE_ENVIROMENT_DIALOG"; //$NON-NLS-1$
	}

	@Override
	protected String getHelpContextId() {
		return null;
	}

	@Override
	protected Object getViewerInput() {
		return fInput;
	}

	@Override
	protected String getViewerLabel() {
		return LaunchConfigurationsMessages.EnvironmentTab_19;
	}
	
	@Override
	protected IBaseLabelProvider getLabelProvider() {
		return new ILabelProvider() {
			@Override
			public Image getImage(Object element) {
				return LangImages.IMG_ENVIRONMENT.getImage();
			}
			@Override
			public String getText(Object element) {
				EnvironmentVariable var = (EnvironmentVariable) element;
				return MessageFormat.format(LaunchConfigurationsMessages.EnvironmentTab_7, new Object[] {
						var.getName(), var.getValue() });
			}
			@Override
			public void addListener(ILabelProviderListener listener) {
			}
			@Override
			public void dispose() {
			}
			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}
			@Override
			public void removeListener(ILabelProviderListener listener) {
			}				
		};
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.launchConfigurations.AbstractDebugSelectionDialog#getContentProvider()
	 */
	@Override
	protected IContentProvider getContentProvider() {
		return new IStructuredContentProvider() {
			@Override
			public Object[] getElements(Object inputElement) {
				EnvironmentVariable[] elements = null;
				if (inputElement instanceof HashMap) {
					Comparator<Object> comparator = new Comparator<Object>() {
						@Override
						public int compare(Object o1, Object o2) {
							String s1 = (String) o1;
							String s2 = (String) o2;
							return s1.compareTo(s2);
						}
					};
					TreeMap<Object, Object> envVars = new TreeMap<Object, Object>(comparator);
					envVars.putAll((Map<?, ?>) inputElement);
					elements = new EnvironmentVariable[envVars.size()];
					int index = 0;
					for (Iterator<Object> iterator = envVars.keySet().iterator(); iterator.hasNext(); index++) {
						Object key = iterator.next();
						elements[index] = (EnvironmentVariable) envVars.get(key);
					}
				}
				return elements;
			}
			@Override
			public void dispose() {	
			}
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
		};
	}
}