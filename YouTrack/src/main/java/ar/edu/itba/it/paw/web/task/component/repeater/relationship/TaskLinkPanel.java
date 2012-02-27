package ar.edu.itba.it.paw.web.task.component.repeater.relationship;

import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.web.base.BasePanel;
import ar.edu.itba.it.paw.web.common.link.TaskLink;

public class TaskLinkPanel extends BasePanel {

	public TaskLinkPanel(String id, IModel<Task> model) {
		super(id, model);
		add(new TaskLink("link", model));
	}
	
}
