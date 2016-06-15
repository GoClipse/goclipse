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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.utilbox.status.IStatusMessage;
import melnorme.utilbox.status.Severity;

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
	
	public DATA getResultData() {
		return resultData;
	}
	
	public IStatusMessage getStatusMessage() {
		return statusMessage;
	}
	
	public String getStatusMessageText() {
		if(getStatusMessage() == null) {
			return null;
		} else {
			return getStatusMessage().getMessage();
		}
	}
	
	public boolean isValidResult() {
		return resultData != null;
	}
	
	public DATA getValidResult() throws StatusValidation {
		if(!isValidResult()) {
			throw new StatusValidation(statusMessage.getSeverity(), statusMessage.getMessage());
		}
		return resultData;
	}
	
	@SuppressWarnings("serial")
	public static class StatusValidation extends Throwable implements IStatusMessage {
		
		protected final Severity severity;
		
		public StatusValidation(String message) {
			this(Severity.ERROR, message);
		}
		
		public StatusValidation(Severity severity, String message) {
			super(message, null);
			this.severity = assertNotNull(severity);
		}
		
		@Override
		public Severity getSeverity() {
			return severity;
		}
		
		@Override
		public String getMessage() {
			return super.getMessage();
		}
		
	}
	
}