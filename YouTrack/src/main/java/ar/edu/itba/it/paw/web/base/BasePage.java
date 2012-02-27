package ar.edu.itba.it.paw.web.base;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import ar.edu.itba.it.paw.domain.common.EntityModel;
import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.web.TrackerApp;
import ar.edu.itba.it.paw.web.TrackerSession;
import ar.edu.itba.it.paw.web.auth.LoginPage;

public abstract class BasePage extends WebPage {

	public BasePage() {
		addCommons();
	}

	public BasePage(IModel<?> model) {
		super(model);
		addCommons();
	}

	private void addCommons() {

		add(new BookmarkablePageLink<Void>("headerLogo", getApplication()
				.getHomePage()));

		add(new Label("pageTitle", new Model<String>("You Track - "
				+ getPageTitle())));

		final TrackerSession session = getTrackerSession();

		add(new BookmarkablePageLink<Void>("loginLink", LoginPage.class) {

			@Override
			public boolean isVisible() {
				return !session.isSignedIn();
			}

		});

		add(new Link<Void>("logoutLink") {
			@Override
			public void onClick() {
				session.signOut();
				setResponsePage(TrackerApp.get().getHomePage());
			}

			@Override
			public boolean isVisible() {
				return session.isSignedIn();
			}
		});
		
		add(new Label("currentUser", new EntityModel<User>(User.class, getCurrentUser())) {
			@Override
			public boolean isVisible() {
				return getTrackerSession().isSignedIn();
			}
		});
	}

	protected String getPageTitle(){
		return getString("pageTitle");
	}

	protected static TrackerSession getTrackerSession() {
		return (TrackerSession) Session.get();
	}

	public static User getCurrentUser() {
		return getTrackerSession().getUser();
	}
	
	public static IModel<User> getCurrentUserModel() {
		return  getTrackerSession().getUserModel();
	}
	
	public static Project getCurrentProject() {
		return getTrackerSession().getProject();
	}
	
	public static IModel<Project> getCurrentProjectModel() {
		return getTrackerSession().getProjectModel();
	}

}
