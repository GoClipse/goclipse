package com.googlecode.goclipse.model;

import java.io.Serializable;

import org.eclipse.swt.graphics.Image;

import com.googlecode.goclipse.Activator;

/**
 * 
 * @author steel
 *
 */
public class Node implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 480313969022613471L;
	
	private   String 	name;
	private   String 	insertionText;
	private   Type      type;
	private   int    	line;
	private   String 	documentation;
	protected Image  	image;
	
	/**
	 * @return the documentation
	 */
	public String getDocumentation() {
		return documentation;
	}

	/**
	 * @param documentation the documentation to set
	 */
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	/**
	 * 
	 * @return
	 */
	public int getLine() {
		return line;
	}

	/**
	 * 
	 * @param line
	 */
	public void setLine(int line) {
		this.line = line;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * @return the insertionText
	 */
	public String getInsertionText() {
		return insertionText;
	}

	/**
	 * @param insertionText the insertionText to set
	 */
	public void setInsertionText(String insertionText) {
		this.insertionText = insertionText;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Image image) {
		this.image = image;
	}

	/**
	 * @return the image
	 */
	public Image getImage() {
		return Activator.getImage("icons/go.png");
	}

	@Override
	public int hashCode() {
		String name = getName();
		
		return name == null ? super.hashCode() : name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(this.getClass().isInstance(obj))) {
			return false;
		}
		
		Node other = (Node)obj;
		
		String name1 = getName();
		String name2 = other.getName();
		
		return name1 == null ? name1 == name2 : name1.equals(name2);
	}

}
