package com.googlecode.goclipse.debug.ui;

import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;

/**
 * 
 * @author steel
 *
 */
public class LaunchShortcut implements ILaunchShortcut {

   @Override
   public void launch(ISelection selection, String mode) {
      System.out.println("launch shortcut: selection " + selection  + " mode " + mode);

   }

   @Override
   public void launch(IEditorPart editor, String mode) {
	   System.out.println("launch shortcut: editor" + editor.getTitle()  + " mode " + mode);
   }

}
