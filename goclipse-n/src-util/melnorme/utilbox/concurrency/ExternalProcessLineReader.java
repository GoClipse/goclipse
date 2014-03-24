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
package melnorme.utilbox.concurrency;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * External Process Helper that reads process output in lines, and passes it forward.
 */
public abstract class ExternalProcessLineReader extends ExternalProcessHelper {
	
	protected final Charset charset;
	
	public ExternalProcessLineReader(ProcessBuilder pb, Charset charset) throws IOException {
		super(pb);
		this.charset = charset;
	}
	
	@Override
	protected Runnable createMainReaderTask() {
		return new ReadLineNotifyTask(process.getInputStream()) {
			@Override
			protected void handleReadLine(String line) {
				handleStdOutLine(line);
			}
		};
	}
	
	@Override
	protected Runnable createStdErrReaderTask() {
		return new ReadLineNotifyTask(process.getErrorStream()) {
			@Override
			protected void handleReadLine(String line) {
				handleStdErrLine(line);
			}
		};
	}
	
	protected abstract void handleStdOutLine(String line);
	
	protected abstract void handleStdErrLine(String line);
	
	protected abstract class ReadLineNotifyTask implements Runnable {
		
		protected final InputStream inputStream;
		protected IOException exception;
		
		public ReadLineNotifyTask(InputStream inputStream) {
			this.inputStream = inputStream;
		}
		
		@Override
		public void run() {
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
				String line;
				while ((line = reader.readLine()) != null) {
					handleReadLine(line);
				}
			} catch (IOException ioe) {
				this.exception = ioe;
			}
		}
		
		protected abstract void handleReadLine(String line);
	}
	
}