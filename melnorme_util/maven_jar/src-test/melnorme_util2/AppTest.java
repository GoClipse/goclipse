package melnorme_util2;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public abstract class AppTest 
{
	
	@Test
	public void testBasic() throws Exception {
	}
	
	public static class InnerTest extends AppTest 
	{
		@Test
		public void testXXX() throws Exception {
//			throw new NullPointerException();
		}
	
	}
	
}
