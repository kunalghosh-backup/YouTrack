package ar.edu.itba.it.paw.web.project.working;

import ar.edu.itba.it.paw.web.common.dataprovider.task.AbstractTaskDataProvider;
import ar.edu.itba.it.paw.web.common.dataprovider.task.TaskSortableDataProvider;

public class TasksWorkingProject extends AbstractWorkingProject {

	public TasksWorkingProject() {
		addTaskTable();
	}
	
	@Override
	public AbstractTaskDataProvider getTaskDataProvider() {
		return new TaskSortableDataProvider(tasks);
	}

}
