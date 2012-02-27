package ar.edu.itba.it.paw.web.converter;

import java.util.Locale;

import org.apache.wicket.Application;
import org.apache.wicket.util.convert.IConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import ar.edu.itba.it.paw.web.utils.WebUtils;

public class DateConverter implements IConverter {

	@Override
	public Object convertToObject(String datetime, Locale locale) {
		if(datetime.isEmpty()) {
			return null;
		}
		return DateTimeFormat.forPattern(Application.get().getResourceSettings().getLocalizer().getString("dateFormat", null)).parseDateTime(datetime);
	}

	@Override
	public String convertToString(Object sts, Locale locale) {
		return ((DateTime) sts).toString(WebUtils.getString("dateFormat"), locale);
	}
}
