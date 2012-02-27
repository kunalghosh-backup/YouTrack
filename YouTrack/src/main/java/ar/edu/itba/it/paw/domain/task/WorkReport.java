package ar.edu.itba.it.paw.domain.task;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ar.edu.itba.it.paw.domain.common.Duration;
import ar.edu.itba.it.paw.domain.common.DurationContainer;
import ar.edu.itba.it.paw.domain.common.PersistentEntity;
import ar.edu.itba.it.paw.domain.exception.DomainException;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.utils.ValidationUtils;

@Entity
@Table(name="workreport")
public class WorkReport extends PersistentEntity implements Comparable<WorkReport>, DurationContainer {

	@ManyToOne
	private User author;
	
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	@Column(name="reported_at")
	private DateTime reportedAt;
	
	@Embedded
	private Duration duration;
	
	private String description;
	
	WorkReport(){
	}
	
	WorkReport(User author, DateTime reportedAt, Duration duration, String description) {
		super();
		this.setAuthor(author);
		this.setReportedAt(reportedAt);
		this.setDuration(duration);
		this.setDescription(description);
	}

	public User getAuthor() {
		return author;
	}
	
	private void setAuthor(User author) {
		if(!ValidationUtils.isNull(author)) {
			if(!author.isBanned()) {
				this.author = author;
			}else{
				throw new DomainException("The author is banned");
			}
		}
	}
	
	public DateTime getReportedAt() {
		return reportedAt;
	}
	
	private void setReportedAt(DateTime reportedAt) {
		if(ValidationUtils.isNull(reportedAt)) {
			throw new DomainException("Invalid date");
		}
		this.reportedAt = reportedAt;
	}
	
	@Override
	public Duration getDuration() {
		return duration;
	}
	
	private void setDuration(Duration duration) {
		if(ValidationUtils.isNull(duration)) {
			throw new DomainException("Invalid duration");
		}
		this.duration = duration;
	}
	
	public String getDescription() {
		return description;
	}
	
	private void setDescription(String description) {
		if(!ValidationUtils.between(description, 1, 200)) {
			throw new DomainException("Invalid description");
		}
		this.description = description;
	}

	
	@Override
	public int compareTo(WorkReport o) {
		int aux;
		if((aux = this.reportedAt.compareTo(o.reportedAt)) != 0) {
			return aux;
		}else if((aux = this.description.compareTo(o.description)) != 0) {
			return aux;
		}else{
			return 0;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((duration == null) ? 0 : duration.hashCode());
		result = prime * result
				+ ((reportedAt == null) ? 0 : reportedAt.hashCode());
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
		WorkReport other = (WorkReport) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (reportedAt == null) {
			if (other.reportedAt != null)
				return false;
		} else if (!reportedAt.equals(other.reportedAt))
			return false;
		return true;
	}

}
