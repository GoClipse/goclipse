package com.googlecode.goclipse.editors;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

/**
 * 
 * @author steel
 */
public class ToggleCommentActionDelegate implements IEditorActionDelegate {

	private IEditorPart editorPart;
	
	@Override
	public void run(IAction action) {
		GoEditor editor;
		
		if (editorPart instanceof GoEditor) {
			editor = (GoEditor)editorPart;
		} else {
			editor = null;
		}
		
		if (editor != null) {
			Object obj = editor.getAdapter(Control.class);
			if (obj != null && obj instanceof StyledText) {
				StyledText st = (StyledText)obj;
				int caretPosition = st.getCaretOffset();
				
				final String currentContent = st.getText();
				
				// capture current line
				TextSelection selection = ((TextSelection)editor.getSelectionProvider().getSelection());
				int selectionStart = selection.getOffset();
				int selectionLength = selection.getLength();
				
				boolean addComment = false;
				for(int i = selection.getStartLine(); i <= selection.getEndLine(); i++){
					String line= st.getLine(i);
					if(!line.startsWith("//")){
						addComment = true;
					}
				}
				
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < st.getLineCount(); i++){
					
					if(i >= selection.getStartLine() && i <= selection.getEndLine()) {
						String line= st.getLine(i);

						if(addComment){
							caretPosition+=2;
							
							// calculate selection
							if(i ==  selection.getStartLine()){
								selectionStart+=2;
							}
							else{
								selectionLength+=2;
							}
							sb.append("//"+st.getLine(i)+"\n");
						}
						else{
							// calculate selection
							caretPosition-=2;
							if(i ==  selection.getStartLine()){
								selectionStart-=2;
							}
							else{
								selectionLength-=2;
							}
							sb.append(line.replaceFirst("//", "")+"\n");
						}
						
					}else{
						sb.append(st.getLine(i)+"\n");
					}
					
				}		
				
				st.setText(sb.toString());
				st.setCaretOffset(caretPosition);
				st.setSelectionRange(selectionStart, selectionLength);
				
			}			
		}		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		editorPart = targetEditor;		
	}

}
