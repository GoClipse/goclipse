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
package melnorme.lang.tooling.data;

import melnorme.lang.tooling.data.IStatusMessage;
import melnorme.lang.tooling.data.IValidationSource;
import melnorme.utilbox.fields.IFieldView;


public interface IStatusFieldSource extends IValidationSource {
	
	@Override
	public default IStatusMessage getValidationStatus() {
		return getStatusField().getFieldValue();
	}
	
	IFieldView<IStatusMessage> getStatusField();
	
}