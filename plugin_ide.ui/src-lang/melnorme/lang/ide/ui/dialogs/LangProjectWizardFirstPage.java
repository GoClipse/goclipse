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
package melnorme.lang.ide.ui.dialogs;


import static melnorme.lang.ide.ui.utils.DialogPageUtils.severityToMessageType;
import static org.eclipse.jface.layout.GridDataFactory.fillDefaults;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.core.utils.ProjectValidator;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.utils.WorkbenchUtils;
import melnorme.util.swt.SWTFactory;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.SWTUtil;
import melnorme.util.swt.components.CompositeWidget;
import melnorme.util.swt.components.fields.DirectoryTextField;
import melnorme.util.swt.components.fields.EnablementButtonTextField;
import melnorme.util.swt.components.fields.TextFieldWidget;
import melnorme.util.swt.components.misc.StatusMessageWidget;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.fields.FieldValueListener.FieldChangeListener;
import melnorme.utilbox.status.IStatusMessage;
import melnorme.utilbox.status.Severity;
import melnorme.utilbox.status.StatusException;

public abstract class LangProjectWizardFirstPage extends WizardPage {
	
	protected final NameGroup nameGroup = createNameGroup();
	protected final LocationGroup locationGroup = createLocationGroup();
	protected final ProjectValidationGroup projectValidationGroup = createProjectValidationGroup();
	protected final PreferencesValidationGroup prefValidationGroup = createPreferencesValidationGroup();
	
	public LangProjectWizardFirstPage() {
		super(LangProjectWizardFirstPage.class.getSimpleName());
	}
	
	public LangProjectWizardFirstPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}
	
	protected NameGroup createNameGroup() {
		return new NameGroup();
	}
	protected ProjectValidationGroup createProjectValidationGroup() {
		return new ProjectValidationGroup();
	}
	protected LocationGroup createLocationGroup() {
		return new LocationGroup();
	}
	protected PreferencesValidationGroup createPreferencesValidationGroup() {
		return new PreferencesValidationGroup();
	}
	
	/* -----------------  ----------------- */
	
	public NameGroup getNameGroup() {
		return nameGroup;
	}
	
	public LocationGroup getLocationGroup() {
		return locationGroup;
	}
	
	public ProjectValidationGroup getDetectGroup() {
		return projectValidationGroup;
	}
	
	public IPath getProjectLocation() {
		return locationGroup.getProjectLocation();
	}
	
	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		
		Composite topControl = new Composite(parent, SWT.NULL);
		topControl.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		topControl.setLayout(new GridLayout(1, false));
		createContents(topControl);
		
		setControl(topControl);
		Dialog.applyDialogFont(topControl);
	}
	
	protected GridDataFactory sectionGDF() {
		return fillDefaults().grab(true, false);
	}
	
	protected void createContents(Composite parent) {
		
		nameGroup.createComponent(parent, sectionGDF().create());
		locationGroup.createComponent(parent, sectionGDF().create());
		createContents_ValidationGroups(parent);
		
		FieldChangeListener listener = this::validateDialog;
		nameGroup.textField.addChangeListener(listener);
		locationGroup.addChangeListener(listener);
		
		validateDialog();
	}
	
	protected void createContents_ValidationGroups(Composite parent) {
		projectValidationGroup.createComponent(parent, sectionGDF().hint(500, SWT.DEFAULT).create());
		prefValidationGroup.createComponent(parent, sectionGDF().hint(500, SWT.DEFAULT).create());
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			nameGroup.postSetFocus();
		}
	}
	
	public static class NameGroup extends CompositeWidget {
		
		protected final TextFieldWidget textField = new TextFieldWidget(WizardMessages.LangNewProject_NameGroup_label);
		
		public NameGroup() {
			super(true);
			addChildWidget(textField);
			this.layoutColumns = 2;
			
			validation.addFieldValidationX(true, textField, this::validateProjectName);
		}
		
		public String getName() {
			return textField.getFieldValue();
		}
		
		public TextFieldWidget getNameField() {
			return textField;
		}
		
		public void validateProjectName() throws StatusException {
			getProjectHandle2();
		}
		
		public IProject getProjectHandle2() throws StatusException {
			return new ProjectValidator().getProjectHandle(getName());
		}
		
		/* -----------------  ----------------- */
		
		public void postSetFocus() {
			SWTUtil.post_setFocus(textField.getFieldControl());
		}
		
	}
	
	public IProject getProjectHandle2() throws CommonException {
		return nameGroup.getProjectHandle2();
	}
	
	/* ----------------- Location ----------------- */
	
	public class LocationGroup extends EnablementButtonTextField {
		
		public LocationGroup() {
			super(
				WizardMessages.LangNewProject_Location_Directory_label, 
				WizardMessages.LangNewProject_Location_UseDefault_Label, 
				WizardMessages.LangNewProject_Location_Directory_buttonLabel
			);
		
			nameGroup.getNameField().addChangeListener(this::updateDefaultFieldValue);
			
			addFieldValidationX(true, () -> doValidate());
		}
		
		protected String getProjectName() {
			return nameGroup.getName();
		}
		
		protected boolean isDefaultLocation() {
			return isUseDefault();
		}
		
		protected String getLocationString() {
			return getFieldValue();
		}
		
		@Override
		protected String getDefaultFieldValue() {
			return Platform.getLocation().append(getProjectName()).toOSString();
		}
		
		/* -----------------  ----------------- */
		
		public IPath getProjectLocation() {
			String projectName = getProjectName();
			if(projectName.isEmpty()) {
				return null;
			}
			if(!Path.EMPTY.isValidPath(getLocationString())) {
				return null;
			}
			return Path.fromOSString(getLocationString());
		}
		
		public void doValidate() throws StatusException {
			IProject project = nameGroup.getProjectHandle2();
			
			if(project.exists() && !isDefaultLocation()) {
				throw new StatusException(Severity.ERROR,
					WizardMessages.LangNewProject_Location_projectExistsCannotChangeLocation);
			}
			
			IPath projectLocation = getProjectLocation();
			if(projectLocation == null) {
				throw new StatusException(Severity.ERROR,
					WizardMessages.LangNewProject_Location_invalidLocation);
			}
			
			EclipseUtils.validate(
				() -> ResourceUtils.getWorkspace().validateProjectLocation(project, projectLocation));
		}
		
		/* -----------------  ----------------- */
		
		@Override
		protected Composite doCreateTopLevelControl(Composite parent) {
			return SWTFactoryUtil.createGroup(parent, WizardMessages.LangNewProject_LocationGroup_label);
		}
		
		@Override
		public int getPreferredLayoutColumns() {
			return 3;
		}
		
		@Override
		protected void createContents_Label(Composite parent) {
			label = SWTFactory.createLabel(parent, SWT.NONE, labelText);
		}
		
		@Override
		protected String getNewValueFromButtonSelection() throws OperationCancellation {
			return DirectoryTextField.openDirectoryDialog(getFieldValue(), button.getShell());
		}
		
	}
	
	public class ProjectValidationGroup extends StatusMessageWidget {
		
		@Override
		public void updateWidgetFromInput() {
			IProject projectHandle;
			try {
				projectHandle = getProjectHandle2();
			} catch(CommonException e) {
				projectHandle = null;
			}
			
			IPath projectLoc = getProjectLocation();
			
			if(projectHandle != null && projectHandle.exists()) {
				setStatusMessage(Severity.INFO, WizardMessages.LangNewProject_DetectGroup_projectExists);
			} else if(projectLoc != null && projectLoc.toFile().exists()) {
				setStatusMessage(Severity.INFO, WizardMessages.LangNewProject_DetectGroup_message);
			} else {
				setStatusMessage(null);
			}
		}
		
	}
	
	public class PreferencesValidationGroup extends ProjectValidationGroup {
		
		@Override
		protected void createContents(Composite parent) {
			super.createContents(parent);
			
			hintText.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					openPreferencePage();
				}
			});
		}
		
		protected void openPreferencePage() {
			WorkbenchUtils.openPreferencePage(getShell(), LangUIPlugin_Actual.ROOT_PREF_PAGE_ID);
			updateWidgetFromInput();
		}
		
		@Override
		public void updateWidgetFromInput() {
			try {
				validatePreferences();
				setStatusMessage(null);
			} catch (CommonException ve) {
				setPreferencesErrorMessage(ve);
			}
		}
		
		@SuppressWarnings("unused")
		protected void setPreferencesErrorMessage(CommonException ve) {
			setStatusMessage(Severity.WARNING, 
				"The "+ LangCore_Actual.NAME_OF_LANGUAGE + 
				" preferences have not been configured correctly.\n"+
				"<a>Click here to configure preferences...</a>");
		}
		
	}
	
	protected void validatePreferences() throws StatusException {
		LangCore.settings().SDK_LOCATION.getValue();
	}
	
	protected boolean validateDialog() {
		IStatusMessage validationStatus = getValidationStatus();
		
		boolean valid;
		if(validationStatus == null) {
			setMessage(null);
			setPageComplete(true);
			
			valid = true;
		} else {
			Severity severity = validationStatus.getSeverity();
			setMessage(validationStatus.getMessage(), severityToMessageType(severity));
			
			setPageComplete(severity != Severity.ERROR);
			valid = false;
		}
		
		projectValidationGroup.updateWidgetFromInput();
		prefValidationGroup.updateWidgetFromInput();
		
		return valid;
	}
	
	protected IStatusMessage getValidationStatus() {
		try {
			nameGroup.validate();
			locationGroup.validate();
			return null;
		} catch(StatusException e) {
			return e;
		}
	}
	
}