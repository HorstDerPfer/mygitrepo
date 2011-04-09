package db.training.bob.service;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class IsaConnectionTest {

	private Connection con = null;

	private final String USER = "zfis100";

	private final String PASSWORD = "BOB2009isaAS";

	// private final String HOST = "10.183.38.6";

	private final String HOST = "mellenbach.linux.rz.db.de";

	private final String PORT = "1960";

	private final String SERVICE = "ZFISP.prd.oracle.db.de";

	@Before
	public void setUp() throws ClassNotFoundException {
		Class.forName("oracle.jdbc.driver.OracleDriver");
	}

	@After
	public void tearDown() {
		try {
			con.close();
		} catch (SQLException e) {
		}
	}

	@Test
	@Ignore
	public void testConnection1() {
		try {
			con = DriverManager
			    .getConnection(
			        "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST="
			            + HOST + ")(PORT=" + PORT + ")))(CONNECT_DATA=(SID=" + SERVICE + ")))",
			        USER, PASSWORD);
		} catch (Exception e) {
			fail("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST="
			    + HOST + ")(PORT=" + PORT + ")))(CONNECT_DATA=(SID=" + SERVICE + ")))");
		}
	}

	@Test
	@Ignore
	public void testConnection2() {
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:" + USER + "/" + PASSWORD + "@"
			    + HOST + ":" + PORT + ":" + SERVICE);
		} catch (Exception e) {
			fail("jdbc:oracle:thin:" + USER + "/" + PASSWORD + "@" + HOST + ":" + PORT + ":"
			    + SERVICE);
		}
	}

	@Test
	@Ignore
	public void testConnection3() {
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:" + USER + "/" + PASSWORD + "@"
			    + HOST + ":" + PORT + ":" + SERVICE, USER, PASSWORD);
		} catch (Exception e) {
			fail("jdbc:oracle:thin:@" + HOST + ":" + PORT + ":" + SERVICE);
		}
	}

	@Test
	public void testConnection4() {
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:" + USER + "/" + PASSWORD + "@//"
			    + HOST + ":" + PORT + "/" + SERVICE);
		} catch (Exception e) {
			fail("jdbc:oracle:thin:" + USER + "/" + PASSWORD + "@//" + HOST + ":" + PORT + "/"
			    + SERVICE);
		}
	}

	@Test
	public void testConnection5() {
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:" + USER + "/" + PASSWORD + "@//"
			    + HOST + ":" + PORT + "/" + SERVICE);
		} catch (Exception e) {
			fail("jdbc:oracle:thin:" + USER + "/" + PASSWORD + "@//" + HOST + ":" + PORT + "/"
			    + SERVICE);
		}

	}

	@Test
	public void testConnection6() {
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@//" + HOST + ":" + PORT + "/"
			    + SERVICE, USER, PASSWORD);
		} catch (Exception e) {
			fail("jdbc:oracle:thin:" + USER + "/" + PASSWORD + "@//" + HOST + ":" + PORT + "/"
			    + SERVICE);
		}
	}
}
