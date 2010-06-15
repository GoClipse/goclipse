package com.googlecode.goclipse.external;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.SysUtils;
import com.googlecode.goclipse.builder.Arch;
import com.googlecode.goclipse.builder.CompilerInfo;
import com.googlecode.goclipse.builder.GoConstants;
import com.googlecode.goclipse.preferences.GoPreferencePage;
import com.googlecode.goclipse.preferences.PreferenceConstants;

/**
 * @author steel
 */
public class Compiler {

   private static final String OBJ_FILE_DIRECTORY       = "obj";

   private static final String EMPTY_STRING             = "";
   private static final String COLON                    = ":";
   private static final String SLASH                    = File.separator;
   private static final String RUNNING_COMMAND          = "running command: ";
   private static final String PROGRAM_TERMINATED       = "Program terminated!";
   private static final String PROJECT_PATH             = "Project path= ";
   private static final String CANNOT_FIND_PROJECT_PATH = "Cannot find project path!";

   /**
    * @param project
    * @param resource
    * @return
    * @throws InterruptedException
    * @throws IOException
    */
   public static ArrayList<CompilerInfo> compile(IProject project, IResource resource) throws InterruptedException, IOException {

      List<String> command = new ArrayList<String>();
      String compilerPath = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.COMPILER_PATH);
      String goroot = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GOROOT);
      String goos = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GOOS);
      String goarch = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GOARCH);
      String[] sourcepaths = Environment.INSTANCE.getSourceFoldersAsStringArray(project);
      String outputPath = Environment.INSTANCE.getOutputFolder(project) + SLASH + OBJ_FILE_DIRECTORY;

      validateGoSettings(goroot, goos, goarch);

      if (project != null && !project.exists(new Path(outputPath))) {
         IFolder folder = project.getFolder(outputPath);
         try {
            folder.create(true, true, null);
         }
         catch (CoreException e) {
            SysUtils.debug(e);
         }
      }

      // get the architecture
      Arch arch = Arch.getArch(goarch);

      // collect the files to compile into a package
      ArrayList<IResource> filesToCompile = determineFilesToCompile(resource, resource.getParent());

      // get the project path
      String projectpath = EMPTY_STRING;
      if (project.getLocation() == null) {
         SysUtils.warning(CANNOT_FIND_PROJECT_PATH);
      }
      else {
         projectpath = project.getLocation().toOSString();
         SysUtils.debug(PROJECT_PATH + projectpath);
      }

      // set command path to compiler
      command.add(compilerPath);

      // show all errors
      command.add(GoConstants.COMPILER_OPTION_E);

      setOutputDirectory(project, resource, command, sourcepaths, outputPath, arch, projectpath);

      /*
       * Set search paths
       */
      setSearchPaths(command, projectpath, outputPath);

      /*
       * File to compile
       */
      for (IResource iResource : filesToCompile) {
         command.add(iResource.getLocation().toOSString());
      }

      /*
       * Process to run
       */
      ProcessBuilder builder = new ProcessBuilder(command);
      builder.directory(new File(projectpath));

      SysUtils.debug(RUNNING_COMMAND + command.toString());

      Map<String, String> environ = builder.environment();
      environ.put(GoConstants.GOROOT, goroot);
      environ.put(GoConstants.GOOS, goos);
      environ.put(GoConstants.GOARCH, goarch);
      SysUtils.debug("$GOROOT=" + goroot);
      SysUtils.debug("$GOOS=" + goos);
      SysUtils.debug("$GOARCH=" + goarch);

      final Process process = builder.start();
      InputStream is = process.getInputStream();
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);
      String line;
      ArrayList<CompilerInfo> errors = new ArrayList<CompilerInfo>();

      while ((line = br.readLine()) != null) {
         SysUtils.debug(line);
         int goPos = line.indexOf(GoConstants.GO_SOURCE_FILE_EXTENSION);
         if (goPos == -1) {
        	 errors.add(new CompilerInfo(resource, 0, "no go extension"));
        	 errors.add(new CompilerInfo(resource, 0, line)); //show the line
        	 SysUtils.debug("no .go extension for file? this should not happen");	 
         } else {
        	 line = line.substring(goPos + GoConstants.GO_SOURCE_FILE_EXTENSION.length() + 1);
        	 String[] str = line.split(COLON, 2);
        	 int location = -1; //marker for trouble
        	 try {
        		 location = Integer.parseInt(str[0]);
        	 }catch(NumberFormatException nfe) {        		 
        	 }
             if (location != -1 && str.length > 1) {
                errors.add(new CompilerInfo(resource, Integer.parseInt(str[0]), str[1].trim()));
             } else {
            	 //play safe. to show something in UI
            	errors.add(new CompilerInfo(resource, 0, line)); 
             }
         }
         
      }
      SysUtils.debug(PROGRAM_TERMINATED);

      try {
         project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
      }
      catch (CoreException e) {
         e.printStackTrace();
      }
      return errors;
   }

   /**
    * setup search paths: -I option. the doc says each folder should have it's own -I
    * for now, add outputpath and its direct folders
    * in the future find dependent packages somehow and add only required  
    * @param command current command options
    * @param projectpath
    * @param outputPath
    */
   private static void setSearchPaths(List<String> command, String projectpath, String outputPath) {
	   String basePath = projectpath + SLASH + outputPath;
	   //use quotes to cover for spaces in folder names
	   command.add(GoConstants.COMPILER_OPTION_I +  "\"" + basePath + "\"");
	   File baseFolder = new File(basePath);
	   File[] folders = baseFolder.listFiles(new FileFilter() {
		@Override
		public boolean accept(File file) {
			return file.isDirectory();
		}
		   
	   });
	   for (File folder: folders) {
		   command.add(GoConstants.COMPILER_OPTION_I +  "\"" + folder.getAbsolutePath() + "\"");
	   }
}

/**
    * @param goroot
    * @param goos
    * @param goarch
    */
   private static void validateGoSettings(final String goroot, final String goos, final String goarch) {

      PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

         public void run() {
            if (  goroot == null || goroot.length() == 0 || 
                  goos   == null || goos.length()   == 0 ||
                  goarch == null || goarch.length() == 0 ) {               
               MessageDialog messageDialog = new MessageDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
                     "Goclipse Environment Configuration", null,
                     "The required Goclipse plug-in variables have not been set.  " +
                     "Please set the variables on the following preferences page.",
                     MessageDialog.QUESTION, new String[] { IDialogConstants.OK_LABEL }, 0);

               int i = messageDialog.open();

               PreferenceDialog pref = PreferencesUtil.createPreferenceDialogOn(
                     PlatformUI.getWorkbench().getDisplay().getActiveShell(), 
                     GoPreferencePage.ID, null, null);
               
               if (pref != null) {
                  pref.open();
               }
            }// end if
         }// end run
      });

   }

   /**
    * @param resource
    * @param parent
    * @return
    */
   private static ArrayList<IResource> determineFilesToCompile(IResource resource, IContainer parent) {
      ArrayList<IResource> filesToCompile = new ArrayList<IResource>();

      if (parent.getType() == IResource.FOLDER) {
         IFolder folder = (IFolder) parent;

         try {
            for (IResource iResource : folder.members()) {
               if (iResource.getName().endsWith(GoConstants.GO_SOURCE_FILE_EXTENSION) && iResource.getType() == IResource.FILE) {
                  filesToCompile.add(iResource);
               }
            }
         }
         catch (CoreException e) {
            e.printStackTrace();
         }
      }
      else {
         filesToCompile.add(resource);
      }
      return filesToCompile;
   }

   /**
    * @param project
    * @param resource
    * @param command
    * @param sourcepaths
    * @param outputPath
    * @param arch
    * @param projectpath
    */
   private static void setOutputDirectory(IProject project, IResource resource, List<String> command, String[] sourcepaths,
         String outputPath, Arch arch, String projectpath) {
      /*
       * Set output directory and file name
       */
      if (outputPath != null && outputPath.length() > 0) {
         for (String sourcefolder : sourcepaths) {
        	//parent's path
            String tpath = resource.getParent().getProjectRelativePath().toOSString();

            //getting stuff from src folder
            if (tpath.startsWith(sourcefolder)) {
               //compute output path by replacing sourcefolder prefix with outputpath
               //subfolders should be ok
               tpath = projectpath + SLASH + outputPath + tpath.substring(sourcefolder.length());
               File file = new File(tpath);
               //make sure folders are there
               file.mkdirs();

               command.add(GoConstants.COMPILER_OPTION_O + tpath + SLASH
                        + resource.getName().replace(GoConstants.GO_SOURCE_FILE_EXTENSION, arch.getExtension()));
               
               break;
            }
         }
      }
   }

}
