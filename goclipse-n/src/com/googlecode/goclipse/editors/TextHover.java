package com.googlecode.goclipse.editors;


import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;

import com.googlecode.goclipse.SysUtils;

public class TextHover implements ITextHover {

	@Override
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		System.out.println("getHoverInfo");
		return null;
	}

	@Override
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		SysUtils.debug("getHoverRegion");
		return null;
	}

}
