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
package melnorme.lang.ide.ui.build;

import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import melnorme.lang.ide.core.operations.build.VariablesResolver;
import melnorme.lang.ide.ui.fields.FieldDialog;
import melnorme.lang.ide.ui.utils.ControlUtils;
import melnorme.lang.tooling.commands.CommandInvocation;
import melnorme.lang.tooling.commands.EnvironmentSettings;
import melnorme.util.swt.components.ButtonWidget;
import melnorme.util.swt.components.CompositeWidget;
import melnorme.util.swt.components.fields.EnablementCompositeWidget;
import melnorme.util.swt.components.fields.SetFieldValueOperation;
import melnorme.util.swt.components.fields.TextFieldWidget;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.CommonGetter;
import melnorme.utilbox.fields.Field;
import melnorme.utilbox.status.StatusException;

public class CommandInvocationEditor extends EnablementCompositeWidget<CommandInvocation> {
	
	protected final Field<CommandInvocation> commandInvocation;

	protected final CommonGetter<CommandInvocation> getDefaultArguments;
	protected final VariablesResolver variablesResolver;
	
	protected final TextFieldWidget commandArgumentsField;
	
	public CommandInvocationEditor(CommonGetter<CommandInvocation> getDefaultCommandInvocation,
			VariablesResolver variablesResolver) {
		super("Command Invocation:", LABEL_UseDefault);
		this.createInlined = false;
		
		this.commandInvocation = field();
		
		this.getDefaultArguments = getDefaultCommandInvocation;
		this.variablesResolver = variablesResolver;
		
		commandArgumentsField = init_createButtonTextField();
		commandArgumentsField.addFieldValidationX(true, this::validateArguments);
		commandArgumentsField.onlyValidateWhenEnabled = false;
		addChildWidget(commandArgumentsField);
		commandArgumentsField.addChangeListener(this::updateCommandInvocationField);
		
		createButtonArea();
		
		commandInvocation.addChangeListener(this::updateWidgetFromInput);
	}
	
	public TextFieldWidget getCommandArgumentsWidget() {
		return commandArgumentsField;
	}
	
	@Override
	protected void doUpdateWidgetFromInput() {
		CommandInvocation commandInvocation = this.commandInvocation.get();
		if(commandInvocation == null) {
			commandArgumentsField.set("");
			return; // Ignore
		}
		commandArgumentsField.set(commandInvocation.getCommandArguments());
	}
	
	protected void updateCommandInvocationField() {
		CommandInvocation current = commandInvocation.get();
		if(current == null) {
			return;
		}
		commandInvocation.set(new CommandInvocation(
			commandArgumentsField.get(), 
			current.getEnvironmentVars(), 
			current.isAppendEnvironment()
		));
	}
	
	@Override
	protected CommandInvocation getDefaultFieldValue() throws CommonException {
		return getDefaultArguments.get();
	}
	
	/* ----------------- controls & layout ----------------- */
	
	protected void createButtonArea() {
		ButtonWidget environmentVarButton = new ButtonWidget("Edit Environment variables...", 
			this::handleEditEnvironmentVars);
		ButtonWidget variablesDialogButton = new ButtonWidget("Insert/Edit Substitution Variables...", 
			new SetFieldValueOperation<String>(commandArgumentsField, this::newValueFromCommandVariablesDialog));
		
		CompositeWidget buttonArea = addChildWidget(new CompositeWidget(false) {
			
			{ layoutColumns = 2; }
			
			@Override
			protected void createContents(Composite topControl) {
				super.createContents(topControl);
				
				GridData layoutData = (GridData) variablesDialogButton.getButton().getLayoutData();
				layoutData.grabExcessHorizontalSpace = true;
				layoutData.horizontalAlignment = GridData.END;
			}
		});
		buttonArea.addChildWidget(environmentVarButton);
		buttonArea.addChildWidget(variablesDialogButton);
	}
	
	protected TextFieldWidget init_createButtonTextField() {
		TextFieldWidget buttonTextField = new TextFieldWidget(null) {
			@Override
			protected void createContents_layout() {
				text.setLayoutData(gdGrabAll(40, 100));
			}
		};
		buttonTextField.setMultiLineStyle();
		return buttonTextField;
	}
	
	/* -----------------  ----------------- */
	
	protected void validateArguments() throws StatusException {
		CommandInvocation fieldValue = getFieldValue();
		if(fieldValue == null) {
			return;
		}
		
		fieldValue.validate(variablesResolver);
	}
	
	/* ----------------- button handlers ----------------- */
	
	protected String newValueFromCommandVariablesDialog() throws OperationCancellation {
		StringVariableSelectionDialog variablesDialog = new StringVariableSelectionDialog(
			commandArgumentsField.getFieldControl().getShell());
		
		variablesDialog.setElements(variablesResolver.getVariables());
		
		String addedVar = ControlUtils.openStringVariableSelectionDialog(variablesDialog);
		return commandArgumentsField.getFieldValue() + addedVar;
	}
	
	protected void handleEditEnvironmentVars() {
		Shell shell = getCommandArgumentsWidget().getFieldControl().getShell();
		
		CommandInvocation cmd = getFieldValue();
		EnvironmentSettings envSettings = new EnvironmentSettings(
			cmd.getEnvironmentVars().copyToHashMap(), cmd.isAppendEnvironment());
		
		try {
			envSettings = new FieldDialog<>(shell, new EnvironmentSettingsEditor()).openDialog(envSettings);
		} catch(OperationCancellation e) {
			return;
		}
		
		setFieldValue(new CommandInvocation(
			cmd.getCommandArguments(),
			envSettings.envVars.copy(),
			envSettings.appendEnv
		));
	}
	
}