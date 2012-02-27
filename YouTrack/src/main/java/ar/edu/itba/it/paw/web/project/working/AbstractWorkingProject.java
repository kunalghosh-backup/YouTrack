package ar.edu.itba.it.paw.web.project.working;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.web.TrackerApp;
import ar.edu.itba.it.paw.web.base.BasePage;
import ar.edu.itba.it.paw.web.common.dataprovider.task.AbstractTaskDataProvider;
import ar.edu.itba.it.paw.web.project.component.menu.WorkingProjectMenu;
import ar.edu.itba.it.paw.web.project.working.cell.StatusCell;
import ar.edu.itba.it.paw.web.tables.CoolAjaxFallbackDataTable;
import ar.edu.itba.it.paw.web.task.component.TaskActionPanel;

public abstract class AbstractWorkingProject extends BasePage {

	@SpringBean
	protected TaskRepository tasks;
	
	public AbstractWorkingProject() {
		super();
		add(new WorkingProjectMenu("workingProjectMenu"));
	}
	
	public AbstractWorkingProject(IModel<?> model) {
		super(model);
		add(new WorkingProjectMenu("workingProjectMenu"));
	}
	
	public List<IColumn<Task>> getTaskTableColumns() {
		List<IColumn<Task>> cols = new ArrayList<IColumn<Task>>();
		cols.add(new PropertyColumn<Task>(Model.of(getString("code")), "code"));
		cols.add(new PropertyColumn<Task>(Model.of(getString("title")), "title"));
		cols.add(new PropertyColumn<Task>(Model.of(getString("type")), "type"));
		cols.add(new PropertyColumn<Task>(Model.of(getString("creator")), "creator"));
		cols.add(new PropertyColumn<Task>(Model.of(getString("creationDate")), "createdAt"));
		cols.add(new PropertyColumn<Task>(Model.of(getString("owner")), "owner"));
		cols.add(new PropertyColumn<Task>(Model.of(getString("priority")), "priority"));
		cols.add(new AbstractColumn<Task>(Model.of(getString("status"))) {
			@Override
			public void populateItem(Item<ICellPopulator<Task>> cellItem, String componentId, IModel<Task> rowModel) {
				cellItem.add(new StatusCell(componentId, rowModel));
			}
		});
		cols.add(new PropertyColumn<Task>(Model.of(getString("estimatedTime")), "duration"));
		cols.add(new AbstractColumn<Task>(Model.of(getString("actions"))) {
			@Override
			public void populateItem(Item<ICellPopulator<Task>> cellItem, String componentId, IModel<Task> rowModel) {
				cellItem.add(new TaskActionPanel(componentId, rowModel));
			}
		});
		return cols;
	}
	
	protected void addTaskTable() {
		add(new CoolAjaxFallbackDataTable<Task>("tasks", getTaskTableColumns(), getTaskDataProvider(), TrackerApp.itemsPerPage));
	}
	
	public abstract AbstractTaskDataProvider getTaskDataProvider();

}
