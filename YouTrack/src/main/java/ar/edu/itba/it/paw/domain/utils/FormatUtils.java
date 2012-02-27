package ar.edu.itba.it.paw.domain.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import ar.edu.itba.it.paw.domain.common.Duration;
import ar.edu.itba.it.paw.domain.project.ProjectVersion;


public class FormatUtils {
	
	public static String formatDurationES(Duration duration) {
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
	
	public static String printVersionsWithCommas(Iterable<ProjectVersion> it) {
		String ret = "";
		
		if(ValidationUtils.isNull(it)) {
			return ret;
		}
		
		boolean cma = false;
		for(ProjectVersion v : it) {
			ret += (cma == true ? "," : "") + v.getName();
			cma = true;
		}
		return ret;
	}

	public static String printDateTime(DateTime time) {
		return time.toString(DateTimeFormat.forPattern("dd/MM/yyyy"));
	}
}