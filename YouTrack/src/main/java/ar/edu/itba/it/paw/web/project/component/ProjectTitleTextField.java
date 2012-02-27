package ar.edu.itba.it.paw.web.project.component;

import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.validation.validator.StringValidator.LengthBetweenValidator;

public class ProjectTitleTextField extends RequiredTextField<String> {

	public ProjectTitleTextField(String id) {
		super(id);
		add(LengthBetweenValidator.lengthBetween(4, 80));
	}

}
