package com.googlecode.goclipse.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
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
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.DefaultAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.RGB;

import com.googlecode.goclipse.Activator;

/**
 * @author steel
 */
public class Configuration extends SourceViewerConfiguration {
   private DoubleClickStrategy doubleClickStrategy;
   private GoScanner           keywordScanner;
   private GoEditor			   editor;
   
   /**
    * @param colorManager
    */
   public Configuration(GoEditor editor) {
	   this.editor = editor;
   }

   /**
	 * 
	 */
   public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
      return new String[] { IDocument.DEFAULT_CONTENT_TYPE, PartitionScanner.COMMENT, PartitionScanner.STRING, PartitionScanner.MULTILINE_STRING };
   }

   /**
	 * 
	 */
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
         keywordScanner.setDefaultReturnToken(new Token(new TextAttribute(ColorManager.INSTANCE.getColor(IColorConstants.DEFAULT))));
      }
      return keywordScanner;
   }

   /**
	 * 
	 */
   public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
      PresentationReconciler reconciler = new PresentationReconciler();

      DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getKeywordScanner());
      reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
      reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

      NonRuleBasedDamagerRepairer ndr = 
         new NonRuleBasedDamagerRepairer(new TextAttribute(ColorManager.INSTANCE
            .getColor(IColorConstants.COMMENT)));
      reconciler.setDamager(ndr, PartitionScanner.COMMENT);
      reconciler.setRepairer(ndr, PartitionScanner.COMMENT);

      NonRuleBasedDamagerRepairer nonRuleBasedDamagerRepairer = 
         new NonRuleBasedDamagerRepairer(new TextAttribute(ColorManager.INSTANCE
            .getColor(IColorConstants.STRING)));
      reconciler.setDamager(nonRuleBasedDamagerRepairer, PartitionScanner.STRING);
      reconciler.setRepairer(nonRuleBasedDamagerRepairer, PartitionScanner.STRING);
      
      NonRuleBasedDamagerRepairer nonRuleBasedDamagerRepairer2 = 
         new NonRuleBasedDamagerRepairer(new TextAttribute(ColorManager.INSTANCE
            .getColor(IColorConstants.MULTILINE_STRING)));
      reconciler.setDamager(nonRuleBasedDamagerRepairer2, PartitionScanner.MULTILINE_STRING);
      reconciler.setRepairer(nonRuleBasedDamagerRepairer2, PartitionScanner.MULTILINE_STRING);

      return reconciler;
   }

   /**
	 * 
	 */
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
//      ca.setInformationControlCreator(getInformationControlCreator(sv));
      return ca;
   }

private IContentAssistProcessor getCompletionProcessor() {
	IConfigurationElement[] config = Platform.getExtensionRegistry()
		.getConfigurationElementsFor(Activator.CONTENT_ASSIST_EXTENSION_ID);
	try {
		for (IConfigurationElement e : config) {
			final Object o = e.createExecutableExtension("class");
			if (o instanceof IContentAssistProcessor) {
				return (IContentAssistProcessor)o;
			}
		}
	} catch (CoreException ex) {
		// do nothing
	}
	return new CompletionProcessor();
}

   /**
    * 
    */
   public ITextHover getTextHover(ISourceViewer sv, String contentType) {
      return new TextHover();
   }

	@Override
	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
		return new DefaultAnnotationHover();
	}

	@Override
	public IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer sourceViewer) {
		if (sourceViewer == null)
			return null;

		return new IHyperlinkDetector[] {
			new URLHyperlinkDetector(),
			new GoHyperlinkDetector(editor)
		};
	}

}
