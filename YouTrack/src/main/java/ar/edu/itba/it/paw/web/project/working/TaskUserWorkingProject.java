package ar.edu.itba.it.paw.web.project.working;

import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.web.common.dataprovider.task.AbstractTaskDataProvider;
import ar.edu.itba.it.paw.web.common.dataprovider.task.TaskUserSortableDataProvider;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class TaskUserWorkingProject extends AbstractWorkingProject {

	public TaskUserWorkingProject(IModel<User> userModel) {
		super(userModel);
		addTaskTable();
	}

	public AbstractTaskDataProvider getTaskDataProvider() {
		return new TaskUserSortableDataProvider(tasks, getCurrentProjectModel(), WebUtils.castUser(getDefaultModel()));
	}

}
