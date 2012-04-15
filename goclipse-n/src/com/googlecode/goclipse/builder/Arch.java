package com.googlecode.goclipse.builder;

/**
 * 
 * @author steel
 */
public enum Arch {

	x86  (".8", "8"),
	amd64(".6", "6"),
	arm  (".5", "5");

	private String	extension;
	private String	prefix;

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension
	 *            the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * 
	 * @param extension
	 */
	private Arch(String extension, String prefix) {
		this.extension = extension;
		this.prefix = prefix;
	}

	/**
	 * @param arch
	 * @return
	 */
	public static Arch getArch(String arch) {
		if (amd64.name().equals(arch)) {
			return amd64;
		} else if (x86.name().equals(arch) || "386".equals(arch)) {
			return x86;
		} else if (arm.name().equals(arch)) {
			return arm;
		}

		return null;
	}
}
