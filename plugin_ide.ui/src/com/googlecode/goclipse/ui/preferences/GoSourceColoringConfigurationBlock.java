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
			new SourceColoringElement("Comment", GoColorPreferences.SC__COMMENT.key),
			
			new SourceColoringElement("Text", GoColorPreferences.SC__TEXT.key),
			new SourceColoringElement("Keywords", GoColorPreferences.SC__KEYWORD.key),
			new SourceColoringElement("Keyword - Literals", GoColorPreferences.SC__KW_LITERAL.key),
			new SourceColoringElement("Keyword - Primitives", GoColorPreferences.SC__KW_PRIMITIVE.key),
			new SourceColoringElement("Built-in functions", GoColorPreferences.SC__BUILTIN_FUNCTION.key),
			new SourceColoringElement("Operators", GoColorPreferences.SC__OPERATOR.key),
			new SourceColoringElement("Structural symbols", GoColorPreferences.SC__STRUCTURAL_SYMBOLS.key),
			new SourceColoringElement("Characters", GoColorPreferences.SC__CHARACTER.key),
			new SourceColoringElement("Strings", GoColorPreferences.SC__STRING.key),
			new SourceColoringElement("Multi-line strings", GoColorPreferences.SC__MULTILINE_STRING.key)

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