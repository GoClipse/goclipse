package com.googlecode.goclipse.debug.gdb;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author devoncarew
 */
public class GdbThread {
	GdbConnection connection;
	
	private String id;
	private List<GdbFrame> frames = new ArrayList<GdbFrame>();
	
	public GdbThread(GdbConnection connection, String id) {
		this.connection = connection;
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GdbThread) {
			GdbThread other = (GdbThread) obj;
			
			return getId().equals(other.getId());
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "GdbThread [id=" + id + "]";
	}
	
	public List<GdbFrame> getFrames() {
		return frames;
	}
	
	public void replaceFrames(List<GdbFrame> frames) {
		this.frames.clear();
		this.frames.addAll(frames);
	}

}
