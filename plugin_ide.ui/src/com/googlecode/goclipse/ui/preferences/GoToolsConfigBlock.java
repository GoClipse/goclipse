/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.googlecode.goclipse.core.GoToolPreferences;
import com.googlecode.goclipse.ui.editor.actions.GoFmtOperation;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.ide.ui.preferences.AbstractToolLocationGroup;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;
import melnorme.lang.ide.ui.preferences.pages.DownloadToolTextField;
import melnorme.lang.ide.ui.preferences.pages.LanguageToolsBlock;
import melnorme.lang.ide.ui.utils.ControlUtils;
import melnorme.lang.ide.ui.utils.operations.BasicUIOperation;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.IDisableableWidget;
import melnorme.util.swt.components.fields.ButtonTextField;
import melnorme.util.swt.components.fields.CheckBoxField;
import melnorme.util.swt.components.fields.EnablementButtonTextField2;
import melnorme.util.swt.components.fields.FileTextField;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.fields.Field;
import melnorme.utilbox.misc.Location;

public class GoToolsConfigBlock extends LanguageToolsBlock {
	
	public GoToolsConfigBlock(PreferencesPageContext prefContext) {
		super(prefContext);
		
		addChildWidget(new GoGuruGroup());
		addChildWidget(new GoDefGroup());
		addChildWidget(new GoFmtGroup());
	}
	
	@Override
	protected EngineToolGroup init_createEngineToolGroup() {
		return new EngineToolGroup() {
			
			@Override
			protected ButtonTextField initToolLocationField() {
				return new DownloadToolTextField() {
					@Override
					protected BasicUIOperation getDownloadButtonHandler() {
						return new Start_GoInstallJob_Operation("Download gocode", "Downloading gocode...", 
							this,
							"github.com/nsf/gocode",
							"gocode") {
						};
					}
				};
			}
			
		};
	}
	
	public class GoGuruGroup extends AbstractToolLocationGroup {
		public GoGuruGroup() {
			super("guru");
			
			bindFieldToDerivedPreference(toolLocationField, GoToolPreferences.GO_GURU_Path);
		}
		
		@Override
		protected BasicUIOperation do_getDownloadButtonHandler(DownloadToolTextField toolLocationField) {
			return new Start_GoInstallJob_Operation("Download guru", "Downloading guru...", 
				toolLocationField,
				"golang.org/x/tools/cmd/guru",
				"guru") {
			};
		}
	};
	
	public class GoDefGroup extends AbstractToolLocationGroup {
		
		public GoDefGroup() {
			super("godef");
			
			bindFieldToDerivedPreference(toolLocationField, GoToolPreferences.GODEF_Path);
		}
		
		@Override
		protected BasicUIOperation do_getDownloadButtonHandler(DownloadToolTextField toolLocationField) {
			return new Start_GoInstallJob_Operation("Download godef", "Downloading godef...", 
				toolLocationField,
				"github.com/rogpeppe/godef",
				"godef") {
			};
		}
	};
	
	public class GoFmtGroup extends EnablementButtonTextField2 {
		
		protected final CheckBoxField formatOnSaveField;
		
		public GoFmtGroup() {
			super("gofmt:", "Use default location (from Go installation).");
			
			prefContext.bindToPreference(asEffectiveValueProperty(), GoToolPreferences.GOFMT_Path.getPreference());
			buttonTextField.addFieldValidator(true, GoToolPreferences.GOFMT_Path.getValidator());
			
			this.formatOnSaveField = new CheckBoxField("Format automatically on editor save.");
			
			this.addChildWidget(formatOnSaveField);
			prefContext.bindToPreference(this.formatOnSaveField, ToolchainPreferences.FORMAT_ON_SAVE);
		}
		
		@Override
		protected ButtonTextField init_createButtonTextField(Field<String> field) {
			return new ButtonTextField(field, "Executable:", FileTextField.DEFAULT_BUTTON_LABEL) {
				@Override
				protected String getNewValueFromButtonSelection() throws OperationCancellation {
					return ControlUtils.openFileDialog(getFieldValue(), button.getShell());
				}
			};
		}
		
		@Override
		protected String getDefaultFieldValue() throws CommonException {
			Location rootLoc = Location.create(LangCore.settings().SDK_LOCATION.getGlobalPreference().get());
			return GoFmtOperation.getGofmtLocationFromGoRoot(rootLoc).toPathString();
		}
		
		@Override
		protected void updateChildrenEnablement() {
			for (IDisableableWidget childWidget : getChildWidgets()) {
				if(childWidget == enablementField || childWidget == formatOnSaveField) {
					continue;
				}
				childWidget.setEnabled(!isUseDefault());
			}
		}
		
	}
	
	@Override
	protected void createContents(Composite topControl) {
		
		SWTFactoryUtil.createLabel(topControl, SWT.NONE, "Note: if you've made any changes "
				+ "in the Go SDK preferences, \n"
				+ "make sure you press \"Apply\" in that page before using the `Download...` button.",
				createSubComponentDefaultGridData());
		
		super.createContents(topControl);
	}
	
}