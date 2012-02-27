package ar.edu.itba.it.paw.web.common.dataprovider.task;

import java.util.Iterator;

import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.task.SearchFilter;
import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class TaskFilterSortableDataProvider extends AbstractTaskDataProvider {

	private final IModel<SearchFilter> searchFilter;
	
	public TaskFilterSortableDataProvider(TaskRepository tasks, IModel<SearchFilter> searchFilter) {
		super(tasks);
		this.searchFilter = searchFilter;
	}

	@Override
	public Iterator<? extends Task> iterator(int first, int count) {
		return tasks.filter(getFilter(), getProject(), first, count).iterator();
	}

	@Override
	public int size() {
		return tasks.filter(getFilter(), getProject()).size();
	}

	protected SearchFilter getFilter() {
		return searchFilter.getObject();
	}
	
	protected Project getProject() {
		return WebUtils.castSession(Session.get()).getProject();
	}

}
