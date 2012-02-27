package ar.edu.itba.it.paw.web.common.link;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.web.task.detail.TaskDetail;

public class TaskLink extends Link<Task> {

	public TaskLink(String id, IModel<Task> model) {
		super(id, model);
		add(new Label("code", getModel().getObject().getCode()));
	}

	@Override
	public void onClick() {
		setResponsePage(new TaskDetail(getModel()));
	}

}
