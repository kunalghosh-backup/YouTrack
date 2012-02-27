package ar.edu.itba.it.paw.web.converter;

import java.util.Locale;

import org.apache.wicket.util.convert.IConverter;

import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepository;

public class UserConverter implements IConverter {

	private UserRepository users;
	
	public UserConverter(UserRepository users) {
		this.users = users;
	}
	
	@Override
	public Object convertToObject(String username, Locale locale) {
		return users.getByUsername(username);
	}

	@Override
	public String convertToString(Object user, Locale locale) {
		return ((User) user).getUserName();
	}

}
