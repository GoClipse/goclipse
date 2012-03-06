/**
 * 
 */
package com.googlecode.goclipse.wizards;

import java.util.ArrayList;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

/**
 * 
 */
public class SourceFileComposite extends Composite {

	private Label   folderLabel      = null;
	private Text    sourceFolderText = null;
	private Button  browseButton     = null;
	private Label   sourceFileLabel  = null;
	private Text    sourceFileText   = null;
	private Button  mainCheckbox     = null;
	private boolean isCommand        = false;
	
	private ArrayList<DialogChangeListener> dialogChangeListeners = new ArrayList<DialogChangeListener>(); // @jve:decl-index=0:

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * Before this is run, be sure to set up the launch configuration
		 * (Arguments->VM Arguments) for the correct SWT library path in order
		 * to run with the SWT dlls. The dlls are located in the SWT plugin jar.
		 * For example, on Windows the Eclipse SWT 3.1 plugin jar is:
		 * installation_directory\plugins\org.eclipse.swt.win32_3.1.0.jar
		 */
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setSize(new Point(300, 200));
		new SourceFileComposite(shell, SWT.NONE);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	public SourceFileComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		setLayout(new GridLayout(2, false));
		
		folderLabel = new Label(this, SWT.NONE);
		folderLabel.setText("Source Folder:");
		sourceFolderText = new Text(this, SWT.BORDER);
		sourceFolderText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				fireDialogChange();
			}
		});
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		browseButton = new Button(this, SWT.NONE);
		browseButton.setText("Browse...");
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});
		new Label(this, SWT.NONE);
		sourceFileLabel = new Label(this, SWT.NONE);
		sourceFileLabel.setText("Source File:");
		new Label(this, SWT.NONE);
		sourceFileText = new Text(this, SWT.BORDER);
		sourceFileText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				fireDialogChange();
			}
		});
		setSize(new Point(545, 215));
		setSize(new Point(361, 108));
		new Label(this, SWT.NONE);
		
		
		mainCheckbox = new Button(this, SWT.CHECK);
		mainCheckbox.setText("Check this to make this a command file.");
		new Label(this, SWT.NONE);
		
		Group grpSourceType = new Group(this, SWT.NONE);
		grpSourceType.setText("Source Type");
		grpSourceType.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		Button btnPackageSourceFile = new Button(grpSourceType, SWT.RADIO);
		btnPackageSourceFile.setText("package source file");
		
		Button btnRadioButton = new Button(grpSourceType, SWT.RADIO);
		btnRadioButton.setText("Radio Button");
		new Label(this, SWT.NONE);
		mainCheckbox.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				isCommand = mainCheckbox.getSelection();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				isCommand = mainCheckbox.getSelection();
			}
		});
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */
	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				"Select new source file location");

		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				sourceFolderText.setText(((Path) result[0]).toString());
			}
		}
		fireDialogChange();
	}

	/**
	 * @return the folderLabel
	 */
	public Label getFolderLabel() {
		return folderLabel;
	}

	/**
	 * @param folderLabel
	 *            the folderLabel to set
	 */
	public void setFolderLabel(Label folderLabel) {
		this.folderLabel = folderLabel;
	}

	/**
	 * @return the sourceFolderText
	 */
	public Text getSourceFolderText() {
		return sourceFolderText;
	}

	/**
	 * @param sourceFolderText
	 *            the sourceFolderText to set
	 */
	public void setSourceFolderText(Text sourceFolderText) {
		this.sourceFolderText = sourceFolderText;
	}

	/**
	 * @return the browseButton
	 */
	public Button getBrowseButton() {
		return browseButton;
	}

	/**
	 * @param browseButton
	 *            the browseButton to set
	 */
	public void setBrowseButton(Button browseButton) {
		this.browseButton = browseButton;
	}

	/**
	 * @return the sourceFileLabel
	 */
	public Label getSourceFileLabel() {
		return sourceFileLabel;
	}

	/**
	 * @param sourceFileLabel
	 *            the sourceFileLabel to set
	 */
	public void setSourceFileLabel(Label sourceFileLabel) {
		this.sourceFileLabel = sourceFileLabel;
	}

	/**
	 * @return the sourceFileText
	 */
	public Text getSourceFileText() {
		return sourceFileText;
	}

	/**
	 * @param sourceFileText
	 *            the sourceFileText to set
	 */
	public void setSourceFileText(Text sourceFileText) {
		this.sourceFileText = sourceFileText;
	}

	/**
	 * 
	 * @param listener
	 */
	public void addDialogChangedListener(DialogChangeListener listener) {
		if (!dialogChangeListeners.contains(listener)) {
			dialogChangeListeners.add(listener);
		}
	}

	/**
	 * 
	 * @param listener
	 */
	public void removeDialogChangeListener(DialogChangeListener listener) {
		dialogChangeListeners.remove(listener);
	}
	
	/**
	 * 
	 */
	private void fireDialogChange(){
		for(DialogChangeListener listener:dialogChangeListeners){
			listener.dialogChanged();
		}
	}

	/**
	 * 
	 * @return true if user selected main
	 */
	public boolean isCmdFile() {
	    return isCommand;
    }
} // @jve:decl-index=0:visual-constraint="10,10"
