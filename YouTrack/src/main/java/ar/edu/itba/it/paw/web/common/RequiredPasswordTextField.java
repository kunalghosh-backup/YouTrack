package ar.edu.itba.it.paw.web.common;

import org.apache.wicket.markup.html.form.PasswordTextField;

public class RequiredPasswordTextField extends PasswordTextField {

	public RequiredPasswordTextField(String id) {
		super(id);
		setRequired(true);
	}

}
