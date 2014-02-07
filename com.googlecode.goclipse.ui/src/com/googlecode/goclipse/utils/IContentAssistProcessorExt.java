package com.googlecode.goclipse.utils;

import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.ui.IEditorPart;

/**
 * 
 */
public interface IContentAssistProcessorExt extends IContentAssistProcessor {

  public void setEditorContext(IEditorPart editor);
  
}
