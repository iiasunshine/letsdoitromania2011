package ro.ldir.android.remote;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConnectionUtilsTest
{

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
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testGetWsAddress()
	{
		String url = ConnectionUtils.getWsAddress(ConnectionUtils.ACTION_SIGN_IN);
		System.out.println(url);
		assertFalse("No url returned", url == null || url.length() == 0);
	}
/*
	@Test
	public void testGetHttpConn()
	{
		fail("Not yet implemented");
	}*/

}
