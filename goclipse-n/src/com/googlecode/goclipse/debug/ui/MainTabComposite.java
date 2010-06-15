package com.googlecode.goclipse.debug.ui;

import java.util.ArrayList;
import java.util.Stack;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.builder.GoNature;
import com.googlecode.goclipse.ui.dialogs.ResourceListSelectionDialog;

/**
 * 
 * @author steel
 */
public class MainTabComposite extends Composite {
	private Group projectGroup;
	private Text projectField;
	private Button findProjectButton = null;
	private Group mainGroup = null;
	private Text mainSourceField = null;
	private Button mainSourceSearchButton = null;
	private Group buildConfigurationGroup = null;
	private Combo buildConfigurationCombo = null;
	private Group programArgumentsgroup = null;
	private Text programArgumentsTextArea = null;
	private MainLaunchConfigurationTab mainLaunchConfigurationTab = null;

	/**
	 * 
	 * @param parent
	 * @param style
	 */
	public MainTabComposite(Composite parent, MainLaunchConfigurationTab tab,
			int style) {
		super(parent, style);
		mainLaunchConfigurationTab = tab;
		initialize();
	}

	/**
	 * 
	 */
	private void initialize() {
		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = false;
		gridData1.horizontalAlignment = GridData.BEGINNING;
		gridData1.heightHint = -1;
		gridData1.widthHint = -1;
		gridData1.verticalAlignment = GridData.CENTER;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.heightHint = -1;
		gridData.horizontalIndent = 0;
		gridData.verticalAlignment = GridData.CENTER;
		gridData.widthHint = -1;
		gridData.grabExcessHorizontalSpace = true;
		setSize(new Point(378, 266));
		setLayout(new GridLayout());

		projectGroup = new Group(this, SWT.NONE);
		createMainGroup();
		createBuildConfigurationGroup();
		createProgramArgumentsgroup();
		projectField = new Text(projectGroup, SWT.BORDER);
		projectField.setLayoutData(gridData);
		projectField.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				updateState();
			}
		});

		findProjectButton = new Button(projectGroup, SWT.NONE);
		findProjectButton.setText("Search...");
		findProjectButton.setLayoutData(gridData1);
		findProjectButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					IProject[] projects = Environment.INSTANCE
							.getCurrentProject().getWorkspace().getRoot()
							.getProjects();
					ArrayList<IProject> goProjects = new ArrayList<IProject>();

					for (IProject project : projects) {
						if (project.isOpen()) {
							IProjectNature nature = project.getNature(GoNature.NATURE_ID);
							if (nature != null) {
								goProjects.add(project);
							}
						}
					}

					ResourceListSelectionDialog dialog = new ResourceListSelectionDialog(
							getShell(), goProjects
									.toArray(new IProject[goProjects.size()]));

					dialog.setStartingPattern("?");
					dialog.setTitle("Go Project Selection:");

					dialog.open();
					Object[] objs = dialog.getResult();
					if (objs.length > 0) {
						projectField.setText(((IProject) objs[0]).getName());
					}
				} catch (CoreException e1) {
					e1.printStackTrace();
				}
				mainLaunchConfigurationTab.validate();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		GridLayout projectGroupLayout = new GridLayout();
		projectGroupLayout.makeColumnsEqualWidth = false;
		projectGroupLayout.numColumns = 2;
		projectGroup.setLayout(projectGroupLayout);
		GridData projectGroupLData = new GridData();
		projectGroupLData.horizontalAlignment = GridData.FILL;
		projectGroupLData.grabExcessHorizontalSpace = true;
		projectGroup.setLayoutData(projectGroupLData);
		projectGroup.setText("Project:");

	}

	public Text getProjectField() {
		return projectField;
	}

	/**
	 * This method initializes mainGroup
	 * 
	 */
	private void createMainGroup() {
		GridData gridData3 = new GridData();
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.verticalAlignment = GridData.CENTER;
		gridData3.horizontalAlignment = GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		GridData gridData2 = new GridData();
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = GridData.FILL;
		gridData2.grabExcessVerticalSpace = false;
		gridData2.horizontalAlignment = GridData.FILL;
		mainGroup = new Group(this, SWT.NONE);
		mainGroup.setLayoutData(gridData2);
		mainGroup.setLayout(gridLayout);
		mainGroup.setText("Go Application Main Source File:");
		mainSourceField = new Text(mainGroup, SWT.BORDER);
		mainSourceField.setLayoutData(gridData3);
		mainSourceField.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				updateState();
			}
		});
		mainSourceSearchButton = new Button(mainGroup, SWT.NONE);
		mainSourceSearchButton.setText("Search...");
		mainSourceSearchButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					IProject project = Environment.INSTANCE.getCurrentProject()
							.getWorkspace().getRoot().getProject(getProject());
					String[] pathRoots = Environment.INSTANCE
							.getSourceFoldersAsStringArray(project);

					ArrayList<IResource> resources = new ArrayList<IResource>();

					// load stack
					Stack<IFolder> stack = new Stack<IFolder>();
					for (String path : pathRoots) {
						IResource res = project.findMember(path);
						if (res.getType() == IResource.FOLDER) {
							stack.push((IFolder) res);
						}
					}

					// walk resource tree
					while (!stack.isEmpty()) {
						IFolder folder = stack.pop();
						for (IResource resource : folder.members()) {
							if (resource.getType() == IResource.FILE
									&& resource.getName().endsWith(".go")) {
								resources.add(resource);
							} else if (resource.getType() == IResource.FOLDER) {
								stack.push((IFolder) resource);
							}
						}
					}

					ResourceListSelectionDialog dialog = new ResourceListSelectionDialog(
							getShell(), resources
									.toArray(new IResource[resources.size()]));

					dialog.setStartingPattern("?");
					dialog.setTitle("Go Main File Selection:");

					dialog.open();
					Object[] objs = dialog.getResult();
					if (objs.length > 0) {
						mainSourceField.setText(((IResource) objs[0])
								.getProjectRelativePath().toString());
					}
				} catch (CoreException e1) {
					e1.printStackTrace();
				}
				mainLaunchConfigurationTab.validate();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		setMainConfigEnabled(false);
	}

	/**
	 * This method initializes buildConfigurationGroup
	 * 
	 */
	private void createBuildConfigurationGroup() {
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = GridData.FILL;
		gridData4.verticalAlignment = GridData.CENTER;
		buildConfigurationGroup = new Group(this, SWT.NONE);
		buildConfigurationGroup.setLayout(new GridLayout());
		buildConfigurationGroup.setLayoutData(gridData4);
		createBuildConfigurationCombo();
		buildConfigurationGroup.setText("Build Configuration:");
	}

	/**
	 * This method initializes buildConfigurationCombo
	 * 
	 */
	private void createBuildConfigurationCombo() {
		GridData gridData5 = new GridData();
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.verticalAlignment = GridData.CENTER;
		gridData5.horizontalAlignment = GridData.FILL;
		buildConfigurationCombo = new Combo(buildConfigurationGroup,
				SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);

		buildConfigurationCombo.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				updateState();
			}
		});

		for (BuildConfiguration buildConfiguration : BuildConfiguration
				.values()) {
			buildConfigurationCombo.add(buildConfiguration.toString());
		}
		buildConfigurationCombo.select(0);
		buildConfigurationCombo.setLayoutData(gridData5);
	}

	/**
	 * This method initializes programArgumentsgroup
	 * 
	 */
	private void createProgramArgumentsgroup() {
		GridData gridData7 = new GridData();
		gridData7.grabExcessHorizontalSpace = true;
		gridData7.horizontalAlignment = GridData.FILL;
		gridData7.verticalAlignment = GridData.FILL;
		gridData7.grabExcessVerticalSpace = true;
		GridData gridData6 = new GridData();
		gridData6.verticalSpan = 2;
		gridData6.grabExcessVerticalSpace = true;
		gridData6.horizontalAlignment = GridData.FILL;
		gridData6.verticalAlignment = GridData.FILL;
		gridData6.grabExcessHorizontalSpace = true;
		programArgumentsgroup = new Group(this, SWT.NONE);
		programArgumentsgroup.setLayout(new GridLayout());
		programArgumentsgroup.setLayoutData(gridData6);
		programArgumentsgroup.setText("Program Arguments:");
		programArgumentsTextArea = new Text(programArgumentsgroup, SWT.MULTI
				| SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		programArgumentsTextArea.setLayoutData(gridData7);
		programArgumentsTextArea.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				updateState();
			}
		});
	}

	/**
	 * @param projectname
	 */
	public void setProject(String projectname) {
		projectField.setText(projectname);
	}

	/**
	 * @return
	 */
	public String getProject() {
		return projectField.getText();
	}

	/**
	 * @param file
	 */
	public void setMainFile(String file) {
		mainSourceField.setText(file);
	}

	/**
	 * @return
	 */
	public String getMainFile() {
		return mainSourceField.getText();
	}

	/**
	 * @param config
	 */
	public void setBuildConfig(BuildConfiguration config) {
		if (config == null) {
			buildConfigurationCombo.select(0);
			return;
		}

		switch (config) {
		case DEBUG:
			buildConfigurationCombo.select(config.ordinal());
		case RELEASE:
			buildConfigurationCombo.select(config.ordinal());
		}
	}

	/**
	 * 
	 * @return
	 */
	public BuildConfiguration getBuildConfiguration() {
		switch (buildConfigurationCombo.getSelectionIndex()) {
		case 0:
			return BuildConfiguration.DEBUG;
		case 1:
			return BuildConfiguration.RELEASE;
		}
		return null;
	}

	/**
	 * 
	 * @param args
	 */
	public void setProgramArgs(String args) {
		programArgumentsTextArea.setText(args);
	}

	/**
	 * 
	 * @return
	 */
	public String getProgramArgs() {
		String ret = programArgumentsTextArea.getText().replace("\n", " ");
		ret = ret.replace("\r", " ");
		return ret;
	}

	public void setMainConfigEnabled(boolean b) {
		mainGroup.setEnabled(b);
		mainSourceField.setEnabled(b);
		mainSourceField.setEditable(b);
		mainSourceSearchButton.setEnabled(b);
	}

	/**
    * 
    */
	private void updateState() {
		mainLaunchConfigurationTab.validate();
		mainLaunchConfigurationTab.launchConfigurationDialog.updateButtons();
		mainLaunchConfigurationTab.launchConfigurationDialog.updateMessage();
	}

} // @jve:decl-index=0:visual-constraint="10,10"
