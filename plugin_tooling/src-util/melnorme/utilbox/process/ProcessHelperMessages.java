/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.process;

public interface ProcessHelperMessages {
	
	public static final String ExternalProcess_CouldNotStart =
			"Could not start process: ";
	public static final String ExternalProcess_ErrorStreamReaderIOException =
			"Error reading process stream.";
	public static final String ExternalProcess_TaskCancelled =
			"Task cancelled, process forcibly terminated.";
	public static final String ExternalProcess_ProcessTimeout =
			"Timeout awaiting for process.";
	
	public static final String ExternalProcess_ErrorWritingInput =
			"Error writing to process input.";
	
}