package com.googlecode.goclipse.model;

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
public class Import {

   public String prefix = "";  // either none, some alias, or the package name
   public String path = "";

}
