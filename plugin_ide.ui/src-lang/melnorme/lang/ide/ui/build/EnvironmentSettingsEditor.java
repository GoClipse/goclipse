package melnorme.lang.ide.ui.build;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import _org.eclipse.debug.internal.ui.MultipleInputDialog;
import _org.eclipse.debug.internal.ui.SWTFactory;
import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.tooling.ops.EnvironmentSettings;
import melnorme.util.swt.components.AbstractWidget;
import melnorme.util.swt.components.fields.FieldCompositeWidget;
import melnorme.util.swt.jface.AbstractContentProvider;

class EnvironmentSettingsEditor_Base extends FieldCompositeWidget<EnvironmentSettings> {

	protected Composite topControl;
	
	public EnvironmentSettingsEditor_Base(boolean createInlined) {
		super(createInlined);
	}
	
	@Override
	protected void createContents(Composite topControl) {
		this.topControl = topControl;
		super.createContents(topControl);
	}
	
	protected Shell getShell() {
		return topControl.getShell();
	}
	
	@Override
	protected void _verifyContract_IDisableableComponent() {
//		super._verifyContract_IDisableableComponent();
	}
	
	protected class EnvironmentVariableContentProvider extends AbstractContentProvider {
		@Override
		public Object[] getElements(Object inputElement) {
			return getFieldValue().envVars.map((entry) -> {
				return new EnvironmentVariable(entry.getKey(), entry.getValue());
			}).toArray();
		}
	}
	
}



/**
 * Launch configuration tab for configuring the environment passed
 * into Runtime.exec(...) when a config is launched.
 * <p>
 * Clients may call {@link #setHelpContextId(String)} on this tab prior to control
 * creation to alter the default context help associated with this tab. 
 * </p>
 * <p>
 * This class may be instantiated.
 * </p> 
 * @since 3.0
 * @noextend This class is not intended to be sub-classed by clients.
 */
public class EnvironmentSettingsEditor extends EnvironmentSettingsEditor_Base {

	protected static final String[] envTableColumnHeaders = {
		"Variable", 
		"Value", 
	};
	private static final String NAME_LABEL= "&Name:"; 
	private static final String VALUE_LABEL= "&Value:"; 
	protected static final String P_VARIABLE = "variable"; //$NON-NLS-1$
	protected static final String P_VALUE = "value"; //$NON-NLS-1$
	
	protected TableViewer environmentTable;
	protected Button envAddButton;
	protected Button envEditButton;
	protected Button envRemoveButton;
	protected Button appendEnvironment;
	protected Button replaceEnvironment;
	protected Button envSelectButton;
	
	/**
	 * Constructs a new tab with default context help.
	 */
	public EnvironmentSettingsEditor() {
		super(false);
	}
	
	{
		layoutColumns = 2;
	}
	
	@Override
	protected void doSetEnabled(boolean enabled) {
		super.doSetEnabled(enabled);
		
		if(environmentTable == null) {
			return;
		}
		setControlEnabled(environmentTable.getTable(), enabled);
		setControlEnabled(envAddButton, enabled);
		setControlEnabled(envEditButton, enabled);
		setControlEnabled(envRemoveButton, enabled);
		setControlEnabled(appendEnvironment, enabled);
		setControlEnabled(replaceEnvironment, enabled);
		setControlEnabled(envSelectButton, enabled);
	}
	
	@Override
	protected void createContents(Composite topControl) {
		super.createContents(topControl);
		
		createEnvironmentTable(topControl);
		createTableButtons(topControl);
		createAppendReplace(topControl);
		
		Dialog.applyDialogFont(topControl);
	}

	/**
	 * Creates and configures the widgets which allow the user to
	 * choose whether the specified environment should be appended
	 * to the native environment or if it should completely replace it.
	 * @param parent the composite in which the widgets should be created
	 */
	protected void createAppendReplace(Composite parent) {
		Composite comp = SWTFactory.createComposite(parent, 1, 2, GridData.FILL_HORIZONTAL);
		appendEnvironment= SWTFactory.createRadioButton(comp, LaunchConfigurationsMessages.EnvironmentTab_16); 
		appendEnvironment.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				field().get().appendEnv = appendEnvironment.getSelection();
				notifyFieldChanged();
			}
		});
		replaceEnvironment= SWTFactory.createRadioButton(comp, LaunchConfigurationsMessages.EnvironmentTab_17);
		
		field().addListener(true, (newValue) -> {
			appendEnvironment.setSelection(newValue.appendEnv);
	        replaceEnvironment.setSelection(!newValue.appendEnv);
		});
	}
	
	/**
	 * Creates and configures the table that displayed the key/value
	 * pairs that comprise the environment.
	 * @param parent the composite in which the table should be created
	 */
	protected void createEnvironmentTable(Composite parent) {
		
		Font font = parent.getFont();
		// Create label, add it to the parent to align the right side buttons with the top of the table
		SWTFactory.createLabel(parent, LaunchConfigurationsMessages.EnvironmentTab_Environment_variables_to_set__3, 2);
		// Create table composite
		Composite tableComposite = SWTFactory.createComposite(parent, font, 1, 1, GridData.FILL_BOTH, 0, 0);
		// Create table
		environmentTable = new TableViewer(tableComposite, 
			SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
		Table table = environmentTable.getTable();
		table.setLayout(new GridLayout());
		table.setLayoutData(AbstractWidget.gdGrabAll(400, 200));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setFont(font);
		environmentTable.setContentProvider(new EnvironmentVariableContentProvider());
		environmentTable.setLabelProvider(new EnvironmentVariableLabelProvider());
		environmentTable.setColumnProperties(new String[] {P_VARIABLE, P_VALUE});
		environmentTable.setComparator(new ViewerComparator() {
			@Override
			public int compare(Viewer iviewer, Object e1, Object e2) {
				if (e1 == null) {
					return -1;
				} else if (e2 == null) {
					return 1;
				} else {
					return ((EnvironmentVariable)e1).getName().compareToIgnoreCase(((EnvironmentVariable)e2).getName());
				}
			}
		});
		
		environmentTable.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				handleTableSelectionChanged(event);
			}
		});
		environmentTable.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				if (!environmentTable.getSelection().isEmpty()) {
					handleEnvEditButtonSelected();
				}
			}
		});
		// Create columns
		final TableColumn tc1 = new TableColumn(table, SWT.NONE, 0);
		tc1.setText(envTableColumnHeaders[0]);
		final TableColumn tc2 = new TableColumn(table, SWT.NONE, 1);
		tc2.setText(envTableColumnHeaders[1]);
		final Table tref = table;
		final Composite comp = tableComposite;
		tableComposite.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				Rectangle area = comp.getClientArea();
				Point size = tref.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				ScrollBar vBar = tref.getVerticalBar();
				int width = area.width - tref.computeTrim(0,0,0,0).width - 2;
				if (size.y > area.height + tref.getHeaderHeight()) {
					Point vBarSize = vBar.getSize();
					width -= vBarSize.x;
				}
				Point oldSize = tref.getSize();
				if (oldSize.x > area.width) {
					tc1.setWidth(width/2-1);
					tc2.setWidth(width - tc1.getWidth());
					tref.setSize(area.width, area.height);
				} else {
					tref.setSize(area.width, area.height);
					tc1.setWidth(width/2-1);
					tc2.setWidth(width - tc1.getWidth());
				}
			}
		});
		
		field().addListener(true, (newValue) -> {
			environmentTable.setInput(field().get());
			environmentTable.refresh();
		});
	}
	
	/**
	 * Label provider for the environment table
	 */
	public class EnvironmentVariableLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getColumnText(Object element, int columnIndex) 	{
			String result = null;
			if (element != null) {
				EnvironmentVariable var = (EnvironmentVariable) element;
				switch (columnIndex) {
					case 0: // variable
						result = var.getName();
						break;
					case 1: // value
						result = var.getValue();
						break;
					default:
						break;
				}
			}
			return result;
		}
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			if (columnIndex == 0) {
				return LangImages.IMG_ENV_VAR.getImage();
			}
			return null;
		}
	}

	/**
	 * Responds to a selection changed event in the environment table
	 * @param event the selection change event
	 */
	protected void handleTableSelectionChanged(SelectionChangedEvent event) {
		int size = ((IStructuredSelection)event.getSelection()).size();
		envEditButton.setEnabled(size == 1);
		envRemoveButton.setEnabled(size > 0);
	}
	
	/**
	 * Creates the add/edit/remove buttons for the environment table
	 * @param parent the composite in which the buttons should be created
	 */
	protected void createTableButtons(Composite parent) {
		// Create button composite
		Composite buttonComposite = SWTFactory.createComposite(parent, parent.getFont(), 1, 1, GridData.VERTICAL_ALIGN_BEGINNING | GridData.HORIZONTAL_ALIGN_END, 0, 0);

		// Create buttons
		envAddButton = SWTFactory.createPushButton(buttonComposite, LaunchConfigurationsMessages.EnvironmentTab_New_4, null); 
		envAddButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				handleEnvAddButtonSelected();
			}
		});
		envSelectButton = SWTFactory.createPushButton(buttonComposite, LaunchConfigurationsMessages.EnvironmentTab_18, null); 
		envSelectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				handleEnvSelectButtonSelected();
			}
		});
		envEditButton = SWTFactory.createPushButton(buttonComposite, LaunchConfigurationsMessages.EnvironmentTab_Edit_5, null); 
		envEditButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				handleEnvEditButtonSelected();
			}
		});
		envEditButton.setEnabled(false);
		envRemoveButton = SWTFactory.createPushButton(buttonComposite, LaunchConfigurationsMessages.EnvironmentTab_Remove_6, null); 
		envRemoveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				handleEnvRemoveButtonSelected();
			}
		});
		envRemoveButton.setEnabled(false);
	}
	
	/**
	 * Adds a new environment variable to the table.
	 */
	protected void handleEnvAddButtonSelected() {
		MultipleInputDialog dialog = new MultipleInputDialog(getShell(), LaunchConfigurationsMessages.EnvironmentTab_22); 
		dialog.addTextField(NAME_LABEL, null, false);
		dialog.addVariablesField(VALUE_LABEL, null, true);
		
		if (dialog.open() != Window.OK) {
			return;
		}
		
		String name = dialog.getStringValue(NAME_LABEL);
		String value = dialog.getStringValue(VALUE_LABEL);
		
		if (name != null && value != null && name.length() > 0 && value.length() >0) {
			addVariable(new EnvironmentVariable(name.trim(), value.trim()));
		}
	}
	
	/**
	 * Attempts to add the given variable. Returns whether the variable
	 * was added or not (as when the user answers not to overwrite an
	 * existing variable).
	 * @param variable the variable to add
	 * @return whether the variable was added
	 */
	protected boolean addVariable(EnvironmentVariable variable) {
		String name= variable.getName();
		
			if (getFieldValue().envVars.containsKey(name)) {
				boolean overWrite = MessageDialog.openQuestion(getShell(), LaunchConfigurationsMessages.EnvironmentTab_12, MessageFormat.format(LaunchConfigurationsMessages.EnvironmentTab_13, new Object[] { name })); //
				if (!overWrite) {
					return false;
				}
				getFieldValue().envVars.remove(name);
			}
			
		getFieldValue().envVars.put(name, variable.getValue());
		notifyFieldChanged();
		return true;
	}
	
	/**
	 * Displays a dialog that allows user to select native environment variables 
	 * to add to the table.
	 */
	private void handleEnvSelectButtonSelected() {
		//get Environment Variables from the OS
		Map<String, EnvironmentVariable> envVariables = getNativeEnvironment();
		
		//get Environment Variables from the table
		for (String varName : getFieldValue().envVars.keySet()) {
			envVariables.remove(varName);
		}
		
		NativeEnvironmentSelectionDialog dialog = new NativeEnvironmentSelectionDialog(getShell(), envVariables); 
		dialog.setTitle(LaunchConfigurationsMessages.EnvironmentTab_20); 
		
		int button = dialog.open();
		if (button == Window.OK) {
			Object[] selected = dialog.getResult();
			for (int i = 0; i < selected.length; i++) {
				EnvironmentVariable envVar = (EnvironmentVariable) selected[i];
				getFieldValue().envVars.put(envVar.getName(), envVar.getValue());
			}
		}
		
		notifyFieldChanged();
	}

	/**
	 * Gets native environment variable from the LaunchManager. Creates EnvironmentVariable objects.
	 * @return Map of name - EnvironmentVariable pairs based on native environment.
	 */
	protected Map<String, EnvironmentVariable> getNativeEnvironment() {
		Map<String, String> stringVars = DebugPlugin.getDefault().getLaunchManager().getNativeEnvironmentCasePreserved();
		HashMap<String, EnvironmentVariable> vars = new HashMap<String, EnvironmentVariable>();
		for (Entry<String, String> entry : stringVars.entrySet()) {
			vars.put(entry.getKey(), new EnvironmentVariable(entry.getKey(), entry.getValue()));
		}
		return vars;
	}

	/**
	 * Creates an editor for the value of the selected environment variable.
	 */
	private void handleEnvEditButtonSelected() {
		IStructuredSelection sel= (IStructuredSelection) environmentTable.getSelection();
		EnvironmentVariable originalVar= (EnvironmentVariable) sel.getFirstElement();
		if (originalVar == null) {
			return;
		}
		String originalName= originalVar.getName();
		String value= originalVar.getValue();
		MultipleInputDialog dialog= new MultipleInputDialog(getShell(), LaunchConfigurationsMessages.EnvironmentTab_11); 
		dialog.addTextField(NAME_LABEL, originalName, false);
		if(value != null && value.indexOf(System.getProperty("line.separator")) > -1) { //$NON-NLS-1$
			dialog.addMultilinedVariablesField(VALUE_LABEL, value, true);
		}
		else {
			dialog.addVariablesField(VALUE_LABEL, value, true);
		}
		
		if (dialog.open() != Window.OK) {
			return;
		}
		String name= dialog.getStringValue(NAME_LABEL);
		value= dialog.getStringValue(VALUE_LABEL);
		if (!originalName.equals(name)) {
			if (addVariable(new EnvironmentVariable(name, value))) {
				getFieldValue().envVars.remove(originalVar.getName());
				notifyFieldChanged();
			}
		} else {
			getFieldValue().envVars.put(name, value);
			notifyFieldChanged();
		}
	}

	/**
	 * Removes the selected environment variable from the table.
	 */
	private void handleEnvRemoveButtonSelected() {
		IStructuredSelection sel = (IStructuredSelection) environmentTable.getSelection();
		try {
//			environmentTable.getControl().setRedraw(false);
			for (Iterator<?> i = sel.iterator(); i.hasNext();) {
				EnvironmentVariable var = (EnvironmentVariable) i.next();
				getFieldValue().envVars.remove(var.getName());
//				environmentTable.remove(var);
			}
		} finally {
//			environmentTable.getControl().setRedraw(true);
		}
		notifyFieldChanged();
	}
	
	protected void notifyFieldChanged() {
		field().fireFieldValueChanged();
	}

}
