package ar.edu.itba.it.paw.web.task.component.repeater;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.web.base.BasePanel;

public abstract class SingleTable<T> extends BasePanel {

	public SingleTable(String id, IModel<List<T>> tasksModel, String title) {
		super(id, tasksModel);
		
		add(new Label("title", title));
		
		add(new ListView<T>("elements", tasksModel) {
			@Override
			protected void populateItem(ListItem<T> item) {
				SingleTable.this.populateItem(item);
			}

			@SuppressWarnings("unchecked")
			@Override
			public boolean isVisible() {
				return !((IModel<List<IModel<Task>>>) getDefaultModel()).getObject().isEmpty();
			}
			
		});
		
	}

	protected abstract void populateItem(ListItem<T> item);
	
}
