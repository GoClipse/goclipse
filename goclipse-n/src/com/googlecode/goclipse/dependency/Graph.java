package com.googlecode.goclipse.dependency;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class Graph implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1459770139741600003L;

	static class Vertex implements Serializable {

		private static final long serialVersionUID = -7432158581916697448L;
		
		int outboundLinkCounter;
    	ArrayList<String> outboundLinks=new ArrayList<String>();
    	ArrayList<String> inboundLinks=new ArrayList<String>();
    }

    public HashMap<String,Vertex> vertices=new HashMap<String,Vertex>();


	void addNode(String key) {
	    if (((Vertex)vertices.get(key)) == null) {
	        vertices.put(key,new Vertex());
	    }
	}

	void addEdge(String k1,String k2) {
	    ArrayList<String> outboundLinks = vertices.get(k1).outboundLinks;
	    if (!outboundLinks.contains(k2)){
	    	outboundLinks.add(k2);
	    }
	    ArrayList<String> inboundLinks = vertices.get(k2).inboundLinks;
	    if (!inboundLinks.contains(k1)){
	    	inboundLinks.add(k1);
	    }
	}
	
	void removeNode(String key, boolean removeInbound, boolean removeOutbound){
		Vertex vertex = vertices.remove(key);
		if (vertex != null){
			if (removeInbound) {
				for (String outboundLink : vertex.outboundLinks) {
					Vertex aLinkedVertex = vertices.get(outboundLink);
					if (aLinkedVertex != null){
						aLinkedVertex.inboundLinks.remove(key);
					}
				}
			}
			if (removeOutbound) {
				for (String inboundLink : vertex.inboundLinks) {
					Vertex aLinkedVertex = vertices.get(inboundLink);
					if (aLinkedVertex != null){
						aLinkedVertex.outboundLinks.remove(key);
					}
				}
			}
		}
	}
	
	List<String> topologicalSort() {
		int numVertexes = vertices.size();
		LinkedList<String> sortedList = new LinkedList<String>();
		Stack<String> zero = new Stack<String>();
		for (String key : vertices.keySet()) {
			Vertex vertex = vertices.get(key);
			vertex.outboundLinkCounter = vertex.outboundLinks.size();
			if (vertex.outboundLinkCounter == 0) {
				zero.push(key);
			}
		}

		while (!zero.isEmpty()) {
			String u = zero.pop();
			sortedList.add(u);
			for (String o : vertices.get(u).inboundLinks) {
				Vertex x = (Vertex) vertices.get(o);
				x.outboundLinkCounter--;
				if (x.outboundLinkCounter == 0)
					zero.push(o);
			}
		}
		if (numVertexes != sortedList.size()) {
			return null;
		}
		return sortedList;
	}
	

	/**
	 * Returns all the items that depend on this
	 * @param target
	 * @return
	 */
	List<String> getDependers(Set<String> targets){
		LinkedList<String> sortedList = new LinkedList<String>();
		for (Vertex vertex : vertices.values()) {
			vertex.outboundLinkCounter = 0;
		}
		for (String target : targets) {
			dependerSubtreeHelper(target, sortedList);
		}
		return sortedList;
	}

	private List<String> dependerSubtreeHelper(String target,
			LinkedList<String> sortedList) {
		Vertex aVertex = vertices.get(target);
		if (aVertex != null){
			if (aVertex.outboundLinkCounter == 0){
				aVertex.outboundLinkCounter = 1;
				sortedList.add(target);
				for (String aLink : aVertex.inboundLinks) {
					dependerSubtreeHelper(aLink, sortedList);
				}
			}
		}
		return sortedList;
	}

	
	/** 
	 * Returns all the items that this target depend on
	 * @param target
	 * @return
	 */
	List<String> getDependencies(Set<String> targets){
		LinkedList<String> sortedList = new LinkedList<String>();
		for (Vertex vertex : vertices.values()) {
			vertex.outboundLinkCounter = 0;
		}
		for (String target : targets) {
			dependeeSubtreeHelper(target, sortedList);
		}
		return sortedList;
	}


	private List<String> dependeeSubtreeHelper(String target,
			LinkedList<String> sortedList) {
		Vertex aVertex = vertices.get(target);
		if (aVertex != null){
			if (aVertex.outboundLinkCounter == 0){
				aVertex.outboundLinkCounter = 1;
				for (String aLink : aVertex.outboundLinks) {
					dependeeSubtreeHelper(aLink, sortedList);
				}
				sortedList.add(target);
			}
		}
		return sortedList;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		List<String> nodes = topologicalSort();
		for (String node : nodes) {
			builder.append(node);
			builder.append("\n");
			for (String out : vertices.get(node).outboundLinks) {
				builder.append("  --> ");
				builder.append(out);
				builder.append("\n");
			}
			for (String in : vertices.get(node).inboundLinks) {
				builder.append("  <-- ");
				builder.append(in);
				builder.append("\n");
			}
		}
		return builder.toString();
	}
}