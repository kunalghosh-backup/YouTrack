package ar.edu.itba.it.paw.web.user.list.action;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.User.Privilege;
import ar.edu.itba.it.paw.web.base.BasePanel;

public class UserActionPanel extends BasePanel {

	@SpringBean
	private TaskRepository tasks;
	
	public UserActionPanel(String id, IModel<User> userModel) {
		super(id, userModel);
		add(new Link<User>("banButton", userModel) {
			@Override
			public void onClick() {
				getCurrentUser().revokePrivilegeTo(getModelObject(), Privilege.LOGIN, tasks);
			}

			@Override
			public boolean isVisible() {
				return !getModelObject().equals(getCurrentUser()) && getModelObject().getStatus(tasks).equals(User.Status.FREE);
			}
		});
		add(new Link<User>("recoverButton", userModel) {

			@Override
			public void onClick() {
				getCurrentUser().addPrivilegeTo(getModelObject(), Privilege.LOGIN);
			}

			@Override
			public boolean isVisible() {
				return !getModelObject().equals(getCurrentUser()) && getModelObject().isBanned();
			}
			
		});
		add(new Link<User>("makeAdminButton", userModel) {
			@Override
			public void onClick() {
				getCurrentUser().addPrivilegeTo(getModelObject(), Privilege.ADMIN);
			}

			@Override
			public boolean isVisible() {
				return !getModelObject().equals(getCurrentUser()) && !getModelObject().isAdmin();
			}
		});
		add(new Link<User>("removeAdminButton", userModel) {
			@Override
			public void onClick() {
				getCurrentUser().revokePrivilegeTo(getModelObject(), Privilege.ADMIN, tasks);
			}

			@Override
			public boolean isVisible() {
				return !getModelObject().equals(getCurrentUser()) && getModelObject().isAdmin();
			}
		});
	}

}
