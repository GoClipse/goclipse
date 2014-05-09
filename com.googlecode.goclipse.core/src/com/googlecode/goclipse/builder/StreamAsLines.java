package com.googlecode.goclipse.builder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.goclipse.Activator;

/**
 * 
 */
public class StreamAsLines implements ProcessIStreamFilter {

  private boolean combineLines;
  private List<String> lines = new ArrayList<String>();

  /**
	 * 
	 */
  public StreamAsLines() {
  }
  
  public static StreamAsLines buildStreamAsLines(Process p) {
	    InputStream is = p.getInputStream();
	    InputStream es = p.getErrorStream();
	    StreamAsLines sal = new StreamAsLines();
	    sal.setCombineLines(true);
	    sal.process(is);
	    sal.process(es);
	    return sal;
  }
  
  public static StreamAsLines buildTestStreamAsLines(Process p) {
	    InputStream is = p.getInputStream();
	    InputStream es = p.getErrorStream();
	    StreamAsLines sal = new StreamAsLines();
	    sal.setCombineLines(true);
	    sal.processTestStream(is);
	    sal.processTestStream(es);
	    return sal;
  }

  /**
   * If true then successive lines indented by a tab will be combined into one line. This is used by
   * {@link GoCompiler}.
   * 
   * @param value
   */
  public void setCombineLines(boolean value) {
    this.combineLines = value;
  }

  @Override
  public void process(InputStream iStream) {
    String line = "";
    try {
      InputStreamReader isr = new InputStreamReader(iStream, "UTF-8");
      BufferedReader br = new BufferedReader(isr);

      while ((line = br.readLine()) != null) {
        if (combineLines && line.startsWith("\t") && lines.size() > 0) {
          int index = lines.size() - 1;
          
          String prev = lines.get(index);
          
          // Here we start some special processing for known corner cases
          String note = "";
          if(prev.contains("main redeclared in this block")){
        	  note = "this sometimes happens with missing imports";
        	  
        	  // schedule a go get
          }
          
          if ("".equals(note)){
        	  lines.set(index, prev + " - " + line.substring(1));
          } else {
        	  lines.set(index, prev + " - " + line.substring(1)+"\n\t("+note+")");
          }
          
        } else if ("# command-line-arguments".equals(line)){
        	// TODO do special process for this
        } else if (line.startsWith("go version")){
        	// TODO do special process for this
        } else if(line.contains("executable file not found in $PATH")) {
        	line+="\n\t(you are missing a required tool)";
        } else {
          lines.add(line);
        }
      }
    } catch (Exception e) {
      Activator.logInfo(e);
    }
  }
  
  /**
   * @param iStream
   */
  public void processTestStream(InputStream iStream) {
    String line = "";
    try {
      InputStreamReader isr = new InputStreamReader(iStream, "UTF-8");
      BufferedReader br = new BufferedReader(isr);

      while ((line = br.readLine()) != null) {
        if ("# command-line-arguments".equals(line)){
        	// TODO do special process for this
        } else if (line.startsWith("go version")){
        	// TODO do special process for this
        } else if(line.contains("executable file not found in $PATH")) {
        	line+="\n\t(you are missing a required tool)";
        } else {
          lines.add(line);
        }
      }
    } catch (Exception e) {
      Activator.logInfo(e);
    }
  }

  /**
   * @return
   */
  public List<String> getLines() {
    return lines;
  }

  /**
   * @return
   */
  public String getLinesAsString() {
    if (lines.size() == 0) {
      return null;
    }

    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < lines.size(); i++) {
      if (i > 0) {
        builder.append("\n");
      }

      builder.append(lines.get(i));
    }

    return builder.toString();
  }

  @Override
  public void clear() {
    lines.clear();
  }

}
