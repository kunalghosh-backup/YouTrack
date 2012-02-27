package ar.edu.itba.it.paw.web.common.dataprovider.task;

import org.apache.wicket.Session;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public abstract class AbstractTaskDataProvider extends SortableDataProvider<Task>{

	protected final TaskRepository tasks;

	public AbstractTaskDataProvider(TaskRepository tasks) {
		super();
		this.tasks = tasks;
	}
	
	@Override
	public IModel<Task> model(Task t) {
		return WebUtils.createTaskModel(t);
	}
	
	protected Project getProject() {
		return WebUtils.castSession(Session.get()).getProject();
	}
	
}
