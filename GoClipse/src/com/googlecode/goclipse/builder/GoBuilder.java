package com.googlecode.goclipse.builder;
// http://help.eclipse.org/galileo/topic/org.eclipse.platform.doc.isv/guide/resAdv_builders.htm

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class GoBuilder extends IncrementalProjectBuilder {

	private class BuildResourceCompiler {

		// To make life easier
		private IProject project = getProject();

		// Build status maps
		private Map<String,Vector<String>> packageFiles;
		private Map<String,Vector<String>> packageDeps;

		private IProgressMonitor monitor;

		public BuildResourceCompiler(IProgressMonitor monitor) 
		{
			this.monitor = monitor;
			packageFiles = new HashMap<String,Vector<String>>();
			packageDeps = new HashMap<String,Vector<String>>();
			
			monitor.setTaskName("Updating Makefile...");
		}

		public void fileChanged(IResource resource)
		{
			if (resource.isVirtual() || resource.getRawLocation() == null)
				return; // These aren't real

			if (!resource.getName().endsWith(".go"))
				return; // These aren't source files
			
			monitor.subTask("Scanning: " + resource.getName());

			String filepath = resource.getLocation().toOSString();
			String projpath = resource.getFullPath().toOSString();

			try 
			{
				BufferedReader reader = new BufferedReader(new FileReader(filepath));
				StringBuffer file = new StringBuffer(2048); // some "sensible" amount... 2kb?
				String line;

				while ((line = reader.readLine()) != null)
					file.append(line + "\n");

				// Compile the regular expressions we'll need
				Pattern comment = Pattern.compile("(?:/\\*.*?\\*/|//.*?$)", Pattern.MULTILINE);
				Pattern pkgname = Pattern.compile("^\\s*package\\s+(\\S+)", Pattern.MULTILINE);
				Pattern include = Pattern.compile("^\\s*import\\s*\\(([^)]+)\\)", Pattern.MULTILINE);
				Pattern shrtinc = Pattern.compile("\"(.+?)\"", Pattern.MULTILINE);
				Pattern longinc = Pattern.compile("^\\s*import\\s+(?:[^(\\s]+\\s*)?\"(.+?)\"", Pattern.MULTILINE);

				String[] pieces = comment.split(file);
				file = new StringBuffer(file.length());
				for (String piece : pieces)
				{
					file.append(piece);
				}

				String pkg = null;
				Matcher m = pkgname.matcher(file);
				if (m.find())
				{
					// Find the inner-project relative file name
					String relfile = projpath.substring(projpath.indexOf('/', 1)+1);
					pkg = m.group(1);
					System.out.println("Found package: " + pkg + " (" + relfile + ")");

					// Check to see if the map needs the key created
					if (!packageFiles.containsKey(pkg))
					{
						packageFiles.put(pkg, new Vector<String>());
						packageDeps.put(pkg, new Vector<String>());
					}

					// Add the file to the package in the map
					packageFiles.get(pkg).add(relfile);

				}
				if (pkg == null)
					return;

				m = include.matcher(file);
				Vector<String> deps = packageDeps.get(pkg);
				while (m.find())
				{
					Matcher x = shrtinc.matcher(m.group(1));
					while (x.find())
					{
						String incl = x.group(1);
						if (!deps.contains(incl))
							deps.add(incl);
					} // process each individual include
				} // Multiple include statements

				m = longinc.matcher(file);
				if (m.find())
				{
					// ** identical code to above **
					String incl = m.group(1);
					if (!deps.contains(incl))
						deps.add(incl);
				} // single, long include
				//System.out.println("Could not find package: " + projpath);
			}
			catch (FileNotFoundException ex)
			{
				// This shouldn't really happen
				System.out.println("Could not find file: " + ex.getLocalizedMessage());
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}

			//monitor.worked(1);
		}

		private InputStream makefileBuffer() {
			String exe = project.getName().toLowerCase();
			StringBuffer file = new StringBuffer(1024);

			// Print header
			file.append("### Usage\n");
			file.append("# make -f Makefile-generated <target>\n");
			file.append("#\n");
			file.append("# Targets:\n");
			file.append("# + all (the default)\n");
			file.append("#   - This will build all of the dependencies for and link gobir\n");
			file.append("# + clean\n");
			file.append("#   - This will delete the binary and the object directory\n");
			file.append("# + format\n");
			file.append("#   - This will format all of your source code with gofmt\n");
			file.append("\n");
			file.append("### Directories\n");
			file.append("ROOT   = $(subst \" \",\\ ,$(GOROOT))\n");
			file.append("GOBIN  = $(subst \" \",\\ ,$(HOME))/bin\n");
			file.append("OBJDIR = _obj/\n");
			file.append("\n");
			file.append("### Load architecture variables\n");
			file.append("include $(ROOT)/src/Make.$(GOARCH)\n");
			file.append("\n");
			file.append("### Compiling and Linking\n");
			file.append("COMP = $(GOBIN)/$(GC) -I$(OBJDIR)\n");
			file.append("LINK = $(GOBIN)/$(LD) -L$(OBJDIR)\n");
			file.append("PACK = $(GOBIN)/gopack grc\n");
			file.append("GFMT = $(GOBIN)/gofmt -w\n");
			file.append("\n");
			file.append("### Packages and Dependencies\n");

			// Print out variables for each package
			for (String pkg : packageFiles.keySet())
			{
				String var = pkg.toUpperCase();
				file.append("# Package: " + pkg + "\n");
				file.append(var + "_OBJECT = " + (pkg.equals("main") ? exe : pkg) + "\n");
				file.append(var + "_SOURCE =");
				for (String pkgfile : packageFiles.get(pkg))
					file.append(" " + pkgfile);
				file.append("\n");
				file.append("\n");
			}

			file.append("### Aggregates\n");
			file.append("PACKAGES =");
			Vector<String> depOrdered = new Vector<String>();
			for (String pkg : orderDeps("main", depOrdered))
				file.append(" " + pkg.toUpperCase());
			file.append("\n");
			file.append("OBJECTS = $(addprefix $(OBJDIR), $(addsuffix .$O, $(foreach PKG,$(PACKAGES),$($(PKG)_OBJECT))))\n");
			file.append("\n");
			file.append("### Default rule\n");
			file.append("all : $(OBJDIR) $(OBJECTS) $(MAIN_OBJECT)\n");
			file.append("\n");
			file.append("### Main binary\n");
			file.append("ifdef MAIN_OBJECT\n");
			file.append("$(MAIN_OBJECT) : $(OBJECTS)\n");
			file.append("\t$(LINK) -o $@ $(OBJDIR)$@.$O\n");
			file.append("endif\n");
			file.append("\n");
			file.append("### Objects\n");
			file.append("define package_template\n");
			file.append("$$(OBJDIR)$$($(1)_OBJECT).$$O: $$($(1)_SOURCE)\n");
			file.append("\t$$(COMP) -o $$@ $$^\n");
			file.append("\t$$(PACK) $$(@:.$$O=.a) $$@\n");
			file.append("endef\n");
			file.append("$(foreach PKG,$(PACKAGES),$(eval $(call package_template,$(PKG))))\n");
			file.append("\n");
			file.append("### Utility\n");
			file.append("clean :\n");
			file.append("\trm -rf $(OBJDIR) $(MAIN_OBJECT)\n");
			file.append("\n");
			file.append("format :\n");
			file.append("\t$(GFMT) $(foreach PKG,$(PACKAGES),$($(PKG)_SOURCE))\n");
			file.append("\n");
			file.append("$(OBJDIR) :\n");
			file.append("\tmkdir -p $(OBJDIR)\n");
			file.append("\n");

			file.append("### Makefile automatically generated by GoClipse\n");

			return new ByteArrayInputStream(file.toString().getBytes());
		}

		private Vector<String> orderDeps(String pkg, Vector<String> depOrdered) {
			if (!depOrdered.contains(pkg) && packageDeps.containsKey(pkg))
			{
				for (String dep : packageDeps.get(pkg))
					orderDeps(dep, depOrdered);
				depOrdered.add(pkg);
			}
			// If we're finding all dependencies (pkg main), see if any are left out
			if (pkg.equals("main"))
				for (String ppkg : packageFiles.keySet())
					if (!depOrdered.contains(ppkg))
						orderDeps(ppkg, depOrdered);
			return depOrdered;
		}

		public void compile(String raw_targets) 
		{
			try
			{
				monitor.worked(1);
				monitor.setTaskName("Writing Makefile...");
				
				// Write the makefile
				IFile mf = project.getFile("Makefile");
				if (mf.exists()) mf.setContents(makefileBuffer(), true, false, null);
				else             mf.create(     makefileBuffer(), true, null);
				
				monitor.worked(1);
				monitor.setTaskName("Launching `make`...");

				// Create the process builder
				ProcessBuilder make = new ProcessBuilder(new Vector<String>());
				
				// Set the working directory
				make.directory(mf.getRawLocation().removeLastSegments(1).toFile());
				
				// Create the command
				make.command().add("make");
				for (String piece : raw_targets.split("\\s+"))
					make.command().add(piece);
				
				// Set the environment
				if (!make.environment().containsKey("GOROOT"))
					make.environment().put("GOROOT", "/opt/go");
				if (!make.environment().containsKey("GOOS"))
					make.environment().put("GOOS", "darwin");
				if (!make.environment().containsKey("GOARCH"))
					make.environment().put("GOARCH", "amd64");
				
				// Combine stdout and stderr
				make.redirectErrorStream(true);
				
				// Launch the subprocess and close standard input
				Process proc = make.start();
				proc.getOutputStream().close();
				
				monitor.worked(1);
				monitor.setTaskName("Compiling:");
				
				// Create buffered input streams for the reader and writer
				BufferedReader stdout = new BufferedReader(new InputStreamReader(proc.getInputStream()));

				// Clear errors
				deleteMarkers();
				
				// Error finder
				Pattern error = Pattern.compile("^([^:]+?):([^:]+?):\\s+(.+)$");
				Matcher errors;
				
				// Read the output
				String line;
				while ((line = stdout.readLine()) != null)
				{
					// Check if the line is an error
					errors = error.matcher(line);
					if (errors.matches())
						addMarker(errors.group(1), errors.group(3), Integer.parseInt(errors.group(2)), IMarker.SEVERITY_ERROR);
					else
					{
						monitor.worked(1);
						monitor.subTask(line);
					}
						
					// Write to stdout
					System.out.println(line);
				}
				
				monitor.worked(1);
				monitor.done();
			}
			catch (CoreException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} // Compile everything we discovered
	}

	class GoResourceVisitor implements IResourceDeltaVisitor, IResourceVisitor {
		private BuildResourceCompiler buildList;

		public GoResourceVisitor(BuildResourceCompiler brc)
		{
			buildList = brc;
		}

		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
				// handle added resource
				buildList.fileChanged(resource);
				break;
			case IResourceDelta.REMOVED:
				// handle removed resource
				break;
			case IResourceDelta.CHANGED:
				// handle changed resource
				buildList.fileChanged(resource);
				break;
			}
			
			//return true to continue visiting children.
			return true;
		}

		public boolean visit(IResource resource) {
			buildList.fileChanged(resource);
			//return true to continue visiting children.
			return true;
		}
	}

	public static final String BUILDER_ID = "com.googlecode.goclipse.goBuilder";

	private static final String MARKER_TYPE = "com.googlecode.goclipse.goSyntaxMarker";

	private void addMarker(String filename, String message, int lineNumber, int severity) {
		IFile file = getProject().getFile(filename);
		try {
			IMarker marker = file.createMarker(MARKER_TYPE);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.SEVERITY, severity);
			if (lineNumber == -1) {
				lineNumber = 1;
			}
			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int,
	 *      java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@SuppressWarnings("rawtypes") // args is Map<String,String>
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
		monitor.beginTask("Starting Build...", 5);
		if (kind == FULL_BUILD) {
			fullBuild(monitor);
		} else { // INCREMENTAL_BUILD || AUTO_BUILD
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	private void deleteMarkersOn(IResource resource) {
		try {
			resource.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
		} catch (CoreException ce) {
		}
	}

	protected void fullBuild(final IProgressMonitor monitor)
	throws CoreException {
		BuildResourceCompiler brc = new BuildResourceCompiler(monitor);
		try {
			getProject().accept(new GoResourceVisitor(brc));
		} catch (CoreException e) {
		}
		brc.compile("clean all");
	}
	
	protected void incrementalBuild(IResourceDelta delta,
			IProgressMonitor monitor) throws CoreException {
		// the visitor does the work.
		BuildResourceCompiler brc = new BuildResourceCompiler(monitor);
		//delta.accept(new GoResourceVisitor(brc));
		getProject().accept(new GoResourceVisitor(brc));
		brc.compile("all");
	}

	/**
	 * @throws CoreException
	 */
	private void deleteMarkers() throws CoreException {
		getProject().accept(new IResourceVisitor() {
			public boolean visit(IResource resource) throws CoreException {
				if (resource.getName().endsWith(".go"))
					deleteMarkersOn(resource);
				return true;
			}
		});
	}
}
