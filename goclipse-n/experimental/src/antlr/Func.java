package com.googlecode.goclipse.go.antlr;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Image;

public class Func extends CodeUnit {
	
	public static final Image funcImage = com.googlecode.goclipse.Activator
			.getImageDescriptor("icons/func16.png").createImage();

	
	public Var receiver;
	public ArrayList<Var> params = new ArrayList<Var>();
	public ArrayList<Var> returns = new ArrayList<Var>();

	public Func() {
		image = funcImage;
	}

	@Override
	public String toString() {
		return "func -> " + super.toString();
	}
}
