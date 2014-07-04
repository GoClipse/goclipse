package com.googlecode.goclipse.go.lang.model;

import java.io.File;
import java.io.Serializable;

/**
 * 
 * @author steel
 * 
 */
public abstract class Node implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 480313969022613471L;

	private Package	          pkg;
	private File	          file;
	private String	          name;
	private String	          insertionText;
	private String	          type;
	private int	              line;
	private String	          documentation;
	private Scope             scope;

	public abstract ENodeKind getNodeKind();
	
	/**
	 * @param Package
	 */
	public void setPackage(Package pkg) {
		this.pkg = pkg;
	}

	/**
	 * @return Package
	 */
	public Package getPackage() {
		return this.pkg;
	}

	/**
	 * @return the documentation
	 */
	public String getDocumentation() {
		return documentation;
	}

	/**
	 * @param documentation
	 *            the documentation to set
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
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the insertionText
	 */
	public String getInsertionText() {
		return insertionText;
	}

	/**
	 * @param insertionText
	 *            the insertionText to set
	 */
	public void setInsertionText(String insertionText) {
		this.insertionText = insertionText;
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

		Node other = (Node) obj;

		String name1 = getName();
		String name2 = other.getName();

		return name1 == null ? name1 == name2 : name1.equals(name2);
	}

	/**
	 * @return
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @return
	 */
	public Scope getScope() {
    	return scope;
    }

	/**
	 * @param scope
	 */
	public void setScope(Scope scope) {
    	this.scope = scope;
    }

}
