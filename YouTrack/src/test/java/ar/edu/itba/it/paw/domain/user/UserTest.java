package ar.edu.itba.it.paw.domain.user;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ar.edu.itba.it.paw.domain.exception.DomainException;
import ar.edu.itba.it.paw.domain.exception.PrivilegeException;
import ar.edu.itba.it.paw.domain.task.MockTaskRepository;
import ar.edu.itba.it.paw.domain.task.TaskRepository;

public class UserTest {
	private UserRepository users;
	private TaskRepository tasks;

	public static UserRepository getUserRepository() {
		UserRepository userRepo = new MockUserRepository();

		User admin = new User("admin", "admin", "admin", "adminadmin", "admin@admin.com");
		admin.addPrivilege(User.Privilege.LOGIN);
		admin.addPrivilege(User.Privilege.ADMIN);
		userRepo.add(admin);
		
		User login = new User("login", "login", "login", "loginlogin", "login@login.com");
		login.addPrivilege(User.Privilege.LOGIN);
		userRepo.add(login);

		User banned = new User("banned", "banned", "banned", "banned", "banned@banned.com");
		userRepo.add(banned);
		
		return userRepo;
	}

	/**
	 * Antes de cada caso de prueba, dejamos en el repositorio de usuario un
	 * administrador, y un usuario login.
	 */
	@Before
	public void setContext() {
		users = new MockUserRepository();
		tasks = new MockTaskRepository();

		users = getUserRepository();
	}

	/**
	 * Checkea asignaciónes de privilegios válidas (Realizadas por un admin) -
	 * Dar privilegios de ADMIN/LOGIN - Revocar privilegios de ADMIN/LOGIN
	 */
	@Test
	public void legalPrivilegeTest() {
		User admin = users.getByUsername("admin");
		Assert.assertTrue(admin.isAdmin());

		User login = users.getByUsername("login");
		Assert.assertFalse(login.isAdmin());
		Assert.assertFalse(login.isBanned());

		admin.addPrivilegeTo(login, User.Privilege.ADMIN);
		Assert.assertTrue(login.isAdmin());

		admin.revokePrivilegeTo(login, User.Privilege.ADMIN, tasks);
		Assert.assertFalse(login.isAdmin());

		admin.revokePrivilegeTo(login, User.Privilege.LOGIN, tasks);
		Assert.assertTrue(login.isBanned());

		admin.addPrivilegeTo(login, User.Privilege.LOGIN);
		Assert.assertFalse(login.isBanned());
	}

	/**
	 * Asignación de permisos inválida
	 */
	@Test(expected = PrivilegeException.class)
	public void illegalPrivilegeTest() {
		User login = users.getByUsername("login");
		login.addPrivilegeTo(login, User.Privilege.ADMIN);
	}

	/**
	 * Remoción de permisos inválida
	 */
	@Test(expected = PrivilegeException.class)
	public void illegalPrivilegeTest2() {
		User login = users.getByUsername("login");
		login.revokePrivilegeTo(login, User.Privilege.LOGIN, tasks);
	}

	@Test
	public void legalUserCreation() {
		User admin = users.getByUsername("admin");
		User newUser = new User(admin, "newusername", "newuserlastname", "newuserusername", "newuserpassword", "newuser@newuser.com");
		Assert.assertTrue(newUser.getName().equals("newusername"));
		Assert.assertTrue(newUser.getLastName().equals("newuserlastname"));
		Assert.assertTrue(newUser.getUserName().equals("newuserusername"));
		Assert.assertTrue(newUser.getPassword().equals("newuserpassword"));
		Assert.assertTrue(newUser.getEmail().equals("newuser@newuser.com"));
	}

	/**
	 * Creador sin privilegios
	 */
	@Test(expected = PrivilegeException.class)
	public void illegalUserCreation() {
		User login = users.getByUsername("login");
		User newUser = new User(login, "newuser", "newuser", "newuser",
				"newuser", "newuser@newuser.com");
		newUser.getName();
	}

	/**
	 * Usuario vacío
	 */
	@Test(expected = DomainException.class)
	public void illegalUserCreation2() {
		User admin = users.getByUsername("admin");
		User newUser = new User(admin, "", "", "", "", "");
		newUser.getName();
	}

	/**
	 * Email inválido
	 */
	@Test(expected = DomainException.class)
	public void illegalUserCreation3() {
		User login = users.getByUsername("login");
		User newUser = new User(login, "newuser", "newuser", "newuser", "newuser", "newusernewuser.com");
		newUser.getName();
	}
	
	@Test
	public void checkUserStatus() {
		User login = users.getByUsername("login");
		User banned = users.getByUsername("banned");
		Assert.assertEquals(User.Status.BANNED, banned.getStatus(tasks));
		Assert.assertEquals(User.Status.FREE, login.getStatus(tasks));
	}
	
	@Test
	public void checkWorking() {
		User login = users.getByUsername("login");
		Assert.assertEquals(false, login.isWorking(tasks));
	}

}
