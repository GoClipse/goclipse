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
package melnorme.util.swt.components.fields;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.runtime.CoreException;

import melnorme.lang.ide.core.utils.operation.CommonOperationCallable;
import melnorme.lang.ide.ui.utils.operations.BasicUIOperation;
import melnorme.util.swt.components.FieldWidget;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class SetFieldValueOperation<T> extends BasicUIOperation {
	
	protected final FieldWidget<T> field;
	protected final CommonOperationCallable<T> newValueCallable;
	
	public SetFieldValueOperation(FieldWidget<T> field, CommonOperationCallable<T> callable) {
		this.field = assertNotNull(field);
		this.newValueCallable = assertNotNull(callable);
	}
	
	@Override
	protected void doOperation() throws CoreException, CommonException, OperationCancellation {
		T result = getNewFieldValue();
		if(result != null) {
			field.setFieldValue(result);
		}
	}
	
	protected T getNewFieldValue() throws CoreException, CommonException, OperationCancellation {
		return newValueCallable.call();
	}
	
}