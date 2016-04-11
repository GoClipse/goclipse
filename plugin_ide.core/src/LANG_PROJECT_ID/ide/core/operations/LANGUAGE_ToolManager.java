/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package LANG_PROJECT_ID.ide.core.operations;

import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.tooling.data.LANGUAGE_SDKLocationValidator;
import melnorme.lang.tooling.ops.util.PathValidator;

public class LANGUAGE_ToolManager extends ToolManager {
	
	@Override
	public PathValidator getSDKToolPathValidator() {
		return new LANGUAGE_SDKLocationValidator();
	}
	
}