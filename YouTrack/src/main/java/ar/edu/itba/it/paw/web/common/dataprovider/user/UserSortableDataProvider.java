package ar.edu.itba.it.paw.web.common.dataprovider.user;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepository;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class UserSortableDataProvider extends SortableDataProvider<User> {

	private UserRepository users;

	public UserSortableDataProvider(UserRepository users) {
		this.users = users;
	}
	
	@Override
	public Iterator<? extends User> iterator(int first, int count) {
		return users.getAll(first, count).iterator();
	}

	@Override
	public int size() {
		return users.count();
	}

	@Override
	public IModel<User> model(User u) {
		return WebUtils.createUserModel(u);
	}

}
