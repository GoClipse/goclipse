package com.googlecode.goclipse.editors;

import org.eclipse.jface.internal.text.html.BrowserInformationControl;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.IWidgetTokenKeeper;
import org.eclipse.jface.text.IWidgetTokenOwner;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Shell;

/**
 * @author steel
 */
public class Configuration extends SourceViewerConfiguration {
   private DoubleClickStrategy doubleClickStrategy;
   private GoScanner           keywordScanner;

   /**
    * @param colorManager
    */
   public Configuration() {
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
      ca.setInformationControlCreator(new IInformationControlCreator() {
         
         @Override
         public IInformationControl createInformationControl(Shell parent) {
//            DefaultInformationControl control = new DefaultInformationControl(parent, true);
//            ContentAssistInformationControl control = new ContentAssistInformationControl(parent, SWT.NONE );
            BrowserInformationControl control = new BrowserInformationControl(parent, "Arial", true);
            
//            control.
            return control;
         }
      });

      IContentAssistProcessor cap = new CompletionProcessor();
      ca.setContentAssistProcessor(cap, IDocument.DEFAULT_CONTENT_TYPE);
//      ca.setInformationControlCreator(getInformationControlCreator(sv));
      return ca;
   }

   /**
    * 
    */
   public ITextHover getTextHover(ISourceViewer sv, String contentType) {
      return new TextHover();
   }

}
