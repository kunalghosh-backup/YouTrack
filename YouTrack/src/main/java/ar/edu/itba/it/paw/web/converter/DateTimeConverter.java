package ar.edu.itba.it.paw.web.converter;

import java.util.Locale;

import org.apache.wicket.util.convert.IConverter;
import org.joda.time.DateTime;

import ar.edu.itba.it.paw.web.utils.WebUtils;

public class DateTimeConverter implements IConverter {

	@Override
	public Object convertToObject(String value, Locale locale) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public String convertToString(Object sts, Locale locale) {
		return new StringBuilder(((DateTime) sts).toString(WebUtils.getString("dateFormat") + " HH:mm", locale)).append(" hs").toString();
	}

}
