package com.googlecode.goclipse.builder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.go.lang.lexer.Lexer;
import com.googlecode.goclipse.go.lang.lexer.Tokenizer;
import com.googlecode.goclipse.go.lang.model.Import;
import com.googlecode.goclipse.go.lang.parser.ImportParser;
import com.googlecode.goclipse.preferences.PreferenceConstants;

public class GoGet {

	private List<String> pkgStartWith;
	private List<String> pkgEndWith;
	
	public GoGet() {
		pkgStartWith = new ArrayList<String>();
		pkgStartWith.add("code.google.com");
		pkgStartWith.add("github.com");
		pkgStartWith.add("bitbucket.org");
		pkgStartWith.add("launchpad.net");
		
		pkgEndWith = new ArrayList<String>();
		pkgEndWith.add(".git");
		pkgEndWith.add(".svn");
		pkgEndWith.add(".hg");
		pkgEndWith.add(".bzr");
	}
	
	/**
	 * @param project
	 * @param target
	 */
	public void goGetDependencies(final IProject project, IProgressMonitor monitor, java.io.File target) {
		
		final IPath projectLocation = project.getLocation();
		final String compilerPath   = GoCore.getPreferences().getString(PreferenceConstants.GO_TOOL_PATH);
		final IFile  file           = project.getFile(target.getAbsolutePath().replace(projectLocation.toOSString(), ""));

		try {
			monitor.beginTask("Importing external libraries for " + file.getName() + ":", 5);

			List<String> cmd        = new ArrayList<String>();
			cmd.add(compilerPath);
			cmd.add("get");
			cmd.add("-fix");
			cmd.add("-u");
			
			List<Import> imports = getImports(target);
			List<Import> extImports = new ArrayList<Import>();
			
			for (Import imp: imports) {
				boolean found = false;
				String test = imp.getName();
				for (String start: pkgStartWith) {
					if(test.startsWith(start)) {
						cmd.add(test);
						extImports.add(imp);
						found = true;
						break;
					}
				}
				if(!found) {
					for (String end: pkgEndWith) {
						if(test.contains(end)) {
							cmd.add(test);
							extImports.add(imp);
							break;
						}
					}
				}
			}
			
			monitor.worked(1);
						
			String PATH = System.getenv("PATH");
			PATH = PATH + ":/usr/local/bin";
			
			String goPath = projectLocation.toOSString();
			
	        ProcessBuilder builder = new ProcessBuilder(cmd).directory(target.getParentFile());
	        builder.environment().put(GoConstants.GOROOT, Environment.INSTANCE.getGoRoot(project));
	        builder.environment().put(GoConstants.GOPATH, goPath);
	        builder.environment().put("PATH", PATH);
	        Process p = builder.start();
      
			monitor.worked(3);
			
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				Activator.logInfo(e);
			}

			InputStream   is  = p.getInputStream();
			InputStream   es  = p.getErrorStream();
			StreamAsLines sal = new StreamAsLines();
			sal.setCombineLines(true);
			sal.process(is);
			sal.process(es);
			
			Activator.logInfo(sal.getLinesAsString());
//			boolean exMsg = true;
//			
//			try {
//	            project.deleteMarkers(MarkerUtilities.MARKER_ID, false, IResource.DEPTH_ZERO);
//            } catch (CoreException e1) {
//	            Activator.logError(e1);
//            }
//			
//			MarkerUtilities.deleteFileMarkers(file);
//			if (sal.getLines().size() > 0) {
//				
//				for (String line : sal.getLines()) {
//					if (line.startsWith("package")) {
//						String impt = line.substring(0,line.indexOf(" -"));
//						impt = impt.replaceFirst("package ", "");
//						for (Import i:extImports) {
//							if (i.path.equals(impt)) {
//								MarkerUtilities.addMarker(file, i.getLine(), line.substring(line.indexOf(" -")+2), IMarker.SEVERITY_ERROR);
//							}
//						}
//						
//					} else if (line.contains(".go:")) {
//						try {
//							String[] split 	    = line.split(":");
//							String   path 	    = "GOPATH/"+split[0].substring(split[0].indexOf("/src/")+5);
//							IFile    extfile    = project.getFile(path);
//							int      lineNumber = Integer.parseInt(split[1]);
//							String   msg 		= split[3];
//														
//							if(extfile!=null && extfile.exists()){
//								MarkerUtilities.addMarker(extfile, lineNumber, msg, IMarker.SEVERITY_ERROR);
//								
//							} else if (exMsg) {
//								exMsg = false;
//								MarkerUtilities.addMarker(file, "There are problems with the external imports in this file.\n" +
//										                        "You may want to attempt to resolve them outside of eclipse.\n" +
//										                        "Here is the GOPATH to use: \n\t"+goPath);
//							}
//							
//						} catch (Exception e){
//							Activator.logError(e);
//						}
//					}
//				}
//			}
			
			monitor.worked(1);

	        project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			
		} catch (IOException e1) {
			Activator.logInfo(e1);
		} catch (CoreException e2) {
			Activator.logInfo(e2);
		}
	}

	private List<Import> getImports(File file) throws IOException {
		// TODO this needs to be centralized into a common index...
		Lexer 		  lexer        = new Lexer();
		Tokenizer 	  tokenizer    = new Tokenizer(lexer);
		ImportParser  importParser = new ImportParser(tokenizer, file);
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String temp = "";
		StringBuilder builder = new StringBuilder();
		while( (temp = reader.readLine()) != null ) {
			builder.append(temp);
			builder.append("\n");
		}
		
		reader.close();
		lexer.scan(builder.toString());
		List<Import> imports = importParser.getImports();
		
		return imports;
	}
	
}
