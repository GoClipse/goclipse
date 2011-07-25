package com.googlecode.goclipse.debug.gdb;

import java.util.ArrayList;
import java.util.List;

// stack=[frame={level="0",addr="0x0000000000001c29",func="main.Walk",file="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",fullname="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",line="33"},frame={level="1",addr="0x0000000000001c75",func="main.Walk",file="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",fullname="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",line="35"},frame={level="2",addr="0x0000000000001c3f",func="main.Walk",file="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",fullname="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",line="33"},frame={level="3",addr="0x0000000000001c3f",func="main.Walk",file="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",fullname="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",line="33"},frame={level="4",addr="0x000000000000236e",func="main._func_001",file="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",fullname="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",line="43"},frame={level="5",addr="0x000000f84000c7e4",func="??"},frame={level="6",addr="0x000000f8400000f8",func="??"},frame={level="7",addr="0x000000f8400000f0",func="??"},frame={level="8",addr="0x000000000000dcf9",func="runtime.initdone",file="/Users/dcarew/go/src/pkg/runtime/proc.c",fullname="/Users/dcarew/go/src/pkg/runtime/proc.c",line="174"},frame={level="9",addr="0x0000000000000000",func="??"}]

/**
 * 
 * @author devoncarew
 */
public class GdbContext {
	private GdbThread thread;
	
	public GdbContext(GdbThread thread, GdbEvent event) {
		this.thread = thread;
		
		Object[] stack = (Object[])event.getProperty("stack");
		
		List<GdbFrame> frames = new ArrayList<GdbFrame>();
		
		if (frames != null) {
			for (Object o : stack) {
				GdbProperties props = (GdbProperties)o;
				
				GdbFrame frame = new GdbFrame((GdbProperties)props.getProperty("frame"));
				
				frames.add(frame);
			}
		}
		
		thread.replaceFrames(frames);
	}

	public GdbThread getCurrentThread() {
		return thread;
	}
	
}
