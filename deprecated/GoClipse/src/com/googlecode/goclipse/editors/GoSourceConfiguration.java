package com.googlecode.goclipse.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class GoSourceConfiguration extends SourceViewerConfiguration {
	// The known content types
	public static final String[] KNOWN_CONTENT_TYPES =
		new String[] { IDocument.DEFAULT_CONTENT_TYPE, GoSourcePartitionScanner.GO_COMMENT };

	private GoSourceDoubleClickStrategy doubleClickStrategy;
	private GoSourceScanner scanner;

	public GoSourceConfiguration() {
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return KNOWN_CONTENT_TYPES;
	}

	public ITextDoubleClickStrategy getDoubleClickStrategy(
			ISourceViewer sourceViewer, String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new GoSourceDoubleClickStrategy();
		return doubleClickStrategy;
	}

	//	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
	//		return new JavaAnnotationHover();
	//	}
	//		
	//	public IAutoIndentStrategy getAutoIndentStrategy(ISourceViewer sourceViewer, String contentType) {
	//		return (IDocument.DEFAULT_CONTENT_TYPE.equals(contentType) ? new JavaAutoIndentStrategy() : new DefaultAutoIndentStrategy());
	//	}

	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		//		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getGoTagScanner());
		//		reconciler.setDamager(dr, GoSourcePartitionScanner.GO_KEYWORD);
		//		reconciler.setRepairer(dr, GoSourcePartitionScanner.GO_KEYWORD);

		DefaultDamagerRepairer ddr = new DefaultDamagerRepairer(getGoScanner());
		reconciler.setDamager(ddr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(ddr, IDocument.DEFAULT_CONTENT_TYPE);

		NonRuleBasedDamagerRepairer ndr = new NonRuleBasedDamagerRepairer(new TextAttribute(IGoSourceColorConstants.COMMENT));
		reconciler.setDamager(ndr, GoSourcePartitionScanner.GO_COMMENT);
		reconciler.setRepairer(ndr, GoSourcePartitionScanner.GO_COMMENT);

		return reconciler;
	}

	// Heper for getPresentationReconciler()
	protected GoSourceScanner getGoScanner() {
		if (scanner == null) {
			scanner = new GoSourceScanner();
			scanner.setDefaultReturnToken(new Token(new TextAttribute(
					IGoSourceColorConstants.DEFAULT)));
		}
		return scanner;
	}

}