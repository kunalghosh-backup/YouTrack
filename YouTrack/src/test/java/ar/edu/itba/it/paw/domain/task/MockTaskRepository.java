package ar.edu.itba.it.paw.domain.task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import ar.edu.itba.it.paw.domain.AbstractMockRepository;
import ar.edu.itba.it.paw.domain.common.Duration;
import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.project.ProjectVersion;
import ar.edu.itba.it.paw.domain.task.Relationship.RelationshipType;
import ar.edu.itba.it.paw.domain.task.Task.Priority;
import ar.edu.itba.it.paw.domain.task.Task.Status;
import ar.edu.itba.it.paw.domain.task.Task.TType;
import ar.edu.itba.it.paw.domain.user.User;

import com.google.common.collect.Iterables;

public class MockTaskRepository extends AbstractMockRepository<Task> implements TaskRepository {

	@Override
	public List<Task> getByProject(Project project) {
		List<Task> ret = new LinkedList<Task>();
		for(Task t : db.values()) {
			if(t.getProject().equals(project)) {
				ret.add(t);
			}
		}
		return ret;
	}

	@Override
	public List<Task> getByProjectUser(Project project, User user) {
		List<Task> ret = new LinkedList<Task>();
		for(Task t : getByUser(user)) {
			if(t.getProject().equals(project)) {
				ret.add(t);
			}
		}
		return ret;
	}

	@Override
	public List<Task> getByProjectUserStatuses(Project project, User user, List<Status> statuses) {
		List<Task> ret = new LinkedList<Task>();
		for(Task t : getByProjectUser(project, user)) {
			if(statuses.contains(t.getStatus())) {
				ret.add(t);
			}
		}
		return ret;
	}

	@Override
	public TaskStatistics getStatistics(Project project) {
		return new TaskStatistics(getByProject(project));
	}

	@Override
	public TaskStatistics getVersionStatistics(Project project, ProjectVersion version) {
		List<Task> versionTasks = new LinkedList<Task>();
		for(Task t : getAll()) {
			if(Iterables.contains(t.getVersions(), version)) {
				versionTasks.add(t);
			}
		}
		return new TaskStatistics(versionTasks);
	}

	@Override
	public Integer generateIndex(Project project) {
		return getByProject(project).size() + 1;
	}

	@Override
	public List<Task> getByVersion(ProjectVersion version) {
		return new ArrayList<Task>();
	}

	@Override
	public List<Task> filter(SearchFilter filter, Project project) {
		return new ArrayList<Task>();
	}

	@Override
	public List<Task> getByUser(User user) {
		List<Task> ret = new LinkedList<Task>();
		for(Task t : db.values()) {
			if(t.getOwner() != null && t.getOwner().equals(user)) {
				ret.add(t);
			}
		}
		return ret;
	}

	@Override
	public Map<User, Duration> getMembersWork(Project project, DateTime from, DateTime to) {
		return null;
	}

	@Override
	public TaskFile getTaskFileById(int id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<ChangesLogger> getLastTaskChanges(Project project) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Task getTaskByChangesLogers(ChangesLogger changeLogger, Project project) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean existRelationship(Task taskA, Task taskB, RelationshipType type) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Relationship getRelationShipById(int id) {
		throw new UnsupportedOperationException();
	}


	@Override
	public int count(Project project, User user) {
		return getByProjectUser(project, user).size();
	}

	@Override
	public int count(Project project) {
		return getByProject(project).size();
	}

	@Override
	public List<Task> getByProject(Project project, int first, int count) {
		return list(getByProject(project), first, count);
	}

	@Override
	public List<Task> getByProjectUser(Project project, User user, int first, int count) {
		return list(getByProjectUser(project, user), first, count);
	}

	@Override
	public List<Task> getByProjectUserStatuses(Project project, User user, List<Status> statuses, int first, int count) {
		return list(getByProjectUserStatuses(project, user, statuses), first, count);
	}

	@Override
	public int count(Project project, User user, List<Status> statuses) {
		return getByProjectUserStatuses(project, user, statuses).size();
	}

	@Override
	public List<Task> filter(SearchFilter filter, Project project, int first, int count) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<Priority, Duration> getTaskByPriority(Project project, ProjectVersion projectVersion) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Task> topFive(Project project) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Task> getByVersionAndType(ProjectVersion pv, TType type) {
		throw new UnsupportedOperationException();
	}

}
