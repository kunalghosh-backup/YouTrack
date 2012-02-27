package ar.edu.itba.it.paw.web.project.working.topfive;

import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.web.common.dataprovider.task.AbstractTaskDataProvider;
import ar.edu.itba.it.paw.web.common.dataprovider.task.TopFiveSortableDataProvider;
import ar.edu.itba.it.paw.web.project.working.AbstractWorkingProject;

public class TopFive extends AbstractWorkingProject {

	@SpringBean
	private TaskRepository tasks;

	public TopFive() {
		addTaskTable();
	}
	
	@Override
	public AbstractTaskDataProvider getTaskDataProvider() {
		return new TopFiveSortableDataProvider(tasks);
	}

}
