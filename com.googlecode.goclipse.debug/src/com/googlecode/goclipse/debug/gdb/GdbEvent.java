package com.googlecode.goclipse.debug.gdb;

import com.googlecode.goclipse.debug.GoDebugPlugin;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

// 555^done,stack=[frame={level="0",addr="0x0000000000001c29",func="main.Walk",file="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",fullname="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",line="33"},frame={level="1",addr="0x0000000000001c75",func="main.Walk",file="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",fullname="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",line="35"},frame={level="2",addr="0x0000000000001c3f",func="main.Walk",file="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",fullname="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",line="33"},frame={level="3",addr="0x0000000000001c3f",func="main.Walk",file="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",fullname="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",line="33"},frame={level="4",addr="0x000000000000236e",func="main._func_001",file="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",fullname="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",line="43"},frame={level="5",addr="0x000000f84000c7e4",func="??"},frame={level="6",addr="0x000000f8400000f8",func="??"},frame={level="7",addr="0x000000f8400000f0",func="??"},frame={level="8",addr="0x000000000000dcf9",func="runtime.initdone",file="/Users/dcarew/go/src/pkg/runtime/proc.c",fullname="/Users/dcarew/go/src/pkg/runtime/proc.c",line="174"},frame={level="9",addr="0x0000000000000000",func="??"}]
// 1^done,bkpt={number="2",type="breakpoint",disp="keep",enabled="y",addr="0x000000000000214c",func="main.main",file="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",fullname="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",line="93",times="0",original-location="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go:93"}
// thread-created,id="1",group-id="i1"
// thread-group-started,id="i1",pid="12721"
// running,thread-id="all"
// frame={addr="0x0000000000001c1f",func="main.main",args=[],file="/Users/dcarew/projects/go/helloworld/helloworld.go",fullname="/Users/dcarew/projects/go/helloworld/helloworld.go",line="6"},thread-id="1",stopped-threads="all"
// frame={addr="0x0000000000001c29",func="main.Walk",args=[{name="t",value="0xf84000e7e0"},{name="ch",value="0xf8400139b0"}],file="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",fullname="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",line="33"},thread-id="1",stopped-threads="all"

// [running,thread-id="all"]
// [stopped,frame={addr="0x0000000000001c1f",func="main.main",args=[],file="/Users/dcarew/workspaces/workspace_36_runtime/foo/src/cmd/new_file.go",fullname="/Users/dcarew/workspaces/workspace_36_runtime/foo/src/cmd/new_file.go",line="6"},thread-id="1",stopped-threads="all"]
// [thread-exited,id="1",group-id="i1"]
// [thread-group-exited,id="i1"]
// [thread-group-added,id="i1"]
// [thread-group-started,id="i1",pid="28651"]
// [thread-created,id="1",group-id="i1"]
// [running]
// [running,thread-id="all"]

/**
 * 
 * @author devoncarew
 */
public class GdbEvent extends GdbProperties {
	public static final String RUNNING = "running";
	public static final String STOPPED = "stopped";
	
	public static final String THREAD_GROUP_STARTED = "thread-group-started";
	public static final String THREAD_GROUP_EXITED = "thread-group-exited";
	public static final String THREAD_CREATED = "thread-created";
	public static final String THREAD_EXITED = "thread-exited";
	
	private boolean simple;
	
	private int resultToken = -1;
	private boolean error;
	
	private String extra;
	
	public GdbEvent() {
		
	}
	
	public boolean isSimple() {
		return simple;
	}
	
	public boolean isError() {
		return error;
	}
	
	public static GdbEvent parse(String string) {
		GdbEvent event = new GdbEvent();
		
		int index = string.indexOf(',');
		
		if (index == -1) {
			event.name = string;
			event.simple = true;
			
			event.checkForResultToken();
		} else {
			event.name = string.substring(0, index);
			
			event.checkForResultToken();
			
			// TODO: parse the properties -
			event.extra = string.substring(index + 1);
			
			// thread-id="all"
			// frame={addr="0x0000000000001c1f",func="main.main",args=[],file="/Users/dcarew/workspaces/workspace_36_runtime/foo/src/cmd/new_file.go",fullname="/Users/dcarew/workspaces/workspace_36_runtime/foo/src/cmd/new_file.go",line="6"},thread-id="1",stopped-threads="all"
			// id="1",group-id="i1"
			// id="i1"
			// id="i1"
			// id="i1",pid="28651"
			// id="1",group-id="i1"
			// thread-id="all"
			
			try {
				if (event.extra.length() > 0) {
					event.parseKeyValues(new PushbackReader(new StringReader(event.extra)));
				}
			} catch (IOException ioe) {
				// TODO: display to the user
				
				GoDebugPlugin.logError(ioe);
			}
		}
		
		return event;
	}

	@Override
  public String toString() {
		return name + (resultToken == -1 ? "" : "-" + resultToken);
	}
	
	public int getResultToken() {
		return resultToken;
	}
	
	private void checkForResultToken() {
		// 555^done
		
		int doneIndex = name.indexOf("^done");
    int errorIndex = name.indexOf("^error");
		
		if (doneIndex != -1) {
			try {
				resultToken = Integer.parseInt(name.substring(0, doneIndex));
				
				name = name.substring(doneIndex);
			} catch (NumberFormatException nfe) {

			}
		} else if (errorIndex != -1) {
			try {
				resultToken = Integer.parseInt(name.substring(0, errorIndex));
				
				name = name.substring(errorIndex);
				
				error = true;
			} catch (NumberFormatException nfe) {

			}
		}
	}

}
