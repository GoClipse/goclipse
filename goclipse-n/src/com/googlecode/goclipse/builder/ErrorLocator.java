package com.googlecode.goclipse.builder;

import java.util.ArrayList;

/**
 * @author steel
 */
public class ErrorLocator {
   public static final ErrorLocator INSTANCE = new ErrorLocator();

   /**
    * Define the strategies for finding error locales
    */
   private LocationStrategy[]       errors;

   /**
    * Singleton Constructor
    */
   private ErrorLocator() {
      errors = new LocationStrategy[] {

      new LocationStrategy("fatal error: can't find import:") {
         @Override
         public boolean process(String error, LineData linedata, CompilerInfo info) {
            String line = linedata.line;

            if (error.contains(message)) {
               String token = error.replace(message, "");
               token = token.trim();
               token = "\"" + token;

               info.begin = linedata.charactersBeforeLine + line.indexOf(token) + OFFSET;
               info.end = info.begin + token.length();
               info.isLocationFound = true;
               return true;
            }

            return false;
         }
      },

      new LocationStrategy("undefined:") {
      },

      new LocationStrategy("syntax error near") {
      }

      };
   }

   /**
    * @param lines
    * @param info
    */
   public void locateError(ArrayList<LineData> lines, CompilerInfo info) {

      LineData linedata = lines.get(info.line - 1);
      String error = info.message;

      for (LocationStrategy strategy : errors) {
         if (strategy.process(error, linedata, info)) {
            return;
         }
      }
   }

   /**
	 * 
	 */
   class LocationStrategy {
      protected static final int OFFSET  = 2;
      protected String           message = null;

      public LocationStrategy(String message) {
         this.message = message;
      }

      public boolean process(String error, LineData linedata, CompilerInfo info) {
         String line = linedata.line;

         if (error.contains(message)) {
            String token = error.replace(message, "");
            token = token.trim();

            info.begin = linedata.charactersBeforeLine + line.indexOf(token) + OFFSET;
            info.end = info.begin + token.length();
            info.isLocationFound = true;
            return true;
         }

         return false;
      }
   }

}
