package com.googlecode.goclipse.dependency;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.googlecode.goclipse.dependency.Graph.Vertex;

public class DependencyGraph implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3299801451389034052L;
	
	Graph graph = new Graph();
	
	/**
	 * Asserts that depender depends on dependee.
	 * 
	 * @param depender
	 * @param dependee
	 */
	public void addDependency(String depender, String dependee) {
		graph.addNode(depender);
		graph.addNode(dependee);
		graph.addEdge(depender, dependee);
	}
	
	public void addDependencies(String depender, String ... dependees) {
		graph.addNode(depender);
		for (String dependee : dependees) {
			graph.addNode(dependee);
			graph.addEdge(depender, dependee);
		}
	}
	
	public void removeItem(String item){
		graph.removeNode(item, true, true);
	}

	
	public List<String> getBuildSequence(Set<String> nodes) throws CycleException {
		return graph.getDependers(nodes);
	}

	public List<String> getCompleteBuildSequence() throws CycleException {
		return graph.topologicalSort();
	}


	/**
	 * Call visitor on all nodes in the graph that are ancestors of the
	 * given nodes, depth first
	 * 
	 * @param toBuild
	 * @param visitor
	 * @throws CycleException 
	 */
	public void accept(Set<String> toBuild, IDependencyVisitor visitor) throws CycleException {
		List<String> sequence = getBuildSequence(toBuild);
		acceptHelper(visitor, sequence);
	}

	/**
	 * Calls visitor on all nodes in the graph, depth first.
	 * @param visitor
	 * @throws CycleException 
	 */
	public void accept(IDependencyVisitor visitor) throws CycleException {
		List<String> sequence = getCompleteBuildSequence();
		acceptHelper(visitor, sequence);
	}

	private void acceptHelper(IDependencyVisitor visitor, List<String> sequence) {
		for (String aTarget : sequence) {
			Vertex vertex = graph.vertices.get(aTarget);
			if (vertex != null) {
				visitor.visit(aTarget, vertex.outboundLinks.toArray(new String[0]));
			}
		}
	}
	

	public void reset() {
		graph = new Graph();
	}
	
	@Override
	public String toString() {
		return graph.toString();
	}
}
