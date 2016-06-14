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

import melnorme.lang.ide.ui.utils.operations.BasicUIOperation;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.OperationCallable;
import melnorme.utilbox.fields.IField;

public class SetFieldValueOperation<T> extends BasicUIOperation {
	
	protected final IField<T> field;
	protected final OperationCallable<T> newValueCallable;
	
	public SetFieldValueOperation(IField<T> field, OperationCallable<T> callable) {
		this.field = assertNotNull(field);
		this.newValueCallable = assertNotNull(callable);
	}
	
	@Override
	public void execute() throws CommonException, OperationCancellation {
		T result = getNewFieldValue();
		if(result != null) {
			field.setFieldValue(result);
		}
	}
	
	protected T getNewFieldValue() throws CommonException, OperationCancellation {
		return newValueCallable.call();
	}
	
}