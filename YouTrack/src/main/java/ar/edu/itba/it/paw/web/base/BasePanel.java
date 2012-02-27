package ar.edu.itba.it.paw.web.base;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.web.TrackerSession;

public abstract class BasePanel extends Panel {

	public BasePanel(String id) {
		super(id);
	}
	
	public BasePanel(String id, IModel<?> model) {
		super(id, model);
	}
	
	protected static TrackerSession getTrackerSession() {
		return (TrackerSession) Session.get();
	}
	
	protected static User getCurrentUser() {
		return getTrackerSession().getUser();
	}
	
	protected static IModel<User> getCurrentUserModel() {
		return getTrackerSession().getUserModel();
	}

	protected static Project getCurrentProject() {
		return getTrackerSession().getProject();
	}
	
	protected static IModel<Project> getCurrentProjectModel() {
		return getTrackerSession().getProjectModel();
	}
	
}
