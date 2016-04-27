package melnorme.lang.ide.ui.build;

/**
 * A key/value set whose data is passed into Runtime.exec(...)
 */
public class EnvironmentVariable
{
	// The name of the environment variable
	private String name;
	
	// The value of the environment variable
	private String value;
	
	public EnvironmentVariable(String name, String value)
	{
		this.name = name;
		this.value = value;
	}

	/**
	 * Returns this variable's name, which serves as the key in the key/value
	 * pair this variable represents
	 * 
	 * @return this variable's name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Returns this variables value.
	 * 
	 * @return this variable's value
	 */
	public String getValue()
	{
		return value;
	}
		
//	/**
//	 * Sets this variable's value
//	 * @param value
//	 */
//	public void setValue(String value)
//	{
//		this.value = value;
//	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	
	@Override
	public boolean equals(Object obj) {
		boolean equal = false;
		if (obj instanceof EnvironmentVariable) {
			EnvironmentVariable var = (EnvironmentVariable)obj;
			equal = var.getName().equals(name);
		}
		return equal;		
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
