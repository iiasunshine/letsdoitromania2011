package ro.ldir.tests.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.CleaningEquipment;
import ro.ldir.dto.CleaningEquipment.CleaningType;
import ro.ldir.dto.Equipment;
import ro.ldir.dto.GpsEquipment;
import ro.ldir.dto.Team;
import ro.ldir.dto.TransportEquipment;
import ro.ldir.dto.TransportEquipment.TransportType;

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

	@After
	public void cleanupTeam() {
		removeAssignment();
		deleteTeam();
	}

	private void createTeam() {
		WebResource r = client.resource(BASE + "team");
		insertedTeam = new Team();
		insertedTeam.setTeamName(USER + "'s team" + new Date());
		ClientResponse cr = resourceBuilder(r).entity(insertedTeam,
				MediaType.APPLICATION_XML).post(ClientResponse.class);
		assertEquals(200, cr.getStatus());
	}

	private void deleteTeam() {
		WebResource r = client.resource(BASE + "team/"
				+ insertedTeam.getTeamId());
		ClientResponse cr = resourceBuilder(r).delete(ClientResponse.class);
		assertEquals(200, cr.getStatus());
	}

	private Team getTeam() {
		WebResource r = client.resource(BASE + "team/"
				+ insertedTeam.getTeamId());
		ClientResponse cr = resourceBuilder(r).get(ClientResponse.class);
		assertEquals(200, cr.getStatus());

		return cr.getEntity(Team.class);
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

	private void removeAssignment() {
		WebResource r = client.resource(BASE + "team/"
				+ insertedTeam.getTeamId() + "/chartArea/" + chartAreaId);
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
	public void testAllEquipments() {
		boolean gpsFound = false, cleaningFound = false, transportFound = false;
	
		testInsertCleaning();
		testInsertGps();
		testInsertTransport();

		Team team = getTeam();
		assertEquals(3, team.getEquipments().size());

		for (Equipment e : team.getEquipments()) {
			if (e instanceof GpsEquipment)
				gpsFound = true;
			if (e instanceof CleaningEquipment)
				cleaningFound = true;
			if (e instanceof TransportEquipment)
				transportFound = true;
		}

		assertTrue(gpsFound);
		assertTrue(cleaningFound);
		assertTrue(transportFound);
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

	@Test
	public void testInsertCleaning() {
		CleaningEquipment cleaning = new CleaningEquipment();
		cleaning.setCleaningType(CleaningType.BAGS);
		cleaning.setCount(10);

		WebResource r = client.resource(BASE + "team/"
				+ insertedTeam.getTeamId() + "/cleaning");
		ClientResponse cr = resourceBuilder(r).entity(cleaning,
				MediaType.APPLICATION_XML).put(ClientResponse.class);
		assertEquals(200, cr.getStatus());
	}

	@Test
	public void testInsertGps() {
		GpsEquipment gps = new GpsEquipment();
		gps.setCount(10);

		WebResource r = client.resource(BASE + "team/"
				+ insertedTeam.getTeamId() + "/gps");
		ClientResponse cr = resourceBuilder(r).entity(gps,
				MediaType.APPLICATION_XML).put(ClientResponse.class);
		assertEquals(200, cr.getStatus());
	}

	@Test
	public void testInsertTransport() {
		TransportEquipment transport = new TransportEquipment();
		transport.setTransportType(TransportType.CAR);

		WebResource r = client.resource(BASE + "team/"
				+ insertedTeam.getTeamId() + "/transport");
		ClientResponse cr = resourceBuilder(r).entity(transport,
				MediaType.APPLICATION_XML).put(ClientResponse.class);
		assertEquals(200, cr.getStatus());
	}
}
