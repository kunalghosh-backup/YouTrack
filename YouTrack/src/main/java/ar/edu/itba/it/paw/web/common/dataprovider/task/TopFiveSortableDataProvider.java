package ar.edu.itba.it.paw.web.common.dataprovider.task;

import java.util.Iterator;

import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class TopFiveSortableDataProvider extends AbstractTaskDataProvider {

	public TopFiveSortableDataProvider(TaskRepository tasks) {
		super(tasks);
	}

	@Override
	public Iterator<? extends Task> iterator(int first, int count) {
		return WebUtils.subIterator(tasks.topFive(getProject()), first, count);
	}

	@Override
	public int size() {
		return WebUtils.size(tasks.topFive(getProject()));
	}

	
	
}
