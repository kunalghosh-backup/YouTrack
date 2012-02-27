package ar.edu.itba.it.paw.web.converter;

import java.util.Locale;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepository;
import ar.edu.itba.it.paw.domain.utils.ValidationUtils;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class MemberConverter extends UserConverter implements IConverter {

	public MemberConverter(UserRepository users) {
		super(users);
	}

	@Override
	public Object convertToObject(String username, Locale locale) {
		User u =(User) super.convertToObject(username, locale);
		if(ValidationUtils.hasText(username)) {
			if(u == null || !WebUtils.getCurrentProject().isMember(u)) {
				throw new ConversionException("invalid member");
			}
		}
		return u;
	}

}
