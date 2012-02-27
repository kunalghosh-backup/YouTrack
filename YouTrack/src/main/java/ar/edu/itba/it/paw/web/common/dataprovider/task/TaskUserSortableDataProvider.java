package ar.edu.itba.it.paw.web.common.dataprovider.task;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.domain.user.User;

public class TaskUserSortableDataProvider extends AbstractTaskDataProvider {
	
	private final IModel<User> userModel;
	
	private static Task.Status[] STATUSES = {Task.Status.OPEN, Task.Status.ONGOING};
	
	public TaskUserSortableDataProvider(TaskRepository tasks, IModel<Project> projectModel, IModel<User> userModel) {
		super(tasks);
		this.userModel = userModel;
	}

	@Override
	public Iterator<? extends Task> iterator(int first, int count) {
		return tasks.getByProjectUserStatuses(getProject(), getUser(), Arrays.asList(STATUSES), first, count).iterator();
	}

	@Override
	public int size() {
		return tasks.count(getProject(), getUser(), Arrays.asList(STATUSES));
	}
	
	protected User getUser() {
		return userModel.getObject();
	}

}
