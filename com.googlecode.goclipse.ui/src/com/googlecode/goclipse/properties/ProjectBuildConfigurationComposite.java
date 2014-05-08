package com.googlecode.goclipse.properties;

import java.util.ArrayList;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.CheckedTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.ui.EclipseEnviromentUtils;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Spinner;

/**
 * @author steel
 */
public class ProjectBuildConfigurationComposite extends Composite {

   private static final class TreeContentProvider implements
			ITreeContentProvider {
		@Override
		 public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		 }

		@Override
		 public void dispose() {
		 }

		@Override
		 public Object[] getElements(Object inputElement) {
		    return getChildren(inputElement);
		 }

		@Override
		 public boolean hasChildren(Object element) {
		    if (getChildren(element).length > 0) {
		       return true;
		    }
		    return false;
		 }

		@Override
		 public Object getParent(Object element) {
		    if (element instanceof IResource) {
		       return ((IResource) element).getParent();
		    }
		    return null;
		 }

		@Override
		 public Object[] getChildren(Object parentElement) {
		    if (parentElement instanceof IFolder) {
		       ArrayList<IResource> list = new ArrayList<IResource>();
		       try {
		          for (IResource resource : ((IFolder) parentElement).members()) {
		             if (resource instanceof IFolder) {
		                list.add(resource);
		             }
		          }
		       }
		       catch (CoreException e) {
		    	   Activator.logInfo(e);
		       }
		       return list.toArray();
		    }
		    else if (parentElement instanceof IProject) {

		       ArrayList<IResource> list = new ArrayList<IResource>();
		       try {
		          for (IResource resource : ((IProject) parentElement).members()) {
		             if (resource instanceof IFolder) {
		                list.add(resource);
		             }
		          }
		       }
		       catch (CoreException e) {
		    	   Activator.logInfo(e);
		       }
		       return list.toArray();
		    }
		    return new Object[] {};
		 }
	}

   private Group                     group                     = null;
   private List                      list                      = null;
   private Button                    addButton                 = null;
   private Group                     group1                    = null;
   private Text                      pkgOutputText             = null;
   private Button                    pkgOutputBrowseButton     = null;
   private Text                      binOutputText             = null;
   private Button                    binOutputBrowseButton     = null;
   private Button                    removeButton              = null;
   private TabFolder 				 tabFolder                 = null;
   private Composite                 sourceComposite           = null;
   private Composite                 projectsComposite         = null;
   private Composite                 libraryComposite          = null;
   private Group                     projectGroup              = null;
   private Group                     librariesGroup            = null;
   private List                      projectsList              = null;
   private Button                    addProjectButton          = null;
   private Button                    removeProjectButton       = null;
   private List                      libraryList               = null;
   private Button                    addLibraryButton          = null;
   private Button                    removeLibraryButton       = null;
   private Text 					 txtAutomaticUnitTesting   = null;
   private Text 					 textUnitTestRegex         = null;
   private Spinner                   maxTimeSpinner            = null;
   private ProjectBuildConfiguration projectBuildConfiguration = null;
   private boolean                   outputFoldersOk           = true;

   /**
    * @param parent
    * @param projectBuildConfiguration
    * @param style
    */
   public ProjectBuildConfigurationComposite(Composite parent, ProjectBuildConfiguration projectBuildConfiguration, int style) {
      super(parent, style);
      this.projectBuildConfiguration = projectBuildConfiguration;
      initialize();
   }

   /**
	 * 
	 */
   private void initialize() {
      GridLayout gridLayout1 = new GridLayout();
      gridLayout1.numColumns = 1;
      gridLayout1.makeColumnsEqualWidth = false;
      gridLayout1.horizontalSpacing = 0;
      gridLayout1.marginWidth = 5;
      gridLayout1.marginHeight = 5;
      gridLayout1.verticalSpacing = 0;
      createTabFolder();
      this.setLayout(gridLayout1);
      setSize(new Point(464, 485));
      
      
      ////////////////////////////////////////////////
      //group.setEnabled(false);
      //list.setEnabled(false);
      //removeButton.setEnabled(false);
      //addButton.setEnabled(false);
      removeProjectButton.setEnabled(false);
      projectsList.setEnabled(false);
      addProjectButton.setEnabled(false);
      addLibraryButton.setEnabled(false);
      removeLibraryButton.setEnabled(false);
      libraryList.setEnabled(false);
      ////////////////////////////////////////////////
   }

   /**
    * This method initializes group
    */
   private void createGroup() {
      GridData gridData12 = new GridData();
      gridData12.horizontalAlignment = GridData.FILL;
      gridData12.grabExcessHorizontalSpace = true;
      gridData12.grabExcessVerticalSpace = true;
      gridData12.verticalAlignment = GridData.FILL;
      GridData gridData21 = new GridData();
      gridData21.horizontalAlignment = GridData.BEGINNING;
      gridData21.verticalAlignment = GridData.BEGINNING;
      GridData gridData11 = new GridData();
      gridData11.grabExcessHorizontalSpace = false;
      gridData11.verticalAlignment = GridData.END;
      gridData11.horizontalAlignment = GridData.FILL;
      GridLayout gridLayout = new GridLayout();
      gridLayout.numColumns = 2;
      GridData gridData1 = new GridData();
      gridData1.grabExcessHorizontalSpace = true;
      gridData1.verticalAlignment = GridData.FILL;
      gridData1.grabExcessVerticalSpace = true;
      gridData1.verticalSpan = 2;
      gridData1.horizontalAlignment = GridData.FILL;
      group = new Group(sourceComposite, SWT.NONE);
      group.setLayout(gridLayout);
      group.setLayoutData(gridData12);
      group.setText("Source Folders on Build Path");
      list = new List(group, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);

      list.setItems(Environment.INSTANCE.getSourceFoldersAsStringArray(EclipseEnviromentUtils.getCurrentProject()));
      list.setLayoutData(gridData1);
      addButton = new Button(group, SWT.NONE);
      addButton.setText("Add Folder...");
      addButton.setLayoutData(gridData11);
      addButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
         @Override
        public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
            CheckedTreeSelectionDialog dialog = new CheckedTreeSelectionDialog(EclipseEnviromentUtils.getActiveShell(),
                  new WorkbenchLabelProvider(), new ITreeContentProvider() {

                     @Override
                     public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
                     }

                     @Override
                     public void dispose() {
                     }

                     @Override
                     public Object[] getElements(Object inputElement) {
                        return getChildren(inputElement);
                     }

                     @Override
                     public boolean hasChildren(Object element) {
                        if (getChildren(element).length > 0) {
                           return true;
                        }
                        return false;
                     }

                     @Override
                     public Object getParent(Object element) {
                        if (element instanceof IResource) {
                           return ((IResource) element).getParent();
                        }
                        return null;
                     }

                     @Override
                     public Object[] getChildren(Object parentElement) {
                        if (parentElement instanceof IFolder) {
                           ArrayList<IResource> list = new ArrayList<IResource>();
                           try {
                              for (IResource resource : ((IFolder) parentElement).members()) {
                                 if (resource instanceof IFolder) {
                                    list.add(resource);
                                 }
                              }
                           }
                           catch (CoreException e) {
                        	   Activator.logInfo(e);
                           }
                           return list.toArray();
                        }
                        else if (parentElement instanceof IProject) {

                           ArrayList<IResource> list = new ArrayList<IResource>();
                           try {
                              for (IResource resource : ((IProject) parentElement).members()) {
                                 if (resource instanceof IFolder) {
                                    list.add(resource);
                                 }
                              }
                           }
                           catch (CoreException e) {
                        	   Activator.logInfo(e);
                           }
                           return list.toArray();
                        }
                        return new Object[] {};
                     }
                  });

            dialog.setTitle("Source Folder Selection");
            dialog.setMessage("Select the source folders from the tree:");

            dialog.setInput(EclipseEnviromentUtils.getCurrentProject());
            dialog.open();
            Object[] results = dialog.getResult();
            
            if (results != null) {
	            ArrayList<String> sourceFolderNames = new ArrayList<String>();
	
	            for (Object obj : results) {
	               IFolder folder = ((IFolder) obj);
	               String str = folder.getProjectRelativePath().toString();
	               sourceFolderNames.add(str);
	            }
	
	            list.setItems(sourceFolderNames.toArray(new String[sourceFolderNames.size()]));
            }
         }
      });

      removeButton = new Button(group, SWT.NONE);
      removeButton.setText("Remove Folder...");
      removeButton.setLayoutData(gridData21);
      removeButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
         @Override
        public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
            for (int i : list.getSelectionIndices()) {
               list.remove(i);
            }
         }
      });

   }

   /**
    * This method initializes group1
    */
   private void createGroup1() {
      IProject project = projectBuildConfiguration.getProject();
     
      GridData gridData22 = new GridData();
      gridData22.horizontalAlignment = GridData.FILL;
      gridData22.grabExcessHorizontalSpace = true;
      gridData22.verticalAlignment = GridData.CENTER;
      GridData gridData3 = new GridData();
      gridData3.widthHint = 100;
      GridData gridData5 = new GridData();
      gridData5.horizontalAlignment = GridData.FILL;
      gridData5.grabExcessHorizontalSpace = true;
      gridData5.verticalAlignment = GridData.CENTER;
      GridLayout gridLayout2 = new GridLayout();
      gridLayout2.numColumns = 3;
      group1 = new Group(sourceComposite, SWT.NONE);
      group1.setText("Output Folders");
      group1.setLayoutData(gridData22);
      group1.setLayout(gridLayout2);
      
      Label lblPackageFolder = new Label(group1, SWT.NONE);
      lblPackageFolder.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
      lblPackageFolder.setText("Pkg Folder:");
      pkgOutputText = new Text(group1, SWT.BORDER);
      pkgOutputText.setText(Environment.INSTANCE.getPkgOutputFolder(project).toOSString());
      pkgOutputText.addModifyListener(new ModifyListener() {
         @Override
         public void modifyText(ModifyEvent e) {
            validate();
         }
      });
      pkgOutputText.setLayoutData(gridData5);
      pkgOutputBrowseButton = new Button(group1, SWT.NONE);
      pkgOutputBrowseButton.setText("Browse...");
      pkgOutputBrowseButton.setLayoutData(gridData3);
      pkgOutputBrowseButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
         @Override
        public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
            FolderSelectionDialog dialog = new FolderSelectionDialog(EclipseEnviromentUtils.getActiveShell(),
                  new WorkbenchLabelProvider(), new TreeContentProvider());

            dialog.setTitle("Output Folder Selection");
            dialog.setMessage("Select the output folders from the tree:");

            dialog.setInput(EclipseEnviromentUtils.getCurrentProject());
            dialog.open();
            Object[] results = dialog.getResult();

            ArrayList<String> outputFolderName = new ArrayList<String>();

            for (Object obj : results) {
               IFolder folder = ((IFolder) obj);
               String str = folder.getProjectRelativePath().toString();
               outputFolderName.add(str);
               pkgOutputText.setText(str);
            }
         }
      });
      
      Label lblBinFolder = new Label(group1, SWT.NONE);
      lblBinFolder.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
      lblBinFolder.setText("Bin Folder:");
      binOutputText = new Text(group1, SWT.BORDER);
      binOutputText.setText(Environment.INSTANCE.getBinOutputFolder(project).toOSString());
      binOutputText.addModifyListener(new ModifyListener() {
         @Override
         public void modifyText(ModifyEvent e) {
            validate();
         }
      });
      GridData gridData6 = new GridData();
      gridData6.horizontalAlignment = GridData.FILL;
      gridData6.grabExcessHorizontalSpace = true;
      gridData6.verticalAlignment = GridData.CENTER;
      binOutputText.setLayoutData(gridData6);
      binOutputBrowseButton = new Button(group1, SWT.NONE);
      binOutputBrowseButton.setText("Browse...");
      GridData gridData7 = new GridData();
      gridData7.widthHint = 100;
      binOutputBrowseButton.setLayoutData(gridData7);
      binOutputBrowseButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
         @Override
        public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
            FolderSelectionDialog dialog = new FolderSelectionDialog(EclipseEnviromentUtils.getActiveShell(),
                  new WorkbenchLabelProvider(), new TreeContentProvider());

            dialog.setTitle("Output Folder Selection");
            dialog.setMessage("Select the output folders from the tree:");

            dialog.setInput(EclipseEnviromentUtils.getCurrentProject());
            dialog.open();
            Object[] results = dialog.getResult();

            ArrayList<String> outputFolderName = new ArrayList<String>();

            for (Object obj : results) {
               IFolder folder = ((IFolder) obj);
               String str = folder.getProjectRelativePath().toString();
               outputFolderName.add(str);
               binOutputText.setText(str);
            }
         }
      });
   }

   /**
    * Check the data and set the dialog state correctly
    */
   private void validate() {
      IResource pkgResource = EclipseEnviromentUtils.getCurrentProject().findMember(pkgOutputText.getText());
      IResource binResource = EclipseEnviromentUtils.getCurrentProject().findMember(binOutputText.getText());
      if (pkgResource == null) {
    	  projectBuildConfiguration.setErrorMessage(pkgOutputText.getText() + " is not a valid path...");
    	  outputFoldersOk = false;
      } else if (binResource == null) {
    	  projectBuildConfiguration.setErrorMessage(binOutputText.getText() + " is not a valid path...");
    	  outputFoldersOk = false;
      }
      else {
         projectBuildConfiguration.setErrorMessage(null);
    	  outputFoldersOk = true;
      }

      if (outputFoldersOk) {
         projectBuildConfiguration.setValid(true);
      }
      else {
         projectBuildConfiguration.setValid(false);
      }
   }

   /**
    * This method initializes tabFolder
    */
   private void createTabFolder() {
      GridData gridData4 = new GridData();
      gridData4.horizontalAlignment = GridData.FILL;
      gridData4.verticalAlignment = GridData.FILL;
      gridData4.grabExcessVerticalSpace = true;
      gridData4.grabExcessHorizontalSpace = true;
      tabFolder = new TabFolder(this, SWT.NONE);
      tabFolder.setLayoutData(gridData4);
      createSourceComposite();
      createProjectsComposite();
      createLibraryComposite();
      TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
      tabItem.setText("Source");
      tabItem.setControl(sourceComposite);
      
      TabItem tbtmUnitTest = new TabItem(tabFolder, SWT.NONE);
      tbtmUnitTest.setText("Automatic Unit Testing");
      
      Composite unitTestComposite = new Composite(tabFolder, SWT.NONE);
      tbtmUnitTest.setControl(unitTestComposite);
      unitTestComposite.setLayout(new GridLayout(1, false));
      
      Group grpAutomaticUnitTesting = new Group(unitTestComposite, SWT.NONE);
      grpAutomaticUnitTesting.setLayout(new GridLayout(6, false));
      GridData gd_grpAutomaticUnitTesting = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
      gd_grpAutomaticUnitTesting.widthHint = 250;
      grpAutomaticUnitTesting.setLayoutData(gd_grpAutomaticUnitTesting);
      grpAutomaticUnitTesting.setText("Automatic Unit Testing");
      
      txtAutomaticUnitTesting = new Text(grpAutomaticUnitTesting, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
      txtAutomaticUnitTesting.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND));
      txtAutomaticUnitTesting.setEditable(false);
      txtAutomaticUnitTesting.setText("Automatic Unit Testing is an experimental feature and depending on its effectiveness, may be removed at some future date.  When enabled, package level unit tests matching the regular expression below will be scheduled to run when a source file is modified within the same package.   By default, only test prefixed with 'TestAuto' will be run.  You should change this regular expression to whatever is appropriate for your project.\n\nA package can only have one scheduled run in the queue at a time and each test has an allotted time slot.  The default time slot size is 5 seconds.  If a test runs over its allotted time, it will be killed and the next test in the queue will run.  \n\nCurrently, there is no notification that a test busted its time; it is assumed long running tests are either an errant test (has an infinite loop) or not suitable to be run with this feature.  Failed tests show up as errors within the project.  ");
      txtAutomaticUnitTesting.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 6, 1));
      
      final Button btnEnableAutomaticUnit = new Button(grpAutomaticUnitTesting, SWT.CHECK);
      btnEnableAutomaticUnit.setSelection(Environment.INSTANCE.getAutoUnitTest(EclipseEnviromentUtils.getCurrentProject()));
     
      
      btnEnableAutomaticUnit.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 4, 1));
      btnEnableAutomaticUnit.setText("Enable Automatic Unit Testing");
      new Label(grpAutomaticUnitTesting, SWT.NONE);
      new Label(grpAutomaticUnitTesting, SWT.NONE);
      new Label(grpAutomaticUnitTesting, SWT.NONE);
      
      final Label lblTestNameRegex = new Label(grpAutomaticUnitTesting, SWT.NONE);
      lblTestNameRegex.setEnabled(btnEnableAutomaticUnit.getSelection());
      lblTestNameRegex.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
      lblTestNameRegex.setText("Test Name Regex:");
      
      textUnitTestRegex = new Text(grpAutomaticUnitTesting, SWT.BORDER);
      textUnitTestRegex.setEnabled(btnEnableAutomaticUnit.getSelection());
      
      textUnitTestRegex.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
      String regex = Environment.INSTANCE.getAutoUnitTestRegex(EclipseEnviromentUtils.getCurrentProject());
      if (regex==null || "".equals(regex)) {
    	  regex = "TestAuto[A-Za-z0-9_]*";
      }
      textUnitTestRegex.setText(regex);
      new Label(grpAutomaticUnitTesting, SWT.NONE);
      
      final Label lblTestMaxTime = new Label(grpAutomaticUnitTesting, SWT.NONE);
      lblTestMaxTime.setText("Test Max Time:");
      lblTestMaxTime.setEnabled(btnEnableAutomaticUnit.getSelection());
      
      maxTimeSpinner = new Spinner(grpAutomaticUnitTesting, SWT.BORDER);
      maxTimeSpinner.setIncrement(10);
      maxTimeSpinner.setEnabled(btnEnableAutomaticUnit.getSelection());
      
      int time = Environment.INSTANCE.getAutoUnitTestMaxTime(EclipseEnviromentUtils.getCurrentProject());
      maxTimeSpinner.setMaximum(360000);
      maxTimeSpinner.setMinimum(100);
      maxTimeSpinner.setSelection(time);
      
      Label lblSeconds = new Label(grpAutomaticUnitTesting, SWT.NONE);
      lblSeconds.setText("Milliseconds");
      lblSeconds.setEnabled(false);
      new Label(grpAutomaticUnitTesting, SWT.NONE);
      new Label(grpAutomaticUnitTesting, SWT.NONE);
      TabItem tabItem1 = new TabItem(tabFolder, SWT.NONE);
      tabItem1.setText("Projects");
      tabItem1.setControl(projectsComposite);
      TabItem tabItem2 = new TabItem(tabFolder, SWT.NONE);
      tabItem2.setText("Libraries");
      tabItem2.setControl(libraryComposite);
      
      btnEnableAutomaticUnit.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		Environment.INSTANCE.setAutoUnitTest(
        				EclipseEnviromentUtils.getCurrentProject(),
        				btnEnableAutomaticUnit.getSelection());
        		lblTestNameRegex.setEnabled(btnEnableAutomaticUnit.getSelection());
        		textUnitTestRegex.setEnabled(btnEnableAutomaticUnit.getSelection());
        		lblTestMaxTime.setEnabled(btnEnableAutomaticUnit.getSelection());
        		maxTimeSpinner.setEnabled(btnEnableAutomaticUnit.getSelection());
        	}
      });
      
      projectsComposite.setEnabled(false);
      libraryComposite.setEnabled(false);
   }

   /**
    * This method initializes sourceComposite
    */
   private void createSourceComposite() {
      sourceComposite = new Composite(tabFolder, SWT.NONE);
      sourceComposite.setLayout(new GridLayout());
      createGroup();
      createGroup1();
   }

   /**
    * This method initializes projectsComposite
    */
   private void createProjectsComposite() {
      projectsComposite = new Composite(tabFolder, SWT.NONE);
      projectsComposite.setLayout(new GridLayout());
      createProjectGroup();
   }

   /**
    * This method initializes libraryComposite
    */
   private void createLibraryComposite() {
      libraryComposite = new Composite(tabFolder, SWT.NONE);
      libraryComposite.setLayout(new GridLayout());
      createLibrariesGroup();
   }

   /**
    * This method initializes projectGroup
    */
   private void createProjectGroup() {
      GridData gridData9 = new GridData();
      gridData9.horizontalAlignment = GridData.BEGINNING;
      gridData9.widthHint = 92;
      gridData9.verticalAlignment = GridData.BEGINNING;

      GridData gridData8 = new GridData();
      gridData8.grabExcessHorizontalSpace = false;
      gridData8.verticalAlignment = GridData.CENTER;
      gridData8.widthHint = 92;
      gridData8.horizontalAlignment = GridData.FILL;

      GridData gridData7 = new GridData();
      gridData7.verticalSpan = 2;
      gridData7.verticalAlignment = GridData.FILL;
      gridData7.grabExcessHorizontalSpace = true;
      gridData7.grabExcessVerticalSpace = true;
      gridData7.horizontalAlignment = GridData.FILL;

      GridLayout gridLayout3 = new GridLayout();
      gridLayout3.numColumns = 2;

      GridData gridData2 = new GridData();
      gridData2.horizontalAlignment = GridData.FILL;
      gridData2.grabExcessHorizontalSpace = true;
      gridData2.grabExcessVerticalSpace = true;
      gridData2.verticalAlignment = GridData.FILL;

      projectGroup = new Group(projectsComposite, SWT.NONE);
      projectGroup.setLayoutData(gridData2);
      projectGroup.setLayout(gridLayout3);
      projectGroup.setText("Projects on Build Path");

      projectsList = new List(projectGroup, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
      projectsList.setLayoutData(gridData7);

      addProjectButton = new Button(projectGroup, SWT.NONE);
      addProjectButton.setText("Add...");
      addProjectButton.setLayoutData(gridData8);

      removeProjectButton = new Button(projectGroup, SWT.NONE);
      removeProjectButton.setText("Remove...");
      removeProjectButton.setLayoutData(gridData9);
     
   }

   /**
    * This method initializes librariesGroup
    */
   private void createLibrariesGroup() {
      GridData gridData14 = new GridData();
      gridData14.widthHint = 92;
      GridData gridData13 = new GridData();
      gridData13.horizontalAlignment = GridData.BEGINNING;
      gridData13.widthHint = 92;
      gridData13.verticalAlignment = GridData.BEGINNING;
      GridData gridData10 = new GridData();
      gridData10.verticalSpan = 2;
      gridData10.grabExcessVerticalSpace = true;
      gridData10.horizontalAlignment = GridData.FILL;
      gridData10.verticalAlignment = GridData.FILL;
      gridData10.grabExcessHorizontalSpace = true;
      GridLayout gridLayout4 = new GridLayout();
      gridLayout4.numColumns = 2;
      GridData gridData6 = new GridData();
      gridData6.horizontalAlignment = GridData.FILL;
      gridData6.grabExcessHorizontalSpace = true;
      gridData6.grabExcessVerticalSpace = true;
      gridData6.verticalAlignment = GridData.FILL;
      librariesGroup = new Group(libraryComposite, SWT.NONE);
      librariesGroup.setLayoutData(gridData6);
      librariesGroup.setLayout(gridLayout4);
      librariesGroup.setText("Libraries on Build Path");
      libraryList = new List(librariesGroup, SWT.BORDER);
      libraryList.setLayoutData(gridData10);
      addLibraryButton = new Button(librariesGroup, SWT.NONE);
      addLibraryButton.setText("Add...");
      addLibraryButton.setLayoutData(gridData14);
      removeLibraryButton = new Button(librariesGroup, SWT.NONE);
      removeLibraryButton.setText("Remove...");
      removeLibraryButton.setLayoutData(gridData13);
   }

   /**
    * Get the source folders
    * 
    * @return
    */
   public String[] getSourceFolders() {
      return list.getItems();
   }

   /**
    * Set the source folders
    */
   public void setSourceFolders(String[] folders) {
      list.setItems(folders);
   }

   /**
    * Return the input folder given
    * 
    * @return
    */
   public String getPkgOutputFolder() {
      return pkgOutputText.getText();
   }
   /**
    * Return the input folder given
    * 
    * @return
    */
   public String getBinOutputFolder() {
      return binOutputText.getText();
   }
   
   /**
    * Set the regular expression for the unit test.
    */
   public void setUnitTestRegEx(String regex) {
	   textUnitTestRegex.setText(regex);
   }
   
   /**
    * @return the regular expression for the unit test.
    */
   public String getUnitTestRegEx() {
	   return textUnitTestRegex.getText();
   }
   
   /**
    * @return the maximum time allowed for auto testing on the project.
    */
   public int getUnitTestMaxTime() {
	   return maxTimeSpinner.getDigits();
   }
   
} // @jve:decl-index=0:visual-constraint="10,10"
