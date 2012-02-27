package ar.edu.itba.it.paw.web.task.component.repeater;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.project.ProjectVersion;

public class VersionTable extends SingleTable<ProjectVersion> {

	public VersionTable(String id, IModel<List<ProjectVersion>> tasksModel, String title) {
		super(id, tasksModel, title);
	}

	@Override
	protected void populateItem(ListItem<ProjectVersion> item) {
		item.setDefaultModel(new CompoundPropertyModel<ProjectVersion>(item.getDefaultModel()));
		item.add(new Label("name"));
	}

}
