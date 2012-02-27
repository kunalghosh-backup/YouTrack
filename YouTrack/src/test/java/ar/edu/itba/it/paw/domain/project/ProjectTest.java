package ar.edu.itba.it.paw.domain.project;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ar.edu.itba.it.paw.domain.exception.DomainException;
import ar.edu.itba.it.paw.domain.exception.PrivilegeException;
import ar.edu.itba.it.paw.domain.service.MessagingService;
import ar.edu.itba.it.paw.domain.service.MessagingServiceTestImpl;
import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.domain.task.TaskTest;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepository;
import ar.edu.itba.it.paw.domain.user.UserTest;

import com.google.common.collect.Iterables;

public class ProjectTest {

	private UserRepository users;
	private TaskRepository tasks;
	private ProjectRepository projects;
	
	public static ProjectRepository getProjectRepository(UserRepository userRepo) {
		ProjectRepository projectRepo = new MockProjectRepository();
		
		User admin = userRepo.getByUsername("admin");
		
		Project project = new Project(admin, new DateTime(1500), "PROJECT TEST", "T", "TestDescription", Project.Status.PRIVATE, admin);
		projectRepo.add(project);
		
		return projectRepo;
	}
	
	@Before
	public void setContext() {
		users = UserTest.getUserRepository();
		projects = ProjectTest.getProjectRepository(users);
		tasks = TaskTest.getTaskRepository(users, projects);
	}
	
	/**
	 * Testea agregación y remoción legal de miembros
	 */
	@Test
	public void legalMemberTest() {
		Project project = projects.get(1);
		User admin = users.getByUsername("admin");
		User login = users.getByUsername("login");
		
		MessagingService ms = new MessagingServiceTestImpl();
		int size = Iterables.size(project.getMembers());
		Assert.assertTrue(size == 1);
		project.addMember(admin, login, ms);
		size = Iterables.size(project.getMembers());
		Assert.assertTrue(size == 2);
		project.addMember(admin, login, ms);
		size = Iterables.size(project.getMembers());
		Assert.assertTrue(size == 2);
		project.removeMember(admin, login);
		size = Iterables.size(project.getMembers());
		Assert.assertTrue(size == 1);
	}
	
	/**
	 * Testea agregación inválida de miembro
	 */
	@Test(expected = PrivilegeException.class)
	public void illegalMemberTest() {
		Project project = projects.get(1);
		User login = users.getByUsername("login");
		MessagingService ms = new MessagingServiceTestImpl();
		project.addMember(login, login, ms);
	}
	
	/**
	 * Testea remoción inválida (remover el lider)
	 */
	@Test(expected = DomainException.class)
	public void illegalMemberTest2() {
		Project project = projects.get(1);
		User admin = users.getByUsername("admin");
		project.removeMember(admin, admin);
	}
	
	/**
	 * Testea agregación y remoción legal de version
	 */
	@Test
	public void legalVersionTest() {
		Project project = projects.get(1);
		User admin = users.getByUsername("admin");
		project.addVersion(admin, new DateTime(System.currentTimeMillis()), ProjectVersion.Status.OPEN, "VersionTest", "Version de prueba");
		int size = Iterables.size(project.getVersions());
		Assert.assertTrue(size == 1);
		project.addVersion(admin, new DateTime(System.currentTimeMillis()), ProjectVersion.Status.OPEN, "VersionTest2", "Version de prueba2");
		size = Iterables.size(project.getVersions());
		Assert.assertTrue(size == 2);

		ProjectVersion v = Iterables.get(project.getVersions(), 0);
		project.removeVersion(admin, v, tasks);
		v = Iterables.get(project.getVersions(), 0);
		project.removeVersion(admin, v, tasks);
		
		size = Iterables.size(project.getVersions());
		Assert.assertTrue(size == 0);
	}
	
}
