package ar.edu.itba.it.paw.web.base;

import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.web.TrackerSession;
import ar.edu.itba.it.paw.web.auth.LoginPage;

public abstract class SecuredPage extends BasePage {

	public SecuredPage() {
		validate();
	}

	public SecuredPage(IModel<?> model) {
		super(model);
		validate();
	}

	private void validate() {
		TrackerSession session = getTrackerSession();
		if(!session.isSignedIn()) {
			redirectToInterceptPage(new LoginPage());
		}
	}
	
}
