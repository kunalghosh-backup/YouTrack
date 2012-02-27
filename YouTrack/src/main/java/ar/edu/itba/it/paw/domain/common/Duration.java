package ar.edu.itba.it.paw.domain.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Duration implements DurationContainer {

	private static final int LABOR_DAY_HOURS = 8;
	private static final int LABOR_HOUR_MINUTES = 60;

	@Column(name="duration")
	private int minutes;

	Duration() {
	}
	
	public Duration(int day, int hour, int minute) {
		super();
		this.minutes = day * LABOR_DAY_HOURS * LABOR_HOUR_MINUTES + hour * LABOR_HOUR_MINUTES + minute;
	}
	
	public Duration(int minutes) {
		this.minutes = minutes;
	}

	public int getDay() {
		return minutes / (LABOR_DAY_HOURS * LABOR_HOUR_MINUTES);
	}

	public int getHour() {
		return (minutes - getDay() * LABOR_DAY_HOURS * LABOR_HOUR_MINUTES) / LABOR_HOUR_MINUTES;
	}

	public int getMinute() {
		return minutes - getDay() * LABOR_DAY_HOURS * LABOR_HOUR_MINUTES - getHour() * LABOR_HOUR_MINUTES;
	}

	public int getMinutes() {
		return minutes;
	}

	public Duration add(Duration a) {
		if(a == null){
			return this;
		}
		return new Duration(this.getMinutes() + a.getMinutes());
	}
	
	public Duration diff(Duration other) {
		if(other == null) {
			return this;
		}
		int mdiff = this.minutes - other.minutes;
		return mdiff >= 0 ? new Duration(mdiff) : new Duration(0); 
	}

	@Override
	public String toString() {
		return "Duration [day=" + getDay() + ", hour=" + getHour()
				+ ", minute=" + getMinute() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + minutes;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Duration other = (Duration) obj;
		if (minutes != other.minutes)
			return false;
		return true;
	}


	public Duration getDuration() {
		return this;
	}
	
}
