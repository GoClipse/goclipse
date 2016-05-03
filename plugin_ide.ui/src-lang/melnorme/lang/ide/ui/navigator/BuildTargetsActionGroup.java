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
import static melnorme.utilbox.core.CoreUtil.list;
import static melnorme.utilbox.core.fntypes.CommonGetter.getOrNull;
import static melnorme.utilbox.misc.StringUtil.emptyAsNull;
import static melnorme.utilbox.misc.StringUtil.nullAsEmpty;

import java.text.MessageFormat;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.navigator.ICommonActionConstants;

import melnorme.lang.ide.core.launch.BuildTargetLaunchCreator;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildManagerMessages;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.BuildTargetData;
import melnorme.lang.ide.core.operations.build.ProjectBuildInfo;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.launch.LangLaunchShortcut;
import melnorme.lang.ide.ui.launch.LangLaunchShortcut.BuildTargetLaunchable;
import melnorme.lang.ide.ui.navigator.LangNavigatorActionProvider.ViewPartActionGroup;
import melnorme.lang.ide.ui.utils.BuildUtilities;
import melnorme.lang.ide.ui.utils.UIOperationsStatusHandler;
import melnorme.lang.ide.ui.utils.operations.AbstractJobUIOperation;
import melnorme.lang.tooling.bundle.LaunchArtifact;
import melnorme.lang.tooling.data.Severity;
import melnorme.lang.tooling.data.StatusException;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Collection2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.ownership.Disposable;

public abstract class BuildTargetsActionGroup extends ViewPartActionGroup {
	
	public BuildTargetsActionGroup(IViewPart viewPart) {
		super(viewPart);
	}
	
	@Override
	public void fillContextMenu(IMenuManager menu) {
		Object firstSel = getSelectionFirstElement();
		
		if(firstSel instanceof BuildTargetsContainer) {
			BuildTargetsContainer buildTargetsContainer = (BuildTargetsContainer) firstSel;
			
			menu.add(new BuildAllTargetsAction(buildTargetsContainer));
			menu.add(new BuildEnabledTargetsAction(buildTargetsContainer));
			menu.add(new Separator("additions"));
			menu.add(new ConfigureBuildTargetAction(buildTargetsContainer));
		}
		
		if(firstSel instanceof BuildTargetElement) {
			BuildTargetElement buildTargetElement = (BuildTargetElement) firstSel;
			
			menu.add(new BuildSingleTargetAction(buildTargetElement));
			
			addLaunchActions(menu, buildTargetElement);
			
			menu.add(new Separator("configure_BuildTarget"));
			menu.add(new ToggleEnabledAction(buildTargetElement));
			menu.add(new ToggleAutoBuildEnabledAction(buildTargetElement));
			menu.add(new ConfigureBuildTargetAction(buildTargetElement));
		}
		
	}
	
	protected void addLaunchActions(IMenuManager menu, BuildTargetElement buildTargetElement) {
		
		BuildTarget buildTarget = buildTargetElement.getBuildTarget();
		
		LaunchArtifact mainLaunchArtifact = getOrNull(buildTarget::getMainLaunchArtifact);
		Indexable<LaunchArtifact> launchSubArtifacts = getOrNull(buildTarget::getSubLaunchArtifacts);
		
		String suggestedLaunchSuffix = buildTargetElement.getTargetDisplayName();
		if(buildTarget.isDefaultBuildType()) {
			suggestedLaunchSuffix = null;
		}
		
		LaunchBuildTargetAction runTargetAction = new LaunchBuildTargetAction(buildTargetElement, true, null, 
			suggestedLaunchSuffix, 
			MessageFormat.format("Run {0}", buildTargetElement.getTargetDisplayName()));
		LaunchBuildTargetAction debugTargetAction = new LaunchBuildTargetAction(buildTargetElement, false, null, 
			suggestedLaunchSuffix, 
			MessageFormat.format("Debug {0}", buildTargetElement.getTargetDisplayName()));
		
		
		if(launchSubArtifacts == null) {
			if(mainLaunchArtifact != null) {
				menu.add(runTargetAction);
				menu.add(debugTargetAction);
			}
			
			return;
		}
		
		MenuManager runSubTargetsMenu = new MenuManager("Run ");
		MenuManager debugSubTargetsMenu = new MenuManager("Debug ");
		
		if(mainLaunchArtifact != null) {
			addMainArtifactAction(buildTarget, 
				runTargetAction, debugTargetAction, 
				runSubTargetsMenu, debugSubTargetsMenu);
		}
		
		for(LaunchArtifact launchArtifact : launchSubArtifacts) {
			
			String launchName = createLaunchShortcut().getLaunchNameForSubTarget(launchArtifact.getName());
			String artifactPath = launchArtifact.getArtifactPath();
			
			runSubTargetsMenu.add(new LaunchBuildTargetAction(buildTargetElement, true, artifactPath, launchName, 
				"Run " + launchName + " - " + artifactPath + "")); 
			debugSubTargetsMenu.add(new LaunchBuildTargetAction(buildTargetElement, false, artifactPath, launchName, 
				"Debug " + launchName + " - " + artifactPath + ""));
		}
			
		menu.add(runSubTargetsMenu);
		menu.add(debugSubTargetsMenu);
	}
	
	protected void addMainArtifactAction(
			BuildTarget buildTarget, 
			LaunchBuildTargetAction runTargetAction,
			LaunchBuildTargetAction debugTargetAction, 
			MenuManager runSubTargetsMenu, 
			MenuManager debugSubTargetsMenu) 
	{
		if(buildTarget.isDefaultBuildType()) {
			runTargetAction.setText("Run default executable");
			debugTargetAction.setText("Debug default executable");
		}
		
		runSubTargetsMenu.add(runTargetAction);
		runSubTargetsMenu.add(new Separator());
		debugSubTargetsMenu.add(debugTargetAction);
		debugSubTargetsMenu.add(new Separator());
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
			
			try(Disposable reg = buildManager.autoBuildsEnablement().enterDisable()) {
				// Note: this needs to run in UI
				BuildUtilities.saveEditors(list(getProject()));
			}
			
			new AbstractJobUIOperation(getJobTitle()) {
				@Override
				protected void doBackgroundComputation(IProgressMonitor pm)
						throws CommonException, OperationCancellation {
					doJobRun(pm);
				}
			}.executeAndHandle();
		}
		
		protected String getJobTitle() {
			return opName;
		}
		
		@SuppressWarnings("unused") 
		protected void doJobRun(IProgressMonitor pm) throws CommonException, OperationCancellation {
		}
		
	}
	
	public static class BuildAllTargetsAction extends AbstractBuildElementAction {
		
		public BuildAllTargetsAction(BuildTargetsContainer buildTargetContainer) {
			super(buildTargetContainer, BuildManagerMessages.NAME_BuildAllTargetsAction);
		}
		
		@Override
		protected void doJobRun(IProgressMonitor pm) throws CommonException, OperationCancellation {
			Collection2<BuildTarget> enabledTargets = getBuildInfo().getBuildTargets();
			getBuildManager().newBuildTargetsOperation(getProject(), enabledTargets).execute(pm);
		}
	}
	
	public static class BuildEnabledTargetsAction extends AbstractBuildElementAction {
		
		public BuildEnabledTargetsAction(BuildTargetsContainer buildTargetContainer) {
			super(buildTargetContainer, BuildManagerMessages.NAME_BuildEnabledTargetsAction);
		}
		
		@Override
		protected void doJobRun(IProgressMonitor pm) throws CommonException, OperationCancellation {
			ArrayList2<BuildTarget> enabledTargets = getBuildInfo().getEnabledTargets(true);
			getBuildManager().newBuildTargetsOperation(getProject(), enabledTargets).execute(pm);
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
		
		protected String getBuildTargetName() {
			return buildTarget.getBuildTargetName();
		}
		
	}
	
	public static class ToggleEnabledAction extends AbstractBuildTargetAction {
		
		public ToggleEnabledAction(BuildTargetElement buildTargetElement) {
			super(buildTargetElement, BuildManagerMessages.LABEL_EnableForNormalBuild, Action.AS_CHECK_BOX);
			setChecked(buildTarget.isNormalBuildEnabled());
		}
		
		@Override
		public void doRun() throws StatusException {
			try {
				doRun0();
			} catch(CommonException e) {
				throw e.toStatusException(Severity.ERROR);
			}
		}
		
		protected void doRun0() throws CommonException {
			getBuildInfo().changeEnable(getBuildTargetName(), isChecked(), buildTarget.isAutoBuildEnabled());
		}
		
	}
	
	public static class ToggleAutoBuildEnabledAction extends ToggleEnabledAction {
		
		public ToggleAutoBuildEnabledAction(BuildTargetElement buildTargetElement) {
			super(buildTargetElement);
			setText(BuildManagerMessages.LABEL_EnableForAutoBuild);
			setChecked(buildTarget.isAutoBuildEnabled());
		}
		
		@Override
		protected void doRun0() throws CommonException {
			getBuildInfo().changeEnable(getBuildTargetName(), buildTarget.isNormalBuildEnabled(), isChecked());
		}
		
	}
	
	public static class BuildSingleTargetAction extends AbstractBuildTargetAction {
		
		public BuildSingleTargetAction(BuildTargetElement buildTargetElement) {
			super(buildTargetElement, BuildManagerMessages.NAME_RunBuildTargetAction);
		}
		
		@Override
		protected String getJobTitle() {
			return format(BuildManagerMessages.INFO_BuildTargetAction, 
				getProject().getName(), buildTargetElement.getTargetDisplayName());
		}
		
		@Override
		public void doRun() throws StatusException {
			super.doRun();
		}
		
		@Override
		protected void doJobRun(IProgressMonitor pm) throws CommonException, OperationCancellation {
			getBuildManager().newBuildTargetOperation(getProject(), buildTarget).execute(pm);
		}
		
	}
	
	public class LaunchBuildTargetAction extends AbstractBuildTargetAction {
		
		public String mode;
		public BuildTargetLaunchCreator launchCreator;
		
		public LaunchBuildTargetAction(BuildTargetElement buildTargetElement, boolean isRun, 
				String exePathOverride, String launchNameSuggestion, String actionText) {
			super(buildTargetElement, "");
			mode = isRun ? "run" : "debug";
			
			setText(actionText);
			
			String targetName = buildTarget.getTargetName();
			launchCreator = buildTargetLaunchCreator(project, targetName, exePathOverride, launchNameSuggestion);
		}
		
		protected String getTargetName() {
			return buildTarget.getTargetName();
		}
		
		@Override
		public void doRun() throws StatusException {
			LangLaunchShortcut launchShortcut = createLaunchShortcut();
			BuildTargetLaunchable launchable = launchShortcut.new BuildTargetLaunchable(project, launchCreator);
			launchShortcut.launchTarget(launchable, mode);
		}
		
	}
	
	public static BuildTargetLaunchCreator buildTargetLaunchCreator(IProject project, 
			String targetName, String executablePath, String launchNameSuggestion) {
		
		BuildTargetData buildTargetData = new BuildTargetData(
			targetName,
			false,
			false,
			null,
			executablePath
		);
		
		return new BuildTargetLaunchCreator(project.getName(), buildTargetData) {
			@Override
			protected String getSuggestedConfigName_do() {
				return nullAsEmpty(projectName) + StringUtil.prefixStr(" - ", emptyAsNull(launchNameSuggestion));
			}
		};
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
		return LangUIPlugin_Actual.PLUGIN_ID + ".propertyPages.ProjectBuildConfiguration";
	}
	
}