package ar.edu.itba.it.paw.web.project.detail.members.add;

import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.service.MessagingService;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepository;
import ar.edu.itba.it.paw.web.base.SecuredPage;
import ar.edu.itba.it.paw.web.common.ErrorFeedbackPanel;
import ar.edu.itba.it.paw.web.project.detail.ProjectDetail;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class AddProjectMember extends SecuredPage {

	private transient User newMember;
	
	@SpringBean
	private MessagingService messagingService;
	
	@SpringBean
	private static UserRepository users;
	
	public AddProjectMember(final IModel<Project> projectModel) {
		Form<AddProjectMember> form = new Form<AddProjectMember>("addMemberForm", new CompoundPropertyModel<AddProjectMember>(this)) {
			@Override
			protected void onSubmit() {
				AddProjectMember.this.build();
				setResponsePage(new ProjectDetail());
			}
		};
		form.add(new ErrorFeedbackPanel("feedback"));
		form.add(new DropDownChoice<User>("newMember", getPosibleMembers(projectModel)).setRequired(true));
		form.add(new Link<Void>("cancel") {
			@Override
			public void onClick() {
				setResponsePage(new ProjectDetail());
			}
		});
		add(form);

		add(new Link<Project>("backToProjectDetail") {
			@Override
			public void onClick() {
				setResponsePage(new ProjectDetail());
				
			}
		});

	}

	private void build() {
		getCurrentProject().addMember(getCurrentUser(), newMember, messagingService);
	}
	
	private IModel<List<User>> getPosibleMembers(final IModel<Project> projectModel) {
		return new LoadableDetachableModel<List<User>>() {
			@Override
			protected List<User> load() {
				List<User> members = WebUtils.asList(projectModel.getObject().getMembers());
				List<User> allUsers = users.getAll();
			
				allUsers.removeAll(members);
				return allUsers;
			}
		};
	}

}
