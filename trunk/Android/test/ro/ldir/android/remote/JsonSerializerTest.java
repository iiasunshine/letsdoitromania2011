/**
 * 
 */
package ro.ldir.android.remote;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ro.ldir.android.entities.Garbage;

/**
 * @author cora
 *
 */
public class JsonSerializerTest
{

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

	/**
	 * Test method for {@link ro.ldir.android.remote.JsonSerializer#serialize(ro.ldir.android.remote.IBackendGarbage)}.
	 */
	@Test
	public void testSerialize()
	{
		JsonSerializer serializer = new JsonSerializer();
		IBackendGarbage garbage = new Garbage();
		garbage.setBagCount(10);
		garbage.setDetails("glavoi");
		String json = serializer.serialize(garbage);
		System.out.println(json);
	}

	/**
	 * Test method for {@link ro.ldir.android.remote.JsonSerializer#deserialize(java.lang.String)}.
	 */
	@Test
	public void testDeserialize()
	{
		fail("Not yet implemented");
	}

}
