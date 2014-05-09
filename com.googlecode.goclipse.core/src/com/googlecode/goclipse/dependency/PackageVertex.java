package com.googlecode.goclipse.dependency;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;

import com.googlecode.goclipse.Environment;

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
	private String name;
	

	/**
	 * 
	 */
	private PackageVertex(IProject project, String name) {
		this.project = project;
		this.name = name;
	}

	/**
	 * @param project
	 * @param name
	 * @return
	 */
	static PackageVertex getPackageVertex(IProject project, String packageName) {
		String pkgOutPath =Environment.INSTANCE.getPkgOutputFolder(project).toOSString();
		if (packageName.contains(pkgOutPath)) {
			String p[] = packageName.split(pkgOutPath);
			packageName = p[1].replace(".a", "");
		}
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
		reverseDependencies.add(i);
	}
	
	/**
	 * @param i
	 */
	void removeReverseDependency(String i) {
		reverseDependencies.remove(i);
	}
	
	/**
	 * 
	 */
	void clearReverseDependencies() {
		reverseDependencies.clear();
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
