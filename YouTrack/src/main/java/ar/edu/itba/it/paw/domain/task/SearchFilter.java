package ar.edu.itba.it.paw.domain.task;

import org.joda.time.DateTime;

import ar.edu.itba.it.paw.domain.project.ProjectVersion;
import ar.edu.itba.it.paw.domain.user.User;

public class SearchFilter {

	private String code;
	private String title;
	private String description;
	private User creator;
	private DateTime datefrom;
	private DateTime dateto;
	private User owner;
	private Task.Priority priority;
	private Task.Status status;
	private Task.TType type;
	private ProjectVersion version;
	private ProjectVersion affectedVersion;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public User getCreator() {
		return creator;
	}
	
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	public DateTime getDatefrom() {
		return datefrom;
	}
	
	public void setDatefrom(DateTime datefrom) {
		this.datefrom = datefrom;
	}
	
	public DateTime getDateto() {
		return dateto;
	}
	
	public void setDateto(DateTime dateto) {
		this.dateto = dateto;
	}
	
	public User getOwner() {
		return owner;
	}
	
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	public Task.Priority getPriority() {
		return priority;
	}
	
	public void setPriority(Task.Priority priority) {
		this.priority = priority;
	}
	
	public Task.Status getStatus() {
		return status;
	}
	
	public void setStatus(Task.Status status) {
		this.status = status;
	}

	public Task.TType getType() {
		return type;
	}

	public void setType(Task.TType type) {
		this.type = type;
	}

	public ProjectVersion getVersion() {
		return version;
	}

	public void setVersion(ProjectVersion version) {
		this.version = version;
	}

	public ProjectVersion getAffectedVersion() {
		return affectedVersion;
	}

	public void setAffectedVersion(ProjectVersion affectedVersion) {
		this.affectedVersion = affectedVersion;
	}

	@Override
	public String toString() {
		return "SearchFilter [code=" + code + ", title=" + title
				+ ", description=" + description + ", creator=" + creator
				+ ", datefrom=" + datefrom + ", dateto=" + dateto + ", owner="
				+ owner + ", priority=" + priority + ", status=" + status
				+ ", type=" + type + ", version=" + version
				+ ", affectedVersion=" + affectedVersion + "]";
	}
	
}
