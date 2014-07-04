package com.googlecode.goclipse.go.lang.model;

/**
 * <pre>
 * 
 * Import declaration          Local name of Sin
 * 
 * import   "lib/math"         math.Sin
 * import M "lib/math"         M.Sin
 * import . "lib/math"         Sin
 * 
 * import _ "lib/math"         no access, only runs initialization on given import
 * </pre>
 * 
 * @author stanleysteel
 */
public class Import extends Node {
	
	public enum PrefixType{
      NONE, ALIAS, INCLUDED, UNKNOWN
    }

	private static final long serialVersionUID = 1L;

	public PrefixType prefixType = PrefixType.UNKNOWN;
	public String prefix = ""; // either none, some alias, or the package name
	public String path = "";
	
	@Override
	public ENodeKind getNodeKind() {
		return ENodeKind.IMPORT;
	}
	
	@Override
	public String getName() {
		return path;
	}

}
