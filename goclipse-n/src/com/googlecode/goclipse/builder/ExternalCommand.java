package com.googlecode.goclipse.builder;

import com.googlecode.goclipse.Activator;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * helper class to run an external process.
 *
 */
public class ExternalCommand {
	private String command;
	private ProcessBuilder pBuilder;
	private Process p;
	private List<String> args = new ArrayList<String>();
	private ProcessIStreamFilter resultsFilter;
	private ProcessIStreamFilter errorFilter;
	private ProcessOStreamFilter inputFilter;
	private boolean blockUntilDone = true;
	private long timeout = 0L;
	
	/**
	 * new external command using a full path
	 * @param command
	 */
	public ExternalCommand(String command) {
		this(command, true);
	}
	
  public ExternalCommand(IPath path, boolean blockUntilDone) {
    this(path.toOSString(), blockUntilDone);
  }
  
	public ExternalCommand(String command, boolean blockUntilDone) {
		this.command = command;
		this.blockUntilDone  = blockUntilDone;
		pBuilder = new ProcessBuilder(args);
		
		if (command != null && command.length() > 0) {
		  String workingFolder = Path.fromOSString(command).removeLastSegments(1).toOSString();
		  setWorkingFolder(workingFolder);
		}
	}

	public void setEnvironment(Map<String, String> env) {
		if (env != null) {
			pBuilder.environment().putAll(env);
		}
	}
	
	public void setWorkingFolder(String folder) {
	  if (folder != null && folder.length() > 0) {
	    pBuilder.directory(new File(folder));
	  }
	}
	
	public void setTimeout(long milliseconds){
	    this.timeout = milliseconds;
	}
	
	public void setResultsFilter(ProcessIStreamFilter resultsFilter) {
		this.resultsFilter = resultsFilter;
	}

	public void setErrorFilter(ProcessIStreamFilter errorFilter) {
		this.errorFilter = errorFilter;
	}
	
	public void setInputFilter(ProcessOStreamFilter inputFilter) {
		this.inputFilter = inputFilter;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public boolean commandExists() {
	  if (command == null || command.length() == 0) {
	    return false;
	  } else {
  		File file = new File(command);
  		
  		return file.exists();
	  }
	}
	
	public void destroy(){
		if (p!=null){
			p.destroy();
		}
	}
	
	/**
	 * returns an error string or null if no errors occured
	 * @param parameters
	 * @return
	 */
	public String execute(List<String> parameters) {
		return execute(parameters, false);
	}
	
	public String execute(List<String> parameters, boolean exitStatusIsError) {
		String rez = null;
		try {
			args.clear();
			args.add(command);
			if (parameters != null) {
				args.addAll(parameters);
			}
			Activator.logInfo(pBuilder.directory() + " executing: " +  args);
			
			p = pBuilder.start();
			InStreamWorker processOutput = new InStreamWorker(p.getInputStream(), "output stream thread");
			processOutput.setFilter(resultsFilter);
			InStreamWorker processError = new InStreamWorker(p.getErrorStream(), "error stream thread");
			processError.setFilter(errorFilter);
			
			if (inputFilter != null) {
				inputFilter.setStream(p.getOutputStream());
			}
						
			processOutput.start();
			processError.start();
			if(timeout>0){
                Thread.sleep(timeout);
                p.destroy();
			}
			if (blockUntilDone) {
				processOutput.join();
				processError.join();
			}

			if (exitStatusIsError) {
				int exitValue = p.waitFor();
				if (exitValue != 0){
					rez = this.command+" completed with non-zero exit value ("+exitValue+")";
				}
			}
			//done
		} catch (Exception e) {
			Activator.logError(e);
			rez = e.getLocalizedMessage();
		}
		
		return rez;
	}
	
	
	private class InStreamWorker extends Thread {
		private InputStream is;
		private ProcessIStreamFilter filter;
		public InStreamWorker(InputStream is, String threadName) {
			super(threadName);
			this.is = is;
		}
		public void setFilter(ProcessIStreamFilter filter) {
			this.filter = filter;
			
		}
		@Override
    public void run() {
			try {
				if (filter != null) {
					filter.process(is);
				}
				consume(is); //make sure it consumes everything
				is.close();
			} catch (IOException e) {
				Activator.logInfo(e);
			}
		}
	}
	private void consume(InputStream is) {
		//go through stream up to the end
		byte[] buf = new byte[256];
		try {
			while(is.read(buf, 0, buf.length)>0) {
				//just consume data
			}
		}catch(IOException e) {
			//ignore
		}
	}
	public Map<String, String> environment() {
		// TODO Auto-generated method stub
		return pBuilder.environment();
	}
	
	public String getCommand() {
		return command;
	}

}
