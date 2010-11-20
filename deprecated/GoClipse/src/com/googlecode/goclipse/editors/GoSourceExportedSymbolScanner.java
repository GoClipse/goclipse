package com.googlecode.goclipse.editors;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

public class GoSourceExportedSymbolScanner extends WordRule {

	public GoSourceExportedSymbolScanner() {
		super(new WordDetector());
		fDefaultToken = new Token(new TextAttribute(IGoSourceColorConstants.EXPORTED, null, SWT.ITALIC, null));
	}

	private static class WordDetector implements IWordDetector {
		public boolean isWordStart(char c) {
			return Character.isJavaIdentifierStart(c) && Character.isUpperCase(c);
		}

		public boolean isWordPart(char c) {
			// TODO Auto-generated method stub
			return Character.isJavaIdentifierPart(c);
		}
	}

}
