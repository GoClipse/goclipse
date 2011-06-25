package com.googlecode.goclipse.wizards;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ProjectComposite extends Composite {

   private Label     nameLabel                        = null;
   private Text      nameField                        = null;
   private Group     contentGroup                     = null;
   private Button    newProjectInWorkspaceRadioButton = null;
   private Button    newProjectFromSourceRadioButton  = null;
   private Label     directoryLabel                   = null;
   private Text      existingSourcePathText           = null;
   private Button    browseButton                     = null;

   /**
    * This method initializes contentGroup
    */
   private void createContentGroup() {
      GridData gridData6 = new GridData();
      gridData6.widthHint = 75;
      gridData6.horizontalSpan = 2;

      GridData gridData5 = new GridData();
      gridData5.grabExcessHorizontalSpace = true;
      gridData5.verticalAlignment = GridData.CENTER;
      gridData5.horizontalAlignment = GridData.FILL;

      GridData gridData4 = new GridData();
      gridData4.horizontalSpan = 4;

      GridData gridData3 = new GridData();
      gridData3.horizontalSpan = 4;

      GridData gridData2 = new GridData();
      gridData2.horizontalSpan = 3;
      gridData2.horizontalAlignment = GridData.FILL;
      gridData2.verticalAlignment = GridData.FILL;
      gridData2.grabExcessVerticalSpace = false;
      gridData2.grabExcessHorizontalSpace = true;

      GridLayout gridLayout1 = new GridLayout();
      gridLayout1.numColumns = 4;
      gridLayout1.horizontalSpacing = 5;
      gridLayout1.marginWidth = 10;
      gridLayout1.marginHeight = 10;
      gridLayout1.verticalSpacing = 7;

      contentGroup = new Group(this, SWT.SHADOW_ETCHED_IN);
      contentGroup.setText("Contents");
      contentGroup.setEnabled(false);
      contentGroup.setLayout(gridLayout1);
      contentGroup.setLayoutData(gridData2);

      newProjectInWorkspaceRadioButton = new Button(contentGroup, SWT.RADIO);
      newProjectInWorkspaceRadioButton.setText("Create new project in workspace");
      newProjectInWorkspaceRadioButton.setSelection(true);
      newProjectInWorkspaceRadioButton.setLayoutData(gridData4);
      newProjectFromSourceRadioButton = new Button(contentGroup, SWT.RADIO);
      newProjectFromSourceRadioButton.addSelectionListener(new SelectionListener() {

         @Override
         public void widgetSelected(SelectionEvent arg0) {
            toggleState();
         }

         private void toggleState() {
            if (newProjectFromSourceRadioButton.getSelection()) {
               directoryLabel.setEnabled(true);
               existingSourcePathText.setEnabled(true);
               browseButton.setEnabled(true);
            }
            else {
               directoryLabel.setEnabled(false);
               existingSourcePathText.setEnabled(false);
               browseButton.setEnabled(false);
            }
         }

         @Override
         public void widgetDefaultSelected(SelectionEvent arg0) {
            toggleState();
         }
      });

      newProjectFromSourceRadioButton.setText("Create project from existing source");
      newProjectFromSourceRadioButton.setLayoutData(gridData3);

      directoryLabel = new Label(contentGroup, SWT.NONE);
      directoryLabel.setText("Directory:");
      directoryLabel.setEnabled(false);

      IWorkspace workspace = ResourcesPlugin.getWorkspace();
      IWorkspaceRoot root = workspace.getRoot();

      // To get to the directory of your Workspace
      IPath path = root.getLocation();
      String stringPath = path.toString();

      existingSourcePathText = new Text(contentGroup, SWT.BORDER);
      existingSourcePathText.setText(stringPath);
      existingSourcePathText.setEnabled(false);
      existingSourcePathText.setLayoutData(gridData5);

      browseButton = new Button(contentGroup, SWT.NONE);
      browseButton.setText("Browse...");
      browseButton.setLayoutData(gridData6);
      browseButton.setEnabled(false);
      browseButton.addSelectionListener(new SelectionListener() {

         @Override
         public void widgetSelected(SelectionEvent e) {
            //selectedPath = new DirectoryDialog(shell).open();
         }

         @Override
         public void widgetDefaultSelected(SelectionEvent e) {
            // TODO Auto-generated method stub
         }
      });
   }

   /**
    * @param args
    */
   public static void main(String[] args) {
      // TODO Auto-generated method stub
      /*
       * Before this is run, be sure to set up the launch configuration
       * (Arguments->VM Arguments) for the correct SWT library path in order to
       * run with the SWT dlls. The dlls are located in the SWT plugin jar. For
       * example, on Windows the Eclipse SWT 3.1 plugin jar is:
       * installation_directory\plugins\org.eclipse.swt.win32_3.1.0.jar
       */
      Display display = Display.getDefault();
      Shell shell = new Shell(display);
      shell.setLayout(new FillLayout());
      shell.setSize(new Point(300, 200));
      new ProjectComposite(shell, SWT.NONE);
      shell.open();

      while (!shell.isDisposed()) {
         if (!display.readAndDispatch())
            display.sleep();
      }
      display.dispose();
   }

   public ProjectComposite(Composite parent, int style) {
      super(parent, style);

      initialize();
   }

   private void initialize() {
      GridData gridData1 = new GridData();
      gridData1.heightHint = -1;

      GridData gridData = new GridData();
      gridData.horizontalSpan = 2;
      gridData.horizontalAlignment = GridData.FILL;
      gridData.verticalAlignment = GridData.CENTER;
      gridData.grabExcessHorizontalSpace = true;

      GridLayout gridLayout = new GridLayout();
      gridLayout.numColumns = 3;
      gridLayout.horizontalSpacing = 5;
      gridLayout.marginWidth = 10;
      gridLayout.marginHeight = 10;
      gridLayout.verticalSpacing = 5;

      nameLabel = new Label(this, SWT.NONE);
      nameLabel.setText("Project name:");
      nameLabel.setLayoutData(gridData1);

      nameField = new Text(this, SWT.BORDER);
      nameField.setLayoutData(gridData);

      this.setLayout(gridLayout);
      createContentGroup();
      setSize(new Point(449, 311));
   }

   public Text getNameField() {
      return nameField;
   }

} // @jve:decl-index=0:visual-constraint="10,10"
