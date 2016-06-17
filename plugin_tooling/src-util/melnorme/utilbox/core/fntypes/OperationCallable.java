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
package melnorme.utilbox.core.fntypes;

import java.util.concurrent.Callable;

import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public interface OperationCallable<RET> extends Callable<RET> {
	
	@Override
	RET call() throws CommonException, OperationCancellation;
	
	default CommonResult<RET> callToResult() {
		try {
			return new CommonResult<>(call());
		} catch(CommonException e) {
			return new CommonResult<>(null, e);
		} catch(OperationCancellation e) {
			return new CommonResult<>(null, e);
		}
	}
	
	default SupplierExt<CommonResult<RET>> toResultSupplier() {
		return this::callToResult;
	}
	
	default ResultRunnable<CommonResult<RET>> toResultRunnable() {
		SupplierExt<CommonResult<RET>> supplier = this.toResultSupplier();
		return new ResultRunnable<CommonResult<RET>>() {
			@Override
			public CommonResult<RET> call() {
				return supplier.get();
			}
		};
	}
	
}