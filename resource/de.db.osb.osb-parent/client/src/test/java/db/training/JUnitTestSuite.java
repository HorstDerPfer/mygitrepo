package db.training;

import junit.framework.Test;
import junit.framework.TestSuite;

public class JUnitTestSuite {

	public static Test suite() {
		TestSuite testSuite = new TestSuite("JUnit Test Suite");

		// Demo Testfaelle
		testSuite.addTestSuite(JUnitDemoTest.class);

		// Security Testfaelle
		// testSuite.addTestSuite(PasswordValidatorTest.class);
		// testSuite.addTestSuite(HibernateSecurityServicetest.class);
		// testSuite.addTestSuite(BaumassnahmeAnyVoterTest.class);
		// testSuite.addTestSuite(CriteriaTest.class);

		// OSB Testfaelle
		// testSuite.addTestSuite(BuendelServiceImplTest.class);
		// testSuite.addTestSuite(KorridorTest.class);
		// testSuite.addTestSuite(MasterBuendelTest.class);

		return testSuite;
	}
}
