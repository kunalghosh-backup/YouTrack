package ar.edu.itba.it.paw.web.project.component;

import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.validation.validator.StringValidator.LengthBetweenValidator;

public class ProjectVersionNameTextField extends RequiredTextField<String> {

	public ProjectVersionNameTextField(String id) {
		super(id);
		add(LengthBetweenValidator.lengthBetween(1, 100));
	}

}
