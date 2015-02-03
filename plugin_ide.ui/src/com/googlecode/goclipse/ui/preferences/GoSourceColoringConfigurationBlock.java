package com.googlecode.goclipse.ui.preferences;

import static melnorme.utilbox.core.CoreUtil.array;

import java.io.InputStream;

import melnorme.lang.ide.ui.text.coloring.AbstractSourceColoringConfigurationBlock;
import melnorme.util.swt.jface.LabeledTreeElement;

import org.eclipse.jface.preference.IPreferenceStore;

import com.googlecode.goclipse.ui.text.GoColorPreferences;

public class GoSourceColoringConfigurationBlock extends AbstractSourceColoringConfigurationBlock {
	
	private static final String PREVIEW_FILE_NAME = "SourceColoringPreviewFile.go";
	
	protected static final LabeledTreeElement[] treeElements = array(
		new SourceColoringCategory("Source", array(
			new SourceColoringElement("Comment", GoColorPreferences.SYNTAX_COLORING__COMMENT.key),
			
			new SourceColoringElement("Text", GoColorPreferences.SYNTAX_COLORING__TEXT.key),
			new SourceColoringElement("Keyword", GoColorPreferences.SYNTAX_COLORING__KEYWORD.key),
			new SourceColoringElement("Keyword - Literal", GoColorPreferences.SYNTAX_COLORING__VALUE.key),
			new SourceColoringElement("Primitive", GoColorPreferences.SYNTAX_COLORING__PRIMITIVE.key),
			new SourceColoringElement("Built-in function", GoColorPreferences.SYNTAX_COLORING__BUILTIN_FUNCTION.key),
			new SourceColoringElement("Operator", GoColorPreferences.SYNTAX_COLORING__OPERATOR.key),
			new SourceColoringElement("Structural Symbols", GoColorPreferences.SYNTAX_COLORING__STRUCTURAL_SYMBOLS.key),
			new SourceColoringElement("String", GoColorPreferences.SYNTAX_COLORING__STRING.key),
			new SourceColoringElement("Multi-line string", GoColorPreferences.SYNTAX_COLORING__MULTILINE_STRING.key)

		))
	);
	
	public GoSourceColoringConfigurationBlock(IPreferenceStore store) {
		super(store);
	}
	
	@Override
	protected LabeledTreeElement[] getTreeElements() {
		return treeElements;
	}
	
	@Override
	protected InputStream getPreviewContentAsStream() {
		return getClass().getResourceAsStream(PREVIEW_FILE_NAME);
	}
	
}