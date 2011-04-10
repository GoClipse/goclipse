package com.googlecode.goclipse.go.antlr;

import org.eclipse.swt.graphics.Image;

public class CodeUnit {
	private Image DEFAULT_IMAGE = com.googlecode.goclipse.Activator.getImageDescriptor("icons/orange_cube16.png").createImage();
	
	public String filepath		= "";
	public String scopename     = "";
	public int    line  		= -1;
	public int    start 		= -1;
	public int    stop  		= -1;
	public String displayText   = "";
	public String insertionText = "";
	public Image  image 		= DEFAULT_IMAGE;
	public String comment       = "";
	
	@Override
	public String toString() {
		return "display_text=\""+ displayText+ "\" pos=\""+line+":"+start+":"+stop+"\" file=\""+filepath+"\" scopename=\""+scopename+"\"";
	}
}
