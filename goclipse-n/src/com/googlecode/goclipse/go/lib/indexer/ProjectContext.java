package com.googlecode.goclipse.go.lib.indexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.googlecode.goclipse.Environment;

/**
 * 
 * @author steel
 */
public class ProjectContext {
	
	/**
	 * 
	 */
	public static ProjectContext INSTANCE = new ProjectContext();
	
	/**
	 * 
	 */
	private HashMap<String, HashMap<Package, Package>> project = new HashMap<String, HashMap<Package, Package>>();
	
	/**
	 * 
	 */
	private ProjectContext() {}
	
	/**
	 * Get the context of the current project.
	 * @return
	 */
	public List<Package> getCurrentProjectContext() {
		return new ArrayList<Package>(project.get(Environment.INSTANCE.getCurrentProject().getName()).keySet());
	}
	
	/**
	 * Get the context of the current project.
	 * @return
	 */
	public void addCurrentProjectContext(Package package1){
		HashMap<Package, Package> map = project.get(Environment.INSTANCE.getCurrentProject().getName());
		
		if(map==null){
			map = new HashMap<Package, Package>();
			project.put(Environment.INSTANCE.getCurrentProject().getName(), map);
		}
		
		map.put(package1, package1);
	}
}
