package com.googlecode.goclipse.ui.preferences;

import static melnorme.utilbox.core.CoreUtil.array;

import java.io.InputStream;

import com.googlecode.goclipse.ui.text.GoColorPreferences;

import melnorme.lang.ide.ui.text.coloring.AbstractSourceColoringConfigurationBlock;
import melnorme.util.swt.jface.LabeledTreeElement;

public class GoSourceColoringConfigurationBlock extends AbstractSourceColoringConfigurationBlock {
	
	private static final String PREVIEW_FILE_NAME = "SourceColoringPreviewFile.go";
	
	public GoSourceColoringConfigurationBlock() {
		super();
	}
	
	@Override
	protected LabeledTreeElement[] createTreeElements() {
		return array(
			new SourceColoringCategory("Source", array(
				new SourceColoringElement("Comment", GoColorPreferences.COMMENT),
				
				new SourceColoringElement("Text", GoColorPreferences.DEFAULT),
				new SourceColoringElement("Keywords", GoColorPreferences.KEYWORD),
				new SourceColoringElement("Keyword - Literals", GoColorPreferences.KW_LITERAL),
				new SourceColoringElement("Keyword - Primitives", GoColorPreferences.KW_PRIMITIVE),
				new SourceColoringElement("Characters", GoColorPreferences.CHARACTER),
				new SourceColoringElement("Strings", GoColorPreferences.STRING),
				new SourceColoringElement("Multi-line strings", GoColorPreferences.MULTILINE_STRING),
				new SourceColoringElement("Numbers", GoColorPreferences.NUMBER),
				new SourceColoringElement("Built-in functions", GoColorPreferences.BUILTIN_FUNCTION),
				new SourceColoringElement("Operators", GoColorPreferences.OPERATOR),
				new SourceColoringElement("Structural symbols", GoColorPreferences.STRUCTURAL_SYMBOLS)
			))
		);
	}
	
	@Override
	protected InputStream getPreviewContentAsStream() {
		return getClass().getResourceAsStream(PREVIEW_FILE_NAME);
	}
	
}