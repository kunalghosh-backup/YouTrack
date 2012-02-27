package ar.edu.itba.it.paw.web.common.dataprovider.task;

import java.util.Iterator;

import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.TaskRepository;

public class TaskSortableDataProvider extends AbstractTaskDataProvider {

	public TaskSortableDataProvider(TaskRepository tasks) {
		super(tasks);
	}
	
	@Override
	public Iterator<? extends Task> iterator(int first, int count) {
		return tasks.getByProject(getProject(), first, count).iterator();
	}

	@Override
	public int size() {
		return tasks.count(getProject());
	}

}
