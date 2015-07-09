/*******************************************************************************
 * Copyright (c) 2005, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 		IBM - initial implementation?
 * 		DLTK team - initial implementation?
 * 		Bruno Medeiros - modifications. 
 *******************************************************************************/
package melnorme.lang.ide.ui.launch;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.array;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.launching.LaunchConstants;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.actions.CalculateValueUIOperation;
import melnorme.lang.ide.ui.actions.SimpleUIOperation;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.lang.ide.ui.utils.WorkbenchUtils;
import melnorme.utilbox.collections.HashSet2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public abstract class AbstractLaunchShortcut2 implements ILaunchShortcut {
	
	public static interface ILaunchTarget {
		
		IProject getProject();
		
		String getProgramPath();
		
		IResource getAssociatedResource();
		
	}
	
	public static class ResourceLaunchTarget implements ILaunchTarget {
		
		protected final IResource resource;
		
		public ResourceLaunchTarget(IResource resource) {
			this.resource = assertNotNull(resource);
		}
		
		@Override
		public IProject getProject() {
			return resource.getProject();
		}
		
		@Override
		public String getProgramPath() {
			return resource.getProjectRelativePath().toString();
		}
		
		@Override
		public IResource getAssociatedResource() {
			return resource;
		}
		
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public void launch(IEditorPart editor, String mode) {
		IFile editorFile = EditorUtils.findFileOfEditor(editor);
		if(editorFile != null) {
			launchElements(array(editorFile), mode);
		} else {
			UIOperationExceptionHandler.handleError("Don't know how to launch editor.", null);
		}
	}
	
	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			launchElements(ssel.toArray(), mode);
		}
	}
	
	protected ILaunchTarget[] doFindLaunchables(Object[] elements, @SuppressWarnings("unused") IProgressMonitor pm)
			throws CommonException, OperationCancellation {
		HashSet2<ILaunchTarget> launchTargets = new HashSet2<>(elements.length);
		
		for(Object element : elements) {
			ResourceLaunchTarget launchTarget = getLaunchTargetForElement(element);
			if(launchTarget != null) {
				launchTargets.add(launchTarget);
			}
		}
		return launchTargets.toArray(ILaunchTarget.class);
	}
	
	protected ResourceLaunchTarget getLaunchTargetForElement(Object element) 
			throws CommonException, OperationCancellation {
		IResource resource;
		if(element instanceof IResource) {
			resource = (IResource) element;
		} else {
			resource = EclipseUtils.getAdapter(element, IResource.class);
		}
		
		if(resource != null) {
			return getLaunchTargetForResource(resource);
		}
		return null;
	}
	
	protected ResourceLaunchTarget getLaunchTargetForResource(IResource resource)
			throws CommonException, OperationCancellation {
		return new ResourceLaunchTarget(resource);
	}
	
	/* ----------------- search for Launch targets ----------------- */
	
	public void launchElements(Object[] elements, String mode) {
		
		new CalculateValueUIOperation<ILaunchTarget[]>("Configuring launch") {
			
			@Override
			protected ILaunchTarget[] doBackgroundValueComputation(IProgressMonitor monitor)
					throws CoreException, CommonException, OperationCancellation {
				monitor.beginTask(LangUIMessages.LaunchShortcut_searchingForScripts, 1);
				return doFindLaunchables(elements, monitor);
			};
			
			@Override
			protected void handleComputationResult() throws CoreException, CommonException, OperationCancellation {
				ILaunchTarget launchableResource = null;
				if(result == null || result.length == 0) {
					throw new CommonException(LangUIMessages.LaunchShortcut_selectionContainsNoScript);
				} else if(result.length == 1) {
					launchableResource = result[0];
				} else {
					launchableResource = chooseLaunchable(result);
				}
				launchTarget(launchableResource, mode);
			}

		}
		.executeAndGetHandledResult();
	}
	
	
	/* ----------------- Choose launch target ----------------- */
	
	protected ILaunchTarget chooseLaunchable(ILaunchTarget[] launchTargets) throws OperationCancellation {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(getActiveShell(), 
			createLaunchTargetLabelProvider());
		dialog.setElements(launchTargets);
		dialog.setTitle(LangUIMessages.LaunchShortcut_selectLaunchableToLaunch);
		dialog.setMessage(LangUIMessages.LaunchShortcut_selectLaunchableToLaunch);
		if (dialog.open() == Window.OK) {
			return (ILaunchTarget) dialog.getResult()[0];
		}
		throw new OperationCancellation();
	}
	
	protected LabelProvider createLaunchTargetLabelProvider() {
		return new LabelProvider() {
			@Override
			public String getText(Object element) {
				ILaunchTarget launchTarget = (ILaunchTarget) element;
				return launchTarget.getProgramPath();
			}
		};
	}
	
	/* ----------------- Launch a launch target ----------------- */
	
	protected void launchTarget(ILaunchTarget launchTarget, String mode) {
		new SimpleUIOperation("Preparing launch") {
			@Override
			protected void handleComputationResult() throws CoreException, CommonException, OperationCancellation {
				doLaunchTarget(launchTarget, mode);
			};
		}.executeAndHandle();
	}
	
	protected void doLaunchTarget(ILaunchTarget launchTarget, String mode) throws CoreException, OperationCancellation {
		ILaunchConfiguration config = findLaunchConfiguration(launchTarget, getConfigurationType());
		assertNotNull(config);
		DebugUITools.launch(config, mode);
	}
	
	/** @return the type of configuration this shortcut is applicable to. */
	protected ILaunchConfigurationType getConfigurationType() {
		return getLaunchManager().getLaunchConfigurationType(getLaunchTypeId());
	}
	
	protected abstract String getLaunchTypeId();
	
	protected ILaunchManager getLaunchManager() {
		return DebugPlugin.getDefault().getLaunchManager();
	}
	
	/**
	 * Locate a launch configuration to for the given launchable. If one cannot be found, create one.
	 * @throws OperationCancellation if user cancelled launch.
	 */
	protected ILaunchConfiguration findLaunchConfiguration(ILaunchTarget launchable,
			ILaunchConfigurationType configType) throws CoreException, OperationCancellation {
		
		ILaunchConfiguration[] configs 
			= DebugPlugin.getDefault().getLaunchManager(). getLaunchConfigurations(configType);
		
		List<ILaunchConfiguration> candidateConfigs = new ArrayList<>();
		
		for (ILaunchConfiguration config : configs) {
			
			String attrProgramPath = config.getAttribute(LaunchConstants.ATTR_PROGRAM_PATH, "");
			String attrProjectName = config.getAttribute(LaunchConstants.ATTR_PROJECT_NAME, "");
			
			if (attrProgramPath.equals(launchable.getProgramPath()) && 
				attrProjectName.equals(launchable.getProject().getName())) 
			{
				candidateConfigs.add(config);
			}
		}
		
		int candidateCount = candidateConfigs.size();
		if(candidateCount == 0) {
			return createConfiguration(launchable);
		} else if (candidateCount == 1) {
			return (ILaunchConfiguration) candidateConfigs.get(0);
		} else {
			return chooseConfiguration(candidateConfigs);
		}
	}
	
	protected ILaunchConfiguration createConfiguration(ILaunchTarget launchable) throws CoreException {
		String suggestedName = launchable.getProject().getName();
		return createConfiguration(launchable, suggestedName);
	}
	
	protected ILaunchConfiguration createConfiguration(ILaunchTarget launchable, String suggestedName) 
			throws CoreException {
		ILaunchConfiguration config = null;
		ILaunchConfigurationWorkingCopy wc = null;
		
		ILaunchConfigurationType configType = getConfigurationType();
		String launchName = getLaunchManager().generateLaunchConfigurationName(suggestedName);
		wc = configType.newInstance(null, launchName);
		
		wc.setAttribute(LaunchConstants.ATTR_PROJECT_NAME, launchable.getProject().getName());
		wc.setAttribute(LaunchConstants.ATTR_PROGRAM_PATH, launchable.getProgramPath());
		wc.setMappedResources(new IResource[] { launchable.getAssociatedResource() });
		
		config = wc.doSave();
		
		return config;
	}
	
	/**
	 * Show a selection dialog that allows the user to choose one of the
	 * specified launch configurations. Return the chosen config, or
	 * <code>null</code> if the user cancelled the dialog.
	 */
	protected ILaunchConfiguration chooseConfiguration(List<ILaunchConfiguration> configList) 
			throws OperationCancellation {
		IDebugModelPresentation labelProvider = DebugUITools.newDebugModelPresentation();
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(getActiveShell(), labelProvider);
		dialog.setElements(configList.toArray());
		dialog.setTitle(LangUIMessages.LaunchShortcut_selectLaunch_title);
		dialog.setMessage(LangUIMessages.LaunchShortcut_selectLaunch_message);
		dialog.setMultipleSelection(false);
		int result = dialog.open();
		labelProvider.dispose();
		if (result == Window.OK) {
			return (ILaunchConfiguration) dialog.getFirstResult();
		}
		throw new OperationCancellation();
	}
	
	protected Shell getActiveShell() {
		return WorkbenchUtils.getActiveWorkbenchShell();
	}
	
}