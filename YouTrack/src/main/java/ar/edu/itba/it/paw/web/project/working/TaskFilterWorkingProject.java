package ar.edu.itba.it.paw.web.project.working;

import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.task.SearchFilter;
import ar.edu.itba.it.paw.web.common.dataprovider.task.AbstractTaskDataProvider;
import ar.edu.itba.it.paw.web.common.dataprovider.task.TaskFilterSortableDataProvider;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class TaskFilterWorkingProject extends AbstractWorkingProject {

	public TaskFilterWorkingProject(IModel<SearchFilter> searchFilterModel) {
		super(searchFilterModel);
		addTaskTable();
	}

	@Override
	public AbstractTaskDataProvider getTaskDataProvider() {
		return new TaskFilterSortableDataProvider(tasks, WebUtils.castSearchFilter(getDefaultModel()));
	}

}
