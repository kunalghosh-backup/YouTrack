package ar.edu.itba.it.paw.web.converter;

import java.util.Locale;

import org.apache.wicket.util.convert.IConverter;

import ar.edu.itba.it.paw.domain.project.ProjectVersion;

public class ProjectVersionConverter implements IConverter{

	@Override
	public Object convertToObject(String value, Locale locale) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public String convertToString(Object version, Locale locale) {
		return ((ProjectVersion) version).getName();
	}

}
