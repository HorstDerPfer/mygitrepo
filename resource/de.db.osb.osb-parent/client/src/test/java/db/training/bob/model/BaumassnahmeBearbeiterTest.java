package db.training.bob.model;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import db.training.easy.core.model.User;

/**
 * Anforderungen zu Bob-Ticket 76
 * 
 * @author gergs
 * 
 */
public class BaumassnahmeBearbeiterTest {

	Baumassnahme baumassnahme;

	final String login1 = "login1";

	@Before
	public void setUp() throws Exception {
		baumassnahme = new Baumassnahme();
	}

	@Test
	public void testBearbeiterEmpty() {
		assertEquals(0, baumassnahme.getBearbeiter().size());
	}

	@Test
	public void testBearbeiterAdd() {
		User user1 = new User();
		user1.setLoginName(login1);

		baumassnahme.addBearbeiter(user1);

		assertEquals(1, baumassnahme.getBearbeiter().size());

		List<Bearbeiter> bearbeiterList = baumassnahme.getBearbeiter();
		assertEquals(user1, bearbeiterList.get(0).getUser());
	}

	@Test
	public void testBearbeiterReplace() {
		User user1 = new User();
		user1.setLoginName(login1);

		baumassnahme.addBearbeiter(user1);

		List<Bearbeiter> bearbeiterList = baumassnahme.getBearbeiter();
		assertEquals(1, bearbeiterList.size());
		assertEquals(user1, bearbeiterList.get(0).getUser());
		assertEquals(true, bearbeiterList.get(0).getAktiv());

		baumassnahme.setBearbeiterStatus(user1, false);

		List<Bearbeiter> newBearbeiterList = baumassnahme.getBearbeiter();
		assertEquals(1, newBearbeiterList.size());
		assertEquals(user1, newBearbeiterList.get(0).getUser());
		assertEquals(false, newBearbeiterList.get(0).getAktiv());
	}

	@Test
	public void testBearbeiterDelete() {
		User bearbeiter1 = new User();

		baumassnahme.addBearbeiter(bearbeiter1);
		assertEquals(1, baumassnahme.getBearbeiter().size());

		baumassnahme.removeBearbeiter(bearbeiter1);
		assertEquals(0, baumassnahme.getBearbeiter().size());
	}

	@Test
	public void testBearbeiterNoDuplicates() {
		User bearbeiter1 = new User();
		bearbeiter1.setLoginName(login1);

		baumassnahme.addBearbeiter(bearbeiter1);
		assertEquals(1, baumassnahme.getBearbeiter().size());
		baumassnahme.addBearbeiter(bearbeiter1);
		assertEquals(1, baumassnahme.getBearbeiter().size());
	}

	@Test
	public void testBearbeiterOrder() {
		final String login2 = "login2";
		final String login3 = "login3";
		User bearbeiter1 = new User();
		bearbeiter1.setLoginName(login2);
		User bearbeiter2 = new User();
		bearbeiter2.setLoginName(login3);
		User bearbeiter3 = new User();
		bearbeiter3.setLoginName(login1);

		baumassnahme.addBearbeiter(bearbeiter1);
		baumassnahme.addBearbeiter(bearbeiter2);
		baumassnahme.addBearbeiter(bearbeiter3);

		assertEquals(3, baumassnahme.getBearbeiter().size());

		List<Bearbeiter> bearbeiterList = baumassnahme.getBearbeiter();
		assertEquals(bearbeiter1, bearbeiterList.get(0).getUser());
		assertEquals(bearbeiter2, bearbeiterList.get(1).getUser());
		assertEquals(bearbeiter3, bearbeiterList.get(2).getUser());
	}

}
