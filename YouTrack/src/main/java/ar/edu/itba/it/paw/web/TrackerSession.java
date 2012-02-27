package ar.edu.itba.it.paw.web;

import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebSession;

import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepository;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class TrackerSession extends WebSession {

	private IModel<User> user;
	private IModel<Project> project;
	
	public static TrackerSession get() {
		return (TrackerSession) Session.get();
	}
	
	public TrackerSession(Request request) {
		super(request);
	}

	public User getUser() {
		return WebUtils.getObjectSafely(user);
	}
	
	public IModel<User> getUserModel() {
		return user;
	}
	
	public Project getProject() {
		return WebUtils.getObjectSafely(project);
	}
	
	public IModel<Project> getProjectModel() {
		return project;
	}
	
	public TrackerSession setProject(Project p) {
		this.project = WebUtils.createProjectModel(p);
		return this;
	}
	
	public User signIn(String username, String password, UserRepository users) {
		User user = users.login(username, password);
		if(user != null && !user.isBanned()) {
			this.user = WebUtils.createUserModel(user);
		}
		return user;
	}

	public boolean isSignedIn() {
		return user != null;
	}
	
	public void signOut() {
		Session.get().invalidate();
		clear();
	}

	@Override
	protected void detach() {
		super.detach();
		if(user != null) {
			user.detach();
		}
		if(project != null) {
			project.detach();
		}
	}

}
