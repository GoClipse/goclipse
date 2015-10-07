package com.googlecode.goclipse.ui.editor;

import melnorme.lang.ide.ui.editor.structure.AbstractLangStructureEditor;
import melnorme.lang.ide.ui.editor.text.LangPairMatcher;

public class GoEditor extends AbstractLangStructureEditor {
	
	public GoEditor() {
		super();
	}
	
	@Override
	protected LangPairMatcher init_createBracketMatcher() {
		return new LangPairMatcher("{}[]()".toCharArray());
	}
	
}