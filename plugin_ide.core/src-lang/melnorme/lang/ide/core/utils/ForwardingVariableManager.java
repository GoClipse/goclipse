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
package melnorme.lang.ide.core.utils;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IStringVariable;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.IValueVariableListener;
import org.eclipse.core.variables.VariablesPlugin;

public class ForwardingVariableManager implements IStringVariableManager {
	
	protected final IStringVariableManager parentVarMgr;
	
	public ForwardingVariableManager() {
		this(VariablesPlugin.getDefault().getStringVariableManager());
	}
	
	public ForwardingVariableManager(IStringVariableManager parentVarMgr) {
		this.parentVarMgr = assertNotNull(parentVarMgr);
	}
	
	@Override
	public IStringVariable[] getVariables() {
		return parentVarMgr.getVariables();
	}
	
	@Override
	public IValueVariable[] getValueVariables() {
		return parentVarMgr.getValueVariables();
	}
	
	@Override
	public IValueVariable getValueVariable(String name) {
		return parentVarMgr.getValueVariable(name);
	}
	
	@Override
	public IDynamicVariable[] getDynamicVariables() {
		return parentVarMgr.getDynamicVariables();
	}
	
	@Override
	public IDynamicVariable getDynamicVariable(String name) {
		return parentVarMgr.getDynamicVariable(name);
	}
	
	@Override
	public String getContributingPluginId(IStringVariable variable) {
		return parentVarMgr.getContributingPluginId(variable);
	}
	
	@Override
	public String performStringSubstitution(String expression) throws CoreException {
		return parentVarMgr.performStringSubstitution(expression);
	}
	
	@Override
	public String performStringSubstitution(String expression, boolean reportUndefinedVariables) throws CoreException {
		return parentVarMgr.performStringSubstitution(expression, reportUndefinedVariables);
	}
	
	@Override
	public void validateStringVariables(String expression) throws CoreException {
		parentVarMgr.validateStringVariables(expression);
	}
	
	@Override
	public IValueVariable newValueVariable(String name, String description) {
		return parentVarMgr.newValueVariable(name, description);
	}
	
	@Override
	public IValueVariable newValueVariable(String name, String description, boolean readOnly, String value) {
		return parentVarMgr.newValueVariable(name, description, readOnly, value);
	}
	
	@Override
	public void addVariables(IValueVariable[] variables) throws CoreException {
		parentVarMgr.addVariables(variables);
	}
	
	@Override
	public void removeVariables(IValueVariable[] variables) {
		parentVarMgr.removeVariables(variables);
	}
	
	@Override
	public void addValueVariableListener(IValueVariableListener listener) {
		parentVarMgr.addValueVariableListener(listener);
	}
	
	@Override
	public void removeValueVariableListener(IValueVariableListener listener) {
		parentVarMgr.removeValueVariableListener(listener);
	}
	
	@Override
	public String generateVariableExpression(String varName, String arg) {
		return parentVarMgr.generateVariableExpression(varName, arg);
	}
	
}