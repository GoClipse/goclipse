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
package melnorme.lang.tooling.toolchain.ops;

import melnorme.utilbox.status.IStatusMessage;
import melnorme.utilbox.status.StatusException;

/**
 * Result for a tool operation. 
 * An exception result should be only be used for non-critical failures, that is,
 * only if the tool was successfully started but could not complete the operation 
 * due to an error in the data being analyzed.
 * 
 * Critical failures, such as the tool not being found in the first place, or the tool crashing, 
 * these should be handled with a different class.
 *
 */
public class ToolResponse<DATA> {
	
	protected final DATA resultData; // can be null
	protected final IStatusMessage statusMessage;
	
	public ToolResponse(DATA resultValue) {
		this(resultValue, null);
	}
	
	public ToolResponse(DATA resultData, IStatusMessage statusMessaage) {
		this.resultData = resultData;
		this.statusMessage = statusMessaage;
	}
	
	public DATA getValidResult() throws StatusException {
		if(statusMessage != null) {
			throw statusMessage.toStatusException();
		}
		return resultData;
	}
	
	public IStatusMessage getStatusMessage() {
		return statusMessage;
	}
	
}