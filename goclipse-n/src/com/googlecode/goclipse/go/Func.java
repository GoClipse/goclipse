package com.googlecode.goclipse.go;

public class Func implements ReferencialEntity {


	private Type    returnType;
	private String  displayName;
	private String  insertionName;
	
	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInsertionName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * @return the returnType
	 */
	public Type getReturnType() {
		return returnType;
	}

	/**
	 * @param returnType the returnType to set
	 */
	public void setReturnType( Type returnType) {
		this.returnType = returnType;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @param insertionName the insertionName to set
	 */
	public void setInsertionName(String insertionName) {
		this.insertionName = insertionName;
	}

}
