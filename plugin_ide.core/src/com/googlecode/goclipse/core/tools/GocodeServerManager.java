package com.googlecode.goclipse.core.tools;

import java.nio.file.Path;

import org.eclipse.debug.core.DebugPlugin;

import com.googlecode.goclipse.core.GoToolPreferences.GoToolValidator;
import com.googlecode.goclipse.tooling.gocode.GocodeCompletionOperation;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.engine.LanguageServerHandler;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IToolOperationMonitor;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.ProcessStartKind;
import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.common.ops.JobExecutor;
import melnorme.lang.utils.validators.PathValidator;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

/**
 * For Go, gocode is the closest to a language server, since it's the only tool that runs in daemon mode.
 */
public class GocodeServerManager extends LanguageServerHandler<GocodeServerInstance> {
	
	public GocodeServerManager(JobExecutor jobExecutor, ToolManager toolMgr) {
		super(jobExecutor, toolMgr);
	}
	
	@Override
	protected PathValidator init_ServerToolPathValidator() {
		return new GoToolValidator("gocode path");
	}
	
	@Override
	protected String getLanguageServerName() {
		return GocodeServerInstance.GOCODE_SERVER_Name;
	}
	
	@Override
	protected GocodeServerInstance doCreateServerInstance(IOperationMonitor om)
			throws CommonException, OperationCancellation {
		Path gocodePath = getServerPath();
		
		ArrayList2<String> commandLine = new ArrayList2<String>();
		commandLine.add(gocodePath.toString());
		commandLine.add("-s");
		if (GocodeCompletionOperation.USE_TCP) {
			commandLine.add("-sock=tcp");
		}
		
		LangCore.logInfo("Starting gocode server: " + 
			DebugPlugin.renderArguments(commandLine.toArray(String.class), null));
		
		ProcessBuilder pb = new ProcessBuilder(commandLine);
		
		IToolOperationMonitor opMonitor = toolMgr.startNewOperation(ProcessStartKind.ENGINE_SERVER, true, false);
		String prefixText = "==== Starting gocode server ====\n";
		
		gocodeSetEnableBuiltins(gocodePath, om, opMonitor, prefixText);
		
		ExternalProcessNotifyingHelper process = 
				toolMgr.new RunToolTask(opMonitor, prefixText, null, pb, om).startProcess();
		
		return new GocodeServerInstance(gocodePath, process);
	}
	
	protected void gocodeSetEnableBuiltins(Path gocodePath, IOperationMonitor om, 
			IToolOperationMonitor toolOpMonitor, String prefixText)
			throws CommonException, OperationCancellation {
		ProcessBuilder pb = new ProcessBuilder(gocodePath.toString(), "set", "propose-builtins", "true");
		toolMgr.new RunToolTask(toolOpMonitor, prefixText, null, pb, om).runProcess();
	}
	
}