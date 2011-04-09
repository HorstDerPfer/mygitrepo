package db.training.easy.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;
import db.training.easy.core.model.User;
import db.training.logwrapper.Logger;

/**
 * test fuer UserService. Der Test ist unvollstaendig.
 * 
 * @author hennebrueder
 * 
 */
public class UserServiceTest extends TestCase {

	// FIXME hoh: UserServiceTests laufen nicht (hoh)
	// FIXME hoh: Test-User aus DB löschen! (hoh)

	UserService userService;

	private List<User> toDelete;

	private static Logger log = Logger.getLogger(UserServiceTest.class);

	protected void setUp() throws Exception {
		super.setUp();
		userService = new UserServiceImpl();
		toDelete = new ArrayList<User>();
	}

	protected void tearDown() throws Exception {
		if (log.isDebugEnabled())
			log.debug("cleaning up " + toDelete.size() + " elements");
		/*
		 * ACHTUNG: es wird erwartet, dass die Sortierung der Liste foreign key constraints beachtet
		 * 
		 */
		for (Object obj : toDelete) {
			if (log.isDebugEnabled())
				log.debug("Delete: " + obj);
			userService.deleteUser(((User) obj).getId());
		}
		super.tearDown();
	}

	public void testFindAll() {
		User user = new User();
		user.setName("UserServiceTest2");
		user.setLoginName("UserServiceTest2");
		userService.createUser(user);
		toDelete.add(user);

		List<User> list = userService.findAllUsers();
		assertNotNull(list);
		assertTrue(list.size() > 0);
		assertTrue(list.contains(user));
	}

	public void testFindUsersById() {
		User user1 = new User();
		user1.setFirstName("User 1");
		user1.setName("UserServiceTest2");
		user1.setLoginName("UserServiceTest2");
		userService.createUser(user1);
		toDelete.add(user1);

		User user2 = new User();
		user2.setFirstName("User 2");
		user2.setName("UserServiceTest2");
		user2.setLoginName("UserServiceTest2");
		userService.createUser(user2);
		toDelete.add(user2);

		List<User> allUsers = userService.findAllUsers();
		assertNotNull(allUsers);
		assertTrue(allUsers.size() > 0);
		User firstUser = allUsers.get(0);
		User user = userService.findUserById(firstUser.getId());
		assertNotNull(user);
		assertEquals(firstUser.getId(), user.getId());

		// Erstelle Liste aller UserIds und erzeuge Zufallszahl, die keine
		// gültige UserId ist
		List<Integer> idList = new ArrayList<Integer>();
		for (User tmpUser : allUsers) {
			idList.add(tmpUser.getId());
		}
		Random r = new Random();
		int randomId = r.nextInt();
		while (idList.contains(randomId))
			randomId = r.nextInt();
		// Suche User per ungültiger UserId.
		user = userService.findUserById(randomId);
		assertNull(user);
	}

	public void testFindUsersByRoleName() {
		// TODO hoh: Wenn UserRoleType in UserDao gefixt ist, Test hier
		// vervollständigen (hoh)

		// User user1 = new User();
		// user1.setRoles();
		// user1.setName("UserServiceTest2");
		// getCurrentSession().save(user1);
		// toDelete.add(user1);

		/*
		 * List<User> list = userService.findUsersByRoleName(RoleName.PRODUCT_MANAGER);
		 * assertTrue(list.size() > 0);
		 * 
		 * list = userService.findUsersByRoleName(RoleName.ACCOUNT_MANAGER); assertTrue(list.size() >
		 * 0);
		 */
	}

	public void testFindUserByLoginName() {
		String login = "testFindUserByLoginName";
		User user = new User();
		user.setEmail(login);
		user.setName("UserServiceTest2");
		user.setLoginName("UserServiceTest2");
		userService.createUser(user);
		toDelete.add(user);

		User foundUser = userService.findUserByLoginName(login);
		assertNotNull(foundUser);

		user = userService.findUserByLoginName("badUserNamexyz123");
		assertNull(user);
	}

	public void testFindUserByEmail() {
		String email = "easy@bahn.de";
		User user = new User();
		user.setEmail(email);
		user.setName("UserServiceTest2");
		user.setLoginName("UserServiceTest2");
		userService.createUser(user);
		toDelete.add(user);

		User foundUser = userService.findUserByEmail(email);
		assertNotNull(foundUser);

		user = userService.findUserByEmail("badEmailNamexyz123");
		assertNull(user);
	}

	public void testFindUserByPipeLineId() {
		// TODO hoh: Test erstellen (hoh)
	}

	public void testConfirmUser() {
		// TODO hoh: Test erstellen (hoh)
	}

	public void testResetPassword() {
		// TODO hoh: Test erstellen (hoh)
	}

	public void testGeneratePassword() {
		// TODO hoh: Test erstellen (hoh)
	}

	public void testEncryptPassword() {
		User user = userService.findUserByLoginName("account");
		userService.updateUser(user);
	}

	public void testChangePassword() {
		// TODO hoh: Test erstellen (hoh)
	}

	public void testCreateUser() {
		String login = "UserServiceTest2";
		User user = new User();
		user.setEmail("test@test.de");
		user.setName(login);
		user.setLoginName(login);

		userService.createUser(user);
		toDelete.add(user);

		int userId = user.getId();
		User savedUser = userService.findUserById(userId);

		assertEquals(savedUser.getEmail(), "test@test.de");
		assertEquals(savedUser.getName(), login);
		assertEquals(savedUser.getLoginName(), login);
	}

	public void testFindUsersByLoginNames() {
		User user = new User();
		user.setEmail("test@test.de");
		user.setName("UserServiceTest2");
		user.setLoginName("UserServiceTest2");

		userService.createUser(user);
		toDelete.add(user);

		/*
		 * List<User> list = userService.findUsersByLoginnames("xy1234idkwsi");
		 * assertEquals("expect no user for cryptic random name", 0, list.size()); list =
		 * userService.findUsersByLoginnames("UserServiceTest2"); assertEquals(1, list.size());
		 */
	}
}
