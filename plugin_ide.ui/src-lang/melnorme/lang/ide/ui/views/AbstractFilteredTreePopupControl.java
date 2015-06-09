/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.views;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.array;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.util.swt.jface.TreeViewerExt;
import melnorme.util.swt.jface.TreeViewerUtil;
import melnorme.utilbox.misc.ArrayUtil;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlExtension;
import org.eclipse.jface.text.IInformationControlExtension2;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public abstract class AbstractFilteredTreePopupControl extends PopupDialog implements
	IInformationControl, 
	IInformationControlExtension,
	IInformationControlExtension2, 
	DisposeListener 
{
	
	protected final int treeStyle;
	
	protected TreeElementsFilter treeElementsFilter;
	
	protected Text filterText;
	protected TreeViewerExt treeViewer;
	
	protected Composite fViewMenuButtonComposite;
	
	protected String filteringString = "";
	
	
	public AbstractFilteredTreePopupControl(Shell parent, int shellStyle, int treeStyle) {
		super(parent, shellStyle, true, true, false, true, true, null, null);
		this.treeStyle = treeStyle;
		
		// Title and info text must be set to some value to have the controls created, so set empty values.
		// These will be updated later.
		if(hasHeader()) {
			setTitleText("");
		}
		setInfoText("");
		
		create();
		
		setInfoText("Outline"); // TODO: Use info text for more element information, or key-toggle info
	}
	
	protected Text getFilterText() {
		return filterText;
	}
	
	protected TreeViewer getTreeViewer() {
		return treeViewer;
	}
	
	@Override
	public void dispose() {
		close();
	}
	
	protected boolean hasHeader() {
		return false; // Subclasses may reimplement
	}
	
	/* ----------------- Shell creation (JDT based) ----------------- */
	
	@Override
	public void setVisible(boolean visible) {
		if(visible) {
			open();
		} else {
			saveDialogBounds(getShell());
			getShell().setVisible(false);
		}
	}
	
	@Override
	public void setSizeConstraints(int maxWidth, int maxHeight) {
	}
	
	@Override
	public Point computeSizeHint() {
		// return the shell's size - note that it already has the persisted size if persisting is enabled.
		return getShell().getSize();
	}
	
	@Override
	public void setLocation(Point location) {
		/*
		 * If the location is persisted, it gets managed by PopupDialog - fine. Otherwise, the location is
		 * computed in Window#getInitialLocation, which will center it in the parent shell / main
		 * monitor, which is wrong for two reasons:
		 * - we want to center over the editor / subject control, not the parent shell
		 * - the center is computed via the initalSize, which may be also wrong since the size may
		 *   have been updated since via min/max sizing of AbstractInformationControlManager.
		 * In that case, override the location with the one computed by the manager. Note that
		 * the call to constrainShellSize in PopupDialog.open will still ensure that the shell is
		 * entirely visible.
		 */
		if (!getPersistLocation() || getDialogSettings() == null)
			getShell().setLocation(location);
	}
	
	@Override
	protected IDialogSettings getDialogSettings() {
		return LangUIPlugin.getDialogSettings(getDialogSettingsId());
	}
	
	protected abstract String getDialogSettingsId();
	
	@Override
	public boolean hasContents() {
		return treeViewer != null && treeViewer.getInput() != null;
	}
	
	@Override
	public void setSize(int width, int height) {
		getShell().setSize(width, height);
	}
	
	@Override
	public void addDisposeListener(DisposeListener listener) {
		getShell().addDisposeListener(listener);
	}
	
	@Override
	public void removeDisposeListener(DisposeListener listener) {
		getShell().removeDisposeListener(listener);
	}
	
	@Override
	public void setForegroundColor(Color foreground) {
		applyForegroundColor(foreground, getContents());
	}
	
	@Override
	public void setBackgroundColor(Color background) {
		applyBackgroundColor(background, getContents());
	}
	
	@Override
	public boolean isFocusControl() {
		return getShell().getDisplay().getActiveShell() == getShell();
	}
	
	@Override
	public void setFocus() {
		getShell().forceFocus();
		filterText.setFocus();
	}
	
	@Override
	public void addFocusListener(FocusListener listener) {
		getShell().addFocusListener(listener);
	}
	
	@Override
	public void removeFocusListener(FocusListener listener) {
		getShell().removeFocusListener(listener);
	}
	
	@Override
	public void widgetDisposed(DisposeEvent event) {
		treeViewer = null;
		filterText = null;
	}
	
	/* ----------------- Create controls ----------------- */
	
	@Override
	protected Control createTitleMenuArea(Composite parent) {
		fViewMenuButtonComposite= (Composite) super.createTitleMenuArea(parent);
		
		// If there is a header, then the filter text must be created underneath the title and menu area.
		if(hasHeader()) {
			filterText = createFilterText(parent);
		}
		
		return fViewMenuButtonComposite;
	}
	
	@Override
	protected Control createTitleControl(Composite parent) {
		if(hasHeader()) {
			return super.createTitleControl(parent);
		}
		filterText = createFilterText(parent);
		return filterText;
	}
	
	protected Text createFilterText(Composite parent) {
		filterText = new Text(parent, SWT.NONE);
		Dialog.applyDialogFont(filterText);
		
		filterText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		filterText.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == 0x0D || e.keyCode == SWT.KEYPAD_CR) {
					gotoSelectedElement();
				}
				if(e.keyCode == SWT.ARROW_DOWN) {
					treeViewer.getTree().setFocus();
				}
				if(e.keyCode == SWT.ARROW_UP) {
					treeViewer.getTree().setFocus();
				}
				if(e.character == SWT.ESC) {
					dispose();
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
		
		return filterText;
	}
	
	protected Tree createTree(Composite parent, int treeStyle) {
		final Tree tree = new Tree(parent, SWT.SINGLE | (treeStyle & ~SWT.MULTI));
		
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.heightHint = tree.getItemHeight() * 12;
		tree.setLayoutData(gd);
		
		tree.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.character == 0x1B) {
					dispose();
				}
			}
		});
		
		// Auto mouse click listeners
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				gotoSelectedElement();
			}
		});
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if(e.button != 1)
					return;
				
				if(tree == e.getSource() && tree.getSelectionCount() == 1) {
					TreeItem selection = tree.getSelection()[0];
					
					if(selection.equals(tree.getItem(new Point(e.x, e.y)))) {
						gotoSelectedElement();
					}
				}
			}
		});
		
		return tree;
	}
	
	protected void createTreeViewer(Composite parent, int treeStyle) {
		Tree tree = createTree(parent, treeStyle);
		
		treeViewer = new TreeViewerFilterExt(tree);
		TreeViewerUtil.addTreeViewerMouseAutoScroller(treeViewer);
		
		treeElementsFilter = createTreeFilter();
		treeViewer.addFilter(treeElementsFilter);
		
		treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		createTreeViewer(parent, treeStyle);
		
		filterText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String text = ((Text) e.widget).getText();
				setFilteringString(text);
			}
		});
		
		addDisposeListener(this);
		return treeViewer.getControl();
	}
	
	@Override
	protected void setTabOrder(Composite composite) {
		if (hasHeader()) {
			composite.setTabList(array(filterText, treeViewer.getTree()));
		} else {
			fViewMenuButtonComposite.setTabList(array(filterText));
			composite.setTabList(array(fViewMenuButtonComposite, treeViewer.getTree()));
		}
	}
	
	/* ----------------- input / selection ----------------- */
	
	@Override
	public void setInformation(String information) {
		// ignored, IInformationControlExtension2.setInput(Object) should be used instead.
	}
	
	@Override
	public void setInput(Object information) {
		doSetInput(information, information);
	}
	
	protected void doSetInput(Object newInput, Object selectedElement) {
		filterText.setText("");
		treeViewer.setInput(newInput);
		treeViewer.setSelectedElement(selectedElement);
	}
	
	protected Object getSelectedElement() {
		if(treeViewer == null)
			return null;
		
		return treeViewer.getSelectionFirstElement();
	}
	
	protected Object firstDirectlyFilteredInElement;
	
	protected void setFilteringString(String pattern) {
		this.filteringString = pattern;
		refreshViewerFiltering();
	}
	
	protected void refreshViewerFiltering() {
		treeViewer.getControl().setRedraw(false);
		treeViewer.refresh();
		treeViewer.setSelectedElement(firstDirectlyFilteredInElement);
		treeViewer.expandAll();
		treeViewer.getControl().setRedraw(true);
	}
	
	/* ----------------- Filter ----------------- */
	
	protected class TreeViewerFilterExt extends TreeViewerExt {
		public TreeViewerFilterExt(Tree tree) {
			super(tree);
		}
		
		@Override
		protected void internalInitializeTree(Control widget) {
			AbstractFilteredTreePopupControl.this.firstDirectlyFilteredInElement = null;
			super.internalInitializeTree(widget);
		}
		
		@Override
		protected void internalRefresh(Object element, boolean updateLabels) {
			if(element == getRoot()) {
				AbstractFilteredTreePopupControl.this.firstDirectlyFilteredInElement = null;
			}
			super.internalRefresh(element, updateLabels);
		}
	}
	
	protected TreeElementsFilter createTreeFilter() {
		return new TreeElementsFilter();
	}
	
	protected class TreeElementsFilter extends ViewerFilter {
		
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			assertTrue(viewer == treeViewer);
			return elementFilteredIn(element);
		}
		
		protected boolean elementFilteredIn(Object element) {
			if(elementDirectlyFilteredIn(element)) {
				if(firstDirectlyFilteredInElement == null) {
					firstDirectlyFilteredInElement = element;
				}
				return true;
			}
			
			return hasFilteredInChildren(element);
		}
		
		protected boolean hasFilteredInChildren(Object element) {
			Object[] children = ((ITreeContentProvider) treeViewer.getContentProvider()).getChildren(element);
			
			for(Object child : ArrayUtil.nullToEmpty(children)) {
				if(elementFilteredIn(child)) {
					return true;
				}
			}
			return false;
		}
		
	}
	
	protected boolean elementDirectlyFilteredIn(Object element) {
		if(filteringString.length() == 0)
			return true;
		
		String matchName = getText(element);
		assertNotNull(matchName);
		return matchName != null && matchNameDirectlyFilteredIn(matchName);
	}
	
	protected String getText(Object element) {
		IBaseLabelProvider provider = treeViewer.getLabelProvider();
		if(provider instanceof ILabelProvider) {
			ILabelProvider labelProvider = (ILabelProvider) provider;
			return labelProvider.getText(element);
		} else if(provider instanceof IStyledLabelProvider) {
			IStyledLabelProvider labelProvider = (IStyledLabelProvider) provider;
			return labelProvider.getStyledText(element).getString();
		} else if(provider instanceof DelegatingStyledCellLabelProvider) {
			DelegatingStyledCellLabelProvider styledCellLabelProvider = (DelegatingStyledCellLabelProvider) provider;
			IStyledLabelProvider labelProvider = styledCellLabelProvider.getStyledStringProvider();
			return labelProvider.getStyledText(element).getString();
		}
		return null;
	}
	
	protected abstract boolean matchNameDirectlyFilteredIn(String matchName);
	
	/* ----------------- Actions / operations ----------------- */
	
	@Override
	protected void fillDialogMenu(IMenuManager dialogMenu) {
		super.fillDialogMenu(dialogMenu);
		fillViewMenu(dialogMenu);
	}
	
	@SuppressWarnings("unused")
	protected void fillViewMenu(IMenuManager viewMenu) {
	}
	
	protected abstract void gotoSelectedElement();
	
}