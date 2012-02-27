package ar.edu.itba.it.paw.web.project.detail;

import java.util.Iterator;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.web.base.SecuredPage;
import ar.edu.itba.it.paw.web.project.detail.members.add.AddProjectMember;
import ar.edu.itba.it.paw.web.project.detail.members.work.filter.ProjectMemberWorkFilter;
import ar.edu.itba.it.paw.web.project.detail.statistics.task.TaskStatisticsPanel;
import ar.edu.itba.it.paw.web.project.detail.versions.ProjectVersionPanel;
import ar.edu.itba.it.paw.web.project.working.TasksWorkingProject;
import ar.edu.itba.it.paw.web.tables.changes.lastchanges.LastTaskChangesPanel;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class ProjectDetail extends SecuredPage {
	
	
	public ProjectDetail() {
		super(new CompoundPropertyModel<IModel<Project>>(getCurrentProjectModel()));
		add(new Label("code"));
		add(new Label("title"));
		add(new Label("description"));
		add(new Label("status"));
		add(new Label("owner"));
		add(new Label("createdAt"));
		addMembersTable();
		add(new Link<Void>("addMembersButton"){

			@Override
			public void onClick() {
				setResponsePage(new AddProjectMember(getCurrentProjectModel()));
			}
			
		});
		add(new ProjectVersionPanel("projectVersionPanel"));
		addTasksStadistics(getCurrentProjectModel());
		add(new LastTaskChangesPanel("lastChangesPanel"));
		add(new Link<Void>("backButton") {
			@Override
			public void onClick() {
				setResponsePage(new TasksWorkingProject());
			}
		});
		add(new Link<Void>("membersWorkButton"){
			@Override
			public void onClick() {
				setResponsePage(new ProjectMemberWorkFilter(getCurrentProjectModel()));
			}
		});
	}
		
	private void addTasksStadistics(IModel<Project> projectModel) {
		add(new TaskStatisticsPanel("taskStatisticsPanel", projectModel));
	}

	private void addMembersTable() {
		add(new RefreshingView<User>("projectMemberList") {
			@Override
			protected Iterator<IModel<User>> getItemModels() {
				return WebUtils.toModelList(getCurrentProject().getMembers(), User.class).iterator();
			}

			@Override
			protected void populateItem(Item<User> item) {
				item.setDefaultModel(new CompoundPropertyModel<User>(item.getModel()));
				item.add(new Label("userName"));
				item.add(new Label("name"));
				item.add(new Label("lastName"));
				item.add(new Label("email"));
				item.add(new Link<User>("remove", item.getModel()) {
					@Override
					public void onClick() {
						getCurrentProject().removeMember(getCurrentUser(), getModelObject());
					}

					@Override
					public boolean isVisible() {
						return !getCurrentProject().getOwner().equals(getModelObject());
					}
				});
			}
		});
	}

}
