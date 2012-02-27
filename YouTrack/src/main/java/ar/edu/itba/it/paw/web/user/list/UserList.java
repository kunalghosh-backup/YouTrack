package ar.edu.itba.it.paw.web.user.list;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepository;
import ar.edu.itba.it.paw.domain.user.UserRequestRepository;
import ar.edu.itba.it.paw.web.base.SecuredPage;
import ar.edu.itba.it.paw.web.common.dataprovider.user.UserSortableDataProvider;
import ar.edu.itba.it.paw.web.project.list.ProjectList;
import ar.edu.itba.it.paw.web.tables.CoolAjaxFallbackDataTable;
import ar.edu.itba.it.paw.web.user.create.CreateUser;
import ar.edu.itba.it.paw.web.user.list.action.UserActionPanel;
import ar.edu.itba.it.paw.web.user.register.list.UserRequestList;

public class UserList extends SecuredPage {

	@SpringBean
	private UserRepository users;
	
	@SpringBean
	private UserRequestRepository userRequests;
	
	@SpringBean
	private TaskRepository tasks;
	
	private static final int itemsPerPage = 10;
	
	public UserList() {
		
		add(new Link<Void>("projects") {
			@Override
			public void onClick() {
				setResponsePage(ProjectList.class);
			}
		});

		add(new Link<Void>("userRequests") {
			@Override
			public void onClick() {
				setResponsePage(UserRequestList.class);
			}

			@Override
			public boolean isVisible() {
				return (userRequests.count() > 0);
			}
		});
		
		add(new CoolAjaxFallbackDataTable<User>("users", getUserTableColumns(), new UserSortableDataProvider(users), itemsPerPage));
		
		add(new Link<Void>("createUser") {
			@Override
			public void onClick() {
				setResponsePage(CreateUser.class);
			}
		});
	}
	
	@Override
	protected String getPageTitle() {
		return getString("pageTitle");
	}
	
	private List<IColumn<User>> getUserTableColumns() {
		List<IColumn<User>> cols = new ArrayList<IColumn<User>>(5);
		cols.add(new PropertyColumn<User>(Model.of(getString("user")), "userName"));
		cols.add(new AbstractColumn<User>(Model.of(getString("type"))) {
			@Override
			public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, final IModel<User> rowModel) {
				Label l = new Label(componentId, getString("admin")){
					@Override
					public boolean isVisible() {
						return rowModel.getObject().isAdmin();
					}
				};
				l.add(new SimpleAttributeModifier("class", "label label-success"));
				cellItem.add(l);
			}
		});
		cols.add(new PropertyColumn<User>(Model.of(getString("fullName")), "fullName"));
		cols.add(new PropertyColumn<User>(Model.of(getString("email")), "email"));
		cols.add(new AbstractColumn<User>(Model.of(getString("status"))) {
			@Override
			public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, IModel<User> rowModel) {
				cellItem.add(new Label(componentId, Model.of(rowModel.getObject().getStatus(tasks))));
			}
		});
		cols.add(new AbstractColumn<User>(Model.of(getString("actions"))){
			@Override
			public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, IModel<User> rowModel) {
				cellItem.add(new UserActionPanel(componentId, rowModel));
			}
		});
		return cols;
	}
	
	
	
}
