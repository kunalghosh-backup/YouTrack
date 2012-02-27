package ar.edu.itba.it.paw.web.project.component;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.validation.validator.StringValidator.LengthBetweenValidator;

public class ProjectDescriptionTextArea extends TextArea<String> {

	public ProjectDescriptionTextArea(String id) {
		super(id);
		setRequired(true);
		add(LengthBetweenValidator.maximumLength(200));
	}

}
