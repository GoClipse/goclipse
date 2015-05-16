/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.dialogs;


import static org.eclipse.jface.layout.GridDataFactory.fillDefaults;

import java.net.URI;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.tooling.data.AbstractValidator.ValidationException;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.SWTUtil;
import melnorme.util.swt.components.AbstractComponentExt;
import melnorme.util.swt.components.fields.CheckBoxField;
import melnorme.util.swt.components.fields.DirectoryTextField;
import melnorme.util.swt.components.fields.TextField;
import melnorme.utilbox.fields.IFieldValueListener;
import melnorme.utilbox.misc.StringUtil;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.dialogs.PreferencesUtil;

public abstract class LangProjectWizardFirstPage extends WizardPage {
	
	protected final NameGroup nameGroup = new NameGroup();
	protected final LocationGroup locationGroup = createLocationGroup();
	protected final ProjectValidationGroup projectValidationGroup = createProjectValidationGroup();
	protected final PreferencesValidationGroup prefValidationGroup = createPreferencesValidationGroup();
	
	public LangProjectWizardFirstPage() {
		super(LangProjectWizardFirstPage.class.getSimpleName());
	}
	
	public LangProjectWizardFirstPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}
	
	protected ProjectValidationGroup createProjectValidationGroup() {
		return new ProjectValidationGroup();
	}
	protected LocationGroup createLocationGroup() {
		return new LocationGroup(nameGroup);
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
	
	public URI getProjectLocationUri() {
		return ResourceUtils.toUri(getProjectLocation());
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
		projectValidationGroup.createComponent(parent, sectionGDF().hint(500, SWT.DEFAULT).create());
		prefValidationGroup.createComponent(parent, sectionGDF().hint(500, SWT.DEFAULT).create());
		
		IFieldValueListener listener = new IFieldValueListener() {
			@Override
			public void fieldValueChanged() {
				validateDialog();
			}
		};
		nameGroup.textField.addValueChangedListener(listener);
		locationGroup.getSelectionField().addValueChangedListener(listener);
		locationGroup.getLocationField().addValueChangedListener(listener);
		
		validateDialog();
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			nameGroup.postSetFocus();
		}
	}
	
	public static class NameGroup extends AbstractComponentExt {
		
		protected TextField textField = new TextField(WizardMessages.LangNewProject_NameGroup_label);
		
		public String getName() {
			return textField.getFieldValue();
		}
		
		public TextField getNameField() {
			return textField;
		}
		
		public IProject getProjectHandle() {
			String name = getName();
			return name.isEmpty() ? null : ResourceUtils.getProject(name);
		}
		
		protected IStatus getValidationStatus() {
			
			final String name = getName();
			if(name.isEmpty()) {
				return LangCore.createErrorStatus(
					WizardMessages.LangNewProject_Name_error_emptyProjectName);
			}
			
			// Check name validity
			final IStatus nameStatus = ResourceUtils.getWorkspace().validateName(name, IResource.PROJECT);
			if(!nameStatus.isOK()) {
				return nameStatus;
			}
			
			return Status.OK_STATUS;
		}
		
		/* -----------------  ----------------- */
		
		@Override
		public int getPreferredLayoutColumns() {
			return 2;
		}
		
		@Override
		protected void createContents(Composite topControl) {
			textField.createComponentInlined(topControl);
		}
		
		@Override
		public void updateComponentFromInput() {
		}
		
		public void postSetFocus() {
			SWTUtil.post_setFocus(textField.getFieldControl());
		}
		
	}
	
	public IProject getProjectHandle() {
		return nameGroup.getProjectHandle();
	}
	
	/* ----------------- Location ----------------- */
	
	public static class LocationGroup extends AbstractComponentExt {
		
		protected final NameGroup nameGroup;
		
		protected final CheckBoxField selectionField = new CheckBoxField(
			WizardMessages.LangNewProject_Location_UseDefault_Label);
		protected final DirectoryTextField locationField = new DirectoryTextField(
			WizardMessages.LangNewProject_Location_Directory_label,
			WizardMessages.LangNewProject_Location_Directory_buttonLabel);
		
		public LocationGroup(NameGroup nameGroup) {
			this.nameGroup = nameGroup;
			
			selectionField.setFieldValue(true);
		}
		
		public CheckBoxField getSelectionField() {
			return selectionField;
		}
		
		public TextField getLocationField() {
			return locationField;
		}
		
		protected String getProjectName() {
			return nameGroup.getName();
		}
		
		protected boolean isDefaultLocation() {
			return selectionField.getFieldValue();
		}
		
		protected String getLocationString() {
			return locationField.getFieldValue();
		}
		
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
		
		public IStatus getValidationStatus() {
			
			IPath projectLocation = getProjectLocation();
			
			if(projectLocation == null) {
				return LangCore.createErrorStatus(
					WizardMessages.LangNewProject_Location_invalidLocation);
			}
			
			if(nameGroup.getProjectHandle().exists() && !isDefaultLocation()) {
				return LangCore.createErrorStatus(
					WizardMessages.LangNewProject_Location_projectExistsCannotChangeLocation);
			}
			
			IProject project = nameGroup.getProjectHandle();
			IStatus locationStatus = ResourceUtils.getWorkspace().validateProjectLocation(project, projectLocation);
			if(!locationStatus.isOK()) {
				return locationStatus;
			}
			
			return Status.OK_STATUS;
		}
		
		/* -----------------  ----------------- */
		
		@Override
		protected Composite doCreateTopLevelControl(Composite parent) {
			return SWTFactoryUtil.createGroup(parent, WizardMessages.LangNewProject_LocationGroup_label);
		}
		
		@Override
		protected GridLayoutFactory createTopLevelLayout() {
			return GridLayoutFactory.swtDefaults().numColumns(getPreferredLayoutColumns());
		}
		
		@Override
		public int getPreferredLayoutColumns() {
			return 3;
		}
		
		@Override
		protected void createContents(Composite topControl) {
			selectionField.createComponentInlined(topControl);
			locationField.createComponentInlined(topControl);
			
			selectionField.addValueChangedListener(new IFieldValueListener() {
				@Override
				public void fieldValueChanged() {
					updateComponentFromInput();
				}
			});
			nameGroup.getNameField().addValueChangedListener(new IFieldValueListener() {
				@Override
				public void fieldValueChanged() {
					updateComponentFromInput();
				}
			});
			
		}
		
		@Override
		public void updateComponentFromInput() {
			if(isDefaultLocation()) {
				locationField.setEnabled(false);
				locationField.setFieldValue(Platform.getLocation().append(getProjectName()).toOSString());
			} else {
				locationField.setEnabled(true);
			}
		}
		
	}
	
	public class ProjectValidationGroup extends AbstractComponentExt {
		
		protected Label icon;
		protected Link hintText;
		
		protected Composite topControl;
		
		@Override
		public int getPreferredLayoutColumns() {
			return 2;
		}
		
		@Override
		public void createComponentInlined(Composite parent) {
			this.topControl = parent;
		}
		
		@Override
		protected void createContents(Composite topControl) {
			this.topControl = topControl;
			
			icon = SWTFactoryUtil.createLabel(topControl, SWT.LEFT, "", GridDataFactory.swtDefaults().create());
			icon.setImage(Dialog.getImage(Dialog.DLG_IMG_MESSAGE_INFO));
			
			hintText = SWTFactoryUtil.createLink(topControl, SWT.LEFT, "", 
				GridDataFactory.fillDefaults().grab(true, false).create());
		}
		
		@Override
		public void updateComponentFromInput() {
			IProject projectHandle = getProjectHandle();
			
			IPath projectLoc = getProjectLocation();
			
			if(projectHandle != null && projectHandle.exists()) {
				setValidationMessage(WizardMessages.LangNewProject_DetectGroup_projectExists);
			} else if(projectLoc != null && projectLoc.toFile().exists()) {
				setValidationMessage(WizardMessages.LangNewProject_DetectGroup_message);
			} else {
				setValidationMessage(null);
			}
		}
		
		protected void setValidationMessage(String message) {
			icon.setVisible(message != null);
			hintText.setVisible(message != null);
			hintText.setText(StringUtil.nullAsEmpty(message));
			
			topControl.getParent().layout();
		}
	}
	
	public class PreferencesValidationGroup extends ProjectValidationGroup {
		
		@Override
		protected void createContents(Composite parent) {
			super.createContents(parent);
			
			icon.setImage(Dialog.getImage(Dialog.DLG_IMG_MESSAGE_WARNING));
			
			hintText.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					openPreferencePage();
				}
			});
		}
		
		protected void openPreferencePage() {
			PreferenceDialog pref = PreferencesUtil.createPreferenceDialogOn(getShell(), 
				LangUIPlugin_Actual.ROOT_PREF_PAGE_ID, null, null);
			
			if (pref != null) {
				pref.open();
				updateComponentFromInput();
			}
		}
		
		@Override
		public void updateComponentFromInput() {
			try {
				validatePreferences();
				setValidationMessage(null);
			} catch (ValidationException ve) {
				setPreferencesErrorMessage(ve);
			}
		}
		
		@SuppressWarnings("unused")
		protected void setPreferencesErrorMessage(ValidationException ve) {
			setValidationMessage("The "+ LangUIPlugin_Actual.LANGUAGE_NAME + 
				" preferences have not been configured correctly.\n"+
				"<a>Click here to configure preferences...</a>");
		}
		
	}
	
	protected abstract void validatePreferences() throws ValidationException;
	
	protected boolean validateDialog() {
		IStatus validationStatus = getValidationStatus();
		
		boolean valid;
		if(validationStatus.isOK()) {
			setErrorMessage(null);
			setMessage(null);
			setPageComplete(true);
			
			valid = true;
		} else {
			setErrorMessage(validationStatus.getMessage());
			setPageComplete(false);
			valid = false;
		}
		
		projectValidationGroup.updateComponentFromInput();
		prefValidationGroup.updateComponentFromInput();
		
		return valid;
	}
	
	protected IStatus getValidationStatus() {
		IStatus status = nameGroup.getValidationStatus();
		
		if(status.getSeverity() == IStatus.ERROR)
			return status;
		
		IStatus locationStatus = locationGroup.getValidationStatus();
		if(locationStatus.getSeverity() > status.getSeverity()) {
			status = locationStatus;
		}
		
		if(!status.isOK())
			return status;
		
		return Status.OK_STATUS;
	}
	
}