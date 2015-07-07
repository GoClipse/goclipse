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
package melnorme.lang.ide.ui.navigator;

import static java.text.MessageFormat.format;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.navigator.ICommonActionConstants;

import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildManagerMessages;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.ui.navigator.LangNavigatorActionProvider.ViewPartActionGroup;
import melnorme.lang.ide.ui.operations.EclipseJobUIOperation;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.lang.tooling.data.StatusException;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class BuildTargetsActionGroup extends ViewPartActionGroup {
	
	public BuildTargetsActionGroup(IViewPart viewPart) {
		super(viewPart);
	}
	
	@Override
	public void fillContextMenu(IMenuManager menu) {
		Object firstSel = getSelectionFirstElement();
		
		if(firstSel instanceof BuildTargetElement) {
			BuildTargetElement buildTargetElement = (BuildTargetElement) firstSel;
			
			menu.add(new RunBuildTargetAction(buildTargetElement));
			menu.add(new Separator("modify_BuildTarget"));
			menu.add(new ToggleEnabledAction(buildTargetElement));
		}
		
		if(firstSel instanceof BuildTargetsContainer) {
			BuildTargetsContainer buildTargetsContainer = (BuildTargetsContainer) firstSel;
			
			menu.add(new BuildAllTargetsAction(buildTargetsContainer));
			menu.add(new BuildEnabledTargetsAction(buildTargetsContainer));
		}
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
		actionBars.setGlobalActionHandler(ICommonActionConstants.OPEN, new RunBuildTargetAction(buildTargetElement));
	}
	
	public static abstract class AbstractBuildAction extends Action {
		
		protected final BuildManager buildManager;
		protected final IProject project;
		protected final String opName;
		
		public AbstractBuildAction(BuildTargetsContainer buildTargetContainer, String opName) {
			this(buildTargetContainer, opName, Action.AS_UNSPECIFIED);
		}
		
		public AbstractBuildAction(BuildTargetsContainer buildTargetContainer, String opName, int style) {
			this(buildTargetContainer.getBuildManager(), buildTargetContainer.getProject(), opName, style);
		}
		
		public AbstractBuildAction(BuildManager buildManager, IProject project, String opName, int style) {
			super(opName, style);
			this.buildManager = buildManager;
			this.project = project;
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
				UIOperationExceptionHandler.handleStatusMessage(getStatusDialogTitle(), se);
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
	
	public static class BuildAllTargetsAction extends AbstractBuildAction {
		
		public BuildAllTargetsAction(BuildTargetsContainer buildTargetContainer) {
			super(buildTargetContainer, BuildManagerMessages.NAME_BuildAllTargetsAction);
		}
		
		@Override
		protected void doJobRun(IProgressMonitor pm) throws CoreException, CommonException, OperationCancellation {
			Indexable<BuildTarget> enabledTargets = getBuildInfo().getBuildTargets();
			getBuildManager().newBuildTargetOperation(getProject(), enabledTargets).execute(pm);
		}
	}
	
	public static class BuildEnabledTargetsAction extends AbstractBuildAction {
		
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
	
	public static abstract class AbstractBuildTargetAction extends AbstractBuildAction {
		
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
	
	public static class RunBuildTargetAction extends AbstractBuildTargetAction {
		
		public RunBuildTargetAction(BuildTargetElement buildTargetElement) {
			super(buildTargetElement, BuildManagerMessages.NAME_RunBuildTargetAction);
		}
		
		@Override
		protected String getJobTitle() {
			return format(BuildManagerMessages.INFO_BuildTargetAction, 
				getProject(), buildTargetElement.getTargetDisplayName());
		}
		
		@Override
		protected void doJobRun(IProgressMonitor pm) throws CoreException, CommonException, OperationCancellation {
			buildTarget.newBuildTargetOperation(getProject()).execute(pm);
		}
		
	}
	
}