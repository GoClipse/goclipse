package com.googlecode.goclipse.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.ui.EclipseEnviromentUtils;

/**
 * @author steel
 */
public class ProjectBuildConfigurationComposite extends Composite {

   private TabFolder 				 tabFolder                 = null;
   private Text 					 txtAutomaticUnitTesting   = null;
   private Text 					 textUnitTestRegex         = null;
   private Spinner                   maxTimeSpinner            = null;

   /**
    * @param parent
    * @param projectBuildConfiguration
    * @param style
    */
   public ProjectBuildConfigurationComposite(Composite parent, int style) {
      super(parent, style);
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
