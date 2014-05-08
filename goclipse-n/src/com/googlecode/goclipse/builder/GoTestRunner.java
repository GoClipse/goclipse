package com.googlecode.goclipse.builder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.go.CodeContext;
import com.googlecode.goclipse.go.lang.model.Function;

/**
 * 
 */
public class GoTestRunner {
	
	private static GoTestRunner instance = new GoTestRunner();
	
	private Queue<TestConfig>    testQueue     = new LinkedList<TestConfig>();
	
	/**
	 * queue guard is used to prevent the same test from existing in the
	 * queue more than once at any given time.
	 */
	private Map<String, String>  queueGuard    = Collections.synchronizedMap(new HashMap<String, String>());
	private Thread               testRunner    = new Thread("Go Test Runner");
	private TestConfig           activeTest    = null;
	private Process              activeProcess = null;
	private boolean              running       = true;
	
	private static MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		
		for (int i = 0; i < existing.length; i++) {
			if (name.equals(existing[i].getName())) {
				return (MessageConsole) existing[i];
			}
		}
		
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}
	
	/**
	 * 
	 */
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			
			while(running){
				
				// get test off of queue
				if (testQueue.size() > 0) {
					synchronized (instance.testQueue) {
						activeTest = testQueue.remove();
						queueGuard.remove(buildQueueGuardKey(activeTest));
                    }
				}
				
				if (activeTest != null) {
					runTest();
					activeTest = null;
				}
				
				try {
					synchronized (instance.testQueue) {
						while (testQueue.size() == 0) {
							testQueue.wait();
						}
					}
                } catch (InterruptedException e) {
                	Activator.logError(e);
                }
			}
		}

		/**
         * 
         */
        private void runTest() {
            
        	try {
            	final ProcessBuilder testProcessBuilder = configureProcess();
                activeProcess = testProcessBuilder.start();
                
                // kill process
                new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							int maxTime = Environment.INSTANCE.getAutoUnitTestMaxTime(activeTest.project);
	                        Thread.sleep(maxTime);
	                        Runtime rt = Runtime.getRuntime();
	                        if(activeTest!=null) {
		                        if (Activator.isWindows()) {
		                	        rt.exec("taskkill /F /IM " + activeTest.workingDir.getName()+ ".test.exe");
		                		} else if (Activator.isMac()) {
		                			rt.exec("killall -c " + activeTest.workingDir.getName() + ".test");
		                		} else {
		                			rt.exec("pkill " + activeTest.workingDir.getName() + ".test");
		                		}
	                        }
                        } catch (InterruptedException e) {
                        	 Activator.logError(e);
                        } catch (IOException e) {
                        	Activator.logError(e);
                        }
						
					}
				}).start();
                
                try {
                	activeProcess.waitFor();
            		
            	} catch (InterruptedException e) {
            		Activator.logInfo(e);
            	}
                
                markErrors();
                
            } catch (IOException e1) {
                Activator.logInfo("IOException caught during testing of "+activeTest.pkgPath);
            }
        }
        
        /**
         * @return
         */
        private ProcessBuilder configureProcess() {
            String[] testCmd = { activeTest.compilerPath,
                                 GoConstants.GO_TEST_COMMAND,
                                 "-test.run="+Environment.INSTANCE.getAutoUnitTestRegex(activeTest.project)
                               };
            
            final ProcessBuilder testProcessBuilder = new ProcessBuilder(testCmd).directory(activeTest.workingDir);
            testProcessBuilder.environment().put(GoConstants.GOROOT, activeTest.goroot);
            testProcessBuilder.environment().put(GoConstants.GOPATH, activeTest.goPath);
            testProcessBuilder.environment().put("PATH", activeTest.path);
            return testProcessBuilder;
        }
        
		/**
         * 
         */
        private void markErrors() {
            StreamAsLines sal = StreamAsLines.buildTestStreamAsLines(activeProcess);
            
            if (sal.getLines().size() > 0) {
            	processTestOutput(sal.getLines(), activeTest);
            }
        }
		
	};
	
	/**
	 * 
	 */
	private GoTestRunner(){
		testRunner = new Thread(runnable, "Go Test Runner");
		testRunner.start();
	}
	
	/**
	 * A naive key generator.
	 * @param config
	 * @return
	 */
	private String buildQueueGuardKey(TestConfig config) {
		return config.project.getName()+":"+config.pkgPath;
	}
	
	/**
     * @param project
     * @param compilerPath
     * @param file
     * @param pkgPath
     * @param workingDir
     * @param goPath
     * @param path
     * @param goroot
     * @param errorCount
     * @throws IOException
     */
    public static void scheduleTest(final IProject project,    final String compilerPath,
    		                        final IFile    file,       final String pkgPath,
                                    final File     workingDir, final String goPath,
                                    final String   path,       final String goroot,
                                    final int      errorCount) throws IOException {
    	
    	if ( errorCount == 0 && Environment.INSTANCE.getAutoUnitTest(project)) {
    	
    		TestConfig t = instance.new TestConfig(project, compilerPath, file, pkgPath, workingDir, goPath, path, goroot);
    		
    		synchronized (instance.testQueue) {
    			String key = instance.buildQueueGuardKey(t);
    			if(!instance.queueGuard.containsKey(key)){
    				instance.testQueue.add(t);
    				instance.queueGuard.put(key, key);
    				instance.testQueue.notify();
    			}
            }
    	}
    }
    
    private static void processTestOutput(List<String> lines, TestConfig activeTest) {
        
    	MessageConsole console = findConsole(activeTest.project.getName()+" Auto Test");
    	console.clearConsole();
    	console.activate();
    	MessageConsoleStream out = console.newMessageStream();
    	List<String> failedTests = new ArrayList<String>();
    	
	    try {
	    	boolean    success = true;
	        IContainer parent  = activeTest.file.getParent();
	        
	        for(int i = 0; i < lines.size(); i++) {
	        	
	        	String line = lines.get(i);
	        	out.write(line);
	        	out.write("\n");
	        	out.flush();
	        	
	        	if(line.startsWith("panic:")) {
	        		
	        		success = false;
	        		String stackTrace = "";
	        		
	        		for (;i < lines.size(); i++) {
	        			line = lines.get(i);
	        			stackTrace+=line+"\n";
	        			
	        			if (line.matches("(^.*_test.go:[0-9]+.*)") ) {
	        				String[] parts = line.split(":");
	        				
	        				if(parts.length > 0) {
	        					String[] fileParts = parts[0].trim().split(File.separatorChar=='\\' ? "\\\\" : File.separator);
	        					String filename = fileParts[fileParts.length-1];
	        					IResource testFile = parent.findMember(filename);
	            				
	            	    		if(parts.length > 1){
	            	    			parts = parts[1].split("\\+");
	            	    		}

	            	    		int lineNo = 1;
	            	    		lineNo = Integer.parseInt(parts[0].trim());
	            	    		MarkerUtilities.addMarker(testFile, lineNo, "A panic occurs during this test.\n"+stackTrace, IMarker.SEVERITY_ERROR);
	        				}
	        			} else if (line.contains("main.main()")) {
	        				
	        				break;
	        			} else {
	        				continue;
	        			}
	        		}
	        	} else if (line.matches("(^.*_test.go:[0-9]+:[0-9]+:.*)") ) {
	        		success = false;
	        		String[] parts = line.split(":");
	        		
	        		String message = "";
	        		if(parts.length > 3) {
	        			message = parts[3];
	        		}
	        		
	        		int lineNo = 1;
	        		lineNo = Integer.parseInt(parts[1]);
	        		IResource testFile = parent.findMember(parts[0]);
	        		MarkerUtilities.addMarker(testFile, lineNo, message, IMarker.SEVERITY_ERROR);
	        		
	        	} else if (line.matches("(^.*_test.go:[0-9]+:.*)") ) {
	        		success = false;
	        		String[] parts = line.split(":");
	        		
	        		String message = "";
	        		if(parts.length > 2){
	        			message = parts[2];
	        		}
	        		
	        		int lineNo = 1;
	        		lineNo = Integer.parseInt(parts[1]);
	        		IResource testFile = parent.findMember(parts[0]);
	        		MarkerUtilities.addMarker(testFile, lineNo, "Test: "+message, IMarker.SEVERITY_ERROR);
	        
	        	} else if (line.matches("(^.*--- FAIL:.*)") ) {
	        		success = false;
	        		failedTests.add(line.substring(0, line.indexOf('('))
	        				.replace("--- FAIL: ", "").trim());
	        	}
	        }
	        
	        if (success) {
	        	MarkerUtilities.addMarker(parent, 1, parent.getName()
	        			+ " tests were successful at "
	        			+ new Date(), IMarker.SEVERITY_INFO);
	        } else {
	        	// parse test file and mark correctly failed tests
	        	File        f       = parent.getLocation().toFile();
	        	CodeContext context = CodeContext.getTestCodeContext(activeTest.project, f);
	        	
	        	for (String name:failedTests){
	        		Function func = context.getFunctionForName(name+"()");
	        		if(func != null) {
	        			IResource res = parent.findMember(func.getFile().getName());
	        			MarkerUtilities.addMarker(res, func.getLine(), name+" failed.", IMarker.SEVERITY_ERROR);
	        		}
	        	}
	        }
	        out.flush();
	        
        } catch (NumberFormatException e) {
        	Activator.logInfo(e);
        } catch (IOException e) {
        	Activator.logInfo(e);
        }
    }
    
    /**
     * 
     */
    class TestConfig {
		IProject project;
		String   compilerPath;
        IFile    file;
        String   pkgPath;
        File     workingDir;
        String   goPath;
        String   path;
        String   goroot;
        
        public TestConfig(final IProject project,     final String compilerPath,
                          final IFile    file,        final String pkgPath,
                          final File     workingDir,  final String goPath,
                          final String   path,        final String goroot) {
        	
        	this.project      = project;
        	this.compilerPath = compilerPath;
        	this.file         = file;
        	this.pkgPath      = pkgPath;
        	this.workingDir   = workingDir;
        	this.goPath       = goPath;
        	this.path         = path;
        	this.goroot       = goroot;
        	
        }
	}
}
