package ar.edu.itba.it.paw.web.tables.changes.lastchanges;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.task.ChangesLogger;
import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.web.base.BasePanel;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class LastTaskChangesPanel extends BasePanel {

	@SpringBean
	private TaskRepository tasks;
	
	public LastTaskChangesPanel(String id) {
		super(id);
		final ListView<ChangesLogger> list = new ListView<ChangesLogger>("elements", getLastTaskChangesLoggerModel()) {
			@Override
			protected void populateItem(ListItem<ChangesLogger> item) {
				Task task = tasks.getTaskByChangesLogers(item.getModelObject(), getCurrentProject());
				item.setDefaultModel(new CompoundPropertyModel<ChangesLogger>(item.getModel()));
				item.add(new Label("task", WebUtils.createTaskModel(task)));
				item.add(new Label("createdAt"));
				item.add(new Label("edithor"));
				item.add(new Label("oldValue"));
				item.add(new Label("newValue"));
			}

			@Override
			public boolean isVisible() {
				return !getModelObject().isEmpty();
			}
		};
		
		add(list);
		
		add(new Label("emptyMessage", getString("emptyMessage")) {
			@Override
			public boolean isVisible() {
				return !list.isVisible();
			}
		});
	}

	private IModel<List<ChangesLogger>>  getLastTaskChangesLoggerModel() {
		return new LoadableDetachableModel<List<ChangesLogger>>() {
			@Override
			protected List<ChangesLogger> load() {
				return tasks.getLastTaskChanges(getCurrentProject());
			}
		};
	}

}
