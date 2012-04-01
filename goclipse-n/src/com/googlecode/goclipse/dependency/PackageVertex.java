package com.googlecode.goclipse.dependency;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;

/**
 * 
 */
class PackageVertex {

	/**
	 * HashMap<String="ProjectName", HashMap<String="Package Name", PackageVertex>>
	 */
	private static HashMap<String, HashMap<String, PackageVertex>> existingVertices = new HashMap<String, HashMap<String,PackageVertex>>();
	
	/**
	 * 
	 */
	private Set<String>	dependencies = new HashSet<String>();
	
	/**
	 * 
	 */
	private Set<String>	reverseDependencies = new HashSet<String>();
	
	/**
	 * 
	 */
	private IProject project;
	
	/**
	 * 
	 */
	private String packageName;
	

	/**
	 * 
	 */
	private PackageVertex(IProject project, String packageName) {
		this.project = project;
		this.packageName = packageName;
	}

	/**
	 * @param project
	 * @param name
	 * @return
	 */
	static PackageVertex getPackageVertex(IProject project, String packageName) {
		
		HashMap<String, PackageVertex> map = existingVertices.get(project.getName());
		if (map == null) {
			map = new HashMap<String, PackageVertex>();
			existingVertices.put(project.getName(), map);
		}

		PackageVertex pv = map.get(packageName);
		if (pv == null) {
			pv = new PackageVertex(project, packageName);
			map.put(packageName, pv);
		}
		
		return pv;
	}
	
	/**
	 * @param i
	 */
	void addDependency(String i) {
		dependencies.add(i);
	}
	
	/**
	 * @param i
	 */
	void removeDependency(String i) {
		dependencies.remove(i);
	}
	
	/**
	 * 
	 */
	void clearDependencies() {
		dependencies.clear();
	}
	
	/**
	 * @param i
	 */
	void addReverseDependency(String i) {
		dependencies.add(i);
	}
	
	/**
	 * @param i
	 */
	void removeReverseDependency(String i) {
		dependencies.remove(i);
	}
	
	/**
	 * 
	 */
	void clearReverseDependencies() {
		dependencies.clear();
	}

	/**
	 * @return
	 */
	public Set<String> getDependencies() {
    	return dependencies;
    }

	/**
	 * @return
	 */
	public Set<String> getReverseDependencies() {
    	return reverseDependencies;
    }
	
}
