package ar.edu.itba.it.paw.web.project.list;

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

import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.project.ProjectRepository;
import ar.edu.itba.it.paw.domain.utils.ValidationUtils;
import ar.edu.itba.it.paw.web.TrackerApp;
import ar.edu.itba.it.paw.web.base.BasePage;
import ar.edu.itba.it.paw.web.common.dataprovider.project.ProjectSortableDataProvider;
import ar.edu.itba.it.paw.web.project.component.ProjectActionPanel;
import ar.edu.itba.it.paw.web.project.create.CreateEditProject;
import ar.edu.itba.it.paw.web.tables.CoolAjaxFallbackDataTable;
import ar.edu.itba.it.paw.web.user.list.UserList;

public class ProjectList extends BasePage {
	
	@SpringBean
	private ProjectRepository projects;
	
	public ProjectList() {

		add(new Link<Void>("createProject") {

			@Override
			public void onClick() {
				setResponsePage(CreateEditProject.class);
			}

			@Override
			public boolean isVisible() {
				return ValidationUtils.isAdminUser(getTrackerSession().getUser());
			}
			
		});
		
		add(new Link<Void>("users") {
			@Override
			public void onClick() {
				setResponsePage(UserList.class);
			}
			@Override
			public boolean isVisible() {
				return getTrackerSession().isSignedIn() && getCurrentUser().isAdmin();
			}
		});
		
		add(new CoolAjaxFallbackDataTable<Project>("projects", getProjectTableColumns(), new ProjectSortableDataProvider(projects, getTrackerSession().getUserModel()), TrackerApp.itemsPerPage));
		
	}
	
	private List<IColumn<Project>> getProjectTableColumns() {
		List<IColumn<Project>> cols = new ArrayList<IColumn<Project>>();
		cols.add(new PropertyColumn<Project>(Model.of(getString("code")), "code"));
		cols.add(new PropertyColumn<Project>(Model.of(getString("title")), "title"));
		cols.add(new PropertyColumn<Project>(Model.of(getString("creationDate")), "createdAt"));
		cols.add(new PropertyColumn<Project>(Model.of(getString("status")), "status"));
		cols.add(new PropertyColumn<Project>(Model.of(getString("owner")), "owner"));
		cols.add(new AbstractColumn<Project>(Model.of(getString("actions"))) {
			@Override
			public void populateItem(Item<ICellPopulator<Project>> cellItem, String componentId, IModel<Project> rowModel) {
				cellItem.add(new ProjectActionPanel(componentId, rowModel));
			}
		});
		return cols;
	}

}
