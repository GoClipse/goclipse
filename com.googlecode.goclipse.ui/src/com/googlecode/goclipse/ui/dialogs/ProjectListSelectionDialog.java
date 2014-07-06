/**
 * 
 */
package com.googlecode.goclipse.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.builder.GoNature;
import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.ui.GoPluginImages;

/**
 * @author kevin
 *
 */
public class ProjectListSelectionDialog extends Dialog {

	private static final String title = "Required Project Selection";
	private static final String topMsg = "Select project to add:";

	private CheckboxTableViewer tableViewer;
	private Label top;
	private Button selectAll, deselectAll;
	private final IProject currentProject;
	private IProject[] checkedProjects;

	/**
	 * @param parentShell
	 * @param currentProject
	 */
	public ProjectListSelectionDialog(Shell parentShell, IProject _currentProject) {
		super(parentShell);
		this.currentProject = _currentProject;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		this.getShell().setText(title);
		Composite composite = this.createDialogArea2(parent);

		createTopComp(composite);
		createTableComp(composite);
		createSelectionBtnsComp(composite);

		return composite;
	}

	private Composite createDialogArea2(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		FormLayout layout = new FormLayout();
		layout.spacing = 7;
		layout.marginHeight = layout.marginWidth = 10;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		applyDialogFont(composite);
		return composite;
	}

	private void createSelectionBtnsComp(Composite composite) {
		selectAll = new Button(composite, SWT.PUSH);
		selectAll.setText("Select All");
		FormData data = new FormData();
		data.top = new FormAttachment(tableViewer.getTable(), 0);
		data.left = new FormAttachment(0, 0);
		selectAll.setLayoutData(data);
		selectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				makeProjectSelected(true);
			}

		});

		deselectAll = new Button(composite, SWT.PUSH);
		deselectAll.setText("Deselect All");
		data = new FormData();
		data.top = new FormAttachment(tableViewer.getTable(), 0);
		data.left = new FormAttachment(selectAll, 0);
		deselectAll.setLayoutData(data);
		deselectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				makeProjectSelected(false);
			}

		});
	}

	private void createTableComp(Composite composite) {
		tableViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		FormData data = new FormData();
		data.top = new FormAttachment(top, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, -50);
		tableViewer.getTable().setLayoutData(data);
		tableViewer.setContentProvider(contentProvider);
		tableViewer.setLabelProvider(labelProvider);
		tableViewer.setInput("");
		this.madeDefaultCheckItems();
	}

	private void madeDefaultCheckItems() {
		try {
			// IProject[] projects = this.currentProject.getReferencedProjects();
			List<IProject> projects = Environment.INSTANCE.getProjectDependencies(this.currentProject);
			for (IProject project : projects) {
				tableViewer.setChecked(project, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void makeProjectSelected(boolean b) {
		tableViewer.setAllChecked(b);
	}

	private void createTopComp(Composite parent) {
		top = new Label(parent, SWT.NONE);
		FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		top.setLayoutData(data);
		top.setText(topMsg);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, 500);
	}

	@Override
	protected void okPressed() {
		Object[] projects = tableViewer.getCheckedElements();
		if (projects != null) {
			checkedProjects = new IProject[projects.length];
			for (int i = 0; i < projects.length; i++) {
				if (projects[i] instanceof IProject) {
					checkedProjects[i] = (IProject) projects[i];
				}
			}
		}
		super.okPressed();
	}

	@Override
	protected void cancelPressed() {
		super.cancelPressed();
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	IStructuredContentProvider contentProvider = new IStructuredContentProvider() {

		@Override
		public void dispose() {

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		@Override
		public Object[] getElements(Object inputElement) {
			return getAllGoProjects();
		}
	};

	LabelProvider labelProvider = new LabelProvider() {

		@Override
		public Image getImage(Object element) {
			return GoPluginImages.GO_ICON.getImage();
		}

		@Override
		public String getText(Object element) {
			if (element instanceof IProject) {
				IProject project = (IProject) element;
				StringBuilder builder = new StringBuilder();
				builder.append(project.getName());
				builder.append(" ( ");
				builder.append(project.getLocation());
				builder.append(" )");
				return builder.toString();
			}
			return super.getText(element);
		}

	};

	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		ProjectListSelectionDialog dialog = new ProjectListSelectionDialog(shell, null);
		dialog.open();
	}

	protected Object[] getAllGoProjects() {
		IProject[] projects = GoCore.getWorkspaceRoot().getProjects();
		return filterGoProject(projects);
	}

	/**
	 * remove non_goProject and the current project
	 * 
	 * @param projects
	 * @return
	 */
	private Object[] filterGoProject(IProject[] projects) {
		List<IProject> list = new ArrayList<IProject>();
		for (IProject project : projects) {
			try {
				boolean isSameNameProject = project.getName().equals(currentProject.getName());
				if (project.hasNature(GoNature.NATURE_ID) && !isSameNameProject) {
					list.add(project);
				}
			} catch (Exception e) {
			}
		}
		return list.toArray();
	}

	public IProject[] getCheckedProjects() {
		return checkedProjects;
	}

}
