package ar.edu.itba.it.paw.web.user.create;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.User.Privilege;
import ar.edu.itba.it.paw.domain.user.UserRepository;
import ar.edu.itba.it.paw.web.base.SecuredPage;
import ar.edu.itba.it.paw.web.user.form.UserForm;
import ar.edu.itba.it.paw.web.user.list.UserList;

public class CreateUser extends SecuredPage {
	
	@SpringBean
	private UserRepository users;
	
	public CreateUser() {
		super();
		
		add(new Link<Void>("users") {
			@Override
			public void onClick() {
				setResponsePage(UserList.class);
			}
		});
		
		add(new UserForm("userForm") {

			@Override
			protected void onSubmit(String name, String lastName, String userName, String password, String email) {
				User u = new User(getCurrentUser(), name, lastName, userName, password, email);
				getCurrentUser().addPrivilegeTo(u, Privilege.LOGIN);
				users.add(u);
				setResponsePage(UserList.class);
			}
			
		});
		
	}

	@Override
	protected String getPageTitle() {
		return getString("titlePage");
	}

}
