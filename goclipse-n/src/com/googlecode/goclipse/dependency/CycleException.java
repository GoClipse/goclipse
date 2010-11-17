package com.googlecode.goclipse.dependency;

import java.util.HashSet;
import java.util.Set;

public class CycleException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 479163738910643517L;
	private Set<String> nodes = new HashSet<String>();

	public CycleException() {
		super("Cycle detected");
	}

	public void addNode(String key) {
		nodes.add(key);
	}
	
	public Set<String> getNodes() {
		return nodes;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(" [");
		boolean first = true;
		for (String node : nodes) {
			if (!first) {
				builder.append(", ");
			}
			builder.append(node);
			first = false;
		}
		builder.append("]");
		return super.toString()+builder.toString();
	}

}
