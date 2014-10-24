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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.launching.LaunchConstants;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.lang.ide.ui.utils.WorkbenchUtils;
import melnorme.utilbox.misc.ArrayUtil;

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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

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
			launchEditorFile(editorFile, mode);
		} else {
			UIOperationExceptionHandler.handleError(
				"Don't know how to launch editor.", null);
		}
	}
	
	protected void launchEditorFile(IFile editorFile, String mode) {
		launch(editorFile, mode);
	}
	
	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			searchAndLaunch(((IStructuredSelection) selection).toArray(), mode);
		}
	}
	
	protected void launch(IResource resource, String mode) {
		launch(resourceToLaunchTarget(resource), mode);
	}
	
	protected ResourceLaunchTarget resourceToLaunchTarget(IResource resource) {
		return new ResourceLaunchTarget(resource);
	}
	
	protected void launch(ILaunchTarget launchTarget, String mode) {
		ILaunchConfiguration config = findLaunchConfiguration(launchTarget, getConfigurationType());
		if (config != null) {
			DebugUITools.launch(config, mode);
		}
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
	 */
	protected ILaunchConfiguration findLaunchConfiguration(ILaunchTarget launchable,
			ILaunchConfigurationType configType) {
		
		try {
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
			if (candidateCount == 0) {
				return createConfiguration(launchable);
			} else if (candidateCount == 1) {
				return (ILaunchConfiguration) candidateConfigs.get(0);
			} else {
				ILaunchConfiguration config = chooseConfiguration(candidateConfigs);
				return config; // can be null
			}
			
		} catch (CoreException ce) {
			LangUIPlugin.logStatus(ce);
			
			return createConfiguration(launchable);
		}
	}
	
	protected ILaunchConfiguration createConfiguration(ILaunchTarget launchable) {
		String suggestedName = launchable.getProject().getName();
		return createConfiguration(launchable, suggestedName);
	}
	
	protected ILaunchConfiguration createConfiguration(ILaunchTarget launchable, String suggestedName) {
		ILaunchConfiguration config = null;
		ILaunchConfigurationWorkingCopy wc = null;
		try {
			ILaunchConfigurationType configType = getConfigurationType();
			String launchName = getLaunchManager().generateLaunchConfigurationName(suggestedName);
			wc = configType.newInstance(null, launchName);
			wc.setAttribute(
				LaunchConstants.ATTR_PROJECT_NAME,
				launchable.getProject().getName());
			wc.setAttribute(
				LaunchConstants.ATTR_PROGRAM_PATH,
				launchable.getProgramPath());
			
			wc.setMappedResources(new IResource[] { launchable.getAssociatedResource() });
			config = wc.doSave();
		} catch (CoreException ce) {
			LangCore.logStatus(ce);
		}
		return config;
	}
	
	/* ----------------- search for Launch targets ----------------- */
	
	public void searchAndLaunch(Object[] search, String mode) {
		ILaunchTarget[] launchables = null;
		try {
			launchables = findLaunchables(search, PlatformUI.getWorkbench().getProgressService());
		} catch (InterruptedException e) {
			return;
		} catch (CoreException e) {
			MessageDialog.openError(getShell(), LangUIMessages.ScriptLaunchShortcut_Error0, e.getMessage());
			return;
		}
		ILaunchTarget launchableResource = null;
		if (launchables.length == 0) {
			MessageDialog.openError(getShell(), LangUIMessages.ScriptLaunchShortcut_Error1, 
				LangUIMessages.LaunchShortcut_selectionContainsNoScript);
		} else if (launchables.length > 1) {
			launchableResource = chooseLaunchable(launchables);
		} else {
			launchableResource = launchables[0];
		}
		if (launchableResource != null) {
			launch(launchableResource, mode);
		}
	}
	
	protected Shell getShell() {
		return WorkbenchUtils.getActiveWorkbenchShell();
	}
	
	protected ILaunchTarget[] findLaunchables(final Object[] elements, IRunnableContext context) 
			throws InterruptedException, CoreException {
		try {
			FindLaunchablesResult runnable = new FindLaunchablesResult() {
				@Override
				public void run(IProgressMonitor pm) throws InvocationTargetException {
					pm.beginTask(LangUIMessages.LaunchShortcut_searchingForScripts, 1);
					result = findLaunchablesDo(elements, pm);
					pm.done();
				}
			};
			context.run(true, true, runnable);
			return runnable.result;
		} catch (InvocationTargetException e) {
			throw (CoreException) e.getTargetException();
		}
	}
	
	public static abstract class FindLaunchablesResult implements IRunnableWithProgress {
		protected ILaunchTarget[] result;
	}
	
	protected ILaunchTarget[] findLaunchablesDo(Object[] objects, @SuppressWarnings("unused") IProgressMonitor pm) {
		HashSet<ILaunchTarget> list = new HashSet<>(objects.length);
		
		for (Object object : objects) {
			IResource resource;
			if(object instanceof IResource) {
				resource = (IResource) object;
			} else {
				resource = EclipseUtils.getAdapter(object, IProject.class);
			}
			
			if(resource != null) {
				ResourceLaunchTarget launchTarget = resourceToLaunchTarget(resource);
				if(launchTarget != null) {
					list.add(launchTarget);
				}
			}
		}
		return ArrayUtil.createFrom(list, ILaunchTarget.class);
	}
	
	protected ILaunchTarget chooseLaunchable(ILaunchTarget[] launchTargets) {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), createLaunchTargetLabelProvider());
		dialog.setElements(launchTargets);
		dialog.setTitle(LangUIMessages.LaunchShortcut_selectLaunchableToLaunch);
		dialog.setMessage(LangUIMessages.LaunchShortcut_selectLaunchableToLaunch);
		if (dialog.open() == Window.OK) {
			return (ILaunchTarget) dialog.getResult()[0];
		}
		return null;
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
	
	/**
	 * Show a selection dialog that allows the user to choose one of the
	 * specified launch configurations. Return the chosen config, or
	 * <code>null</code> if the user cancelled the dialog.
	 */
	protected ILaunchConfiguration chooseConfiguration(List<ILaunchConfiguration> configList) {
		IDebugModelPresentation labelProvider = DebugUITools.newDebugModelPresentation();
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), labelProvider);
		dialog.setElements(configList.toArray());
		dialog.setTitle(LangUIMessages.LaunchShortcut_selectLaunch_title);
		dialog.setMessage(LangUIMessages.LaunchShortcut_selectLaunch_message);
		dialog.setMultipleSelection(false);
		int result = dialog.open();
		labelProvider.dispose();
		if (result == Window.OK) {
			return (ILaunchConfiguration) dialog.getFirstResult();
		}
		return null;
	}
	
}