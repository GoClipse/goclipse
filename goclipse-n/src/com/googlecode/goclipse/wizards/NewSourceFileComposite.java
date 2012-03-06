package com.googlecode.goclipse.wizards;

import java.util.WeakHashMap;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Path;
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
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

public class NewSourceFileComposite extends Composite {

	private Text	sourceFolderName;
	private Text	sourceFilename;
	
	public enum SourceFileType {
		PACKAGE_FILE,
		MAIN_DEFAULT,
		MAIN_WITH_PARAMETERS,
		MAIN_WEBSERVER,
		TEST
	}
	
	private SourceFileType sourceFileType = SourceFileType.PACKAGE_FILE;
	
	private WeakHashMap<DialogChangeListener, DialogChangeListener>	dialogChangeListeners
		= new WeakHashMap<DialogChangeListener, DialogChangeListener>();

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public NewSourceFileComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(3, false));

		Label lblSourceFolder = new Label(this, SWT.NONE);
		lblSourceFolder.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
		        false, false, 1, 1));
		lblSourceFolder.setText("Source Folder:");

		sourceFolderName = new Text(this, SWT.BORDER);
		GridData gd_sourceFolderName = new GridData(SWT.LEFT, SWT.CENTER,
		        false, false, 1, 1);
		gd_sourceFolderName.widthHint = 263;
		sourceFolderName.setLayoutData(gd_sourceFolderName);
		sourceFolderName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				fireDialogChange();
			}
		});

		Button browseButton = new Button(this, SWT.NONE);
		browseButton.setText("Browse...");
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});

		Label lblSourceFilename = new Label(this, SWT.NONE);
		lblSourceFilename.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
		        false, false, 1, 1));
		lblSourceFilename.setText("Source FIle:");

		sourceFilename = new Text(this, SWT.BORDER);
		GridData gd_sourceFilename = new GridData(SWT.LEFT, SWT.CENTER, false,
		        false, 1, 1);
		gd_sourceFilename.widthHint = 263;
		sourceFilename.setLayoutData(gd_sourceFilename);
		sourceFilename.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				fireDialogChange();
			}
		});
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		Group grpSourceFileType = new Group(this, SWT.NONE);
		grpSourceFileType.setText("Source File Type");
		grpSourceFileType.setLayout(new GridLayout(1, false));
		GridData gd_grpSourceFileType = new GridData(SWT.LEFT, SWT.CENTER,
		        false, false, 1, 1);
		gd_grpSourceFileType.widthHint = 273;
		grpSourceFileType.setLayoutData(gd_grpSourceFileType);

		final Button btnPackageSourceFile = new Button(grpSourceFileType, SWT.RADIO);
		btnPackageSourceFile.setSelection(true);
		btnPackageSourceFile.setText("Package Source File");
		btnPackageSourceFile.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				eval();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				eval();
			}

			public void eval() {
				if (btnPackageSourceFile.getSelection()) {
					sourceFileType = SourceFileType.MAIN_DEFAULT;
				}
			}
		});

		final Button btnCommandSourceFile = new Button(grpSourceFileType, SWT.RADIO);
		GridData gd_btnCommandSourceFile = new GridData(SWT.LEFT, SWT.CENTER,
		        false, false, 1, 1);
		gd_btnCommandSourceFile.widthHint = 258;
		btnCommandSourceFile.setLayoutData(gd_btnCommandSourceFile);
		btnCommandSourceFile.setText("Command Source File");

		final CCombo combo = new CCombo(grpSourceFileType, SWT.BORDER);
		combo.setItems(new String[] { "Empty Main Function", "Main Function with Parameters", "Simple Web Server" });
		combo.select(0);
		combo.setEnabled(false);
		combo.setEditable(false);

		GridData gd_combo = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_combo.widthHint = 231;
		combo.setLayoutData(gd_combo);
		combo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) { eval();}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) { eval();}
			
			public void eval() {
				switch (combo.getSelectionIndex()){
					case 0:
						sourceFileType = SourceFileType.MAIN_DEFAULT;
						return;
					case 1:
						sourceFileType = SourceFileType.MAIN_WITH_PARAMETERS;
						return;
					case 2:
						sourceFileType = SourceFileType.MAIN_WEBSERVER;
						return;
					}
			}
		});

		btnCommandSourceFile.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				eval();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				eval();
			}

			public void eval() {
				if (btnCommandSourceFile.getSelection()) {
					combo.setEnabled(true);
					switch (combo.getSelectionIndex()){
						case 0:
							sourceFileType = SourceFileType.MAIN_DEFAULT;
							return;
						case 1:
							sourceFileType = SourceFileType.MAIN_WITH_PARAMETERS;
							return;
						case 2:
							sourceFileType = SourceFileType.MAIN_WEBSERVER;
							return;
					}
				} else {
					combo.setEnabled(false);
				}
			}
		});

		final Button btnTestSourceFile = new Button(grpSourceFileType, SWT.RADIO);
		btnTestSourceFile.setText("Test Source File");
		new Label(this, SWT.NONE);
		btnTestSourceFile.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				eval();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				eval();
			}

			public void eval() {
				String filename = sourceFilename.getText();
				if (btnTestSourceFile.getSelection()) {
					sourceFileType = SourceFileType.TEST;
					if (!filename.contains("_test.go")) {
						sourceFilename.setText(filename.replace(".go", "_test.go"));
					}
				} else {
					if (filename.contains("_test.go")) {
						sourceFilename.setText(filename.replace("_test.go", ".go"));
					}
				}
			}
		});
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
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
				sourceFolderName.setText(((Path) result[0]).toString());
			}
		}
		
		fireDialogChange();
	}

	/**
	 * 
	 * @param listener
	 */
	public void addDialogChangedListener(DialogChangeListener listener) {
		if (!dialogChangeListeners.keySet().contains(listener)) {
			dialogChangeListeners.put(listener, listener);
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
	private void fireDialogChange() {
		for (DialogChangeListener listener : dialogChangeListeners.keySet()) {
			listener.dialogChanged();
		}
	}

	/**
	 * 
	 * @return {@link SourceFileType}
	 */
	public SourceFileType getSourceFileType() {
		return sourceFileType;
	}

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
		shell.setSize(new Point(600, 400));
		new NewSourceFileComposite(shell, SWT.NONE);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	/**
	 * @return
	 */
	public Text getSourceFolderName() {
    	return sourceFolderName;
    }

	/**
	 * @param sourceFolderName
	 */
	public void setSourceFolderName(Text sourceFolderName) {
    	this.sourceFolderName = sourceFolderName;
    }

	/**
	 * @return
	 */
	public Text getSourceFilename() {
    	return sourceFilename;
    }

	/**
	 * @param sourceFilename
	 */
	public void setSourceFilename(Text sourceFilename) {
    	this.sourceFilename = sourceFilename;
    }
}
