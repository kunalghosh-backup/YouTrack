package ar.edu.itba.it.paw.domain.task;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ar.edu.itba.it.paw.domain.common.Duration;
import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.project.ProjectRepository;
import ar.edu.itba.it.paw.domain.project.ProjectTest;
import ar.edu.itba.it.paw.domain.service.MessagingServiceTestImpl;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepository;
import ar.edu.itba.it.paw.domain.user.UserTest;

import com.google.common.collect.Iterables;

public class TaskTest {

	private static UserRepository users;
	private static TaskRepository tasks;
	private static ProjectRepository projects;

	public static TaskRepository getTaskRepository(UserRepository userRepo, ProjectRepository projectRepo) {
		TaskRepository taskRepo = new MockTaskRepository();
		
		User admin = userRepo.getByUsername("admin");
		Project project = projectRepo.getByCode("T");
		
		Task t1 = new Task(admin, project, new DateTime(1500), "Test", Task.Priority.TRIVIAL, 1, Task.TType.ERROR, new MessagingServiceTestImpl());
		taskRepo.add(t1);

		return taskRepo;
	}

	@Before
	public void setContext() {
		users = UserTest.getUserRepository();
		projects = ProjectTest.getProjectRepository(users);
		tasks = TaskTest.getTaskRepository(users, projects);
	}

	/**
	 * Checkea que la creación de task sea por un usuario con privilegios y que se envie un mail en la creacion
	 */
	@Test
	public void legalTaskCreation() {
		User admin = users.getByUsername("admin");
		Project project = projects.getByCode("T");
		
		MessagingServiceTestImpl ms = new MessagingServiceTestImpl();
		
		Task t = new Task(admin, project, new DateTime(1500), "Test", Task.Priority.TRIVIAL, 1, Task.TType.ERROR, ms);
		Assert.assertTrue(t.getCode().equals(project.getCode() + "-" + t.getIndex()));
		Assert.assertTrue(ms.mailSent == 1);
	}
	
	/**
	 * Checkea que no se hayan creado logs en la creación de una task, y que se creen cuando se edita la tarea.
	 */
	@Test
	public void logChangesTest() {
		Task t = tasks.get(1);
		User admin = users.getByUsername("admin");

		int logn = t.getChangesLogger().size();
		/* No se loggearon cambios en la creación */
		Assert.assertTrue(logn == 0);
		
		/* Se loguea solo una vez el cambio de descripcion */
		t.setDescription("Nueva descripcion", admin);
		t.setDescription("Nueva descripcion", admin);
		logn = t.getChangesLogger().size();
		Assert.assertTrue(logn == 1);
		
		/* Se loguea solo una vez el cambio de descripcion */
		t.setTitle("Nuevo titulo", admin);
		t.setTitle("Nuevo titulo", admin);
		logn = t.getChangesLogger().size();
		Assert.assertTrue(logn == 2);
		
		/* Se loguea solo una vez el cambio de descripcion */
		t.setDuration(new Duration(15), admin);
		t.setDuration(new Duration(15), admin);
		logn = t.getChangesLogger().size();
		Assert.assertTrue(logn == 3);
		
		/* Se loguea solo una vez el cambio de titulo */
		t.setType(Task.TType.IMPROVEMENT, admin);
		t.setType(Task.TType.IMPROVEMENT, admin);
		logn = t.getChangesLogger().size();
		Assert.assertTrue(logn == 4);
		
		/*Se loguea solo una vez el cambio de prioridad */
		t.setPriority(Task.Priority.HIGH, admin);
		t.setPriority(Task.Priority.HIGH, admin);
		logn = t.getChangesLogger().size();
		Assert.assertTrue(logn == 5);
	}
	
	/**
	 * Testea que se mande email cuando se cambia el estado de una tarea
	 */
	@Test
	public void statusEmailTest() {
		Task t = tasks.get(1);
		User admin = users.getByUsername("admin");
		
		MessagingServiceTestImpl ms = new MessagingServiceTestImpl();
		t.open(admin, ms);
		Assert.assertTrue(ms.mailSent == 1);
	}

	/**
	 * Testea que se agregue un comentario
	 */
	@Test
	public void testComments() {
		Task t = tasks.get(1);
		User admin = users.getByUsername("admin");
		int size = Iterables.size(t.getComments());
		t.addComment(admin, "Nuevo comentario");
		Assert.assertTrue(Iterables.size(t.getComments()) == size + 1);
		t.addComment(admin, "Otro comentario");
		Assert.assertTrue(Iterables.size(t.getComments()) == size + 2);
		t.addComment(admin, "Otro otro comentario");
		Assert.assertTrue(Iterables.size(t.getComments()) == size + 3);
	}
	
	/**
	 * Testea que se agregue un reporte de trabajo
	 */
	@Test
	public void testWorkComments() {
		Task t = tasks.get(1);
		User admin = users.getByUsername("admin");
		int size = Iterables.size(t.getWorkReports());
		t.addReport(admin, new Duration(5), "Nuevo reporte");
		Assert.assertTrue(Iterables.size(t.getWorkReports()) == size + 1);
		t.addReport(admin, new Duration(5), "Otro reporte");
		Assert.assertTrue(Iterables.size(t.getWorkReports()) == size + 2);
		t.addReport(admin, new Duration(5), "Otro otro reporte");
		Assert.assertTrue(Iterables.size(t.getWorkReports()) == size + 3);
	}
	
}
