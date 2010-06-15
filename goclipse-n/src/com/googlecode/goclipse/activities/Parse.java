package com.googlecode.goclipse.activities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.googlecode.goclipse.model.GoSourceMetadata;

/**
 * @author steel
 */
public class Parse {
   enum State {
      DEFAULT, FUNC, VAR
   }

   public static GoSourceMetadata parse(String filename, List<String> tokenData) throws IOException {
      BufferedReader br = new BufferedReader(new FileReader(filename));
      StringBuilder accumulator = new StringBuilder();
      char c = 0;
      int t = 0;

      while ((t = br.read()) != -1) {
         c = (char) t;

      }
      return null;
   }

   public static void main(String[] args) {

   }

}
