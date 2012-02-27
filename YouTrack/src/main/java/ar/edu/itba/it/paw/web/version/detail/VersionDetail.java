package ar.edu.itba.it.paw.web.version.detail;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.project.ProjectVersion;
import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.web.TrackerApp;
import ar.edu.itba.it.paw.web.base.BasePage;
import ar.edu.itba.it.paw.web.common.dataprovider.task.TaskTypeSortableDataProvider;
import ar.edu.itba.it.paw.web.project.component.menu.WorkingProjectMenu;
import ar.edu.itba.it.paw.web.project.detail.statistics.version.ProjectVersionStatistics;
import ar.edu.itba.it.paw.web.tables.CoolAjaxFallbackDataTable;
import ar.edu.itba.it.paw.web.task.component.TaskActionPanel;

public class VersionDetail extends BasePage {

	@SpringBean
	private TaskRepository tasks;
	
	public VersionDetail(IModel<ProjectVersion> versionModel) {
		super(new CompoundPropertyModel<ProjectVersion>(versionModel));
		
		add(new WorkingProjectMenu("menu"));
		
		add(new Label("name"));
		add(new Label("description"));

		addVersionTable("errors", new TaskTypeSortableDataProvider(tasks, versionModel, Task.TType.ERROR));
		addVersionTable("improvements", new TaskTypeSortableDataProvider(tasks, versionModel, Task.TType.IMPROVEMENT));
		addVersionTable("features", new TaskTypeSortableDataProvider(tasks, versionModel, Task.TType.NEW_FEATURE));
		addVersionTable("tasks", new TaskTypeSortableDataProvider(tasks, versionModel, Task.TType.TASK));
		addVersionTable("technicals", new TaskTypeSortableDataProvider(tasks, versionModel, Task.TType.TECHNICAL));
		
		add(new ProjectVersionStatistics("graph", versionModel));
		
	}
	
	protected void addVersionTable(String id, TaskTypeSortableDataProvider dataprovider) {
		add(new CoolAjaxFallbackDataTable<Task>(id, getTaskTableColumns(), dataprovider, TrackerApp.itemsPerPage));
	}
	
	public List<IColumn<Task>> getTaskTableColumns() {
		List<IColumn<Task>> cols = new ArrayList<IColumn<Task>>();
		cols.add(new PropertyColumn<Task>(Model.of(getString("code")), "code"));
		cols.add(new PropertyColumn<Task>(Model.of(getString("title")), "title"));
		cols.add(new AbstractColumn<Task>(Model.of(getString("actions"))) {
			@Override
			public void populateItem(Item<ICellPopulator<Task>> cellItem, String componentId, IModel<Task> rowModel) {
				cellItem.add(new TaskActionPanel(componentId, rowModel));
			}
		});
		return cols;
	}
}
