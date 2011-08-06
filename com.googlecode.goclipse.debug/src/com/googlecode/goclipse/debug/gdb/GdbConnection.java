package com.googlecode.goclipse.debug.gdb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.eclipse.core.resources.IFile;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.debug.core.model.IStreamsProxy;

import com.googlecode.goclipse.debug.GoDebugPlugin;

// http://www.linuxfoundation.org/en/DMI_Wiki_Content
// http://davis.lbl.gov/Manuals/GDB/gdb_24.html

// run - start execution
// backtrace - print all stack traces
// break file:line - set a breakpoint
// continue - resume execution
// next - step over
// step - step in
// finish - step out
// kill/quit ?
// thread - 
// info threads
// info locals

// 555-stack-list-frames
// 555^done,stack=[frame={level="0",addr="0x0000000000001c29",func="main.Walk",file="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",fullname="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",line="33"},frame={level="1",addr="0x0000000000001c75",func="main.Walk",file="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",fullname="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",line="35"},frame={level="2",addr="0x0000000000001c3f",func="main.Walk",file="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",fullname="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",line="33"},frame={level="3",addr="0x0000000000001c3f",func="main.Walk",file="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",fullname="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",line="33"},frame={level="4",addr="0x000000000000236e",func="main._func_001",file="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",fullname="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",line="43"},frame={level="5",addr="0x000000f84000c7e4",func="??"},frame={level="6",addr="0x000000f8400000f8",func="??"},frame={level="7",addr="0x000000f8400000f0",func="??"},frame={level="8",addr="0x000000000000dcf9",func="runtime.initdone",file="/Users/dcarew/go/src/pkg/runtime/proc.c",fullname="/Users/dcarew/go/src/pkg/runtime/proc.c",line="174"},frame={level="9",addr="0x0000000000000000",func="??"}]

// -stack-list-locals 0
// -stack-list-locals 1
// -stack-list-arguments 0
// -stack-list-arguments 1 [# #]
// -stack-select-frame frame-number
// -thread-select threadnum

/**
 * 
 * @author devoncarew
 */
public class GdbConnection implements IStreamListener {
	private static final String EOL = System.getProperty("line.separator");
	
	private static final String WAITING_PROMPT = "(gdb) ";
	
	private IProcess process;
	private IStreamsProxy streamsProxy;
	private StringBuilder inBuffer = new StringBuilder();
	
	private List<GdbConnectionListener> listeners = new ArrayList<GdbConnectionListener>();
	
	private int nextToken;
	private Map<Integer, Callback> callbackMap = new HashMap<Integer, GdbConnection.Callback>();
	
	private Map<String, GdbThread> threads = new HashMap<String, GdbThread>();
	
	public GdbConnection(IProcess process) {
		this.process = process;
		
		this.streamsProxy = process.getStreamsProxy();
	}
	
	public void start() {		
		streamsProxy.getOutputStreamMonitor().addListener(this);
		
		inBuffer.append(streamsProxy.getOutputStreamMonitor().getContents());
		
		processBuffer();
	}
	
	public void addConnectionListener(GdbConnectionListener listener) {
		listeners.add(listener);
	}
	
	public void removeConnectionListener(GdbConnectionListener listener) {
		listeners.remove(listener);
	}
	
	protected void sendCommand(String command) throws IOException {
		if (!process.isTerminated()) {
			streamsProxy.write(command + EOL);
		}
	}
	
	public void dispose() {
		if (!process.isTerminated()) {
			streamsProxy.getOutputStreamMonitor().removeListener(this);
		}
	}

	@Override
	public void streamAppended(String text, IStreamMonitor monitor) {
		inBuffer.append(text);
		
		processBuffer();
	}

	private void processBuffer() {
		int eolStart = inBuffer.indexOf(EOL);
		
		while (eolStart != -1) {
			String eventStr = inBuffer.substring(0, eolStart);
			
			inBuffer.delete(0, eolStart + EOL.length());
			
			try {
				handleGdbInput(eventStr);
			} catch (IOException exception) {
				GoDebugPlugin.logError(exception);
			}
			
			eolStart = inBuffer.indexOf(EOL);
		}
	}

	protected void handleGdbInput(String eventText) throws IOException {
		if (WAITING_PROMPT.equals(eventText)) {
			// at the (gdb) prompt
			
			//System.out.println("PROMPT");
		} else if (eventText.startsWith("~\"")) {
			// cli output
			
			//System.out.println("USER  [" + removeQuotes(eventText) + "]");
		} else if (eventText.startsWith("&\"")) {
			// gdb debugging output
			
			//System.out.println("GDBDEBUG [" + removeQuotes(eventText) + "]");
		} else if (eventText.startsWith("@\"")) {
			// "@" string-output
			// The target output stream contains any textual output from the running target.
			
			System.out.println("STDOUT    [" + removeQuotes(eventText) + "]");
		} else if (eventText.startsWith("*")) {
			handleEvent(eventText.substring(1));
		} else if (eventText.startsWith("=")) {
			handleState(eventText.substring(1));
		} else if (eventText.startsWith("^")) {
			// "^done" [ "," results ]
			// The synchronous operation was successful, results are the return values.
			// -or-
			// "^running"
			// The asynchronous operation was successfully started. The target is running.
			
			//System.out.println("OP-RESULT [" + eventText.substring(1) + "]");
		} else if (isAsyncResult(eventText)) {
			handleAsyncResult(eventText);
		} else {
			System.out.println("UNKNOWN   [" + eventText + "]");
		}
	}

	private boolean isAsyncResult(String text) {
		// nnn^done
		// 2^error,msg="Undefined MI command: stack-list-localz"

		if (text.indexOf("^done") != -1) {
			try {
				Integer.parseInt(text.substring(0, text.indexOf("^done")));
				return true;
			} catch (NumberFormatException nfe) {
				return false;
			}
		} else if (text.indexOf("^error") != -1) {
			try {
				Integer.parseInt(text.substring(0, text.indexOf("^error")));
				return true;
			} catch (NumberFormatException nfe) {
				return false;
			}
		} else {
			return false;
		}
	}

	private void handleAsyncResult(String text) {
		GdbEvent event = GdbEvent.parse(text);
		
		if (event.getResultToken() != -1) {
			Callback callback = callbackMap.remove(event.getResultToken());
			
			if (callback != null) {
				callback.handleResult(event);
			}
		}
	}

	private void handleState(String text) {
		// STATE     [thread-group-started,id="i1",pid="28651"]
		
		GdbEvent event = GdbEvent.parse(text);
		
		// [thread-group-added,id="i1"]
		// [thread-group-started,id="i1",pid="13185"]
		// [thread-group-exited,id="i1"]
		
		// [thread-created,id="1",group-id="i1"]
		// [thread-exited,id="1",group-id="i1"]
		
		if (event.getName().equals(GdbEvent.THREAD_GROUP_STARTED)) {
			//handleThreadGroupStarted(event.getPropertyString("id"), event.getPropertyString("pid"));
		} else if (event.getName().equals(GdbEvent.THREAD_GROUP_EXITED)) {
			//handleThreadGroupExited(event.getPropertyString("id"));
		} else if (event.getName().equals(GdbEvent.THREAD_CREATED)) {
			handleThreadCreated(event.getPropertyString("id"), event.getPropertyString("group-id"));
		} else if (event.getName().equals(GdbEvent.THREAD_EXITED)) {
			handleThreadExited(event.getPropertyString("id"), event.getPropertyString("group-id"));
		} else {
			// TODO:
			
			//System.out.println("STATE     [" + text + "]");
		}
	}

	private void handleThreadCreated(String id, String groupId) {
		GdbThread thread = new GdbThread(this, id);
		
		threads.put(id, thread);
	}

	private void handleThreadExited(String id, String groupId) {
		threads.remove(id);
	}

	private void handleEvent(String text) throws IOException {
		GdbEvent event = GdbEvent.parse(text);
		
		if (event.getName().equals("stopped") && event.isSimple()) {
			for (GdbConnectionListener listener : listeners) {
				listener.handleFinished();
			}
		} if (event.getName().equals(GdbEvent.RUNNING)) {
			fireResumed(event);
		} if (event.getName().equals(GdbEvent.STOPPED)) {
			handleSuspended(event);
		} else {
			// TODO:
			
			//System.out.println("EVENT     [" + text + "]");
		}
	}

	protected void fireResumed(GdbEvent event) {
		for (GdbConnectionListener listener : listeners) {
			listener.handleResumed(event);
		}
	}
	
	private void handleSuspended(GdbEvent event) throws IOException {
		final String threadId = event.getPropertyString("thread-id");
		
		sendAsyncCall("-stack-list-frames", new Callback() {
			@Override
			public void handleResult(GdbEvent event) {
				GdbThread thread = threads.get(threadId);
				
				GdbContext context = new GdbContext(thread, event);
				
				for (GdbConnectionListener listener : listeners) {
					listener.handleSuspended(context);
				}
			}
		});
	}
	
	private String removeQuotes(String text) {
		if (text.length() >= 3) {
			return text.substring(2, text.length());
		} else {
			return text;
		}
	}

	public void sendQuit() throws IOException {
		sendCommand("quit");
	}

	public void sendRun() throws IOException {
		sendCommand("run");
	}

	public void sendContinue() throws IOException {
		sendCommand("continue");
	}
	
	public void sendStop() throws IOException {
		sendCommand("stop");
	}
	
	public void sendStep() throws IOException {
		sendCommand("next");
	}
	
	public void sendStepIn() throws IOException {
		sendCommand("step");
	}
	
	public void sendStepOut() throws IOException {
		sendCommand("finish");
	}
	
	public void createBreakpoint(IFile file, int line, Callback callback) throws IOException {
		String fileName = file.getLocation().toOSString();
		
		sendAsyncCall("-break-insert " + fileName + ":" + line, callback);
	}
	
	public void removeBreakpoint(int bpNumber) throws IOException {
		sendCommand("-break-delete " + bpNumber);
	}
	
	public void enableBreakpoint(int bpNumber, boolean enabled) throws IOException {
		if (enabled) {
			sendCommand("-break-enable " + bpNumber);
		} else {
			sendCommand("-break-disable " + bpNumber);
		}
	}
	
	public void deleteBreakpoint(int bpNumber) throws IOException {
		sendCommand("-break-delete " + bpNumber);
	}
	
	public static interface Callback {
		public void handleResult(GdbEvent event);
	}
	
	protected void sendAsyncCall(String command, Callback callback) throws IOException {
		int token = nextToken++;
		
		try {
			if (callback != null) {
				callbackMap.put(token, callback);
			}
			
			sendCommand(token + command);
		} catch (IOException ioe) {
			if (callback != null) {
				callbackMap.remove(token);
			}
			
			throw ioe;
		}
	}

	public List<GdbThread> getThreads() {
		return new ArrayList<GdbThread>(threads.values());
	}

	public List<GdbVariable> getVariables(GdbFrame gdbFrame) throws IOException {
		// locals=[name="bob"]
		final List<GdbVariable> result = new ArrayList<GdbVariable>();
		final CountDownLatch latch = new CountDownLatch(1);
		
		sendCommand("-stack-select-frame " + gdbFrame.getIndex());
		
		sendAsyncCall("-stack-list-locals 1", new Callback() {
			@Override
			public void handleResult(GdbEvent event) {
				if (!event.isError()) {
					Object obj = event.getProperty("locals"); // stack-args
					
					if (obj instanceof Object[]) {
						Object[] objs = (Object[])obj;
						
						for (Object o : objs) {
							if (o instanceof GdbProperties) {
								GdbProperties props = (GdbProperties)o;
								
								result.add(new GdbVariable(
									props.getPropertyString("name"), props.getPropertyString("value")));
							}
						}
					}
				}
				
				latch.countDown();
			}
		});
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

}
