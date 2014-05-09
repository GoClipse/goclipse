package com.googlecode.goclipse.go.antlr;

public class Import extends CodeUnit{
	
	enum Type {
		UNKNOWN,
		NORMAL, 	  // import   "lib/math"  math.Sin
		ALIAS, 		  // import M "lib/math"  M.Sin
		FULL_IMPORT,  // import . "lib/math"  Sin
		INIT_ONLY 	  // import _ "lib/math"  
	}
	
	public String alias;
	public String importPath;
	public Type   type = Type.UNKNOWN;  
	
	@Override
	public String toString() {
		return "import -> import_path=\""+importPath+"\" alias=\""+alias+"\" type=\""+type+"\" "+super.toString();
	}
}
