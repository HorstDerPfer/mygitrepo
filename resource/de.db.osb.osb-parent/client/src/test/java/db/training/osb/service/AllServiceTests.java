package db.training.osb.service;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Tested alle TestCases für OSB-Services.
 * 
 * @author nmoehring
 */
public class AllServiceTests extends TestSuite {

	public AllServiceTests(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		AllServiceTests serviceTests = new AllServiceTests("OSB-ServiceTests");

		// serviceTests.addTestSuite(BuendelServiceImplTest.class);

		return serviceTests;
	}

}
