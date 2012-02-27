package ar.edu.itba.it.paw.web.common.textfield.autocomplete.renderer;

import ar.edu.itba.it.paw.domain.user.User;

public class UserAutoCompleteConverterRenderer extends AbstractAutoCompleteConverterRenderer<User> {

	@Override
	protected String getTextValue(User u) {
		return super.getTextValue(User.class, u);
	}

}
