package ar.edu.itba.it.paw.web.common.textfield.autocomplete.renderer;

import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AbstractAutoCompleteTextRenderer;

public abstract class AbstractAutoCompleteConverterRenderer<T> extends AbstractAutoCompleteTextRenderer<T> {

	protected String getTextValue(Class<T> type, T object) {
		return Application.get().getConverterLocator().getConverter(type).convertToString(object, Session.get().getLocale());
	}
	
}
