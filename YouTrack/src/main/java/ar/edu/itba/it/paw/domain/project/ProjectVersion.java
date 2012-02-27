package ar.edu.itba.it.paw.domain.project;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ar.edu.itba.it.paw.domain.common.Duration;
import ar.edu.itba.it.paw.domain.common.PersistentEntity;
import ar.edu.itba.it.paw.domain.exception.DomainException;
import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.domain.utils.ValidationUtils;

@Entity
@Table(name="project_version")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class ProjectVersion extends PersistentEntity implements Comparable<ProjectVersion> {

	public enum Status{
		OPEN("Abierta"), ONGOING("En curso"), RELEASED("Liberada");
		
		private String name;
		
		private Status(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
		
		public String getValue() {
			return this.toString();
		}
	}

	@Column(name="release_date", nullable=false)
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime releaseDate;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Column(nullable=false)
	private String name;
	
	private String description;
	
	ProjectVersion(){
	}
	
	ProjectVersion(DateTime releaseDate, Status status, String name, String description) {
		super();
		this.setReleaseDate(releaseDate);
		this.setStatus(status);
		this.setName(name);
		this.setDescription(description);
	}
	
	public DateTime getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(DateTime releaseDate) {
		if(!ValidationUtils.isNull(releaseDate)){
			this.releaseDate = releaseDate;
		}else{
			throw new DomainException("Invalid date");
		}
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		if(!ValidationUtils.isNull(status)){
			this.status = status;
		}else{
			throw new DomainException("Invalid status");
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(ValidationUtils.hasText(name)){
			this.name = name;
		}else{
			throw new DomainException("Invalid name");
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if(ValidationUtils.isNull(description)){
			this.description = "";
		}else{
			this.description = description;
		}
	}

	public float getProgress(TaskRepository tasks) {
		int total = 0;
		int completed = 0;
		for(Task t : getTasks(tasks)) {
			if(t.getStatus() == Task.Status.COMPLETED || t.getStatus() == Task.Status.CLOSED) {
				completed++;
			}
			total++;
		}
		return ((float)completed)/total;
	}
	

	public Duration getWorkedTime(TaskRepository tasks) {
		Duration w = new Duration(0);
		for(Task t : getTasks(tasks)) {
			w = w.add(t.getWorkedTime());
		}
		return w;
	}
	
	public Duration getEstimatedTime(TaskRepository tasks) {
		Duration e = new Duration(0);
		for(Task t : getTasks(tasks)) {
			e = e.add(t.getDuration());
		}
		return e;
	}
	
	public Duration getEstimatedFinish(TaskRepository tasks) {
		return getEstimatedTime(tasks).diff(getWorkedTime(tasks));
	}
	
	public List<Task> getTasks(TaskRepository tasks) {
		return tasks.getByVersion(this);
	}
	
	@Override
	public int compareTo(ProjectVersion o) {
		int aux;
		if((aux = releaseDate.compareTo(o.releaseDate)) != 0) {
			return aux;
		}else if((aux = name.compareTo(o.name)) != 0) {
			return aux;
		}
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((releaseDate == null) ? 0 : releaseDate.hashCode());
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
		ProjectVersion other = (ProjectVersion) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (releaseDate == null) {
			if (other.releaseDate != null)
				return false;
		} else if (!releaseDate.equals(other.releaseDate))
			return false;
		return true;
	}

}
