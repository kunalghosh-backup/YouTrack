package ar.edu.itba.it.paw.web.task.component.repeater.relationship;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.web.TrackerApp;
import ar.edu.itba.it.paw.web.common.dataprovider.task.list.AbstractRelationshipDataProvider;
import ar.edu.itba.it.paw.web.tables.CoolAjaxFallbackDataTable;

public class RelationDataTable extends CoolAjaxFallbackDataTable<Task> {

	public RelationDataTable(String id, IModel<String> displayModel, AbstractRelationshipDataProvider dataProvider) {
		super(id, getRelationshipTableColumns(displayModel), dataProvider, TrackerApp.itemsPerPage);
	}
	
	private static List<IColumn<Task>> getRelationshipTableColumns(IModel<String> displayModel) {
		List<IColumn<Task>> cols = new ArrayList<IColumn<Task>>(1);
		cols.add(new AbstractColumn<Task>(displayModel) {
			@Override
			public void populateItem(Item<ICellPopulator<Task>> cellItem, String componentId, IModel<Task> rowModel) {
				cellItem.add(new TaskLinkPanel(componentId, rowModel));
			}
		});
		return cols;
	}
	
}
