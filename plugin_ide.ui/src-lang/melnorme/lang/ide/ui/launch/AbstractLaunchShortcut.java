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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.launching.LaunchConstants;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.editor.EditorUtils;
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
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public abstract class AbstractLaunchShortcut implements ILaunchShortcut {
	
	@Override
	public void launch(IEditorPart editor, String mode) {
		IFile editorFile = findResourceOfEditor(editor);
		if(editorFile != null) {
			launch(editorFile.getProject(), mode);
		}
	}
	
	protected IFile findResourceOfEditor(IEditorPart editor) {
		return EditorUtils.findFileOfEditor(editor);
	}
	
	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			searchAndLaunch(((IStructuredSelection) selection).toArray(), mode);
		}
	}
	
	public void searchAndLaunch(Object[] search, String mode) {
		IResource[] launchables = null;
		try {
			launchables = findLaunchables(search, PlatformUI.getWorkbench().getProgressService());
		} catch (InterruptedException e) {
			return;
		} catch (CoreException e) {
			MessageDialog.openError(getShell(), LangUIMessages.ScriptLaunchShortcut_Error0, e.getMessage());
			return;
		}
		IResource launchableResource = null;
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
	
	protected IResource[] findLaunchables(final Object[] elements, IRunnableContext context) 
			throws InterruptedException, CoreException {
		try {
			final IResource[][] res = new IResource[1][];
			
			IRunnableWithProgress runnable = new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor pm) throws InvocationTargetException {
					pm.beginTask(LangUIMessages.LaunchShortcut_searchingForScripts, 1);
					res[0] = findLaunchablesDo(elements, pm);
					pm.done();
				}
			};
			context.run(true, true, runnable);
			
			return res[0];
		} catch (InvocationTargetException e) {
			throw (CoreException) e.getTargetException();
		}
	}
	
	protected IResource[] findLaunchablesDo(Object[] objects, @SuppressWarnings("unused") IProgressMonitor pm) {
		List<IResource> list = new ArrayList<>(objects.length);
		
		for (Object object : objects) {
			if(object instanceof IFile) {
				IFile f = (IFile) object;
				if(!f.getName().startsWith(".")) {
					list.add(f);
				}
			} else if(object instanceof IProject) {
				IProject proj = (IProject) object;
				list.add(proj);
			} else {
				IProject project = EclipseUtils.getAdapter(object, IProject.class);
				if(project != null) {
					list.add(project);
				}
			}
		}
		return ArrayUtil.createFrom(list, IResource.class);
	}
	
	protected IResource chooseLaunchable(IResource[] scripts) {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), new WorkbenchLabelProvider());
		dialog.setElements(scripts);
		dialog.setTitle(LangUIMessages.LaunchShortcut_selectLaunchableToLaunch);
		dialog.setMessage(LangUIMessages.LaunchShortcut_selectLaunchableToLaunch);
		if (dialog.open() == Window.OK) {
			return (IResource) dialog.getResult()[0];
		}
		return null;
	}
	
	
	protected void launch(IResource resource, String mode) {
		ILaunchConfiguration config = findLaunchConfiguration(resource, getConfigurationType());
		if (config != null) {
			DebugUITools.launch(config, mode);
		}
	}
	
	/** @return the type of configuration this shortcut is applicable to */
	protected abstract ILaunchConfigurationType getConfigurationType();
	
	protected ILaunchManager getLaunchManager() {
		return DebugPlugin.getDefault().getLaunchManager();
	}
	
	/**
	 * Locate a configuration to relaunch for the given type. If one cannot be found, create one.
	 */
	protected ILaunchConfiguration findLaunchConfiguration(IResource launchable, 
			ILaunchConfigurationType configType) {
		List<ILaunchConfiguration> candidateConfigs = Collections.emptyList();
		try {
			ILaunchConfiguration[] configs = DebugPlugin.getDefault().getLaunchManager().
					getLaunchConfigurations(configType);
			candidateConfigs = new ArrayList<ILaunchConfiguration>(configs.length);
			for (int i = 0; i < configs.length; i++) {
				ILaunchConfiguration config = configs[i];
				String attrProgramPath = config.getAttribute(LaunchConstants.ATTR_PROGRAM_PATH, "");
				String attrProjectName = config.getAttribute(LaunchConstants.ATTR_PROJECT_NAME, "");
				
				if (attrProgramPath.equals(launchable.getProjectRelativePath().toString())
							&& attrProjectName.equals(launchable.getProject().getName())) {
					candidateConfigs.add(config);
				}
			}
		} catch (CoreException ce) {
			LangUIPlugin.logStatus(ce);
		}
		
		int candidateCount = candidateConfigs.size();
		if (candidateCount == 0) {
			return createConfiguration(launchable);
		} else if (candidateCount == 1) {
			return (ILaunchConfiguration) candidateConfigs.get(0);
		} else {
			ILaunchConfiguration config = chooseConfiguration(candidateConfigs);
			if (config != null) {
				return config;
			}
		}
		
		return null;
	}
	
	protected ILaunchConfiguration createConfiguration(IResource launchable) {
		ILaunchConfiguration config = null;
		ILaunchConfigurationWorkingCopy wc = null;
		try {
			ILaunchConfigurationType configType = getConfigurationType();
			String suggestedName = launchable.getProject().getName();
			String launchName = getLaunchManager().generateLaunchConfigurationName(suggestedName);
			wc = configType.newInstance(null, launchName);
			wc.setAttribute(
				LaunchConstants.ATTR_PROJECT_NAME,
				suggestedName);
			wc.setAttribute(
				LaunchConstants.ATTR_PROGRAM_PATH,
				launchable.getProjectRelativePath().toPortableString());
			
			wc.setMappedResources(new IResource[] { launchable });
			config = wc.doSave();
		} catch (CoreException ce) {
			LangCore.logStatus(ce);
		}
		return config;
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