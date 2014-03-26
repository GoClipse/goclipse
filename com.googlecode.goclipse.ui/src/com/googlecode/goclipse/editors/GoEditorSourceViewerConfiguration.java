package com.googlecode.goclipse.editors;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.preferences.PreferenceConstants;
import com.googlecode.goclipse.utils.IContentAssistProcessorExt;

import melnorme.lang.ide.ui.editors.BestMatchHover;
import melnorme.util.swt.jface.ColorManager;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.URLHyperlinkDetector;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.DefaultAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;

/**
 * @author steel
 */
public class GoEditorSourceViewerConfiguration extends TextSourceViewerConfiguration {
	
	private DoubleClickStrategy	doubleClickStrategy;
	private GoScanner	        keywordScanner;
	private GoEditor	        editor;
	private MonoReconciler	    reconciler;

	public GoEditorSourceViewerConfiguration(GoEditor editor, IPreferenceStore preferenceStore) {
		super(preferenceStore);

		this.editor = editor;
	}

	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE, PartitionScanner.COMMENT, PartitionScanner.STRING,
		        PartitionScanner.MULTILINE_STRING };
	}

	@Override
	public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new DoubleClickStrategy();
		return doubleClickStrategy;
	}

	/**
	 * @return
	 */
	protected GoScanner getKeywordScanner() {
		if (keywordScanner == null) {
			keywordScanner = new GoScanner();
		}

		return keywordScanner;
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getKeywordScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
		boolean useHighlighting = prefStore.getBoolean(PreferenceConstants.FIELD_USE_HIGHLIGHTING);

		if (useHighlighting) {
			Color commentColor = ColorManager.INSTANCE.getColor(PreferenceConverter.getColor(prefStore,
			        PreferenceConstants.FIELD_SYNTAX_COMMENT_COLOR));
			Color stringColor = ColorManager.INSTANCE.getColor(PreferenceConverter.getColor(prefStore,
			        PreferenceConstants.FIELD_SYNTAX_STRING_COLOR));
			Color multilineStringColor = ColorManager.INSTANCE.getColor(PreferenceConverter.getColor(prefStore,
			        PreferenceConstants.FIELD_SYNTAX_MULTILINE_STRING_COLOR));

			int commentStyle = prefStore.getInt(PreferenceConstants.FIELD_SYNTAX_COMMENT_STYLE);
			int stringStyle = prefStore.getInt(PreferenceConstants.FIELD_SYNTAX_STRING_STYLE);
			int multilineStringStyle = prefStore.getInt(PreferenceConstants.FIELD_SYNTAX_MULTILINE_STRING_STYLE);

			NonRuleBasedDamagerRepairer ndr = new NonRuleBasedDamagerRepairer(new TextAttribute(commentColor, null,
			        commentStyle));
			reconciler.setDamager(ndr, PartitionScanner.COMMENT);
			reconciler.setRepairer(ndr, PartitionScanner.COMMENT);

			NonRuleBasedDamagerRepairer nonRuleBasedDamagerRepairer = new NonRuleBasedDamagerRepairer(
			        new TextAttribute(stringColor, null, stringStyle));
			reconciler.setDamager(nonRuleBasedDamagerRepairer, PartitionScanner.STRING);
			reconciler.setRepairer(nonRuleBasedDamagerRepairer, PartitionScanner.STRING);

			NonRuleBasedDamagerRepairer nonRuleBasedDamagerRepairer2 = new NonRuleBasedDamagerRepairer(
			        new TextAttribute(multilineStringColor, null, multilineStringStyle));
			reconciler.setDamager(nonRuleBasedDamagerRepairer2, PartitionScanner.MULTILINE_STRING);
			reconciler.setRepairer(nonRuleBasedDamagerRepairer2, PartitionScanner.MULTILINE_STRING);
		}
		return reconciler;
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sv) {
		final ContentAssistant ca = new ContentAssistant();
		ca.enableAutoActivation(true);
		ca.setAutoActivationDelay(100);

		ca.setProposalPopupOrientation(IContentAssistant.PROPOSAL_OVERLAY);
		ca.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
		ca.setContextInformationPopupBackground(ColorManager.INSTANCE.getColor(new RGB(150, 150, 0)));
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
		return new TextHover();
	}
	
	@Override
	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
		return new DefaultAnnotationHover();
	}

	@Override
	public IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer sourceViewer) {
		
		if (sourceViewer == null) {
			return null;
		}

		return new IHyperlinkDetector[] { new URLHyperlinkDetector(), new GoHyperlinkDetector(editor) };
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
