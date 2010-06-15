package com.googlecode.goclipse.debug.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.FileEditorInput;

import com.googlecode.goclipse.builder.GoLauncher;

/**
 * 
 * @author steel
 *
 */
public class LaunchShortcut implements ILaunchShortcut {

   @Override
   public void launch(ISelection selection, String mode) {
      System.out.println("launch shortcut: selection " + selection  + " mode " + mode);
      if (selection != null) {
    	  if (selection instanceof TreeSelection) {
    		  TreeSelection ts = (TreeSelection)selection;
    		  Object e = ts.getFirstElement();
    		  if (e instanceof IFile) {
    			  IFile file = (IFile)e;
    			  final String fName = file.getProjectRelativePath().toOSString();
    			  final String pName = file.getProject().getName();
    			  new Thread(new Runnable() {
    		    	  public void run() {
    		    		  GoLauncher.createExecutable(pName, fName);
    		    		  GoLauncher.execute(pName, fName, null);
    		    	  }
    		      }, fName).start();
    			  	  
    		  }
    	  }
    	  
      }

   }

   @Override
   public void launch(IEditorPart editor, String mode) {
	   System.out.println("launch shortcut: editor" + editor.getTitle()  + " mode " + mode);
	   IEditorInput ei = editor.getEditorInput();
	   if (ei != null && ei instanceof FileEditorInput) {
		   FileEditorInput fei = (FileEditorInput)ei;
		   IFile file = fei.getFile();
		   final String fName = file.getProjectRelativePath().toOSString();
			  final String pName = file.getProject().getName();
			  new Thread(new Runnable() {
		    	  public void run() {
		    		  GoLauncher.createExecutable(pName, fName);
		    		  GoLauncher.execute(pName, fName, null);
		    	  }
		      }, fName).start();
	   }
	   
   }

}
