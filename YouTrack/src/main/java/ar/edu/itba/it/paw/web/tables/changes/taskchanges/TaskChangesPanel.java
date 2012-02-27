package ar.edu.itba.it.paw.web.tables.changes.taskchanges;

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import ar.edu.itba.it.paw.domain.common.EntityModel;
import ar.edu.itba.it.paw.domain.task.ChangesLogger;
import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.web.base.BasePanel;
import ar.edu.itba.it.paw.web.common.label.DateTimeLabel;

public class TaskChangesPanel extends BasePanel {

	public TaskChangesPanel(String id, IModel<Task> taskModel) {
		super(id, taskModel);
		
		final IModel<List<IModel<ChangesLogger>>> listModel = getChangeListModel(taskModel);
		
		add(new ListView<IModel<ChangesLogger>>("elements", listModel){
			@Override
			protected void populateItem(ListItem<IModel<ChangesLogger>> item) {
				item.setDefaultModel(new CompoundPropertyModel<ChangesLogger>(item.getDefaultModel()));
				item.add(new DateTimeLabel("createdAt"));
				item.add(new Label("edithor"));
				item.add(new Label("oldValue"));
				item.add(new Label("newValue"));
			}

			@Override
			public boolean isVisible() {
				return !getModelObject().isEmpty();
			}
			
		});
		
		add(new Label("noChanges", getString("noChanges")) {
			@Override
			public boolean isVisible() {
				return listModel.getObject().isEmpty();
			}
		});
		
	}
	
	private IModel<List<IModel<ChangesLogger>>> getChangeListModel(final IModel<Task> taskModel) {
		return new LoadableDetachableModel<List<IModel<ChangesLogger>>>() {

			@Override
			protected List<IModel<ChangesLogger>> load() {
				List<IModel<ChangesLogger>> ans = new LinkedList<IModel<ChangesLogger>>();
				for(ChangesLogger cl : taskModel.getObject().getChangesLogger()){
					ans.add(new EntityModel<ChangesLogger>(ChangesLogger.class, cl));
				}
				return ans;
			}
		};
	}

}
