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
package melnorme.utilbox.process;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

import melnorme.utilbox.concurrency.IRunnableFuture2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.Result;
import melnorme.utilbox.misc.StringUtil;


public interface IExternalProcessHandler {
	
	Process getProcess();
	
	/* ----------------- stdout and stderr methods ----------------- */
	
	IRunnableFuture2<? extends Result<?, ?>> getStdOutTask();
	
	IRunnableFuture2<? extends Result<?, ?>> getStdErrTask();
	
	/*----------  Termination awaiting functionality ----------*/
	
	public static final int NO_TIMEOUT = -1;
	
	default void awaitTermination(boolean destroyOnError)
			throws InterruptedException, OperationCancellation, IOException {
		try {
			awaitTermination(NO_TIMEOUT, destroyOnError);
		} catch(TimeoutException e) {
			throw assertFail();
		}
	}
	
	void awaitTermination(int timeoutMs, boolean destroyOnError)
			throws InterruptedException, TimeoutException, OperationCancellation, IOException;
	
	/* -----------------  Writing to the process input  ----------------- */
	
	void writeInput(String input, Charset charset) throws IOException;
	
	default void writeInput(String input) throws IOException {
		writeInput(input, StringUtil.UTF8);
	}
	
	default void writeInput_(String input) throws CommonException {
		writeInput_(input, StringUtil.UTF8);
	}
	
	default void writeInput_(String input, Charset charset) throws CommonException {
		try {
			writeInput(input, charset);
		} catch (IOException e) {
			throw new CommonException(ProcessHelperMessages.ExternalProcess_ErrorWritingInput, e);
		}
	}
	
}