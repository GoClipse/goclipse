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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.layout.GridData;

import melnorme.lang.ide.core.operations.build.CommandInvocation;
import melnorme.lang.ide.core.operations.build.VariablesResolver;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.utils.ControlUtils;
import melnorme.lang.tooling.data.StatusException;
import melnorme.util.swt.components.AbstractCompositeWidget;
import melnorme.util.swt.components.fields.EnablementButtonTextField;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.CommonGetter;
import melnorme.utilbox.fields.IFieldValueListener;

public class CommandInvocationEditor extends AbstractCompositeWidget {
	
	protected final EnablementButtonTextField commandArgumentsField;
	protected final CommonGetter<String> getDefaultArguments;
	protected final VariablesResolver variablesResolver;
	
	public CommandInvocationEditor(CommonGetter<String> getDefaultCommandArguments,
			VariablesResolver variablesResolver) {
		super(false);
		
		this.getDefaultArguments = getDefaultCommandArguments;
		this.variablesResolver = variablesResolver;
		
		commandArgumentsField = new CommandArgumentsField();
		commandArgumentsField.setLabelText("Command Invocation");
		commandArgumentsField.setMultiLineStyle();
		
		addSubComponent(commandArgumentsField);
		
		IValidationSourceX validationSource = this::validateArguments;
		validation.addFieldValidation(true, commandArgumentsField, validationSource);
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 1;
	}
	
	protected void validateArguments() throws StatusException {
		String commandArguments = commandArgumentsField.getEffectiveFieldValue();
		if(commandArguments == null) {
			try {
				commandArguments = getDefaultArguments.get();
			} catch(CommonException e) {
				throw e.toStatusException();
			}
		}
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
	
	public class CommandArgumentsField extends EnablementButtonTextField {

		public CommandArgumentsField() {
			super(LangUIMessages.Fields_CommandArguments, LABEL_UseDefault, LangUIMessages.Fields_VariablesButtonLabel);
		}
		
		@Override
		public int getPreferredLayoutColumns() {
			return 1;
		}
		
		@Override
		protected void createContents_layout() {
			text.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).hint(40, 100).create());
			button.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		}
		
		@Override
		protected void doSetEnabled(boolean enabled) {
			super.doSetEnabled(enabled);
		}
		
		@Override
		protected String getNewValueFromButtonSelection2() throws CoreException, CommonException, OperationCancellation {
			StringVariableSelectionDialog variablesDialog = new StringVariableSelectionDialog(text.getShell());
			
			variablesDialog.setElements(variablesResolver.getVariables());
			
			String addedVar = ControlUtils.openStringVariableSelectionDialog(variablesDialog);
			return getFieldValue() + addedVar;
		}
		
		@Override
		protected String getDefaultFieldValue() throws CommonException {
			return getDefaultArguments.get();
		}
	}
	
	public EnablementButtonTextField getCommandArgumentsField() {
		return commandArgumentsField;
	}
	
	public void addListener(IFieldValueListener listener) {
		commandArgumentsField.addListener(listener);
	}
	
	public String getEffectiveFieldValue() {
		return commandArgumentsField.getEffectiveFieldValue();
	}
	
	public void setEffectiveFieldValue(String commandArguments) {
		commandArgumentsField.setEffectiveFieldValue(commandArguments);
	}
	
}