package ar.edu.itba.it.paw.web.converter;

import java.util.Locale;

import org.apache.wicket.util.convert.IConverter;

import ar.edu.itba.it.paw.domain.task.Task;

public class TaskConverter implements IConverter {

	@Override
	public Object convertToObject(String value, Locale locale) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public String convertToString(Object o, Locale locale) {
		Task t = (Task) o;
		return new StringBuilder(t.getCode()).append(" - ").append(t.getTitle()).toString();
	}

}
