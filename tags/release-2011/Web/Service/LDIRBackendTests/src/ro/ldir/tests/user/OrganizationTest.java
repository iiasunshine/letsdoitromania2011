package ro.ldir.tests.user;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ro.ldir.dto.Organization;
import ro.ldir.dto.Organization.OrganizationType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class OrganizationTest extends UserTest {
	static Organization org;

	@BeforeClass
	public static void addOrganization() {
		org = new Organization();
		org.setType(OrganizationType.ONG);
		org.setName("organization");

		WebResource r = client.resource(BASE + "organization");
		ClientResponse cr = resourceBuilder(r).entity(org,
				MediaType.APPLICATION_XML).post(ClientResponse.class);
		assertEquals(200, cr.getStatus());

		r = client.resource(BASE + "user/" + userId + "/organizations");
		cr = resourceBuilder(r).accept(MediaType.APPLICATION_XML).get(
				ClientResponse.class);
		assertEquals(200, cr.getStatus());

		Organization orgs[] = cr.getEntity(Organization[].class);
		assertEquals(1, orgs.length);
		org = orgs[0];
	}

	@AfterClass
	public static void removeOrganization() {
		WebResource r = client.resource(BASE + "organization/"
				+ org.getOrganizationId());
		ClientResponse cr = resourceBuilder(r).delete(ClientResponse.class);
		assertEquals(200, cr.getStatus());
	}

	@Before
	public void addTeam() {
		createTeam();
		setTeamId();
	}

	@Test
	public void joinTeam() {
		WebResource r = client.resource(BASE + "organization/"
				+ org.getOrganizationId() + "/team");
		ClientResponse cr = resourceBuilder(r).entity(insertedTeam,
				MediaType.APPLICATION_XML).post(ClientResponse.class);
		assertEquals(200, cr.getStatus());
	}

	@After
	public void removeTeam() {
		deleteTeam();
	}
}
