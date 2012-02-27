package ar.edu.itba.it.paw.web.project.detail.versions;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.project.ProjectVersion;
import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.web.TrackerApp;
import ar.edu.itba.it.paw.web.base.BasePanel;
import ar.edu.itba.it.paw.web.common.dataprovider.version.VersionSortableDataProvider;
import ar.edu.itba.it.paw.web.project.detail.versions.create.CreateEditProjectVersion;
import ar.edu.itba.it.paw.web.tables.CoolAjaxFallbackDataTable;

public class ProjectVersionPanel extends BasePanel {
	
	@SpringBean
	TaskRepository tasks;

	public ProjectVersionPanel(String id) {
		super(id);
		
		add(new Link<Void>("newVersion") {
			@Override
			public void onClick() {
				setResponsePage(new CreateEditProjectVersion());
				
			}
		});

		add(new CoolAjaxFallbackDataTable<ProjectVersion>("projectVersionList", getVersionTableColumns(), new VersionSortableDataProvider(), TrackerApp.itemsPerPage));
	}
	
	private List<IColumn<ProjectVersion>> getVersionTableColumns() {
		List<IColumn<ProjectVersion>> cols = new ArrayList<IColumn<ProjectVersion>>();
		cols.add(new PropertyColumn<ProjectVersion>(Model.of(getString("name")), "name"));
		cols.add(new PropertyColumn<ProjectVersion>(Model.of(getString("releaseDate")), "releaseDate"));
		cols.add(new PropertyColumn<ProjectVersion>(Model.of(getString("status")), "status"));
		cols.add(new PropertyColumn<ProjectVersion>(Model.of(getString("description")), "description"));
		cols.add(new AbstractColumn<ProjectVersion>(Model.of(getString("actions"))) {
			@Override
			public void populateItem(Item<ICellPopulator<ProjectVersion>> cellItem, String componentId, IModel<ProjectVersion> rowModel) {
				cellItem.add(new ProjectVersionActionPanel(componentId, rowModel));
			}
		});
		return cols;
	}
}
