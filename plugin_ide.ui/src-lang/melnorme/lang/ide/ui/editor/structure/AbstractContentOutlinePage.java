/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor.structure;


import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.navigator.ICommonMenuConstants;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.AbstractWidget;
import melnorme.util.swt.components.misc.StatusMessageWidget;
import melnorme.utilbox.fields.ListenerListHelper;

/**
 * Abstract helper class for an {@link IContentOutlinePage}, similar to {@link ContentOutlinePage},
 * but with a different implementation (with minor improvements.
 * 
 * @author Bruno
 *
 */
public abstract class AbstractContentOutlinePage extends Page implements IContentOutlinePage, IPostSelectionProvider {
	
	protected final ListenerListHelper<ISelectionChangedListener> selectionChangedListeners = 
			new ListenerListHelper<>();
	protected final ListenerListHelper<ISelectionChangedListener> postSelectionChangedListeners = 
			new ListenerListHelper<>();
	
	protected Composite topControl;
	protected TreeViewer treeViewer;
	protected StatusMessageWidget statusWidget;
	
	public AbstractContentOutlinePage() {
		super();
	}
	
	@Override
	public void init(IPageSite pageSite) {
		super.init(pageSite);
		getSite().setSelectionProvider(this);
	}
	
	@Override
	public void dispose() {
		selectionChangedListeners.clear();
		postSelectionChangedListeners.clear();
		
		super.dispose();
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public void createControl(Composite parent) {
		topControl = SWTFactoryUtil.createGridComposite(parent, 1, null);
		topControl.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).spacing(0, 0).create());
		
		statusWidget = new StatusMessageWidget() {
			@Override
			protected GridLayoutFactory createTopLevelLayout() {
				return GridLayoutFactory.fillDefaults().numColumns(getPreferredLayoutColumns()).margins(5, 5);
			}
		};
		statusWidget.createComponent(topControl, AbstractWidget.gdGrabHorizontal());
		
		createTreeViewer(topControl);
		treeViewer.getTree().setLayoutData(AbstractWidget.gdGrabAll());
    }
	
	@Override
	public Control getControl() {
		return topControl;
	}
	
	public TreeViewer getTreeViewer() {
		return treeViewer;
	}
	
	protected TreeViewer createTreeViewer(Composite parent) {
		treeViewer = new TreeViewer(parent, SWT.MULTI);
		
		treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
		// TODO: allow certain StructureElements to not be initially expanded
		treeViewer.setUseHashlookup(true);
		
		setupTreeViewerListeners();
		createTreeViewerMenu();
		
		return treeViewer;
	}
	
	/* -----------------  ----------------- */
	
	protected void createTreeViewerMenu() {
		Tree tree = treeViewer.getTree();
		
		String menuId = LangUIPlugin.PLUGIN_ID + ".OutlineContextMenu";
		
		MenuManager manager = new MenuManager("OutlineContextMenu", menuId);
		manager.setRemoveAllWhenShown(true);
		manager.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager m) {
				contextMenuAboutToShow(m);
			}
		});
		Menu treeContextMenu = manager.createContextMenu(tree);
		tree.setMenu(treeContextMenu);
		
		getSite().registerContextMenu(menuId, manager, treeViewer);
	}
	
	protected void contextMenuAboutToShow(IMenuManager menu) {
		if(menu.isEmpty()) {
			menu.add(new Separator(ICommonMenuConstants.GROUP_ADDITIONS));
		}
	}

	/* -----------------  ----------------- */
	
	@Override
	public void setFocus() {
		if (treeViewer != null) {
			treeViewer.getControl().setFocus();
		}
	}
	
	/* ----------------- ISelectionProvider ----------------- */ 
	
	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangedListeners.addListener(listener);
	}
	@Override
	public void addPostSelectionChangedListener(ISelectionChangedListener listener) {
		postSelectionChangedListeners.addListener(listener);
	}
	
	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangedListeners.removeListener(listener);
	}
	@Override
	public void removePostSelectionChangedListener(ISelectionChangedListener listener) {
		postSelectionChangedListeners.removeListener(listener);
	}
	
	protected void setupTreeViewerListeners() {
		getTreeViewer().addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				for(ISelectionChangedListener listener : selectionChangedListeners.getListeners()) {
					listener.selectionChanged(event);
				}
				treeViewerSelectionChanged(event);
			}
		});
		
		getTreeViewer().addPostSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				for(ISelectionChangedListener listener : postSelectionChangedListeners.getListeners()) {
					listener.selectionChanged(event);
				}
				treeViewerPostSelectionChanged(event);
			}
		});
	}
	
	@Override
	public ISelection getSelection() {
		if(treeViewer == null) {
			return StructuredSelection.EMPTY;
		}
		return treeViewer.getSelection();
	}
	
	@Override
	public void setSelection(ISelection selection) {
		if(treeViewer != null) {
			treeViewer.setSelection(selection);
		}
	}
	
	@SuppressWarnings("unused")
	protected void treeViewerSelectionChanged(SelectionChangedEvent event) {
	}
	
	@SuppressWarnings("unused")
	protected void treeViewerPostSelectionChanged(SelectionChangedEvent event) {
	}
	
}