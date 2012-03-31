package com.googlecode.goclipse.dependency;

import java.util.HashMap;

/**
 * 
 */
class UndirectedEdge {
	
	String a = "";
	String b = "";
	
	/**
	 * 
	 */
	private static final HashMap<String, UndirectedEdge> edges = new HashMap<String, UndirectedEdge>();
	
	/**
	 * 
	 */
	private UndirectedEdge(String a, String b){
		this.a = a;
		this.b = b;
	}
	
	/**
	 * @param a
	 * @param b
	 * @return
	 */
	public static UndirectedEdge buildEdge (final String a, final String b) {
		UndirectedEdge edge;
		String key = "%";
		
		int order = a.compareTo(b);
		
		if(order <= 0){
			key  = a+key+b;
		} else {
			key = b+key+a;
		}
		
		edge = edges.get(key);
		
		if (edge==null) {
			edge = new UndirectedEdge(a, b);
			edges.put(key, edge);
		}
		
		return edge;
	}
}
