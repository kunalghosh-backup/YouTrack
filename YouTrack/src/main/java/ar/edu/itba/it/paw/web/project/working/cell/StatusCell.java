package ar.edu.itba.it.paw.web.project.working.cell;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.web.base.BasePanel;

public class StatusCell extends BasePanel {

	public StatusCell(String id, final IModel<Task> model) {
		super(id, new CompoundPropertyModel<Task>(model));
		add(new Label("status"));
		add(new Label("solution") {
			@Override
			public boolean isVisible() {
				return model.getObject().getStatus() == Task.Status.COMPLETED;
			}
		});
	}
	
}
