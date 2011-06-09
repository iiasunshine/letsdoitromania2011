package ro.ldir.tests.user;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.Team;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/** Test for joining a team. */
public class TeamOperationsTest extends UserTest {
	private Team insertedTeam;

	private void assignChartedArea() {
		ChartedArea toAssign = new ChartedArea();
		toAssign.setAreaId(chartAreaId);

		WebResource r = client.resource(BASE + "team/"
				+ insertedTeam.getTeamId() + "/chartArea");
		ClientResponse cr = resourceBuilder(r).entity(toAssign,
				MediaType.APPLICATION_XML).post(ClientResponse.class);
		assertEquals(200, cr.getStatus());
	}

	private void createTeam() {
		WebResource r = client.resource(BASE + "team");
		insertedTeam = new Team();
		insertedTeam.setTeamName(USER + "'s team" + new Date());
		ClientResponse cr = resourceBuilder(r).entity(insertedTeam,
				MediaType.APPLICATION_XML).post(ClientResponse.class);
		assertEquals(200, cr.getStatus());
	}

	@Test
	public void nameEquals() {
		WebResource r = client.resource(BASE + "user/" + userId
				+ "/managedTeams");
		ClientResponse cr = resourceBuilder(r).get(ClientResponse.class);
		assertEquals(200, cr.getStatus());

		Team managedTeams[] = cr.getEntity(Team[].class);
		assertEquals(1, managedTeams.length);
		assertEquals(insertedTeam.getTeamName(), managedTeams[0].getTeamName());
	}

	@After
	public void cleanupTeam() {
		removeAssignment();
		deleteTeam();
	}

	private void removeAssignment() {
		WebResource r = client.resource(BASE + "team/"
				+ insertedTeam.getTeamId() + "/chartArea/" + chartAreaId);
		ClientResponse cr = resourceBuilder(r).delete(ClientResponse.class);
		assertEquals(200, cr.getStatus());
	}

	private void deleteTeam() {
		WebResource r = client.resource(BASE + "team/"
				+ insertedTeam.getTeamId());
		ClientResponse cr = resourceBuilder(r).delete(ClientResponse.class);
		assertEquals(200, cr.getStatus());
	}

	private void setTeamId() throws ClientHandlerException {
		WebResource r = client.resource(BASE + "user/" + userId
				+ "/managedTeams");
		ClientResponse cr = resourceBuilder(r).get(ClientResponse.class);
		assertEquals(200, cr.getStatus());

		Team managedTeams[] = cr.getEntity(Team[].class);
		assertEquals(1, managedTeams.length);

		insertedTeam.setTeamId(managedTeams[0].getTeamId());
	}

	@Before
	public void setupTeam() {
		createTeam();
		setTeamId();
		assignChartedArea();
	}

	@Test
	public void testAssignedChartedAreas() {
		WebResource r = client.resource(BASE + "team/"
				+ insertedTeam.getTeamId() + "/chartArea");
		ClientResponse cr = resourceBuilder(r).get(ClientResponse.class);
		assertEquals(200, cr.getStatus());

		ChartedArea cas[] = cr.getEntity(ChartedArea[].class);
		assertEquals(1, cas.length);
		assertEquals(chartAreaId, cas[0].getAreaId().intValue());
	}

	@Test
	public void testChartedBy() {
		WebResource r = client.resource(BASE + "geo/chartedArea/" + chartAreaId
				+ "/chartedBy");
		ClientResponse cr = resourceBuilder(r).get(ClientResponse.class);
		assertEquals(200, cr.getStatus());

		Team teams[] = cr.getEntity(Team[].class);
		assertEquals(1, teams.length);
		assertEquals(insertedTeam.getTeamId(), teams[0].getTeamId());
	}
}
