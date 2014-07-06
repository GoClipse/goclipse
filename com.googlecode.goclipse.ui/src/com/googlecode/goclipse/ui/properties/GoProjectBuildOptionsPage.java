/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.properties;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import melnorme.lang.ide.ui.dialogs.AbstractProjectPropertyPage;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.builder.GoConstants;
import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.go.lang.GoPathElement;
import com.googlecode.goclipse.go.lang.GoPathType;
import com.googlecode.goclipse.preferences.PreferenceConstants;
import com.googlecode.goclipse.ui.GoUIPlugin;
import com.googlecode.goclipse.ui.dialogs.ProjectListSelectionDialog;
import com.googlecode.goclipse.ui.launch.BuildConfiguration;

public class GoProjectBuildOptionsPage extends AbstractProjectPropertyPage {

	ListViewer listViewer;
	Button buttonAdd;
	Button buttonExternalPath;
	Button buttonRemove;
	Button buttonModify;
	List goPathList;
	TabFolder tabFolder;
	Combo buildOptionCombo;
	Text buildOptionTxt;

	//
	private IProject currentProject;
	//
	// edit checked project
	private IProject[] selectedProjects;

	public GoProjectBuildOptionsPage() {
	}

	@Override
	protected Control createContents(Composite parent, IProject project) {
		this.currentProject = project;
		initializeDialogUnits(parent);
		Composite basicInfoComposite = this.createBasicComposite(parent);
		// this.createTopLable(basicInfoComposite, project);

		tabFolder = new TabFolder(basicInfoComposite, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		this.createProjectGoPathSettingTab(tabFolder, project);
		this.createProjectBuildOptionSettingTab(tabFolder, project);

		this.initialGoPathTabUIData();
		this.initialGoBuildTabUIData();

		return basicInfoComposite;
	}

	private void initialGoBuildTabUIData() {
		try {
			Properties props = Environment.INSTANCE.getProjectProperties(this.currentProject);
			String currentBuild = props.getProperty(GoConstants.CurrentBuildType);
			BuildConfiguration buildConf = BuildConfiguration.get(currentBuild);

			if (buildConf != null) {
				if (BuildConfiguration.RELEASE == buildConf) {
					String option = props.getProperty(BuildConfiguration.RELEASE.name());
					this.buildOptionTxt.setText(option);
					buildOptionCombo.select(0);
				} else if (BuildConfiguration.DEBUG == buildConf) {
					String option = props.getProperty(BuildConfiguration.DEBUG.name());
					this.buildOptionTxt.setText(option);
					buildOptionCombo.select(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initialGoPathTabUIData() {
	}

	/**
	 * ProjectGoPathSettingTab
	 * 
	 * @param tabFolder2
	 * @param project
	 */
	private void createProjectGoPathSettingTab(TabFolder tabFolder2, IProject project) {
		TabItem tabItem = new TabItem(tabFolder2, SWT.NULL);
		tabItem.setText("Project GoPath Setting");
		Composite buildPathComp = new Composite(tabFolder, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = 5;
		layout.marginHeight = 5;
		buildPathComp.setLayout(layout);
		this.createGoPathListComp(buildPathComp, project);
		tabItem.setControl(buildPathComp);
	}

	/**
	 * ProjectBuildOptionSettingTab
	 * 
	 * @param tabFolder2
	 * @param project
	 */
	private void createProjectBuildOptionSettingTab(TabFolder tabFolder2, IProject project) {
		TabItem tabItem = new TabItem(tabFolder2, SWT.NULL);
		tabItem.setText("Project Build Option Setting");
		Composite comp = new Composite(tabFolder, SWT.NONE);
		// GridLayout
		// GridLayout layout = new GridLayout();
		// layout.numColumns = 1;
		// layout.marginWidth = 5;
		// layout.marginHeight = 5;
		// comp.setLayout(layout);
		// FormLayout
		FormLayout layout = new FormLayout();
		layout.spacing = 7;
		layout.marginHeight = layout.marginWidth = 10;
		comp.setLayout(layout);
		this.createGoBuildOptionComp(comp, project);
		tabItem.setControl(comp);
	}

	private void createGoBuildOptionComp(Composite comp, IProject project) {
		Label suject = new Label(comp, SWT.None | SWT.RIGHT);
		suject.setText("Build Option Type: ");
		FormData fdata = new FormData();
		fdata.top = new FormAttachment(0, 3);
		fdata.left = new FormAttachment(0, 0);
		fdata.right = new FormAttachment(0, 200);
		suject.setLayoutData(fdata);

		//
		buildOptionCombo = new Combo(comp, SWT.READ_ONLY | SWT.SIMPLE | SWT.DROP_DOWN);
		fdata = new FormData();
		fdata.top = new FormAttachment(0, 0);
		fdata.left = new FormAttachment(suject, 0);
		fdata.right = new FormAttachment(100, 0);
		buildOptionCombo.setLayoutData(fdata);
		buildOptionCombo.add(BuildConfiguration.RELEASE.name());
		buildOptionCombo.add(BuildConfiguration.DEBUG.name());
		buildOptionCombo.select(0);
		buildOptionCombo.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				Properties props = Environment.INSTANCE.getProjectProperties(currentProject);
				int selectIndex = buildOptionCombo.getSelectionIndex();
				
				String value = null;
				if(selectIndex == 0){
					value = props.getProperty(BuildConfiguration.RELEASE.name());
				}else{
					value = props.getProperty(BuildConfiguration.DEBUG.name());
				}
				
				if(value != null){
					buildOptionTxt.setText(value);
				}else{
					buildOptionTxt.setText("");
				}
			}
		});

		Text buildInfoHelp = new Text(comp, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		fdata = new FormData();
		fdata.top = new FormAttachment(suject, 7);
		fdata.left = new FormAttachment(0, 0);
		fdata.right = new FormAttachment(100, 0);
		fdata.bottom = new FormAttachment(suject, 120);
		buildInfoHelp.setLayoutData(fdata);
		buildInfoHelp
				.setText("go build [-o output] [-i] [build flags] [packages]\nThe following arguments should only for [-i] [build flags] options");
		buildInfoHelp.setEditable(false);

		buildOptionTxt = new Text(comp, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		fdata = new FormData();
		fdata.top = new FormAttachment(buildInfoHelp, 10);
		fdata.left = new FormAttachment(0, 0);
		fdata.right = new FormAttachment(100, 0);
		fdata.bottom = new FormAttachment(100, 0);
		buildOptionTxt.setLayoutData(fdata);
		buildOptionTxt.setText("");
	}

	/**
	 * Go Path Setting Composite
	 * @param parent
	 * @param project
	 */
	private void createGoPathListComp(Composite parent, IProject project) {
		Composite listComp = new Composite(parent, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		listComp.setLayoutData(data);

		FormLayout layout = new FormLayout();
		listComp.setLayout(layout);

		listViewer = new ListViewer(listComp, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		FormData fdata = new FormData();
		fdata.top = new FormAttachment(0, 0);
		fdata.left = new FormAttachment(0, 0);
		fdata.right = new FormAttachment(100, -200);
		fdata.bottom = new FormAttachment(100, 0);
		listViewer.getList().setLayoutData(fdata);

		listViewer.setContentProvider(new GoPathListContentProvider());
		listViewer.setLabelProvider(new GoPathListLabelProvider());
		listViewer.setInput(getGoPathElements());

		// left button area
		Composite leftButtonArea = new Composite(listComp, SWT.NONE);
		fdata = new FormData();
		fdata.top = new FormAttachment(0, 10);
		fdata.left = new FormAttachment(listViewer.getList(), 10);
		fdata.right = new FormAttachment(100, -10);
		fdata.bottom = new FormAttachment(100, -10);
		leftButtonArea.setLayoutData(fdata);
		this.createLeftButtonComp(leftButtonArea, project);
	}

	private void createLeftButtonComp(Composite leftButtonArea, IProject project) {
		FormLayout layout = new FormLayout();
		layout.spacing = 7;
		leftButtonArea.setLayout(layout);

		buttonAdd = new Button(leftButtonArea, SWT.PUSH);
		buttonAdd.setText("Add Project GoPath");
		FormData fdata = new FormData();
		fdata.top = new FormAttachment(0, 0);
		fdata.left = new FormAttachment(0, 0);
		fdata.right = new FormAttachment(100, 0);
		buttonAdd.setLayoutData(fdata);
		buttonAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ProjectListSelectionDialog dialog = new ProjectListSelectionDialog(Display.getCurrent()
						.getActiveShell(), currentProject);
				dialog.open();
				if(dialog.getReturnCode() == Dialog.OK){
					selectedProjects = dialog.getCheckedProjects();
					listViewer.refresh();
				}
			}
		});

		buttonExternalPath = new Button(leftButtonArea, SWT.PUSH);
		buttonExternalPath.setText("Add External GoPath");
		fdata = new FormData();
		fdata.top = new FormAttachment(buttonAdd, 0);
		fdata.left = new FormAttachment(0, 0);
		fdata.right = new FormAttachment(100, 0);
		buttonExternalPath.setLayoutData(fdata);
		buttonExternalPath.setEnabled(false);

		buttonRemove = new Button(leftButtonArea, SWT.PUSH);
		buttonRemove.setText("Remove Project GoPath");
		fdata = new FormData();
		fdata.top = new FormAttachment(buttonExternalPath, 20);
		fdata.left = new FormAttachment(0, 0);
		fdata.right = new FormAttachment(100, 0);
		buttonRemove.setLayoutData(fdata);
		buttonRemove.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				removeSelectedProject();
			}

		});
	}

	protected void removeSelectedProject() {
		ISelection select = this.listViewer.getSelection();
		if (!select.isEmpty()) {
			if (select instanceof StructuredSelection) {
				Iterator sSelectIt = ((StructuredSelection) select).iterator();
				StringBuilder error = new StringBuilder();
				error.append("You can not remove following goPath which set up in Global Prefernece: \n");
				error.append("-------------------------------------------------\n");
				java.util.List<GoPathElement> removeList = new ArrayList<GoPathElement>();
				boolean hasError = false;
				while (sSelectIt.hasNext()) {
					GoPathElement ele = (GoPathElement) sSelectIt.next();
					if (ele.getPathType() != null && com.googlecode.goclipse.go.lang.GoPathType.Project == ele.getPathType()) {
						removeList.add(ele);
					} else {
						hasError = true;
						error.append(ele.getName()).append("\n");
					}
				}
				// warning message
				if (hasError) {
					MessageBox msgBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_ERROR | SWT.OK);
					msgBox.setText("Warning");
					msgBox.setMessage(error.toString());
					msgBox.open();
				}
				// delete
				try {
					java.util.List<IProject> exists = Environment.INSTANCE.getProjectDependencies(getProject());
					java.util.List<IProject> refList = new ArrayList<IProject>();
					for (IProject project : exists) {
						if (!isDelete(project, removeList)) {
							refList.add(project);
						}
					}
					Environment.INSTANCE.setProjectDependency(getProject(), refList.toArray(new IProject[refList.size()]));
					listViewer.refresh();
				} catch (Exception e) {
				}
			}
		}
	}

	private boolean isDelete(IProject project, java.util.List<GoPathElement> removeList) {
		for (GoPathElement gpe : removeList) {
			if (gpe.getName().equals(project.getLocation().toOSString())) {
				return true;
			}
		}
		return false;
	}

	private void createTopLable(Composite basicInfoComposite, IProject project) {
		Text pathText = new Text(basicInfoComposite, SWT.MULTI);
		String path = "Project Resource Path:\n" + project.getLocation().toOSString();
		pathText.setText(path);
		pathText.setEditable(false);

		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		pathText.setLayoutData(gd);

		Label line = new Label(basicInfoComposite, SWT.BORDER);
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.heightHint = 0;
		line.setLayoutData(gd);

		Label title = new Label(basicInfoComposite, SWT.NONE);
		title.setText("Project GoPath Setting:");
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.heightHint = 20;
		title.setLayoutData(gd);
	}

	/**
	 * Main composite
	 * 
	 * @param parent
	 * @return
	 */
	private Composite createBasicComposite(Composite parent) {
		Composite basicInfoComposite = new Composite(parent, SWT.NONE);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		basicInfoComposite.setLayoutData(data);

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = 5;
		layout.marginHeight = 5;
		basicInfoComposite.setLayout(layout);
		return basicInfoComposite;
	}

	
	
	@Override
	protected void performApply() {

		IRunnableWithProgress runnable = new IRunnableWithProgress() {

			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				// GoPath setting
				if (selectedProjects != null) {
					Environment.INSTANCE.setProjectDependency(currentProject, selectedProjects);
					selectedProjects = null;
				}
				// Build options
				Properties props = Environment.INSTANCE.getProjectProperties(currentProject);
				int selectIndex = buildOptionCombo.getSelectionIndex();

				if (selectIndex == 0) {
					props.setProperty(GoConstants.CurrentBuildType, BuildConfiguration.RELEASE.name());
					props.setProperty(BuildConfiguration.RELEASE.name(), buildOptionTxt.getText());
				} else {
					props.setProperty(GoConstants.CurrentBuildType, BuildConfiguration.DEBUG.name());
					props.setProperty(BuildConfiguration.DEBUG.name(), buildOptionTxt.getText());
				}
				
			}
		};
		
		GoUIPlugin.fireProjectChange(currentProject, runnable);

		super.performApply();
	}

	@Override
	protected void performDefaults() {
	}

	@Override
	public boolean performOk() {
		return true;
	}

	class GoPathListContentProvider implements IStructuredContentProvider {

		@Override
		public void dispose() {

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		@Override
		public Object[] getElements(Object inputElement) {
			return getGoPathElements();
		}

	}

	class GoPathListLabelProvider extends LabelProvider {

		@Override
		public String getText(Object element) {
			if (element instanceof GoPathElement) {
				GoPathElement gpe = (GoPathElement) element;
				StringBuilder builder = new StringBuilder();
				if (gpe.getPathType() != null) {
					builder.append(gpe.getPathType().name()).append(": ");
				}
				builder.append(gpe.getName());
				return builder.toString();
			}
			return super.getText(element);
		}

	}

	/**
	 * GoPath Elements
	 * @return
	 */
	public GoPathElement[] getGoPathElements() {
		java.util.List<GoPathElement> goPathList = new ArrayList<GoPathElement>();

		goPathList.add(new GoPathElement(GoPathType.Preference, GoConstants.GOROOT, getGoRootSrcFolder()));

		String[] goPath = Environment.INSTANCE.getOSEnvGoPath(this.getProject());
		this.appendGoPathElements(GoPathType.OS_Env, goPathList, goPath);

		goPath = Environment.INSTANCE.getPrefGoPath(this.getProject());
		this.appendGoPathElements(GoPathType.Preference, goPathList, goPath);

		try {
			if(this.selectedProjects!=null){
				for (IProject project : selectedProjects) {
					GoPathElement ele = new GoPathElement(GoPathType.Project, project.getLocation().toOSString(), Path
							.fromOSString(project.getLocation().toOSString()).toFile());
					goPathList.add(ele);
				}
			}else{
				java.util.List<IProject> projectlist = Environment.INSTANCE.getProjectDependencies(this.getProject());
				for (IProject project : projectlist) {
					GoPathElement ele = new GoPathElement(GoPathType.Project, project.getLocation().toOSString(), Path
							.fromOSString(project.getLocation().toOSString()).toFile());
					goPathList.add(ele);
				}
			}
		} catch (Exception e) {
		}

		return goPathList.toArray(new GoPathElement[goPathList.size()]);

	}

	private void appendGoPathElements(GoPathType type, java.util.List<GoPathElement> goPathList2, String[] goPath) {
		if (goPath != null && goPath.length > 0) {
			for (int i = 0; i < goPath.length; i++) {
				GoPathElement ele = new GoPathElement(type, goPath[i], Path.fromOSString(goPath[i]).toFile());
				goPathList2.add(ele);
			}
		}
	}

	protected File getGoRootSrcFolder() {
		String goRoot = GoCore.getPreferences().getString(PreferenceConstants.GOROOT);
		File srcFolder = Path.fromOSString(goRoot).toFile();
		return srcFolder;
	}
}