package ar.edu.itba.it.paw.domain.task;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import ar.edu.itba.it.paw.domain.common.Duration;
import ar.edu.itba.it.paw.domain.common.Repository;
import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.project.ProjectVersion;
import ar.edu.itba.it.paw.domain.task.Relationship.RelationshipType;
import ar.edu.itba.it.paw.domain.task.Task.Priority;
import ar.edu.itba.it.paw.domain.user.User;

public interface TaskRepository extends Repository<Task> {

	List<Task> getByProject(Project project);
	
	List<Task> getByProject(Project project, int first, int count);
	
	List<Task> getByProjectUser(Project project, User user);
	
	List<Task> getByProjectUser(Project project, User user, int first, int count);

	List<Task> getByProjectUserStatuses(Project project, User user, List<Task.Status> statuses);
	
	List<Task> getByProjectUserStatuses(Project project, User user, List<Task.Status> statuses, int first, int count);
	
	int count(Project project, User user, List<Task.Status> statuses);

	TaskStatistics getStatistics(Project project);
	
	TaskStatistics getVersionStatistics(Project project, ProjectVersion version);
	
	Integer generateIndex(Project project);
	
	List<Task> getByVersion(ProjectVersion version);
	
	List<Task> filter(SearchFilter filter, Project project);
	
	List<Task> filter(SearchFilter filter, Project project, int first, int count);
	
	List<Task> getByUser(User user);
	
	Map<User, Duration> getMembersWork(Project project, DateTime from, DateTime to);
	
	TaskFile getTaskFileById(int id);
	
	List<ChangesLogger> getLastTaskChanges(Project project);
	
	Task getTaskByChangesLogers(ChangesLogger changeLogger, Project project);

	boolean existRelationship(Task taskA, Task taskB, RelationshipType type);
	
	Relationship getRelationShipById(int id);
	
	int count(Project project, User user);
	
	int count(Project project);
	
	public Map<Priority, Duration> getTaskByPriority(Project project, ProjectVersion projectVersion);
	
	List<Task> topFive(Project project);
	
	List<Task> getByVersionAndType(ProjectVersion pv, Task.TType type);
	
}
