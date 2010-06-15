package com.googlecode.goclipse.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.SysUtils;
/**
 * limitations: 
 * - dependency is computed at package level - full package is built everytime a file is changed
 * 
 * @author ami
 *
 */
public class GoDependencyManager implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient Map<String, String> env;
	private transient boolean caseInsensitive = false;

	private transient ExternalCommand depToolCmd = null;

	/**
	 * key = file (project relative path including file name) 
	 * value=list of files dependent on key file (relative to project paths)
	 */
	private Map<String, List<String>> dependents = new HashMap<String, List<String>>();
	/**
	 * key = file (path in project)
	 * value = declared package in source
	 */
	private Map<String, String> packages = new HashMap<String, String>();
	private Map<String, Package> rpacks = new HashMap<String, Package>();
	//package path, package
	private Map<String, Package> mpacks = new HashMap<String, Package>();
	
	//turns true after a full build
	private transient boolean fullBuild = false;
	//turns false after first incremental
	private transient boolean firstTime = true;
	

	public GoDependencyManager() {
		depToolCmd = new ExternalCommand(Environment.INSTANCE.getDependencyTool());
	}

	public void setEnvironment(Map<String, String> env) {
		this.env = env;
		if (env.get(GoConstants.GOOS).equals("windows")) {
			//paths in Windows are case insensitive. 
			//it will convert all paths to lowercase in maps for searching
			caseInsensitive = true;
		} else {
			caseInsensitive = false;
		}
	}

	/**
	 * removes dependencies for resources that will be removed.
	 * 
	 * @param toRemove
	 * @param pmonitor
	 */
	public void removeDep(List<IResource> toRemove, IProgressMonitor pmonitor) {
		for (IResource res : toRemove) {
			IPath relPath = res.getProjectRelativePath();
			String pathInPrj = relPath.toOSString();
			if (caseInsensitive) {
				pathInPrj = pathInPrj.toLowerCase();
			}
			
			//remove entries from files that have this resource as dependent
			//don't care about files that depend on the resource. they will be updated some other way
			removeFromParents(pathInPrj);
			//delete resource
			
			String dPackName = packages.get(pathInPrj);
			IPath dirPath = relPath.removeLastSegments(1);
			String fName = relPath.segment(relPath.segmentCount() - 1);
			String pPath = dirPath.toOSString();
			if (caseInsensitive) {
				pPath = pPath.toLowerCase();
				fName = fName.toLowerCase();
			}
			String key = pPath + dPackName;
			dependents.remove(pathInPrj);
			packages.remove(pathInPrj);
			Package p = mpacks.get(key);
			if (p!=null) {
				p.files.remove(fName);
				p.fPaths.remove(pathInPrj);
				if (p.files.size() == 0) {
					mpacks.remove(key);
				}
			}
		}
	}
	
	/**
	 * build direct dependency list: 
	 * - files in the same package
	 * - dependent files and files in their package
	 * @param targets
	 * @param addTargets
	 * @param usePaths
	 * @return
	 */
	public List<String> getDirectDep(List<IResource> targets, List<String> usePaths, boolean includeTargets) {
		List<String> paths = usePaths == null?new ArrayList<String>(): usePaths;
		Set<String> work = new HashSet<String>(paths);
		for (IResource res : targets) {
			IPath relPath = res.getProjectRelativePath();
			String pathInPrj = relPath.toOSString();
			IPath dirPath = relPath.removeLastSegments(1);
			String fName = relPath.segment(relPath.segmentCount() - 1);
			String pPath = dirPath.toOSString();
			if (caseInsensitive) {
				pPath = pPath.toLowerCase();
				fName = fName.toLowerCase();
				pathInPrj = pathInPrj.toLowerCase();
			}
			String dPackName = packages.get(pathInPrj);
			String key = pPath + dPackName;
			Package p = mpacks.get(key);
			//p.fPaths contains all package dependencies
			//add all first level dependencies of the package
			for (String packagePath: p.fPaths) {
				boolean eq = caseInsensitive?pathInPrj.equalsIgnoreCase(packagePath):pathInPrj.equals(packagePath);
				if (includeTargets || !eq) {
					work.add(packagePath);
				} 			
				List<String> dep = dependents.get(packagePath);
				if (dep != null) {
					work.addAll(dep);
				}	
			}
			
			
		}
		return new ArrayList<String>(work);
	}

	/**
	 * computes and updates dependencies for given files
	 * 
	 * @param toChange
	 * @param pmonitor
	 */
	public void buildDep(List<IResource> toChange,
			IProgressMonitor pmonitor) {
		depToolCmd.setEnvironment(env);
		List<String> depToolParams = new ArrayList<String>();
		StreamAsLines output = new StreamAsLines();
		depToolCmd.setResultsFilter(output);
		
		for (IResource res : toChange) {
			String fullPath = res.getLocation().toOSString();
			IPath relPath = res.getProjectRelativePath();
			String pathInPrj = relPath.toOSString();
			if (caseInsensitive) {
				pathInPrj = pathInPrj.toLowerCase();
			}
			
			//remove entries from files that have this resource as dependent
			//don't care about files that depend on the resource. they will be updated some other way
			removeFromParents(pathInPrj);
			
			IPath dirPath = relPath.removeLastSegments(1);
			depToolParams.clear();
			depToolParams.add(fullPath);
			depToolCmd.execute(depToolParams);
			boolean first = true;
			for (String pkgImport: output.getLines()) {
				//pkgImport (actually a file reference) may be global or relative to the path of the resource
				//if it is relative, it starts with .
				//global packages are ignored in source dependencies
				//first output line contains the name of the declared package with "p:" as prefix
				if (caseInsensitive) {
					pkgImport = pkgImport.toLowerCase();
				}
				if (first) {
					//handle package declaration
					first = false;
					String dPackName = pkgImport.substring(2,pkgImport.length());
					packages.put(pathInPrj, dPackName);
					//update package map
					String packPath = dirPath.toOSString();
					String origPath = packPath;
					String fName = relPath.segment(relPath.segmentCount() - 1);
					if (caseInsensitive) {
						packPath = packPath.toLowerCase();
						fName = fName.toLowerCase();
						dPackName = dPackName.toLowerCase();
					}
					String key = packPath + dPackName;
					Package p = mpacks.get(key);
					if (p == null) {
						p = new Package();
						p.name = dPackName;
						p.path = packPath;
						p.relPath = origPath; 
						mpacks.put(key, p);
					}
					if (p.files == null) {
						p.files = new ArrayList<String>();
						p.fPaths = new ArrayList<String>();
					}
					if (!p.files.contains(fName)) {
						p.files.add(fName);
						p.fPaths.add(pathInPrj);
					}
					rpacks.put(pathInPrj, p);
					//SysUtils.debug(p.toString());
				}
				//compute relative path to project for the imported file.
				String noQuotes = pkgImport.substring(1, pkgImport.length() - 1);
				if (!noQuotes.startsWith(".")) {
					//global packages are ignored in source dependencies
					continue;
				}
				IPath importedPath = dirPath.append(noQuotes);
				String iPath = importedPath.toOSString() + GoConstants.GO_SOURCE_FILE_EXTENSION;
				if (caseInsensitive) {
					iPath = iPath.toLowerCase();
				}
				List<String> deps = dependents.get(iPath);
				if (deps == null) {
					deps = new ArrayList<String>();
					dependents.put(iPath, deps);
				}
				deps.add(pathInPrj);
			}
		}
		//all packages should be in place now, create package dependency (works on all packages: slow, inefficient)
		packageDependency();
	}
	
	private void packageDependency() {
		//not smart, but safe it makes sure no idle dependencies between packages remain during incremental build
		for (String key: mpacks.keySet()) {
			Package p = mpacks.get(key);
			p.dependents.clear();
			p.parents.clear();
		}
		for (String key: mpacks.keySet()) {
			Package p = mpacks.get(key);
			for (String path :p.fPaths) {
				List<String> dep = dependents.get(path);
				if (dep != null) {
					for (String depPath: dep) {
						Package depPack = rpacks.get(depPath);
						if (!p.dependents.contains(depPack)) {
							p.dependents.add(depPack);
							depPack.parents.add(p);
						}
					}
				}
			}
		}
		//attach order number starting from packages that have no parents
		List<Package> topPackages = new ArrayList<Package>();
		for (Package p : mpacks.values()) {
			p.ord = null; //prepare for ordering below;
			if (p.parents.isEmpty()) {
				topPackages.add(p);
			}
		}
		//build order. go on the tree level by level first setting numbers on level then on all direct children of that level
		int ord = 0;
		List<Package> work = new ArrayList<Package>();
		List<Package> toExp = new ArrayList<Package>(topPackages);
		List<Package> visited = new ArrayList<Package>();
		while (!toExp.isEmpty()) {
			work.addAll(toExp);
			toExp.clear();
			boolean advanced = false;
			for (Package p : work) {
				if (allParentsHaveOrder(p)) {
					advanced = true;
					p.ord = ord++;	
					for (Package d: p.dependents) {
						if (!visited.contains(d) && !toExp.contains(d)) {
							toExp.add(d);
						}
					}
					visited.add(p);
				} else {
					toExp.add(p);//wait for all parents to be visited
				}
			}
			if (!advanced) {
				//there is a cycle
				//don't care about ordering
				
				SysUtils.debug("cycle" + work);
				//give one of them a number and continue
				Package p = work.get(0);
				p.ord = ord++;
				for (Package d: p.dependents) {
					if (!visited.contains(d) && !toExp.contains(d)) {
						toExp.add(d);
					}
				}
				visited.add(p);
				
			}
			work.clear();
		};
	}
	
	private boolean allParentsHaveOrder(Package pack) {
		boolean ret = true;
		for (Package p: pack.parents) {
			if (p.ord == null) {
				ret = false;
			}
		}
		return ret;
	}
	
	/**
	 * looks in dependents and removes all right side entries that refer to argument
	 * @param pathInPrj
	 */
	private void removeFromParents(String pathInPrj) {
		for (String parent: dependents.keySet()) {
			List<String> deps = dependents.get(parent);
			if (deps !=null) {
				List<String> newDeps = new ArrayList<String>();
				for (String dep: deps) {
					if (!dep.equals(pathInPrj)) {
						newDeps.add(dep);
					}
				}
				dependents.put(parent, newDeps);
			}
		}
	}

	/**
	 * clears dependencies for the project
	 * 
	 * @param pmonitor
	 */
	public void clearState(IProgressMonitor pmonitor) {
		dependents.clear();
		packages.clear();
		mpacks.clear();
	}
	
	public List<IResource> getResources(List<String> relPaths, IProject project) {
		List<IResource> res = new ArrayList<IResource>();
		for (String path : relPaths) {
			IResource r = project.getFile(path);
			if (r != null) {
				res.add(r);
			} else {
				SysUtils.warning("can't find resource: " + path);
			}
		}
		return res;
	}

	@Override
	public String toString() {
		return "GoDependencyManager [\ndependencies=" + depToolCmd + ",\n dependents="
				+ dependents + ",\n env=" + env + ",\n packages=" + packages + "\n]";
	}

	/**
	 * 
	 * @param paths
	 * @param pmonitor
	 * @return <package name>, List<project relative paths>
	 * there may be files in the same package but in different folders!
	 */
	public List<Package> sortForCompile(List<String> paths, IProgressMonitor pmonitor) {
		List<Package> packs = new ArrayList<Package>();//sorted List
		//package path, package
		Map<String, Package> mLpacks = mpacks;//new HashMap<String, Package>();
		for (String rPath : paths) {
			if (caseInsensitive) {
				rPath = rPath.toLowerCase();
			}
			IPath rp = Path.fromOSString(rPath);
			String fName = rp.segment(rp.segmentCount() - 1);
			IPath pp = rp.removeLastSegments(1);
			String pps = pp.toOSString();
			String dPackName = packages.get(rPath);
			if (dPackName == null) {
				//no longer in packages
				//ignore
				continue;
			}
			if (caseInsensitive) {
				pps = pps.toLowerCase();
				dPackName = dPackName.toLowerCase();
			}
			String key = pps + dPackName;
			Package p = mLpacks.get(key);
			if (!packs.contains(p)) {
				packs.add(p);
			}
		}
		
		//a package level sorting
		Collections.sort(packs, new Comparator<Package>() {
			@Override
			public int compare(Package o1, Package o2) {
				if (o1.equals(o2)) {
					return 0;
				} 
				int ret =  Integer.valueOf(o1.ord).compareTo(Integer.valueOf(o2.ord));
				return ret;
			}
		});

		return packs;
	}
	
	/**
	 * build entire dependency list for the given list of resources
	 * it goes recursively through dependents of dependents
	 * returns a list of project relative paths (including targets)
	 */
	public List<String> getDeepDep(List<String> targets) {
		List<String> paths = new ArrayList<String>();
		List<String> work = new ArrayList<String>();
		work.addAll(targets);
		while (work.size() != 0) {
			String path = work.remove(0);
			if (!paths.contains(path)) {
				List<String> dep = dependents.get(path);
				if (dep != null) {
					work.addAll(dep);
				}
			}
		}
		return paths;
	}

	public void prepareFull() {
		fullBuild = true;
	}

	public void prepareIncremental(IProject project) {
		
		if (!firstTime) {
			return;
		}
		firstTime = false;
		if (!fullBuild) {
			//if full build was not executed at least once this session
			//then load saved configuration
			loadSaved(project);
		}
		
	}
	
	public void loadSaved(IProject project) {
		SysUtils.debug("loading dependencies");
		IWorkspace root = ResourcesPlugin.getWorkspace();
		IPath base = root.getRoot().getLocation();
		IPath prj = base.append(".metadata").append(".go").append(".prj").append(project.getName());
		String inFile = prj.append("dep.data").toOSString();
		// Read from disk using FileInputStream
		FileInputStream fIn;
		try {
			fIn = new FileInputStream(inFile);
			// Read object using ObjectInputStream
			ObjectInputStream objIn = new ObjectInputStream(fIn);
			// Read an object
			Object obj = objIn.readObject();
			GoDependencyManager gdm = (GoDependencyManager)obj;
			if (gdm.dependents != null) {
				this.dependents = gdm.dependents;
			}
			if (gdm.packages != null) {
				this.packages = gdm.packages;
			}
			if (gdm.mpacks != null) {
				this.mpacks = gdm.mpacks;
			}
			if (gdm.rpacks != null) {
				this.rpacks = gdm.rpacks;
			}
			objIn.close();
			SysUtils.debug(gdm.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}



		
	}

	public void save(IProject project) {
		SysUtils.debug("saving dependencies for project " + project);
		
		IWorkspace root = ResourcesPlugin.getWorkspace();
		IPath base = root.getRoot().getLocation();
		IPath prj = base.append(".metadata").append(".go").append(".prj").append(project.getName());
		String prjPath = prj.toOSString();
		File prjFile = new File(prjPath);
		if (!prjFile.exists()) {
			prjFile.mkdirs();
		}
		String outFile = prj.append("dep.data").toOSString();
		try {
			// Write to disk with FileOutputStream
			FileOutputStream fOut = new FileOutputStream(outFile);
	
			// Write object with ObjectOutputStream
			ObjectOutputStream objOut = new	ObjectOutputStream (fOut);
	
			// Write object out to disk
			objOut.writeObject(this);
			objOut.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	

	
}
class Package implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String relPath;
	public Integer ord;
	String name; //declared in source
	String path; //relative path to project
	List<String> files; //local to package path
	List<String> fPaths; //relative path to project
	List<Package> parents = new ArrayList<Package>();
	List<Package> dependents = new ArrayList<Package>();
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Package other = (Package) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}
	@Override
	public String toString() {
		StringBuffer dep = new StringBuffer();
		for (Package p: dependents) {
			dep.append(" (p:").append(p.path).append(" n:").append(p.name).append(") \n");
		}
		return "Package [\nord=" + ord + "\nname=" + name + ",\npath="
				+ path + "\nfiles=" + files + "\nfPaths=" + fPaths + "\n" +
				"deps:" + dep.toString() +"\n]";
	}
	
	
}
