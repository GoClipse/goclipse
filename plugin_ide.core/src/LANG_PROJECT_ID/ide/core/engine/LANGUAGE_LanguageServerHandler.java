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
package LANG_PROJECT_ID.ide.core.engine;

import java.nio.file.Path;

import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.engine.LanguageServerHandler;
import melnorme.lang.ide.core.engine.LanguageServerInstance;
import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.common.ops.JobExecutor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class LANGUAGE_LanguageServerHandler extends LanguageServerHandler<LanguageServerInstance> {
	
	public LANGUAGE_LanguageServerHandler(JobExecutor jobExecutor, ToolManager toolMgr) {
		super(jobExecutor, toolMgr);
	}
	
	@Override
	protected LanguageServerInstance doCreateServerInstance(IOperationMonitor om)
			throws CommonException, OperationCancellation {
		Path serverPath = getServerPath();
		return new LanguageServerInstance(serverPath, null) {
			
			@Override
			protected String getLanguageServerName() {
				return LangCore_Actual.LANGUAGE_SERVER_Name;
			}
		};
	}
}