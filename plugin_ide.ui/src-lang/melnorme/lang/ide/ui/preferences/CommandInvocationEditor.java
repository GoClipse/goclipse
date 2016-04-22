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
package melnorme.lang.ide.ui.preferences;

import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import melnorme.lang.ide.core.operations.build.CommandInvocation;
import melnorme.lang.ide.core.operations.build.VariablesResolver;
import melnorme.lang.ide.ui.utils.ControlUtils;
import melnorme.lang.tooling.data.StatusException;
import melnorme.util.swt.components.ButtonWidget;
import melnorme.util.swt.components.CompositeWidget;
import melnorme.util.swt.components.fields.EnablementCompositeWidget;
import melnorme.util.swt.components.fields.SetFieldValueOperation;
import melnorme.util.swt.components.fields.TextFieldWidget;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.CommonGetter;
import melnorme.utilbox.fields.Field;
import melnorme.utilbox.fields.FieldValueListener.FieldChangeListener;

public class CommandInvocationEditor extends EnablementCompositeWidget<CommandInvocation> {
	
	protected final Field<CommandInvocation> commandInvocation;

	protected final CommonGetter<String> getDefaultArguments;
	protected final VariablesResolver variablesResolver;
	
	protected final TextFieldWidget commandArgumentsField;
	
	public CommandInvocationEditor(CommonGetter<String> getDefaultCommandArguments,
			VariablesResolver variablesResolver) {
		super("Command Invocation:", LABEL_UseDefault);
		this.createInlined = false;
		
		this.commandInvocation = getField();
		
		this.getDefaultArguments = getDefaultCommandArguments;
		this.variablesResolver = variablesResolver;
		
		commandArgumentsField = init_createButtonTextField();
		commandArgumentsField.addFieldValidationX(true, this::validateArguments);
		commandArgumentsField.onlyValidateWhenEnabled = false;
		addChildWidget(commandArgumentsField);
		commandArgumentsField.addChangeListener(this::updateCommandInvocationField);
		
		createButtonArea();
		
		commandInvocation.addChangeListener(this::updateWidgetFromInput);
	}
	
	public TextFieldWidget getButtonTextField() {
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
		commandInvocation.set(new CommandInvocation(commandArgumentsField.get(), variablesResolver));
	}
	
	@Override
	protected CommandInvocation getDefaultFieldValue() throws CommonException {
		return new CommandInvocation(getDefaultArguments.get(), variablesResolver);
	}
	
	/* ----------------- controls & layout ----------------- */
	
	protected void createButtonArea() {
		ButtonWidget environmentVarButton = new ButtonWidget("Edit Environment variables...", 
			this::handleEditEnvironmentVars);
		ButtonWidget variablesDialogButton = new ButtonWidget("Insert/Edit Command Variables...", 
			new SetFieldValueOperation<String>(commandArgumentsField, this::newValueFromCommandVariablesDialog));
		
		/* FIXME:  buttons layout issue */
		CompositeWidget buttonArea = addChildWidget(new CompositeWidget(false) {
			
			{ layoutColumns = 2; }
			
			@Override
			protected void createContents(Composite topControl) {
				super.createContents(topControl);
				
				environmentVarButton.getButton().setLayoutData(
					GridDataFactory.fillDefaults().create());
				
				variablesDialogButton.getButton().setLayoutData(
					gdfGrabHorizontal().align(GridData.END, GridData.CENTER).create());
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
		String commandArguments = this.commandArgumentsField.get();
		if(commandArguments == null) {
			handleNoBuildCommandSupplied();
		}
		doValidate(commandArguments);
	}
	
	protected void handleNoBuildCommandSupplied() throws StatusException {
		throw new StatusException("No command supplied.");
	}
	
	protected void doValidate(String commandArguments) throws StatusException {
		new CommandInvocation(commandArguments, variablesResolver).validate();
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
		Shell shell = getButtonTextField().getFieldControl().getShell();
		/* FIXME: todo EnvironmentVar*/
		MessageDialog.openInformation(shell, "TODO", "NOT Implement");
	}
	
	/* FIXME: review */
	public void addChangeListener(FieldChangeListener listener) {
		commandArgumentsField.addChangeListener(listener);
	}
	
	public String getEffectiveFieldValue1() {
		CommandInvocation effectiveValue = getEffectiveFieldValue();
		return effectiveValue == null ? null : effectiveValue.getCommandArguments();
	}
	
	public void setEffectiveFieldValue1(String buildArguments) {
		setEffectiveFieldValue(
			buildArguments == null ? null : new CommandInvocation(buildArguments, variablesResolver));
		
	}
	
}