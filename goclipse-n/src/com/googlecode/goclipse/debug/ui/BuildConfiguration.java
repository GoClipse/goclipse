package com.googlecode.goclipse.debug.ui;

/**
 * 
 * @author steel
 */
public enum BuildConfiguration {

   DEBUG,
   RELEASE;

   /**
    * 
    * @param buildconfig
    * @return
    */
   public static BuildConfiguration get(String buildconfig) {
      if(DEBUG.toString().equals(buildconfig)){
         return DEBUG;
      }
      else if(RELEASE.toString().equals(buildconfig)){
         return RELEASE;
      }
      return null;
   }
}
