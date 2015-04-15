/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling;

import java.text.MessageFormat;

/** Only {@link ToolingMessages} should refer to this class. */
class ToolingMessages_Default {
	
	public static String FIND_DEFINITION_NoTargetFound = "No target found.";
	
	public static String FIND_DEFINITION_ToolError = "Error: ";
	
	public static String TOOLS_ExitedWithNonZeroStatus(int exitValue) {
		return MessageFormat.format("Tool exited with non-zero status: {0}", exitValue);
	}
	
}