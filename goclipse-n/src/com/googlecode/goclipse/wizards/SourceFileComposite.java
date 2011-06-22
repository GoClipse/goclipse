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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

/**
 * @author steel
 * 
 */
public class SourceFileComposite extends Composite {

	private Label folderLabel = null;
	private Text sourceFolderText = null;
	private Button browseButton = null;
	private Label sourceFileLabel = null;
	private Text sourceFileText = null;
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
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public SourceFileComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridData gridData3 = new GridData();
		gridData3.heightHint = -1;
		gridData3.widthHint = 75;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.END;
		gridData2.verticalAlignment = GridData.CENTER;
		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = GridData.CENTER;
		gridData1.horizontalAlignment = GridData.FILL;
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.CENTER;
		gridData.horizontalAlignment = GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.makeColumnsEqualWidth = false;
		folderLabel = new Label(this, SWT.NONE);
		folderLabel.setText("Source Folder:");
		sourceFolderText = new Text(this, SWT.BORDER);
		sourceFolderText.setLayoutData(gridData);
		sourceFolderText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				fireDialogChange();
			}
		});
		
		browseButton = new Button(this, SWT.NONE);
		browseButton.setText("Browse...");
		browseButton.setLayoutData(gridData3);
		browseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});
		sourceFileLabel = new Label(this, SWT.NONE);
		sourceFileLabel.setText("Source File:");
		sourceFileLabel.setLayoutData(gridData2);
		sourceFileText = new Text(this, SWT.BORDER);
		sourceFileText.setLayoutData(gridData1);
		sourceFileText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				fireDialogChange();
			}
		});
		this.setLayout(gridLayout);
		setSize(new Point(361, 108));
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
	
	private void fireDialogChange(){
		for(DialogChangeListener listener:dialogChangeListeners){
			listener.dialogChanged();
		}
	}

} // @jve:decl-index=0:visual-constraint="10,10"
