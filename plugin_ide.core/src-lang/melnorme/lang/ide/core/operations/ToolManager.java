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
package melnorme.lang.ide.core.operations;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.list;

import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Map.Entry;
import java.util.Optional;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;

import melnorme.lang.ide.core.CoreSettings;
import melnorme.lang.ide.core.CoreSettings.SettingsField;
import melnorme.lang.ide.core.ILangOperationsListener;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IToolOperationMonitor;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.ProcessStartKind;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.StartOperationOptions;
import melnorme.lang.ide.core.operations.build.VariablesResolver;
import melnorme.lang.ide.core.operations.build.VariablesResolver.SupplierAdapterVar;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.core.utils.process.AbstractRunProcessTask;
import melnorme.lang.ide.core.utils.process.AbstractRunProcessTask.ProcessStartHelper;
import melnorme.lang.tooling.commands.CommandInvocation;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.toolchain.ops.IToolOperationService;
import melnorme.lang.utils.EnvUtils;
import melnorme.lang.utils.ProcessUtils;
import melnorme.lang.utils.validators.PathValidator;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.collections.MapAccess;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.fields.EventSource;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.misc.PathUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;
import melnorme.utilbox.status.StatusException;
import melnorme.utilbox.status.StatusLevel;

/**
 * Abstract class for running external tools and notifying interested listeners (normally the UI only).
 */
public abstract class ToolManager extends EventSource<ILangOperationsListener>
	implements IStatusMessageHandler
{
	
	protected final CoreSettings settings;
	
	public ToolManager(CoreSettings settings) {
		this.settings = assertNotNull(settings);
	}
	
	public void shutdownNow() {
	}
	
	/* -----------------  ----------------- */
	
	public Path getSDKToolPath(IProject project) throws CommonException {
		return settings.SDK_LOCATION.getValue(project);
	}
	
	public PathValidator getSDKToolPathValidator() {
		return settings.getSDKLocationValidator();
	}
	
	/* -----------------  ----------------- */
	
	protected final IStringVariableManager globalVarManager = VariablesPlugin.getDefault().getStringVariableManager();
	
	public VariablesResolver getVariablesManager(Optional<IProject> project) {
		project = MiscUtil.toOptional(project);
		
		VariablesResolver variablesResolver = new VariablesResolver(globalVarManager);
		
		setupVariableResolver(variablesResolver, project);
		return variablesResolver;
	}
	
	protected void setupVariableResolver(VariablesResolver variablesResolver, Optional<IProject> project) {
		
		SettingsField<Path> pref = LangCore.settings().SDK_LOCATION;
		variablesResolver.putDynamicVar(new SupplierAdapterVar(
			LangCore_Actual.VAR_NAME_SdkToolPath, 
			LangCore_Actual.VAR_NAME_SdkToolPath_DESCRIPTION, 
			pref.getRawValueSupplier(project), 
			pref.getValidator_toString())
		);
	}
	
	/* -----------------  ----------------- */
	
	public ProcessBuilder createSDKProcessBuilder(IProject project, String... sdkOptions)
			throws CommonException {
		Path sdkToolPath = getSDKToolPath(project);
		return createToolProcessBuilder(project, sdkToolPath, sdkOptions);
	}
	
	public final ProcessBuilder createToolProcessBuilder(IProject project, Path toolPath, String... toolArguments)
			throws CommonException {
		Location projectLocation = project == null ? null : ResourceUtils.getProjectLocation2(project);
		ArrayList2<String> commandLine = ProcessUtils.createCommandLine(toolPath, toolArguments);
		return createToolProcessBuilder(commandLine, projectLocation);
	}
	
	public ProcessBuilder createToolProcessBuilder(Indexable<String> commandLine, Location workingDir) {
		return modifyToolProcessBuilder(ProcessUtils.createProcessBuilder(commandLine, workingDir));
	}
	
	public ProcessBuilder modifyToolProcessBuilder(ProcessBuilder pb) {
		Path cmdExePath = PathUtil.createPathOrNull(pb.command().get(0));
		EnvUtils.addCmdDirToPathEnv(cmdExePath, pb);
		return pb;
	}
	
	public ProcessBuilder createSimpleProcessBuilder(IProject project, String... commands) 
			throws CommonException {
		Location workingDir = project == null ? null : ResourceUtils.getProjectLocation2(project);
		return ProcessUtils.createProcessBuilder(list(commands), workingDir);
	}
	
	/* -----------------  ----------------- */
	
	public void logAndNotifyError(String title, StatusException ce) {
		logAndNotifyError(title, title, ce);
	}
	
	public void logAndNotifyError(String msgId, String title, StatusException ce) {
		LangCore.logError(title, ce);
		notifyMessage(msgId, ce.getSeverity().toStatusLevel(), title, ce.getMessage());
	}
	
	@Override
	public void notifyMessage(String msgId, StatusLevel statusLevel, String title, String message) {
		for(ILangOperationsListener listener : getListeners()) {
			listener.notifyMessage(msgId, statusLevel, title, message);
		}
	}
	
	/* ----------------- ----------------- */
	
	public IToolOperationMonitor startNewBuildOperation() {
		return startNewBuildOperation(false);
	}
	
	public IToolOperationMonitor startNewBuildOperation(boolean explicitConsoleNotify) {
		return startNewOperation(ProcessStartKind.BUILD, true, explicitConsoleNotify);
	}
	
	public IToolOperationMonitor startNewOperation(ProcessStartKind kind, boolean clearConsole, 
			boolean activateConsole) {
		return startNewOperation(new StartOperationOptions(kind, clearConsole, activateConsole));
	}
	
	public IToolOperationMonitor startNewOperation(StartOperationOptions options) {
		
		AggregatedToolOperationMonitor aggregatedHandlers = new AggregatedToolOperationMonitor();
		
		for(ILangOperationsListener processListener : getListeners()) {
			IToolOperationMonitor handler = processListener.beginOperation(options);
			aggregatedHandlers.monitors.add(handler);
		}
		return aggregatedHandlers;
	}
	
	public static class AggregatedToolOperationMonitor implements IToolOperationMonitor {
		
		public final ArrayList2<IToolOperationMonitor> monitors = new ArrayList2<>();
		
		public AggregatedToolOperationMonitor() {
		}
		
		@Override
		public void handleProcessStart(String prefixText, String suffixText, ProcessBuilder pb, 
			ProcessStartHelper processStartHelper) {
			for (IToolOperationMonitor monitor : monitors) {
				monitor.handleProcessStart(prefixText, suffixText, pb, processStartHelper);
			}
		}
		
		@Override
		public void writeInfoMessage(String operationMessage) {
			for (IToolOperationMonitor monitor : monitors) {
				monitor.writeInfoMessage(operationMessage);
			}
		}
		
		@Override
		public void activate() {
			for (IToolOperationMonitor monitor : monitors) {
				monitor.activate();
			}
		}
		
	}
	
	/* -----------------  ----------------- */
	
	public final RunToolTask newRunBuildToolOperation(ProcessBuilder pb, IOperationMonitor om) {
		IToolOperationMonitor opHandler = startNewBuildOperation();
		return newRunProcessTask(opHandler, pb, "clean", null, om);
	}
	
	public RunToolTask newRunProcessTask(
		IToolOperationMonitor opMonitor, 
		ProcessBuilder pb, 
		String buildTargetName, CommandInvocation buildCommand, 
		IOperationMonitor om
	) {
		String prefixText;
		if(buildTargetName != null) {
			prefixText = MessageFormat.format(">> Running `{0}` with: ", buildTargetName);
		} else {
			prefixText = ">> Running: ";	
		}
		
		String suffixText = "";
		
		if(buildCommand != null && buildCommand.getEnvironmentVars() != null) {
			MapAccess<String, String> envVars = buildCommand.getEnvironmentVars();
			for (Entry<String, String> entry : envVars.entrySet()) {
				suffixText += "\n   " + entry.getKey() + "=" + entry.getValue();
			}
		}
		
		return new RunToolTask(opMonitor, prefixText, suffixText, pb, om);
	}
	
	public class RunToolTask extends AbstractRunProcessTask {
		
		protected final IToolOperationMonitor opMonitor;
		protected final String prefixText;
		protected final String suffixText;
		
		public RunToolTask(IToolOperationMonitor opMonitor, ProcessBuilder pb, ICancelMonitor cm) {
			this(opMonitor, null, null, pb, cm);
		}
		
		public RunToolTask(IToolOperationMonitor opMonitor, String prefixText, 
				String suffixText, ProcessBuilder pb, ICancelMonitor cm) {
			super(pb, cm);
			this.prefixText = prefixText;
			this.suffixText = suffixText;
			this.opMonitor = assertNotNull(opMonitor);
		}
		
		@Override
		protected void handleProcessStartResult(ProcessStartHelper psh) {
			opMonitor.handleProcessStart(prefixText, suffixText, pb, psh);
		}
		
	}
	
	/* ----------------- ----------------- */
	
	public ExternalProcessResult runEngineTool(ProcessBuilder pb, String processInput, IProgressMonitor pm) 
			throws CommonException, OperationCancellation {
		return runEngineTool(pb, processInput, EclipseUtils.cm(pm));
	}
	
	public final ExternalProcessResult runEngineTool(ProcessBuilder pb, String processInput, ICancelMonitor cm)
			throws CommonException, OperationCancellation {
		IToolOperationMonitor opMonitor = startNewOperation(ProcessStartKind.ENGINE_TOOLS, false, false);
		return new RunToolTask(opMonitor, pb, cm).runProcess(processInput);
	}
	
	/* -----------------  ----------------- */
	
	/** 
	 * Helper to start engine client processes in the tool manager. 
	 */
	public class ToolManagerEngineToolRunner implements IToolOperationService {
		
		@Override
		public ExternalProcessResult runProcess(ProcessBuilder pb, String processInput, ICancelMonitor cm) 
				throws CommonException, OperationCancellation {
			return runEngineTool(pb, processInput, cm);
		}
		
		@Override
		public void logStatus(StatusException statusException) {
			LangCore.logStatusException(statusException);
		}
		
	}
	
	public IToolOperationService getEngineToolsOperationService() {
		return new ToolManagerEngineToolRunner();
	}
	
}