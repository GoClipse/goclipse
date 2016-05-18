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
package melnorme.lang.tooling.ops;


import melnorme.lang.utils.parse.StringParseSource;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class AbstractToolOutputParser2<RESULT> implements ToolOutputParseHelper {
	
	public AbstractToolOutputParser2() {
		super();
	}
	
	public RESULT parseResult(ExternalProcessResult result) throws CommonException, OperationCancellation {
		return parse(result.getStdOutBytes().toString(StringUtil.UTF8));
	}
	
	/* -----------------  ----------------- */
	
	public RESULT parse(String outputSource) throws CommonException {
		return parse(new StringParseSource(outputSource));
	}
	
	protected abstract RESULT parse(StringParseSource outputParseSource) throws CommonException;
	
	protected abstract void handleParseError(CommonException ce) throws CommonException;
	
}