package ar.edu.itba.it.paw.web.project.component;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.utils.ValidationUtils;
import ar.edu.itba.it.paw.web.base.BasePanel;
import ar.edu.itba.it.paw.web.project.create.CreateEditProject;
import ar.edu.itba.it.paw.web.project.working.TasksWorkingProject;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class ProjectActionPanel extends BasePanel {


	public ProjectActionPanel(String id, IModel<Project> projectModel) {
		super(id, projectModel);
		this.setDefaultModel(projectModel);
		
		add(new Link<Project>("workingProject", projectModel) {
			@Override
			public void onClick() {
				getTrackerSession().setProject(getModel().getObject());
				setResponsePage(new TasksWorkingProject());
			}
		});
		
		add(new Link<Project>("changeToPublic", projectModel) {
			@Override
			public void onClick() {
				WebUtils.getProject(getModel()).setStatus(Project.Status.PUBLIC, getTrackerSession().getUser());
			}
			
			@Override
			public boolean isVisible() {
				return WebUtils.getProject(getModel()).getStatus().equals(Project.Status.PRIVATE) && ValidationUtils.isAdminUser(getTrackerSession().getUser());
			}
		});
		
		add(new Link<Project>("changeToPrivate", projectModel) {
			@Override
			public void onClick() {
				WebUtils.getProject(getModel()).setStatus(Project.Status.PRIVATE, getTrackerSession().getUser());
			}
			
			@Override
			public boolean isVisible() {
				return WebUtils.getProject(getModel()).getStatus().equals(Project.Status.PUBLIC) && ValidationUtils.isAdminUser(getTrackerSession().getUser());
			}
		});
		
		add(new Link<Project>("editProject", projectModel) {
			@Override
			public void onClick() {
				setResponsePage(new CreateEditProject(getModel()));
			}

			@Override
			public boolean isVisible() {
				return getModel().getObject().isLeader(getCurrentUser()) || WebUtils.isAdmin();
			}
		});
		
	}
	
}
