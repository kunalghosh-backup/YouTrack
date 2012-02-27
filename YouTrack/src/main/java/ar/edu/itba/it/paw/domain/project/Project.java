 package ar.edu.itba.it.paw.domain.project;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ar.edu.itba.it.paw.domain.common.PersistentEntity;
import ar.edu.itba.it.paw.domain.exception.DomainException;
import ar.edu.itba.it.paw.domain.exception.VersionDeleteException;
import ar.edu.itba.it.paw.domain.service.MessagingService;
import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.utils.ValidationUtils;

@Entity
public class Project extends PersistentEntity implements Comparable<Project>{

	public static enum Status {
		PUBLIC("Público"), 
		PRIVATE("Privado");
	
		private String name;
		
		private Status(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	};
	
	private String title;
	private String code;
	private String description;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Column(name="created_at", nullable=false)
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime createdAt;

	@ManyToOne(optional=false)
	private User owner;
	
	@ManyToMany
	@JoinTable(name="membership")
	private Set<User> members = new HashSet<User>();
	
	@OneToMany(cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Sort(type=SortType.NATURAL)
	@JoinColumn(name="project_id", nullable=false)
	private SortedSet<ProjectVersion> versions = new TreeSet<ProjectVersion>();
	
	Project(){
	}
	
	public Project(User creator, DateTime createdAt, String title, String code, String description, Status status, User owner) {
		super();
		
		ValidationUtils.checkAdminUser(creator);
		
		setOwner(owner, creator);
		setTitle(title, creator);
		setCode(code, creator);
		setDescription(description, creator);
		setCreatedAt(createdAt);
		setStatus(status, creator);
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title, User editor) {
		ValidationUtils.checkAdminOrLeader(editor, this);
		if(ValidationUtils.hasText(title) && ValidationUtils.between(title, 4, 30)) {
			this.title = title;
		}else{
			throw new DomainException("Not valid title");
		}
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code, User editor) {
		ValidationUtils.checkAdminOrLeader(editor, this);
		if(ValidationUtils.hasText(code)){
			this.code = code;
		}else{
			throw new DomainException("Not valid code");
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description, User editor) {
		ValidationUtils.checkAdminOrLeader(editor, this);
		if(ValidationUtils.hasText(description) && ValidationUtils.maxLength(description, 400)){
			this.description = description;
		}else{
			throw new DomainException("Not valid description");
		}
	}

	public DateTime getCreatedAt() {
		return createdAt;
	}

	private void setCreatedAt(DateTime createdAt) {
		if(!ValidationUtils.isNull(createdAt)){
			this.createdAt = createdAt;
		}else{
			throw new DomainException("Not valid createdAt");
		}
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status, User editor) {
		ValidationUtils.checkAdminOrLeader(editor, this);
		if(ValidationUtils.isNull(status)) {
			throw new DomainException("Invalid status");
		}
		this.status = status;
	}

	public void setOwner(User owner, User editor) {
		ValidationUtils.checkAdminOrLeader(editor, this);
		if(!ValidationUtils.isNull(owner)) {
			if(!isMember(owner)){
				members.add(owner);
			}
			this.owner = owner;
		}else{
			throw new DomainException("Invalid owner");
		}
	}

	public User getOwner() {
		return owner;
	}
	
	public void addMember(User projectOwner, User member, MessagingService messagingService) {
		ValidationUtils.checkNotNull(member, "Invalid member");
		ValidationUtils.checkAdminOrLeader(projectOwner, this);
		if(!this.members.contains(member)) {
			messagingService.newMemberNotification(this, member);
			this.members.add(member);
		}
	}
	
	public void removeMember(User projectOwner, User member) {
		ValidationUtils.checkAdminOrLeader(projectOwner, this);
		if(!ValidationUtils.isNull(member)){
			if(member.equals(owner)){
				throw new DomainException("Can't remove the project owner");
			}
			this.members.remove(member);
		}
	}
	
	public boolean isMember(User user) {
		if(user == null) {
			return false;
		}
		return members.contains(user);
	}

	public Iterable<User> getMembers() {
		return members;
	}
	
	public Iterable<User> getMembersWithPrefix(String prefix) {
		List<User> ret = new LinkedList<User>();
		for(User m : getMembers()) {
			if(m.getUserName().startsWith(prefix)) {
				ret.add(m);
			}
		}
		return ret;
	}

	public Iterable<ProjectVersion> getVersions() {
		return versions;
	}
	
	public Iterable<ProjectVersion> getUnreleasedVersions() {
		List<ProjectVersion> ret = new LinkedList<ProjectVersion>();
		for(ProjectVersion v : versions) {
			if(v.getStatus() != ProjectVersion.Status.RELEASED) {
				ret.add(v);
			}
		}
		return ret;
	}
	
	public List<ProjectVersion> getVersionList() {
		return new ArrayList<ProjectVersion>(versions);
	}
	
	public void addVersion(User projectOwner, DateTime releaseDate, ProjectVersion.Status status, String name, String description) {
		ValidationUtils.checkAdminOrLeader(projectOwner, this);
		if(existsVersion(name)) {
			throw new DomainException("Duplicated version");
		}
		versions.add(new ProjectVersion(releaseDate, status, name, description));
	}
	
	public boolean existVersion(String name) {
		for(ProjectVersion v : versions) {
			if(v.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public void addVersion(User projectOwner, ProjectVersion projectVersion) {
		ValidationUtils.checkAdminOrLeader(projectOwner, this);
		versions.add(projectVersion);
	}
	
	public boolean isPublic() {
		return this.status == Status.PUBLIC;
	}

	public void removeVersion(User projectOwner, ProjectVersion version, TaskRepository tasks) {
		ValidationUtils.checkAdminOrLeader(projectOwner, this);
		ValidationUtils.checkNotNull(version, "Invalid version");
		if(hasAssignedTasks(version, tasks)) {
			throw new VersionDeleteException("The version has assigned tasks");
		}
		versions.remove(version);
	}
	
	public boolean hasAssignedTasks(ProjectVersion version, TaskRepository tasks) {
		return !tasks.getByVersion(version).isEmpty();
	}
	
	public int compareTo(Project o) {
		return this.code.compareTo(o.code);
	}
	
	
	public boolean isLeader(User user) {
		return !ValidationUtils.isNull(user) && !ValidationUtils.isNull(this.owner) && this.owner.equals(user);
	}
	
	public boolean existsVersion(String name) {
		for(ProjectVersion v : versions) {
			if(name.equals(v.getName())){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		Project other = (Project) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Project [title=" + title + ", code=" + code + ", description="
		+ description + ", status=" + status + ", createdAt="
		+ createdAt + "]";
	}
	
}
