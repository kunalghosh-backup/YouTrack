package ar.edu.itba.it.paw.web.converter;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import ar.edu.itba.it.paw.domain.common.Duration;
import ar.edu.itba.it.paw.domain.utils.ValidationUtils;

public class DurationConverter implements IConverter {

	@Override
	public Object convertToObject(String durationString, Locale locale) {
		if(durationString == null || durationString.isEmpty()) {
			return null;
		}
		if(!durationString.matches("(\\d+d)?\\s*(\\d+h)?\\s*(\\d+m)?")) {
			throw new ConversionException("invalid duration");
		}
		return convertDuration(durationString);
		
	}

	private Duration convertDuration(String validDuration) {
		int day = 0;
		int hour = 0;
		int minute = 0;
		
		String aux;
		
		Pattern p = Pattern.compile("\\d+d");
		Matcher m = p.matcher(validDuration);
		if(m.find()) {
			aux = m.group();
			aux = aux.substring(0, aux.length() - 1);
			day = Integer.valueOf(aux);
		}
		
		p = Pattern.compile("\\d+h");
		m = p.matcher(validDuration);
		if(m.find()) {
			aux = m.group();
			aux = aux.substring(0, aux.length() - 1);
			hour = Integer.valueOf(aux);
		}
		
		p = Pattern.compile("\\d+m");
		m = p.matcher(validDuration);
		if(m.find()) {
			aux = m.group();
			aux = aux.substring(0, aux.length() - 1);
			minute = Integer.valueOf(aux);
		}
		
		return new Duration(day, hour, minute);
	}
	
	@Override
	public String convertToString(Object value, Locale locale) {
		Duration duration = ((Duration) value);
		
		String res = "";
		if(ValidationUtils.isNull(duration)){
			return res;
		}
		if(duration.getDay() > 0) {
			res += duration.getDay() + "d";
		}
		if(duration.getHour() > 0) {
			if(!res.isEmpty()) {
				res += " ";
			}
			res += duration.getHour() + "h";
		}
		if(duration.getMinute() > 0) {
			if(!res.isEmpty()) {
				res += " ";
			}
			res += duration.getMinute() + "m";
		}
		return res;
	}

}
