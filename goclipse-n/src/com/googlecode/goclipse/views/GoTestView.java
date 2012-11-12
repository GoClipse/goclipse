package com.googlecode.goclipse.views;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.go.lang.lexer.Lexer;
import com.googlecode.goclipse.go.lang.lexer.Tokenizer;
import com.googlecode.goclipse.go.lang.model.Function;
import com.googlecode.goclipse.go.lang.parser.FunctionParser;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class GoTestView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String	ID	= "com.googlecode.goclipse.views.GoTestView";

	private TreeViewer	       viewer;
	private DrillDownAdapter   drillDownAdapter;
	private Action	           action1;
	private Action	           action2;
	private Action	           doubleClickAction;
	private Display	           display;
	private static GoTestView  instance;

	class TestFunction implements IAdaptable {
		protected String	name;
		private   Function	function;
		protected TestFile	parent;
		protected boolean   passed;

		public boolean passed() {
        	return passed;
        }

		public void setPassed(boolean passed) {
        	this.passed = passed;
        }

		public TestFile getParent() {
			return parent;
		}

		public void setParent(TestFile parent) {
			this.parent = parent;
		}

		public TestFunction(Function function) {
			this.name = function.getName();
		}

		public TestFunction() {
		}

		public String getName() {
			return name;
		}
		
		

		@SuppressWarnings("rawtypes")
    @Override
		public Object getAdapter(Class adapter) {
			return null;
		}
	}

	/**
	 * 
	 */
	class TestFile implements IAdaptable {

		private String		            name;
		private TreeParent		        parent;
		private File		            file;
		private IProject		        project;
		private ArrayList<TestFunction>	children;
		private boolean   				passed;

		public TestFile(File file, IProject project) {
			this.file     = file;
			this.project  = project;
			this.name     = file.getName().replace("_test.go", "");
			this.children = new ArrayList<TestFunction>();
		}

		public boolean passed() {
        	return passed;
        }

		public void setPassed(boolean passed) {
        	this.passed = passed;
        }

		public void addChild(TestFunction child) {
			children.add(child);
			child.setParent(this);
		}

		public void removeChild(TestFile child) {
			children.remove(child);
			child.setParent(null);
		}

		public TestFile[] getChildren() {
			return children.toArray(new TestFile[children.size()]);
		}

		public boolean hasChildren() {
			return children.size() > 0;
		}

		public void init() throws IOException {
			
			try {
				Lexer          lexer          = new Lexer();
				Tokenizer      tokenizer      = new Tokenizer(lexer);
				FunctionParser functionParser = new FunctionParser(true, tokenizer, file);

				BufferedReader reader = new BufferedReader(new FileReader(file));
				String temp = "";
				StringBuilder builder = new StringBuilder();
				while ((temp = reader.readLine()) != null) {
					builder.append(temp);
					builder.append("\n");
				}
				reader.close();
				lexer.scan(builder.toString());

				List<Function> functions = functionParser.getFunctions();
				for (Function function : functions) {
					System.out.println(function.getName());

					TestFunction tf = new TestFunction(function);
					children.add(tf);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public String getName() {
			return name;
		}

		public void setParent(TreeParent parent) {
			this.parent = parent;
		}

		public TreeParent getParent() {
			return parent;
		}

		@Override
		public String toString() {
			return getName();
		}

		@SuppressWarnings("rawtypes")
    @Override
		public Object getAdapter(Class key) {
			return null;
		}
	}

	class TreeParent implements IAdaptable {
		private ArrayList<TestFile>	children;

		public TreeParent(File file, IProject project) {
			children = new ArrayList<TestFile>();
		}

		public void addChild(TestFile child) {
			children.add(child);
			child.setParent(this);
		}

		public void removeChild(TestFile child) {
			children.remove(child);
			child.setParent(null);
		}

		public TestFile[] getChildren() {
			return children.toArray(new TestFile[children.size()]);
		}

		public boolean hasChildren() {
			return children.size() > 0;
		}

		@SuppressWarnings("rawtypes")
    @Override
		public Object getAdapter(Class adapter) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	class ViewContentProvider implements IStructuredContentProvider,
	        ITreeContentProvider {
		private TreeParent	invisibleRoot;

		@Override
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {

				if (invisibleRoot == null) {
					initialize();
				}

				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}

		@Override
		public Object getParent(Object child) {
			if (child instanceof TestFile) {
				return ((TestFile) child).getParent();
			} else if (child instanceof TestFunction) {
				return ((TestFunction) child).getParent();
			}
			return null;
		}

		@Override
		public Object[] getChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent) parent).getChildren();
			} else if (parent instanceof TestFile) {
				System.out.println("SSS");
				return ((TestFile) parent).getChildren();
			}
			return new Object[0];
		}

		@Override
		public boolean hasChildren(Object parent) {
			if (parent instanceof TreeParent)
				return ((TreeParent) parent).hasChildren();
			return false;
		}

		private void initialize() {
			buildModel();
		}

		/**
		 * 
		 */
		private void buildModel() {
			// set the root
			IProject project = Environment.INSTANCE.getCurrentProject();
			invisibleRoot = new TreeParent(project.getLocation().toFile(),
			        project);

			List<IFolder> folders = Environment.INSTANCE
			        .getSourceFolders(project);

			for (IFolder folder : folders) {
				File file = folder.getLocation().toFile();
				recurse(file, invisibleRoot, project);
			}
		}

		private void recurse(File file, TreeParent root, IProject project) {
			for (File f : file.listFiles()) {
				if (f.isDirectory()) {
					recurse(f, root, project);
				} else if (f.exists() && f.getName().endsWith("_test.go")) {
					TestFile to = new TestFile(f, project);
					try {
						to.init();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					root.addChild(to);
				}
			}
		}
	}

	class ViewLabelProvider extends StyledCellLabelProvider {

		private int		     IMAGE_SIZE	= 16;

		private final Image		WARNING	= new Image(
		                                        instance.getViewSite()
		                                                .getShell()
		                                                .getDisplay(),
		                                        instance.getViewSite()
		                                                .getShell()
		                                                .getDisplay()
		                                                .getSystemImage(
		                                                        SWT.ICON_WARNING)
		                                                .getImageData()
		                                                .scaledTo(IMAGE_SIZE,
		                                                        IMAGE_SIZE));

		private final Image		ERROR	= new Image(instance.getViewSite()
		                                        .getShell().getDisplay(),
		                                        instance.getViewSite()
		                                                .getShell()
		                                                .getDisplay()
		                                                .getSystemImage(
		                                                        SWT.ICON_ERROR)
		                                                .getImageData()
		                                                .scaledTo(IMAGE_SIZE,
		                                                        IMAGE_SIZE));

		private final Styler	errorStyler;
		private final Styler	passStyler;

		public ViewLabelProvider(final Font boldFont) {
			errorStyler = new Styler() {
				@Override
                public void applyStyles(TextStyle textStyle) {
					//textStyle.font = boldFont;
					textStyle.foreground = new Color(instance.display, new RGB(200, 0, 0));
				}
			};
			
			passStyler = new Styler() {
				@Override
                public void applyStyles(TextStyle textStyle) {
					//textStyle.font = boldFont;
					textStyle.foreground = new Color(instance.display, new RGB(0, 200, 0));
				}
			};
		}

		@Override
        public void update(ViewerCell cell) {
			Object element = cell.getElement();
			
			if (element instanceof TestFile) {
				TestFile file = (TestFile) element;

				Styler style = !file.passed() ? errorStyler : null;
				
				StyledString styledString = new StyledString(file.getName(), style);
				String decoration = MessageFormat
				        .format(" ({0} bytes)", new Object[] { new Long(file.getName().length()) }); //$NON-NLS-1$
				styledString.append(decoration, StyledString.COUNTER_STYLER);

				cell.setText(styledString.toString());
				cell.setStyleRanges(styledString.getStyleRanges());

				if (!file.passed()) {
					cell.setImage(ERROR);
				}
			} else {
				cell.setText("Unknown element"); //$NON-NLS-1$
			}

			super.update(cell);
		}

		@Override
        protected void measure(Event event, Object element) {
			super.measure(event, element);
		}
	}

	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public GoTestView() {
		instance = this;
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new ViewContentProvider());
		FontData[] boldFontData = getModifiedFontData(viewer.getTree()
		        .getFont().getFontData(), SWT.BOLD);
		Font boldFont = new Font(Display.getCurrent(), boldFontData);
		viewer.setLabelProvider(new ViewLabelProvider(boldFont));
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem()
		        .setHelp(viewer.getControl(), "goclipse.viewer");
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private static FontData[] getModifiedFontData(FontData[] originalData,
	        int additionalStyle) {
		FontData[] styleData = new FontData[originalData.length];
		for (int i = 0; i < styleData.length; i++) {
			FontData base = originalData[i];
			styleData[i] = new FontData(base.getName(), base.getHeight(),
			        base.getStyle() | additionalStyle);
		}
		return styleData;
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				GoTestView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		action1 = new Action() {
			@Override
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
		        .getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		action2 = new Action() {
			@Override
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
		        .getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			@Override
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection)
				        .getFirstElement();
				showMessage("Double-click detected on " + obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(),
		        "Go Tests", message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}