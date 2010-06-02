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
	private GoSourceDoubleClickStrategy doubleClickStrategy;
	private GoSourceTagScanner tagScanner;
	private GoSourceScanner scanner;
	private ColorManager colorManager;

	public GoSourceConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			GoSourcePartitionScanner.XML_COMMENT,
			GoSourcePartitionScanner.XML_TAG };
	}
	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new GoSourceDoubleClickStrategy();
		return doubleClickStrategy;
	}

	protected GoSourceScanner getXMLScanner() {
		if (scanner == null) {
			scanner = new GoSourceScanner(colorManager);
			scanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(IGoSourceColorConstants.DEFAULT))));
		}
		return scanner;
	}
	protected GoSourceTagScanner getXMLTagScanner() {
		if (tagScanner == null) {
			tagScanner = new GoSourceTagScanner(colorManager);
			tagScanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(IGoSourceColorConstants.TAG))));
		}
		return tagScanner;
	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr =
			new DefaultDamagerRepairer(getXMLTagScanner());
		reconciler.setDamager(dr, GoSourcePartitionScanner.XML_TAG);
		reconciler.setRepairer(dr, GoSourcePartitionScanner.XML_TAG);

		dr = new DefaultDamagerRepairer(getXMLScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		NonRuleBasedDamagerRepairer ndr =
			new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					colorManager.getColor(IGoSourceColorConstants.GO_COMMENT)));
		reconciler.setDamager(ndr, GoSourcePartitionScanner.XML_COMMENT);
		reconciler.setRepairer(ndr, GoSourcePartitionScanner.XML_COMMENT);

		return reconciler;
	}

}