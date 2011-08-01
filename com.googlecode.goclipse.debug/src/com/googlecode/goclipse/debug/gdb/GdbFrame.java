package com.googlecode.goclipse.debug.gdb;

/**
 * 
 * @author devoncarew
 */
public class GdbFrame {
	private String name;
	private String file;
	private int line;

	public GdbFrame(GdbProperties props) {
		// level="0",
		// addr="0x0000000000001c29",
		// func="main.Walk",
		// file="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",
		// fullname="/Users/dcarew/workspaces/workspace_37_runtime/HelloWorld/src/cmd/new_file.go",
		// line="33"
		
		this.name = props.getPropertyString("func");
		this.file = props.getPropertyString("file");
		this.line = props.getPropertyParseInt("line");
	}

	public String getName() {
		return name + "()";
	}

	public String getFile() {
		if (file == null) {
			return null;
		}
		
		// TODO: temp temp fix the source locator
		int index = file.lastIndexOf('/');
		
		if (index == -1) {
			return file;
		} else {
			return file.substring(index + 1);
		}
	}

	public int getLine() {
		return line;
	}

}
