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
package melnorme.lang.ide.ui.launch;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.array;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.actions.CalculateValueUIOperation;
import melnorme.lang.ide.ui.actions.SimpleUIOperation;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.utils.ControlUtils;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.lang.ide.ui.utils.WorkbenchUtils;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.HashSet2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public abstract class BaseLaunchShortcut implements ILaunchShortcut {
	
	public static interface ILaunchable {
		
		String getLabel();
		
		IProject getProject();
		
		default String getProjectName() {
			return getProject().getName();
		}
		
		boolean matchesLaunchConfiguration(ILaunchConfiguration config) throws CoreException;
		
		ILaunchConfiguration createNewConfiguration() throws CoreException;
		
	}
	
	/* -----------------  ----------------- */
	
	protected Shell getActiveShell() {
		return WorkbenchUtils.getActiveWorkbenchShell();
	}
	
	protected ILaunchManager getLaunchManager() {
		return DebugPlugin.getDefault().getLaunchManager();
	}
	
	@Override
	public void launch(ISelection selection, String mode) {
		if(selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			launchElements(ssel.toArray(), mode);
		}
	}
	
	@Override
	public void launch(IEditorPart editor, String mode) {
		IFile editorFile = EditorUtils.findFileOfEditor(editor);
		if(editorFile != null) {
			launchElements(array(editorFile), mode);
		} else {
			UIOperationExceptionHandler.handleError("Don't know how to launch editor.", null);
		}
	}
	
	public void launchElements(Object[] elements, String mode) {
		
		new CalculateValueUIOperation<ILaunchable[]>("Configuring launch") {
			
			@Override
			protected ILaunchable[] doBackgroundValueComputation(IProgressMonitor pm)
					throws CoreException, CommonException, OperationCancellation {
				
				HashSet2<ILaunchable> launchables = new HashSet2<>();
				for(Object element : elements) {
					doFindLaunchables(launchables, element, pm);
				}
				return launchables.toArray(ILaunchable.class);
			};
			
			@Override
			protected void handleComputationResult() throws CoreException, CommonException, OperationCancellation {
				handleFoundLaunchables(result, mode);
			}

		}
		.executeAndGetHandledResult();
	}
	
	
	/* -----------------  Find launchables from selection  ----------------- */
	
	protected void doFindLaunchables(HashSet2<ILaunchable> launchTargets, Object element, IProgressMonitor pm) 
			throws CoreException, CommonException, OperationCancellation {
		ILaunchable launchTarget = getLaunchTargetForElement(element, pm);
		if(launchTarget != null) {
			launchTargets.add(launchTarget);
		}
	}
	
	protected abstract ILaunchable getLaunchTargetForElement(Object element, IProgressMonitor pm) 
		throws CoreException, CommonException, OperationCancellation;

	/* -----------------  Handle found launchables  ----------------- */
	
	protected void handleFoundLaunchables(ILaunchable[] launchables, String mode) 
			throws CommonException, OperationCancellation {
		ILaunchable launchableResource = getLaunchableResource(launchables);
		launchTarget(launchableResource, mode);
	}
	
	/* ----------------- search for Launch targets ----------------- */
	protected ILaunchable getLaunchableResource(ILaunchable[] launchables) 
			throws CommonException, OperationCancellation {
		if(launchables == null || launchables.length == 0) {
			throw new CommonException(LangUIMessages.LaunchShortcut_selectionContainsNoLaunchables);
		} else if(launchables.length == 1) {
			return launchables[0];
		} else {
			return chooseLaunchable(launchables);
		}
	}
	
	/* ----------------- Choose launch target ----------------- */
	
	protected ILaunchable chooseLaunchable(ILaunchable[] launchTargets) throws OperationCancellation {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(getActiveShell(), 
			createLaunchTargetLabelProvider());
		dialog.setTitle(LangUIMessages.LaunchShortcut_selectLaunchableToLaunch);
		dialog.setMessage(LangUIMessages.LaunchShortcut_selectLaunchableToLaunch);
		
		return ControlUtils.setElementsAndOpenDialog(dialog, launchTargets);
	}
	
	protected LabelProvider createLaunchTargetLabelProvider() {
		return new LabelProvider() {
			@Override
			public String getText(Object element) {
				ILaunchable launchable = (ILaunchable) element;
				return "[" + launchable.getProjectName() + "]" + launchable.getLabel();
			}
		};
	}
	
	/* ----------------- Launch launchable ----------------- */
	
	protected void launchTarget(ILaunchable launchTarget, String mode) {
		new SimpleUIOperation("Preparing launch") {
			@Override
			protected void handleComputationResult() throws CoreException, CommonException, OperationCancellation {
				doLaunchTarget(launchTarget, mode);
			};
		}.executeAndHandle();
	}
	
	protected void doLaunchTarget(ILaunchable launchTarget, String mode) throws CoreException, OperationCancellation {
		ILaunchConfiguration config = findExistingLaunchConfiguration(launchTarget);
		if(config == null) {
			config = launchTarget.createNewConfiguration(); 
		}
		DebugUITools.launch(config, mode);
	}
	
	/* -----------------  Find existing config  ----------------- */
	
	protected ILaunchConfigurationType getLaunchConfigType() {
		return assertNotNull(getLaunchManager().getLaunchConfigurationType(getLaunchTypeId()));
	}
	
	protected abstract String getLaunchTypeId();
	
	protected ILaunchConfiguration findExistingLaunchConfiguration(ILaunchable launchable) 
			throws CoreException, OperationCancellation {
		
		ILaunchConfiguration[] configs = getLaunchManager().getLaunchConfigurations(getLaunchConfigType());
		
		ArrayList2<ILaunchConfiguration> matchingConfigs = new ArrayList2<>();
		
		for(ILaunchConfiguration config : configs) {
			if(launchable.matchesLaunchConfiguration(config)) {
				matchingConfigs.add(config);
			}
		}
		
		if(matchingConfigs.isEmpty()) {
			return null;
		} else if(matchingConfigs.size() == 1) {
			return matchingConfigs.get(0);
		} else {
			return chooseConfiguration(matchingConfigs);
		}
	}
	
	protected ILaunchConfiguration chooseConfiguration(Indexable<ILaunchConfiguration> configs) 
			throws OperationCancellation {
		IDebugModelPresentation labelProvider = DebugUITools.newDebugModelPresentation();
		try {
			ElementListSelectionDialog dialog = new ElementListSelectionDialog(getActiveShell(), labelProvider);
			dialog.setTitle(LangUIMessages.LaunchShortcut_selectLaunch_title);
			dialog.setMessage(LangUIMessages.LaunchShortcut_selectLaunch_message);
			
			dialog.setMultipleSelection(false);
			return ControlUtils.setElementsAndOpenDialog(dialog, configs);
		} finally {
			labelProvider.dispose();
		}
	}
	
}