/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Bruno Medeiros - LANG adaptation and modifications.
 *******************************************************************************/
package melnorme.util.swt.jface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class TreeViewerUtil {
	
	// Originally from org.eclipse.jdt.internal.ui.text.AbstractInformationControl.createDialogArea(Composite)
	
	public static void addTreeViewerMouseAutoScroller(final TreeViewerExt treeViewer) {
		final Tree tree = treeViewer.getTree();
		
		tree.addMouseMoveListener(new MouseMoveListener() {
			TreeItem fLastItem = null;
			
			@Override
			public void mouseMove(MouseEvent e) {
				if(tree.equals(e.getSource())) {
					Object o = tree.getItem(new Point(e.x, e.y));
					if(fLastItem == null ^ o == null) {
						tree.setCursor(o == null ? null : tree.getDisplay().getSystemCursor(SWT.CURSOR_HAND));
					}
					if(o instanceof TreeItem) {
						Rectangle clientArea = tree.getClientArea();
						if(!o.equals(fLastItem)) {
							fLastItem = (TreeItem) o;
							tree.setSelection(new TreeItem[] { fLastItem });
						} else if(e.y - clientArea.y < tree.getItemHeight() / 4) {
							// Scroll up
							Point p = tree.toDisplay(e.x, e.y);
							Item item = treeViewer.scrollUp(p.x, p.y);
							if(item instanceof TreeItem) {
								fLastItem = (TreeItem) item;
								tree.setSelection(new TreeItem[] { fLastItem });
							}
						} else if(clientArea.y + clientArea.height - e.y < tree.getItemHeight() / 4) {
							// Scroll down
							Point p = tree.toDisplay(e.x, e.y);
							Item item = treeViewer.scrollDown(p.x, p.y);
							if(item instanceof TreeItem) {
								fLastItem = (TreeItem) item;
								tree.setSelection(new TreeItem[] { fLastItem });
							}
						}
					} else if(o == null) {
						fLastItem = null;
					}
				}
			}
		});
	}
	
}