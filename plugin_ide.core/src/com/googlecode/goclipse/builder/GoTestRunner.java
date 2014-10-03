package com.googlecode.goclipse.builder;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.builder.GoToolManager.RunGoToolTask;
import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.core.GoProjectPrefConstants;
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
	private boolean              running       = true;
	
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
            
            	final ProcessBuilder testProcessBuilder = configureProcess();
            	
                // timeout kill process
                new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							int maxTime = GoProjectPrefConstants.AUTO_UNIT_TEST_MAX_TIME.get(activeTest.project);
	                        Thread.sleep(maxTime);
	                        Runtime rt = Runtime.getRuntime();
	                        if(activeTest!=null) {
		                        if (MiscUtil.OS_IS_WINDOWS) {
		                	        rt.exec("taskkill /F /IM " + activeTest.workingDir.getName()+ ".test.exe");
		                		} else if (MiscUtil.OS_IS_MAC) {
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
            	RunGoToolTask runTestsTask = GoToolManager.getDefault().
            			createRunToolTask(testProcessBuilder, null, new NullProgressMonitor());
                
                ExternalProcessResult processResult = runTestsTask.startProcessAndAwait();
                
                String stdout = processResult.getStdOutBytes().toString();
                String stderr = processResult.getStdErrBytes().toString();
                
                markErrors(stdout, stderr);
                
            } catch (CoreException e) {
            	GoCore.logError("Error executing tests runner for " + activeTest.pkgPath, e);
			}
        }
        
        /**
         * @return
         */
        private ProcessBuilder configureProcess() {
            String[] testCmd = { activeTest.compilerPath,
                                 GoConstants.GO_TEST_COMMAND,
                                 "-test.run="+GoProjectPrefConstants.AUTO_UNIT_TEST_REGEX.get(activeTest.project)
                               };
            
            final ProcessBuilder testProcessBuilder = new ProcessBuilder(testCmd).directory(activeTest.workingDir);
            testProcessBuilder.environment().put(GoConstants.GOROOT, activeTest.goroot);
            testProcessBuilder.environment().put(GoConstants.GOPATH, activeTest.goPath);
            testProcessBuilder.environment().put("PATH", activeTest.path);
            return testProcessBuilder;
        }
        
        private void markErrors(String stdout, String stderr) {
            List<String> lines = StreamAsLines.buildTestStreamAsLines(
            	new StringReader(stdout), new StringReader(stderr));
			if (lines.size() > 0) {
				processTestOutput(lines, activeTest);
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
	
    public static void scheduleTest(final IProject project,    final String compilerPath,
    		                        final IFile    file,       final String pkgPath,
                                    final File     workingDir, final String goPath,
                                    final String goroot,
                                    final int      errorCount) {
		String path = System.getenv("PATH");
    	
    	if ( errorCount == 0 && GoProjectPrefConstants.ENABLE_AUTO_UNIT_TEST.get(project)) {
    	
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
        
    	List<String> failedTests = new ArrayList<String>();
    	
	    try {
	    	boolean    success = true;
	        IContainer parent  = activeTest.file.getParent();
	        
	        for(int i = 0; i < lines.size(); i++) {
	        	
	        	String line = lines.get(i);
	        	
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
	        		IResource testFile = parent.findMember(parts[0].trim());
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
	        		IResource testFile = parent.findMember(parts[0].trim());
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
	        
        } catch (NumberFormatException e) {
        	Activator.logError(e);
        } catch (IOException | CommonException e) {
        	Activator.logError(e);
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
