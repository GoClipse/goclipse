package com.googlecode.goclipse.ui.editor;

import melnorme.lang.ide.ui.editor.BestMatchHover;
import melnorme.lang.ide.ui.text.AbstractLangSourceViewerConfiguration;

import org.eclipse.cdt.ui.text.IColorManager;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.source.DefaultAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.editors.CompletionProcessor;
import com.googlecode.goclipse.editors.DoubleClickStrategy;
import com.googlecode.goclipse.editors.GoEditor;
import com.googlecode.goclipse.editors.GoEditorReconcilingStrategy;
import com.googlecode.goclipse.ui.GoUIPreferenceConstants;
import com.googlecode.goclipse.ui.text.GoPartitionScanner;
import com.googlecode.goclipse.ui.text.GoScanner;
import com.googlecode.goclipse.utils.IContentAssistProcessorExt;

/**
 * @author steel
 */
public class GoEditorSourceViewerConfiguration extends AbstractLangSourceViewerConfiguration {
	
	protected final GoEditor	        editor;
	private DoubleClickStrategy	doubleClickStrategy;
	private MonoReconciler	    reconciler;

	public GoEditorSourceViewerConfiguration(IPreferenceStore preferenceStore, IColorManager colorManager, 
			GoEditor editor) {
		super(preferenceStore, colorManager, editor);
		this.editor = editor;
	}
	
	@Override
	protected void createScanners() {
		addScanner(new GoScanner(getTokenStoreFactory()), IDocument.DEFAULT_CONTENT_TYPE);
	
		addScanner(createSingleTokenScanner(GoUIPreferenceConstants.SYNTAX_COLORING__COMMENT.key), 
			GoPartitionScanner.COMMENT);
		addScanner(createSingleTokenScanner(GoUIPreferenceConstants.SYNTAX_COLORING__STRING.key), 
			GoPartitionScanner.STRING);
		addScanner(createSingleTokenScanner(GoUIPreferenceConstants.SYNTAX_COLORING__MULTILINE_STRING.key), 
			GoPartitionScanner.MULTILINE_STRING);
	}
	
	@Override
	public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new DoubleClickStrategy();
		return doubleClickStrategy;
	}
	
	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sv) {
		final ContentAssistant ca = new ContentAssistant();
		ca.enableAutoActivation(true);
		ca.setAutoActivationDelay(100);

		ca.setProposalPopupOrientation(IContentAssistant.PROPOSAL_OVERLAY);
		ca.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
		ca.setContextInformationPopupBackground(colorManager.getColor(new RGB(150, 150, 0)));
		ca.setInformationControlCreator(getInformationControlCreator(sv));

		IContentAssistProcessor cap = getCompletionProcessor();
		ca.setContentAssistProcessor(cap, IDocument.DEFAULT_CONTENT_TYPE);
		// ca.setInformationControlCreator(getInformationControlCreator(sv));
		return ca;
	}

	/**
	 * @return
	 */
	private IContentAssistProcessor getCompletionProcessor() {
		
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(
		        Activator.CONTENT_ASSIST_EXTENSION_ID);
		try {
			for (IConfigurationElement e : config) {
				final Object extension = e.createExecutableExtension("class");

				if (extension instanceof IContentAssistProcessorExt) {
					((IContentAssistProcessorExt) extension).setEditorContext(editor);
				}

				if (extension instanceof IContentAssistProcessor) {
					return (IContentAssistProcessor) extension;
				}
			}
		} catch (CoreException ex) {
			// do nothing
		}
		
		return new CompletionProcessor(editor);
	}
	
	@Override
	public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType, int stateMask) {
		if(contentType.equals(IDocument.DEFAULT_CONTENT_TYPE)) {
			return new BestMatchHover(editor, stateMask);
		}
		return null;
	}
	
	@Override
	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
		return new DefaultAnnotationHover();
	}
	
	@Override
	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		
		if (reconciler == null && sourceViewer != null) {
			reconciler = new MonoReconciler(new GoEditorReconcilingStrategy(editor), false);
			reconciler.setDelay(500);
		}

		return reconciler;
	}

	@Override
	public String[] getDefaultPrefixes(ISourceViewer sourceViewer, String contentType) {
		return new String[] { "//", "" };
	}

	@Override
	public String[] getIndentPrefixes(ISourceViewer sourceViewer, String contentType) {
		return new String[] { "\t", "" };
	}

	@Override
	public int getTabWidth(ISourceViewer sourceViewer) {
		
		if (fPreferenceStore == null) {
			return super.getTabWidth(sourceViewer);
		}
		
		return fPreferenceStore.getInt(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_TAB_WIDTH);
	}
	
}