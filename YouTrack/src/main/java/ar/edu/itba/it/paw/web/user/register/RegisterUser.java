package ar.edu.itba.it.paw.web.user.register;

import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.user.UserRequest;
import ar.edu.itba.it.paw.domain.user.UserRequestRepository;
import ar.edu.itba.it.paw.web.base.BasePage;
import ar.edu.itba.it.paw.web.user.form.UserForm;
import ar.edu.itba.it.paw.web.user.register.info.RegisterInfoPage;

public class RegisterUser extends BasePage {

	@SpringBean
	private UserRequestRepository userRequests;
	
	public RegisterUser() {
		add(new UserForm("userForm") {
			@Override
			protected void onSubmit(String name, String lastName, String userName, String password, String email) {
				UserRequest ur = new UserRequest(name, lastName, userName, password, email);
				userRequests.add(ur);
				setResponsePage(RegisterInfoPage.class);
			}
		});
	}
	
}
