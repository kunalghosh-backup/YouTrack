package ar.edu.itba.it.paw.web.version.list;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.common.Duration;
import ar.edu.itba.it.paw.domain.project.ProjectVersion;
import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.web.TrackerApp;
import ar.edu.itba.it.paw.web.base.BasePage;
import ar.edu.itba.it.paw.web.common.dataprovider.version.UnreleasedVersionSortableDataProvider;
import ar.edu.itba.it.paw.web.common.dataprovider.version.VersionSortableDataProvider;
import ar.edu.itba.it.paw.web.common.progressbar.Progress;
import ar.edu.itba.it.paw.web.common.progressbar.ProgressBar;
import ar.edu.itba.it.paw.web.common.progressbar.ProgressModel;
import ar.edu.itba.it.paw.web.project.component.menu.WorkingProjectMenu;
import ar.edu.itba.it.paw.web.tables.CoolAjaxFallbackDataTable;
import ar.edu.itba.it.paw.web.utils.WebUtils;
import ar.edu.itba.it.paw.web.version.list.link.DetailVersionLink;

public class VersionList extends BasePage {

	@SpringBean
	private TaskRepository tasks;
	
	public VersionList() {
		super();
		add(new WorkingProjectMenu("menu"));
		add(new CoolAjaxFallbackDataTable<ProjectVersion>("allVersions", getVersionTableColumns(), new VersionSortableDataProvider(), TrackerApp.itemsPerPage));
		add(new CoolAjaxFallbackDataTable<ProjectVersion>("unreleasedVersions", getUnreleasedTableColumns(), new UnreleasedVersionSortableDataProvider(), TrackerApp.itemsPerPage));
	}
	
	private List<IColumn<ProjectVersion>> getVersionTableColumns() {
		List<IColumn<ProjectVersion>> ret = new ArrayList<IColumn<ProjectVersion>>(6);
		addCommonColumns(ret);
		ret.add(new AbstractColumn<ProjectVersion>(Model.of(getString("estimatedTime"))) {
			@Override
			public void populateItem(Item<ICellPopulator<ProjectVersion>> cellItem, String componentId, IModel<ProjectVersion> rowModel) {
				cellItem.add(new Label(componentId, Model.of(WebUtils.convertToString(Duration.class, rowModel.getObject().getEstimatedTime(tasks)))));
			}
		});
		ret.add(new AbstractColumn<ProjectVersion>(Model.of(getString("workedTime"))) {
			@Override
			public void populateItem(Item<ICellPopulator<ProjectVersion>> cellItem, String componentId, IModel<ProjectVersion> rowModel) {
				cellItem.add(new Label(componentId, Model.of(WebUtils.convertToString(Duration.class, rowModel.getObject().getWorkedTime(tasks)))));
			}
		});
		ret.add(new AbstractColumn<ProjectVersion>(Model.of(getString("estimatedFinishTime"))) {
			@Override
			public void populateItem(Item<ICellPopulator<ProjectVersion>> cellItem, String componentId, IModel<ProjectVersion> rowModel) {
				cellItem.add(new Label(componentId, Model.of(WebUtils.convertToString(Duration.class, rowModel.getObject().getEstimatedFinish(tasks)))));
			}
		});
		ret.add(new AbstractColumn<ProjectVersion>(Model.of(getString("actions"))){
			@Override
			public void populateItem(Item<ICellPopulator<ProjectVersion>> cellItem, String componentId, IModel<ProjectVersion> rowModel) {
				cellItem.add(new DetailVersionLink(componentId, rowModel));
			}
		});
		return ret;
	}

	private List<IColumn<ProjectVersion>> getUnreleasedTableColumns() {
		List<IColumn<ProjectVersion>> ret = new ArrayList<IColumn<ProjectVersion>>(4);
		addCommonColumns(ret);
		ret.add(new AbstractColumn<ProjectVersion>(Model.of(getString("progress"))) {
			@Override
			public void populateItem(Item<ICellPopulator<ProjectVersion>> cellItem,	String componentId,final IModel<ProjectVersion> rowModel) {
				ProgressModel m = new ProgressModel() {
					@Override
					public Progress getProgress() {
						return new Progress((int) (rowModel.getObject().getProgress(tasks) * 100));
					}
				};
				cellItem.add(new ProgressBar(componentId, m));
			}
		});
		return ret;
	}
	
	private void addCommonColumns(List<IColumn<ProjectVersion>> list) {
		list.add(new PropertyColumn<ProjectVersion>(Model.of(getString("name")), "name"));
		list.add(new PropertyColumn<ProjectVersion>(Model.of(getString("releaseDate")), "releaseDate"));
		list.add(new PropertyColumn<ProjectVersion>(Model.of(getString("description")), "description"));
	}
	
}
