package ar.edu.itba.it.paw.web.converter;

import java.util.Locale;

import org.apache.wicket.util.convert.IConverter;

import ar.edu.itba.it.paw.web.utils.WebUtils;

public class WorkDurationConverter extends DurationConverter implements IConverter{

	@Override
	public String convertToString(Object value, Locale locale) {
		String ans = super.convertToString(value, locale);
		if(ans.isEmpty()){
			ans = WebUtils.getString("emptyWorkDuration");
		}
		return ans;
	}
}
