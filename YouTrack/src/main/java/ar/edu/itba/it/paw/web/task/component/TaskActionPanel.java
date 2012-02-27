package ar.edu.itba.it.paw.web.task.component;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.web.base.BasePanel;
import ar.edu.itba.it.paw.web.task.create.CreateEditTask;
import ar.edu.itba.it.paw.web.task.detail.TaskDetail;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class TaskActionPanel extends BasePanel {

	public TaskActionPanel(String id,final IModel<Task> taskModel) {
		super(id, taskModel);
		
		add(new Link<Task>("editTask", taskModel) {
			@Override
			public void onClick() {
				setResponsePage(new CreateEditTask(getModel()));
			}

			@Override
			public boolean isVisible() {
				return WebUtils.isMember();
			}
		});
		
		add(new Link<Task>("detailTask", taskModel) {
			@Override
			public void onClick() {
				getModelObject().addView();
				setResponsePage(new TaskDetail(getModel()));
			}
		});
		
	}

}
