package ar.edu.itba.it.paw.web.common;

import java.util.Arrays;

import org.apache.wicket.markup.html.form.DropDownChoice;

import ar.edu.itba.it.paw.domain.project.ProjectVersion;

public class ProjectStatusDropDownChoise extends DropDownChoice<ProjectVersion.Status>{
	
	public ProjectStatusDropDownChoise(String id) {
		super(id, Arrays.asList(ProjectVersion.Status.values()));
	}
}
