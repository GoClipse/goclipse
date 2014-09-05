package com.googlecode.goclipse.ui.navigator;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionConstants;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;

/**
 * 
 * @author Mihailo Vasiljevic
 * 
 */
public class GoNavigatorActionProvider extends CommonActionProvider {

	private OpenExternalAction openAction;
	
	public GoNavigatorActionProvider() {

	}

	@Override
	public void init(ICommonActionExtensionSite site) {
		super.init(site);
		TreeViewer treeViewer = null;
		if (site.getStructuredViewer() instanceof TreeViewer) {
			treeViewer = (TreeViewer) site.getStructuredViewer();
		}
		openAction = new OpenExternalAction(treeViewer);
		site.getStructuredViewer().addSelectionChangedListener(openAction);
	}

	@Override
	public void dispose() {
		getActionSite().getStructuredViewer().removeSelectionChangedListener(openAction);
		super.dispose();
	}

	@Override
	public void fillActionBars(IActionBars actionBars) {
		super.fillActionBars(actionBars);
		actionBars.setGlobalActionHandler(ICommonActionConstants.OPEN, openAction);
	}
	
}
