package ro.ldir.android.remote;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ro.ldir.android.entities.User;

public class JsonBackendTest
{
	private IBackend backend;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	@Before
	public void setUp() throws Exception
	{
		backend = new JsonBackend();
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testSignIn()
	{
		String user = "crl@mailinator.com";
		String pass = "crl";
		User userObj;
		try
		{
			userObj = backend.signIn(user, pass);
			assertTrue("Invalid user id", userObj.getUserId() != -1);
		} catch (RemoteConnError e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Test
	public void testSignOut()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testAddGarbage()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testAssignImageToGarbage()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetGarbageStatus()
	{
		fail("Not yet implemented");
	}

}
