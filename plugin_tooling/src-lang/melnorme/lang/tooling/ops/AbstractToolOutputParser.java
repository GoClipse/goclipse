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
package melnorme.lang.tooling.ops;


import melnorme.lang.tooling.ToolingMessages;
import melnorme.lang.utils.ParseHelper;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class AbstractToolOutputParser<RESULT> extends ParseHelper {
	
	public AbstractToolOutputParser() {
		super();
	}
	
	public RESULT parse(ExternalProcessResult result) throws CommonException {
		if(result.exitValue != 0) {
			throw new CommonException(ToolingMessages.TOOLS_ExitedWithNonZeroStatus(result.exitValue));
		}
		
		return doParse(result);
	}

	protected RESULT doParse(ExternalProcessResult result) throws CommonException {
		return parse(result.getStdOutBytes().toString(StringUtil.UTF8));
	}
	
	protected abstract RESULT parse(String input) throws CommonException;
	
	protected void handleLineParseError(CommonException ce) throws CommonException {
		 throw ce;
	}
	
}