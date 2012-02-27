package ar.edu.itba.it.paw.web.converter;

import java.util.Locale;

import org.apache.wicket.Application;
import org.apache.wicket.util.convert.IConverter;

public class EnumConverter implements IConverter {

	@Override
	public Object convertToObject(String value, Locale locale) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public String convertToString(Object value, Locale locale) {
		return Application.get().getResourceSettings().getLocalizer().getString(value.toString(), null);
	}

}
