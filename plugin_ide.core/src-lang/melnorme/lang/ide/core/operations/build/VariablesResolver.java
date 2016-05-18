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
package melnorme.lang.ide.core.operations.build;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.text.MessageFormat;
import java.util.function.Supplier;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IStringVariable;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.IValueVariable;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.ForwardingVariableManager;
import melnorme.lang.ide.core.utils.StringSubstitutionEngine;
import melnorme.lang.tooling.data.validation.Validator;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.HashMap2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.status.StatusException;

public class VariablesResolver {
	
	protected final IStringVariableManager parentVarMgr;
	protected final OverlayVariableManager varMgr;
	
	public VariablesResolver(IStringVariableManager parentVarMgr) {
		this.parentVarMgr = assertNotNull(parentVarMgr);
		this.varMgr = new OverlayVariableManager(parentVarMgr);
	}
	
	public String performStringSubstitution(String expression) throws CommonException {
		try {
			return varMgr.performStringSubstitution(expression, true);
		} catch(CoreException ce) {
			throw LangCore.createCommonException(ce);
		}
	}
	
	public OverlayVariableManager getVariableManager() {
		return varMgr;
	}
	
	public Object[] getVariables() {
		return varMgr.getVariables();
	}
	
	public void putDynamicVar(IDynamicVariable dynamicVariable) {
		varMgr.putDynamicVar(dynamicVariable);
	}
	
	public void removeDynamicVar(String dynamicVariableName) {
		varMgr.removeDynamicVar(dynamicVariableName);
	}
	
	public static String variableRefString(String varName) {
		return "${" + varName + "}";
	}
	
	public static class OverlayVariableManager extends ForwardingVariableManager {
		
		protected final HashMap2<String, IDynamicVariable> dynamicVariables = new HashMap2<>();
		
		public OverlayVariableManager(IStringVariableManager parentVarMgr) {
			super(parentVarMgr);
		}
		
		@Override
		public IValueVariable getValueVariable(String name) {
			return super.getValueVariable(name);
		}
		
		@Override
		public IValueVariable[] getValueVariables() {
			return super.getValueVariables();
		}
		
		@Override
		public IDynamicVariable getDynamicVariable(String name) {
			IDynamicVariable variable = dynamicVariables.get(name);
			if(variable != null) {
				return variable;
			}
			
			return super.getDynamicVariable(name);
		}
		
		@Override
		public IDynamicVariable[] getDynamicVariables() {
			HashMap2<String, IDynamicVariable> newMap = dynamicVariables.copyToHashMap();
			
			IDynamicVariable[] parentVars = super.getDynamicVariables();
			for (IDynamicVariable parentVar : parentVars) {
				// Do not put vars that have same name  
				newMap.putIfAbsent(parentVar.getName(), parentVar);
			}
			return newMap.getValuesView().toArray(IDynamicVariable.class);
		}
		
		@Override
		public IStringVariable[] getVariables() {
			IValueVariable[] valueVars = getValueVariables();
			IDynamicVariable[] dynVars = getDynamicVariables();
			
			ArrayList2<IStringVariable> variables = new ArrayList2<>(valueVars.length + dynVars.length);
			variables.addElements(dynVars);
			variables.addElements(valueVars);
			return variables.toArray(IStringVariable.class);
		}
		
		/* -----------------  ----------------- */
		
		public void putDynamicVar(IDynamicVariable dynamicVariable) {
			dynamicVariables.put(dynamicVariable.getName(), dynamicVariable);
		}
		
		public void removeDynamicVar(String dynamicVariableName) {
			dynamicVariables.remove(dynamicVariableName);
		}
		
		/* -----------------  ----------------- */
		
		@Override
		public String performStringSubstitution(String expression, boolean reportUndefinedVariables)
				throws CoreException {
			return new StringSubstitutionEngine().performStringSubstitution(
				expression, reportUndefinedVariables, true, this);
		}
		
	}
	
	/* -----------------  ----------------- */
	
	public static class SupplierAdapterVar implements IDynamicVariable {
		
		protected final String name;
		protected final String description;
		protected final Supplier<String> value;
		protected final Validator<String, String> validator;
		
		public SupplierAdapterVar(String name, String description, Supplier<String> value) {
			this(name, description, value, null);
		}
		
		public SupplierAdapterVar(String name, String description, Supplier<String> value,
				Validator<String, String> validator) {
			this.name = assertNotNull(name);
			this.description = assertNotNull(description);
			this.value = assertNotNull(value);
			
			this.validator = validator;
		}
		
		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String getDescription() {
			return description;
		}
		
		@Override
		public boolean supportsArgument() {
			return false;
		}
		
		@Override
		public String getValue(String argument) throws CoreException {
			if(argument != null) {
				throw LangCore.createCoreException(
					MessageFormat.format("Variable {0} does not accept arguments.", getName()) , null);
			}
			if(validator != null) {
				try {
					return validator.validateField(value.get());
				} catch(StatusException e) {
					String msg = MessageFormat.format("Variable {0} error: {1}", getName(),  e.getMessage());
					throw LangCore.createCoreException(msg, e.getCause());
				}
			}
			return value.get();
		}
	}
	
}