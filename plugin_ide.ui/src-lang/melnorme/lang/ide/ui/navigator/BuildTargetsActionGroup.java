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

import java.text.MessageFormat;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.navigator.ICommonActionConstants;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.BuildTarget;
import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.lang.ide.core.project_model.BuildManagerMessages;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.core.utils.CoreOperationAdapter;
import melnorme.lang.ide.ui.navigator.LangNavigatorActionProvider.ViewPartActionGroup;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.lang.tooling.data.StatusException;
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
	
	public static abstract class AbstractBuildTargetAction extends Action {
			
		protected final BuildTargetElement buildTargetElement;
		public final BuildTarget buildTarget;
		
		public AbstractBuildTargetAction(BuildTargetElement buildTargetElement, String text) {
			this(buildTargetElement, text, Action.AS_UNSPECIFIED);
		}
		
		public AbstractBuildTargetAction(BuildTargetElement buildTargetElement, String text, int style) {
			super(text, style);
			this.buildTargetElement = buildTargetElement;
			this.buildTarget = buildTargetElement.buildTarget;
		}
		
		public ProjectBuildInfo getBuildInfo() {
			return buildTargetElement.getBuildManager().getBuildInfo(buildTargetElement.project);
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
		}
		
	}
	public static class ToggleEnabledAction extends BuildTargetsActionGroup.AbstractBuildTargetAction {
	
		public ToggleEnabledAction(BuildTargetElement buildTargetElement) {
			super(buildTargetElement, BuildManagerMessages.TITLE_ToggleEnabledAction, Action.AS_CHECK_BOX);
			
			setChecked(buildTarget.isEnabled());
		}
		
		@Override
		public void doRun() throws StatusException {
			getBuildInfo().changeEnable(buildTarget, isChecked());
		}
		
	}
	
	public static class RunBuildTargetAction extends AbstractBuildTargetAction {
		public RunBuildTargetAction(BuildTargetElement buildTargetElement) {
			super(buildTargetElement, BuildManagerMessages.TITLE_RunBuildTargetAction);
		}
		
		@Override
		public void run() {
			new Job(MessageFormat.format(BuildManagerMessages.INFO_BuildTargetAction, buildTarget.getTargetName())) {
				
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						new CoreOperationAdapter() {
							@Override
							public void doRun(IProgressMonitor pm) 
									throws CommonException, CoreException, OperationCancellation {
								doOperation(pm);
							}
						}.coreRun(monitor);
						
					} catch(CoreException ce) {
						/* FIXME: report error to user*/
						LangCore.logStatus(ce);
					} catch(OperationCancellation e) {
						return Status.CANCEL_STATUS;
					}
					
					return Status.OK_STATUS;
				}
				
			}
			.schedule();
		}
		
		protected void doOperation(IProgressMonitor pm) throws CoreException, CommonException, OperationCancellation {
			IProject project = getBuildInfo().getProject();
			OperationInfo opInfo = new OperationInfo(project, true, "");
			buildTarget.getToolManager().notifyOperationStarted(opInfo);
			buildTarget.newBuildTargetOperation(opInfo, project, false)
				.execute(pm);
		}
		
	}
	
}