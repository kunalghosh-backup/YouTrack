package ar.edu.itba.it.paw.web.user.register.list;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.common.EntityModel;
import ar.edu.itba.it.paw.domain.user.UserRepository;
import ar.edu.itba.it.paw.domain.user.UserRequest;
import ar.edu.itba.it.paw.domain.user.UserRequestRepository;
import ar.edu.itba.it.paw.web.base.SecuredPage;
import ar.edu.itba.it.paw.web.user.list.UserList;

public class UserRequestList extends SecuredPage {

	@SpringBean
	private UserRequestRepository userRequests;
	
	@SpringBean
	private UserRepository users;
	
	public UserRequestList() {
		super();
		
		add(new Link<Void>("backToUsers") {
			@Override
			public void onClick() {
				setResponsePage(UserList.class);
			}
		});
		
		add(new RefreshingView<UserRequest>("requests") {

			@Override
			protected Iterator<IModel<UserRequest>> getItemModels() {
				List<IModel<UserRequest>> ret = new LinkedList<IModel<UserRequest>>();
				for(UserRequest ur : userRequests.getAll()) {
					ret.add(new EntityModel<UserRequest>(UserRequest.class, ur));
				}
				return ret.iterator();
			}

			@Override
			protected void populateItem(Item<UserRequest> item) {
				item.setDefaultModel(new CompoundPropertyModel<UserRequest>(item.getModel()));
				item.add(new Label("userName"));
				item.add(new Label("fullName"));
				item.add(new Label("email"));
				item.add(new Link<UserRequest>("accept", item.getModel()) {
					@Override
					public void onClick() {
						getModelObject().accept(getCurrentUser(), userRequests, users);
					}
				});
				item.add(new Link<UserRequest>("reject", item.getModel()) {
					@Override
					public void onClick() {
						getModelObject().reject(getCurrentUser(), userRequests);
					}
				});
			}

			@Override
			public boolean isVisible() {
				return userRequests.count() > 0;
			}
			
		});
		
		add(new Label("emptyMessage", getString("noRequests")) {
			@Override
			public boolean isVisible() {
				return !(userRequests.count() > 0);
			}
		});
		
	}
	
}
