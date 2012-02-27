package ar.edu.itba.it.paw.web.project.component.menu;

import org.apache.wicket.markup.html.link.Link;

import ar.edu.itba.it.paw.web.base.BasePanel;
import ar.edu.itba.it.paw.web.project.detail.ProjectDetail;
import ar.edu.itba.it.paw.web.project.list.ProjectList;
import ar.edu.itba.it.paw.web.project.search.SearchFilterModel;
import ar.edu.itba.it.paw.web.project.search.SearchForm;
import ar.edu.itba.it.paw.web.project.working.TaskUserWorkingProject;
import ar.edu.itba.it.paw.web.project.working.TasksWorkingProject;
import ar.edu.itba.it.paw.web.project.working.topfive.TopFive;
import ar.edu.itba.it.paw.web.task.create.CreateEditTask;
import ar.edu.itba.it.paw.web.utils.WebUtils;
import ar.edu.itba.it.paw.web.version.list.VersionList;

public class WorkingProjectMenu extends BasePanel {

	public WorkingProjectMenu(String id) {
		super(id);

		add(new Link<Void>("closeProject") {
			@Override
			public void onClick() {
				setResponsePage(ProjectList.class);
			}
		});
		
		add(new Link<Void>("tasksLink") {
			@Override
			public void onClick() {
				setResponsePage(TasksWorkingProject.class);
			}
		});
		
		add(new Link<Void>("mytasksLink") {
			@Override
			public void onClick() {
				setResponsePage(new TaskUserWorkingProject(getCurrentUserModel()));
			}

			@Override
			public boolean isVisible() {
				return WebUtils.getSession().isSignedIn() && WebUtils.getCurrentProject().isMember(WebUtils.getCurrentUser());
			}
		});
		
		add(new Link<Void>("createTask") {
			@Override
			public void onClick() {
				setResponsePage(new CreateEditTask());
			}
			@Override
			public boolean isVisible() {
				return getCurrentProject().isMember(getCurrentUser());
			}
		});
		
		add(new Link<Void>("topTasks") {
			@Override
			public void onClick() {
				setResponsePage(TopFive.class);
			}
		});
		
		add(new Link<Void>("projectDetail") {
			@Override
			public void onClick() {
				setResponsePage(new ProjectDetail());
			}
			@Override
			public boolean isVisible() {
				return getCurrentProject().isLeader(getCurrentUser());
			}
		});
		
		add(new Link<Void>("advancedSearch") {
			@Override
			public void onClick() {
				setResponsePage(new SearchForm(new SearchFilterModel()));
			}
		});
		
		add(new Link<Void>("versionList") {
			@Override
			public void onClick() {
				setResponsePage(VersionList.class);
			}
		});
	}

}
