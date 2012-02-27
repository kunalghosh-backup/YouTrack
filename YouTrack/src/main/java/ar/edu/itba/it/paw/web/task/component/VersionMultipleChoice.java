package ar.edu.itba.it.paw.web.task.component;

import java.util.List;

import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.project.ProjectVersion;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class VersionMultipleChoice extends ListMultipleChoice<ProjectVersion>{

	public VersionMultipleChoice(String id, final IModel<Project> projectModel) {
		super(id, new LoadableDetachableModel<List<ProjectVersion>>() {
			@Override
			protected List<ProjectVersion> load() {
				return WebUtils.asList(projectModel.getObject().getVersions());
			}
		});
	}

}
