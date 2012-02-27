package ar.edu.itba.it.paw.web.user.register.info;

import org.apache.wicket.markup.html.link.Link;

import ar.edu.itba.it.paw.web.base.BasePage;

public class RegisterInfoPage extends BasePage {

	public RegisterInfoPage() {
		super();
		add(new Link<Void>("backToHome") {
			@Override
			public void onClick() {
				setResponsePage(getApplication().getHomePage());
			}
		});
	}
	
}
