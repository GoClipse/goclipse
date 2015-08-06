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
package melnorme.lang.ide.ui.navigator;

import static java.text.MessageFormat.format;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.navigator.ICommonActionConstants;

import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildManagerMessages;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.launch.LangLaunchShortcut;
import melnorme.lang.ide.ui.launch.LangLaunchShortcut.BuildTargetLaunchable;
import melnorme.lang.ide.ui.navigator.LangNavigatorActionProvider.ViewPartActionGroup;
import melnorme.lang.ide.ui.operations.EclipseJobUIOperation;
import melnorme.lang.ide.ui.utils.UIOperationsStatusHandler;
import melnorme.lang.tooling.data.StatusException;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Collection2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public abstract class BuildTargetsActionGroup extends ViewPartActionGroup {
	
	public BuildTargetsActionGroup(IViewPart viewPart) {
		super(viewPart);
	}
	
	@Override
	public void fillContextMenu(IMenuManager menu) {
		Object firstSel = getSelectionFirstElement();
		
		if(firstSel instanceof BuildTargetElement) {
			BuildTargetElement buildTargetElement = (BuildTargetElement) firstSel;
			
			menu.add(new BuildSingleTargetAction(buildTargetElement));
			
			if(buildTargetElement.getBuildTarget().isLaunchable(buildTargetElement.project)) {
				addLaunchActions(menu, buildTargetElement);
			}
			
			menu.add(new Separator("configure_BuildTarget"));
			menu.add(new ToggleEnabledAction(buildTargetElement));
			menu.add(new ConfigureBuildTargetAction(buildTargetElement));
		}
		
		if(firstSel instanceof BuildTargetsContainer) {
			BuildTargetsContainer buildTargetsContainer = (BuildTargetsContainer) firstSel;
			
			menu.add(new BuildAllTargetsAction(buildTargetsContainer));
			menu.add(new BuildEnabledTargetsAction(buildTargetsContainer));
			menu.add(new Separator("additions"));
			menu.add(new ConfigureBuildTargetAction(buildTargetsContainer));
		}
	}
	
	protected void addLaunchActions(IMenuManager menu, BuildTargetElement buildTargetElement) {
		menu.add(new LaunchBuildTargetAction(buildTargetElement, true));
		menu.add(new LaunchBuildTargetAction(buildTargetElement, false));
	}
	
	@Override
	public void fillActionBars(IActionBars actionBars) {
		Object firstSel = getSelectionFirstElement();
		
		if(firstSel instanceof BuildTargetElement) {
			BuildTargetElement buildTargetElement = (BuildTargetElement) firstSel;
			
			fillActionBars(actionBars, buildTargetElement);
		}
	}
	
	protected void fillActionBars(IActionBars actionBars, BuildTargetElement buildTargetElement) {
		actionBars.setGlobalActionHandler(ICommonActionConstants.OPEN, new BuildSingleTargetAction(buildTargetElement));
	}
	
	public static abstract class AbstractBuildElementAction extends Action {
		
		protected final BuildManager buildManager;
		protected final IProject project;
		protected final String opName;
		
		public AbstractBuildElementAction(BuildTargetsContainer buildTargetContainer, String opName) {
			this(buildTargetContainer, opName, Action.AS_UNSPECIFIED);
		}
		
		public AbstractBuildElementAction(BuildTargetsContainer buildTargetContainer, String opName, int style) {
			this(buildTargetContainer.getBuildManager(), buildTargetContainer.getProject(), opName, style);
		}
		
		public AbstractBuildElementAction(BuildManager buildManager, IProject project, String opName, int style) {
			super(opName, style);
			this.buildManager = assertNotNull(buildManager);
			this.project = assertNotNull(project);
			this.opName = opName;
		}
		
		public IProject getProject() {
			return project;
		}
		
		public BuildManager getBuildManager() {
			return buildManager;
		}
		
		public ProjectBuildInfo getBuildInfo() {
			return getBuildManager().getBuildInfo(getProject());
		}
		
		@Override
		public void run() {
			try {
				doRun();
			} catch(StatusException se) {
				UIOperationsStatusHandler.handleStatus(getStatusDialogTitle(), se);
			}
		}
		
		protected String getStatusDialogTitle() {
			return "Error during: " + getText();
		}
		
		public void doRun() throws StatusException {
			new EclipseJobUIOperation(getJobTitle()) {
				@Override
				protected void doBackgroundComputation(IProgressMonitor pm)
						throws CoreException, CommonException, OperationCancellation {
					doJobRun(pm);
				}
			}.executeAndHandle();
		}
		
		protected String getJobTitle() {
			return opName;
		}
		
		@SuppressWarnings("unused") 
		protected void doJobRun(IProgressMonitor pm) throws CoreException, CommonException, OperationCancellation {
		}
		
	}
	
	public static class BuildAllTargetsAction extends AbstractBuildElementAction {
		
		public BuildAllTargetsAction(BuildTargetsContainer buildTargetContainer) {
			super(buildTargetContainer, BuildManagerMessages.NAME_BuildAllTargetsAction);
		}
		
		@Override
		protected void doJobRun(IProgressMonitor pm) throws CoreException, CommonException, OperationCancellation {
			Collection2<BuildTarget> enabledTargets = getBuildInfo().getBuildTargets();
			getBuildManager().newBuildTargetOperation(getProject(), enabledTargets).execute(pm);
		}
	}
	
	public static class BuildEnabledTargetsAction extends AbstractBuildElementAction {
		
		public BuildEnabledTargetsAction(BuildTargetsContainer buildTargetContainer) {
			super(buildTargetContainer, BuildManagerMessages.NAME_BuildEnabledTargetsAction);
		}
		
		@Override
		protected void doJobRun(IProgressMonitor pm) throws CoreException, CommonException, OperationCancellation {
			ArrayList2<BuildTarget> enabledTargets = getBuildInfo().getEnabledTargets();
			getBuildManager().newBuildTargetOperation(getProject(), enabledTargets).execute(pm);
		}
	}
	
	/* -----------------  ----------------- */
	
	public static abstract class AbstractBuildTargetAction extends AbstractBuildElementAction {
		
		protected final BuildTargetElement buildTargetElement;
		protected final BuildTarget buildTarget;
		
		public AbstractBuildTargetAction(BuildTargetElement buildTargetElement, String text) {
			this(buildTargetElement, text, Action.AS_UNSPECIFIED);
		}
		
		public AbstractBuildTargetAction(BuildTargetElement buildTargetElement, String text, int style) {
			super(buildTargetElement.getBuildManager(), buildTargetElement.project, text, style);
			this.buildTargetElement = assertNotNull(buildTargetElement);
			this.buildTarget = buildTargetElement.getBuildTarget();
		}
		
	}
	
	public static class ToggleEnabledAction extends AbstractBuildTargetAction {
		
		public ToggleEnabledAction(BuildTargetElement buildTargetElement) {
			super(buildTargetElement, BuildManagerMessages.NAME_ToggleEnabledAction, Action.AS_CHECK_BOX);
			setChecked(buildTarget.isEnabled());
		}
		
		@Override
		public void doRun() throws StatusException {
			getBuildInfo().changeEnable(buildTarget, isChecked());
		}
		
	}
	
	public static class BuildSingleTargetAction extends AbstractBuildTargetAction {
		
		public BuildSingleTargetAction(BuildTargetElement buildTargetElement) {
			super(buildTargetElement, BuildManagerMessages.NAME_RunBuildTargetAction);
		}
		
		@Override
		protected String getJobTitle() {
			return format(BuildManagerMessages.INFO_BuildTargetAction, 
				getProject(), buildTargetElement.getTargetDisplayName());
		}
		
		@Override
		protected void doJobRun(IProgressMonitor pm) throws CoreException, CommonException, OperationCancellation {
			getBuildManager().newBuildTargetOperation(getProject(), buildTarget).execute(pm);
		}
		
	}
	
	public class LaunchBuildTargetAction extends AbstractBuildTargetAction {
		
		public String mode;
		
		public LaunchBuildTargetAction(BuildTargetElement buildTargetElement, boolean isRun) {
			super(buildTargetElement, isRun ?
				BuildManagerMessages.NAME_RunTargetAction :
				BuildManagerMessages.NAME_DebugTargetAction
			);
			mode = isRun ? "run" : "debug";
		}
		
		@Override
		public void doRun() throws StatusException {
			LangLaunchShortcut launchShortcut = createLaunchShortcut();
			String targetName = buildTargetElement.getBuildTarget().getTargetName();
			BuildTargetLaunchable launchable = launchShortcut.new BuildTargetLaunchable(project, targetName);
			launchShortcut.launchTarget(launchable, mode);
		}
		
	}
	
	protected abstract LangLaunchShortcut createLaunchShortcut();
	
	public class ConfigureBuildTargetAction extends AbstractBuildElementAction {
		
		protected String targetName;
		
		public ConfigureBuildTargetAction(BuildTargetsContainer buildTargetContainer) {
			super(buildTargetContainer.getBuildManager(), buildTargetContainer.getProject(), 
				BuildManagerMessages.NAME_ConfigureTargetsAction, Action.AS_UNSPECIFIED);
		}
		
		public ConfigureBuildTargetAction(BuildTargetElement buildTargetElement) {
			super(buildTargetElement.getBuildManager(), buildTargetElement.getProject(), 
				BuildManagerMessages.NAME_ConfigureTargetAction, Action.AS_UNSPECIFIED);
			
			targetName = buildTargetElement.getBuildTarget().getTargetName();
		}
		
		@Override
		public void doRun() throws StatusException {
			PreferenceDialog dialog = PreferencesUtil.createPropertyDialogOn(getShell(), getProject(), 
				getProjectConfigPropertyPage(), null, targetName);
			dialog.open();
		}
		
	}
	
	protected String getProjectConfigPropertyPage() {
		return LangUIPlugin_Actual.PLUGIN_ID + ".properties.ProjectBuildConfiguration";
	}
	
}