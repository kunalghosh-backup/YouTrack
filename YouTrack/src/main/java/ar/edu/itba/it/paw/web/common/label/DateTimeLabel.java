package ar.edu.itba.it.paw.web.common.label;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.util.convert.IConverter;
import org.joda.time.DateTime;

import ar.edu.itba.it.paw.web.converter.DateTimeConverter;

public class DateTimeLabel extends Label {

	public DateTimeLabel(String id) {
		super(id);
	}

	@Override
	public IConverter getConverter(Class<?> type) {
		if(DateTime.class.equals(type)) {
			return new DateTimeConverter();
		}
		return super.getConverter(type);
	}

}
