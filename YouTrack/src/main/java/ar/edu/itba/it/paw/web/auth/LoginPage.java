package ar.edu.itba.it.paw.web.auth;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepository;
import ar.edu.itba.it.paw.web.TrackerSession;
import ar.edu.itba.it.paw.web.base.BasePage;
import ar.edu.itba.it.paw.web.common.ErrorFeedbackPanel;
import ar.edu.itba.it.paw.web.common.RequiredPasswordTextField;
import ar.edu.itba.it.paw.web.user.register.RegisterUser;

public class LoginPage extends BasePage {

	@SpringBean
	private UserRepository users;
	
	private transient String username;
	private transient String password;
	
	public LoginPage() {
		
		Form<LoginPage> form = new Form<LoginPage>("loginForm", new CompoundPropertyModel<LoginPage>(this)) {

			@Override
			protected void onSubmit() {
				TrackerSession session = TrackerSession.get();
				User user = session.signIn(username, password, users);
				if(user != null) {
					if(user.isBanned()) {
						error(getString("userBanned"));
					}else{
						if(!continueToOriginalDestination()) {
							setResponsePage(getApplication().getHomePage());
						}
					}
				}else{
					error(getString("invalidCredentials"));
				}
				setRedirect(true);
			}
			
		};
		
		form.add(new ErrorFeedbackPanel("feedback"));
		form.add(new Link<Void>("registerLink") {

			@Override
			public void onClick() {
				setResponsePage(RegisterUser.class);
			}
			
		});
		form.add(new RequiredTextField<String>("username"));
		form.add(new RequiredPasswordTextField("password"));
		form.add(new Button("signin", new ResourceModel("signin")));
		add(form);
		
	}
	
}
