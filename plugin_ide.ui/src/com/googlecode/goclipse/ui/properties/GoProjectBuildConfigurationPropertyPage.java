/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.core.operations.build.VariablesResolver;
import melnorme.lang.ide.ui.build.BuildTargetEditor;
import melnorme.lang.ide.ui.build.CommandInvocationEditor;
import melnorme.lang.ide.ui.preferences.ProjectBuildConfigurationComponent;
import melnorme.lang.ide.ui.preferences.pages.BuildConfigurationPropertyPage;
import melnorme.util.swt.components.misc.StatusMessageWidget;
import melnorme.utilbox.status.Severity;

public class GoProjectBuildConfigurationPropertyPage extends BuildConfigurationPropertyPage {
	
	@Override
	protected ProjectBuildConfigurationComponent createProjectConfigWidget(IProject project) {
		return new GoProjectOptionsBlock(project);
	}
	
	public class GoProjectOptionsBlock extends ProjectBuildConfigurationComponent {
		
		public static final String BUILD_COMMAND_OUTPUT_FORMAT_MESSAGE = 
				"For GoClipse to extract error/warning messages from the output of this command, " +
				"the output must be in the same format of either the `go build` or `gometalinter` command.";
		
		public GoProjectOptionsBlock(IProject project) {
			super(project);
		}
		
		@Override
		protected BuildTargetEditor init_createBuildTargetSettingsComponent() {
			return new BuildTargetEditor(
				getBuildManager(),
				true,
				this::getDefaultBuildCommand, 
				this::getDefaultExecutablePath
			) {
				@Override
				protected CommandInvocationEditor init_createArgumentsField() {
					VariablesResolver varResolver = buildManager.getToolManager().getVariablesManager(null);
					StatusMessageWidget statusWidget = new StatusMessageWidget() {
						
						@Override
						protected void createContents(Composite topControl) {
							super.createContents(topControl);
							((GridData) hintText.getLayoutData()).widthHint = 250;
						}
						
						@Override
						public void updateWidgetFromInput() {
							setStatusMessage(Severity.INFO, BUILD_COMMAND_OUTPUT_FORMAT_MESSAGE);
						}
					};
					BuildCommandEditor buildCommandEditor = new BuildCommandEditor(getDefaultBuildCommand, varResolver);
					buildCommandEditor.addChildWidget(statusWidget);
					return buildCommandEditor;
				}
			};
		}
		
	}
	
}