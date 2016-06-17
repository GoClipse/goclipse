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
package melnorme.utilbox.core.fntypes;

import java.util.function.Supplier;

public interface SupplierExt<RET> extends Supplier<RET>, CallableX<RET, RuntimeException> {
	
	@Override
	default RET call() throws RuntimeException {
		return get();
	}
	
}