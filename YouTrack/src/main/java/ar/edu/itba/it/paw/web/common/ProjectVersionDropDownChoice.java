package ar.edu.itba.it.paw.web.common;

import java.util.Arrays;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.Model;

import ar.edu.itba.it.paw.domain.project.ProjectVersion.Status;

public class ProjectVersionDropDownChoice extends DropDownChoice<Status>{
	
	public ProjectVersionDropDownChoice(String id) {
		super(id, Model.ofList(Arrays.asList(Status.values())));
	}
}
