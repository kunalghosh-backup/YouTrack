package ar.edu.itba.it.paw.web.common.dataprovider.project;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.project.ProjectRepository;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class ProjectSortableDataProvider extends SortableDataProvider<Project> {

	protected final ProjectRepository projects;
	private final IModel<User> userModel;
	
	public ProjectSortableDataProvider(ProjectRepository projects, IModel<User> userModel) {
		super();
		this.projects = projects;
		this.userModel = userModel;
	}

	@Override
	public Iterator<? extends Project> iterator(int first, int count) {
		return projects.getAll(first, count, getUser()).iterator();
	}

	@Override
	public int size() {
		return projects.count(getUser());
	}

	@Override
	public IModel<Project> model(Project p) {
		return WebUtils.createProjectModel(p);
	}
	
	public User getUser() {
		return WebUtils.getObjectSafely(userModel);
	}

}
