package ar.edu.itba.it.paw.web.project.detail.versions;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.project.ProjectVersion;
import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.web.base.BasePanel;
import ar.edu.itba.it.paw.web.project.detail.versions.create.CreateEditProjectVersion;

public class ProjectVersionActionPanel extends BasePanel {

	@SpringBean
	private TaskRepository tasks;
	
	public ProjectVersionActionPanel(String id, IModel<ProjectVersion> projectVersionModel) {
		super(id, projectVersionModel);
		
		add(new Link<ProjectVersion>("removeVersion", getModel()) {
			@Override
			public void onClick() {
				 getCurrentProject().removeVersion(getCurrentUser(), getModelObject(), tasks);
			}
			@Override
			public boolean isVisible() {
				return !getCurrentProject().hasAssignedTasks(getModelObject(), tasks);
			}
		});
		add(new Link<ProjectVersion>("editVersion", getModel()) {
			@Override
			public void onClick() {
				setResponsePage(new CreateEditProjectVersion(getModel()));
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private IModel<ProjectVersion> getModel() {
		return (IModel<ProjectVersion>) getDefaultModel();
	}
	
}
