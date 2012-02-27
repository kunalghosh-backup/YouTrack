package ar.edu.itba.it.paw.domain.task;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
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

import ar.edu.itba.it.paw.domain.common.Duration;
import ar.edu.itba.it.paw.domain.common.DurationContainer;
import ar.edu.itba.it.paw.domain.common.PersistentEntity;
import ar.edu.itba.it.paw.domain.exception.DomainException;
import ar.edu.itba.it.paw.domain.exception.PrivilegeException;
import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.project.ProjectVersion;
import ar.edu.itba.it.paw.domain.service.MessagingService;
import ar.edu.itba.it.paw.domain.task.Relationship.RelationshipType;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.utils.FormatUtils;
import ar.edu.itba.it.paw.domain.utils.ValidationUtils;

import com.google.common.collect.Iterables;

@Entity
public class Task extends PersistentEntity implements Comparable<Task>, DurationContainer {
	
	public static enum Status {
		OPEN("Abierta"), ONGOING("En curso"), COMPLETED("Finalizada"), CLOSED("Cerrada");

		private String name;

		Status(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public String getValue() {
			return this.toString();
		}
	};

	public static enum Priority {
		TRIVIAL("Trivial"), LOW("Baja"), NORMAL("Normal"), HIGH("Alta"), CRITICAL("Crítica");

		private String name;

		private Priority(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public String getValue() {
			return this.toString();
		}
	};

	public static enum Solution {
		RESOLVED("Resuelta"), NOT_BE_RESOLVED("No se resuelve"), IRREPRODUCIBLE("Irreproducible"), DUPLICATED("Duplicada"), INCOMPLETE("Incompleta");

		private String name;

		private Solution(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public String getValue() {
			return this.toString();
		}
	}
	
	public static enum TType {
		ERROR("Error"), NEW_FEATURE("Nueva característica"), IMPROVEMENT("Mejora"), TECHNICAL("Técnica"), TASK("Tarea");
		
		private String name;
		
		private TType(String name) {
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
		
		public String getValue() {
			return this.toString();
		}
	}
	
	
	@ManyToOne(optional=false)
	private Project project;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="creator_id")
	private User creator;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User owner;
	
	@Column(nullable=false)
	private String title;
	
	private String description;
	
	@Column(nullable=false)
	private int index;
	
	@Column(name="created_at")
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime createdAt;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Enumerated(EnumType.STRING)
	private Priority priority;
	
	@Enumerated(EnumType.STRING)
	private Solution solution;
	
	@Enumerated(EnumType.STRING)
	private TType type;
	
	@Embedded
	private Duration duration;
	
	@Column(nullable=false)
	private int views = 0;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="task_id", nullable=false)
	@Sort(type=SortType.NATURAL)
	private SortedSet<TaskComment> comments = new TreeSet<TaskComment>();
	
	@OneToMany(cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name="task_id")
	private Set<Vote> votes = new HashSet<Vote>();
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="task_affected_versions")
	private Set<ProjectVersion> affectedVersions = new HashSet<ProjectVersion>();
	
	@ManyToMany
	@JoinTable(name="task_versions")
	private Set<ProjectVersion> versions = new HashSet<ProjectVersion>();
	
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="task_id", nullable=false)
	@Sort(type=SortType.NATURAL)
	private SortedSet<WorkReport> reports = new TreeSet<WorkReport>();
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="task_id", nullable=false)
	@Sort(type=SortType.NATURAL)
	private SortedSet<ChangesLogger> changesLogger = new TreeSet<ChangesLogger>();
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="task_id", nullable=false)
	private Set<TaskFile> taskFiles = new HashSet<TaskFile>();
	
	@OneToMany(cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name="task_id", nullable=false)
	private Set<Relationship> relationships = new HashSet<Relationship>();
	
	Task(){
	}
	
	private Task(User creator, Project project, DateTime createdAt, String title, Priority priority, Status status, Integer index, TType type, MessagingService messagingService) {

		ValidationUtils.checkAdminOrMember(creator, project);
		
		this.setCreator(creator);
		this.setProject(project);
		this.setCreatedAt(createdAt, creator);
		this.setTitle(title, creator);
		this.setPriority(priority, creator);
		this.setStatus(status,creator, messagingService);
		this.setIndex(index, creator);
		this.setType(type, creator);
		
		messagingService.newTaskNotification(project, this);
	}
	
	public Task(User creator, Project project, DateTime createdAt, String title, Priority priority, Integer index, TType type, MessagingService messagingService) {
		this(creator, project, createdAt, title, priority, Status.OPEN, index, type, messagingService);
	}
	
	public Task(User creator, Project project, DateTime createdAt, String title, Priority priority, Integer index, User owner, String description, Duration duration, TType type, List<ProjectVersion> versions, List<ProjectVersion> affectedVersions, MessagingService messagingService, List<Task> dependsOnList, List<Task> duplicatedWithList, List<Task> relatedWithList, List<Task> requiredForList) {
		this(creator, project,createdAt, title, priority, index, type, messagingService);
		this.setOwner(owner, creator);
		this.setDescription(description, creator);
		this.setDuration(duration, creator);
		this.setType(type, creator);
		this.setVersions(versions, creator);
		this.setAffectedVersions(affectedVersions, creator);
		
		addRelationships(dependsOnList, duplicatedWithList, relatedWithList, requiredForList);
		
	}
	
	private void addRelationships(List<Task> dependsOnList, List<Task> duplicatedWithList, List<Task> relatedWithList, List<Task> requieredForList) {
		if(!ValidationUtils.isNull(dependsOnList)){
			for(Task t : dependsOnList){
				this.addRelationship(t, RelationshipType.DEPENDSON);
			}
		}
		if(!ValidationUtils.isNull(duplicatedWithList)) {
			for(Task t : duplicatedWithList){
				this.addRelationship(t, RelationshipType.DUPLICATEDWITH);
			}
		}

		if(!ValidationUtils.isNull(relatedWithList)) {
			for(Task t : relatedWithList){
				this.addRelationship(t, RelationshipType.RELATEDWITH);
			}
		}
		
		if(!ValidationUtils.isNull(requieredForList)) {
			for(Task t : requieredForList){
				this.addRelationship(t, RelationshipType.REQUIREDFOR);
			}
		}
	}
	
	
	public int getIndex() {
		return index;
	}

	private void setIndex(int index, User editor) {
		if(index >= 0){
			this.index = index;
		}else{
			throw new DomainException("Invalid index");
		}
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner, User editor) {
		ValidationUtils.isAdminOrMember(editor, this.project);
		if(!ValidationUtils.isNull(owner)) {
			if(!Iterables.contains(this.project.getMembers(), owner)) {
				throw new DomainException("The owner muste be a member");
			}
		}
		if(ValidationUtils.areDifferent(this.owner, owner)) {
			if(ValidationUtils.isNull(this.owner)) {
				logChange(editor, "Dueño: No asignado", "Dueño: " + owner.getUserName());
			}else if(ValidationUtils.isNull(owner)) {
				logChange(editor, "Dueño: " + this.owner.getUserName(), "Dueño: No asignado");
			}else{
				logChange(editor, "Dueño: " + this.owner.getUserName(), "Dueño: " + owner.getUserName());
			}
		}
		this.owner = owner;
	}
	
	public Project getProject() {
		return project;
	}

	private void setProject(Project project) {
		if (!ValidationUtils.isNull(project)) {
			this.project = project;
		}else{
			throw new DomainException("Invalid project");
		}
	}

	public Status getStatus() {
		return status;
	}

	private void setStatus(Status status, User editor, MessagingService messagingService) {
		if(ValidationUtils.isNull(status)) {
			throw new DomainException("Invalid status");
		}
		if(!isNew() && ValidationUtils.areDifferent(this.status, status)) {
			logChange(editor, "Estado: " + this.status.getName(), "Estado: " + status.getName());
		}
		Status oldStatus = this.status;
		this.status = status;
		if(oldStatus != null) {
			messagingService.TaskStatusNotification(this.project, this, oldStatus);
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title, User editor) {
		ValidationUtils.checkAdminOrMember(editor, this.project);
		if (ValidationUtils.between(title, 4, 30)) {
			if(!isNew() && ValidationUtils.areDifferent(this.title, title)) {
				logChange(editor, "Título: " + this.title, "Título: " + title);
			}
			this.title = title;
		} else {
			throw new DomainException("Invalid title");
		}
	}

	public String getCode() {
		return this.getProject().getCode() + "-" + this.index;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description , User editor) {
		ValidationUtils.checkAdminOrMember(editor, this.project);
		
		if(!ValidationUtils.isNull(description)) {
			if(ValidationUtils.maxLength(description, 200)) {
				if(!isNew() && ValidationUtils.areDifferent(this.description, description)) {
					logChange(editor, "Descripción: " + this.description, "Descripción: " + description);
				}
				this.description = description;
			}else{
				throw new DomainException("Invalid description");
			}
		}
	}

	public Solution getSolution() {
		return solution;
	}

	private void setSolution(Solution solution, User editor) {
		ValidationUtils.checkAdminOrMember(editor, this.project);
		if(ValidationUtils.isNull(solution)){
			throw new DomainException("Invalid solution");
		}
		if(!isNew()) {
			logChange(editor, "Sin solución", "Solución: " + solution.getName());
		}
		this.solution = solution;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority, User editor) {
		ValidationUtils.checkAdminOrMember(editor, this.project);
		if (ValidationUtils.isNull(priority)) {
			throw new DomainException("Invalid priority");
		}
		if(!isNew() && ValidationUtils.areDifferent(this.priority.getName(), priority.getName())){
			logChange(editor, "Prioridad: " + this.priority.getName(), "Prioridad: " + priority.getName());
		}
		this.priority = priority;
	}

	public User getCreator() {
		return creator;
	}

	private void setCreator(User creator) {
		if(ValidationUtils.isNull(creator)) {
			throw new DomainException("Invalid creator");
		}
		this.creator = creator;
	}

	public DateTime getCreatedAt() {
		return createdAt;
	}

	private void setCreatedAt(DateTime createdAt, User editor) {
		ValidationUtils.checkAdminOrMember(editor, this.project);
		if (!ValidationUtils.isNull(createdAt)) {
			this.createdAt = createdAt;
		} else {
			throw new DomainException("Invalid date");
		}
	}

	@Override
	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration, User editor) {
		ValidationUtils.checkAdminOrMember(editor, this.project);
		if(duration == null) {
			duration = new Duration(0);
		}
		
		if(!isNew() && ValidationUtils.areDifferent(this.duration, duration)) {
			logChange(editor, "Duración: " + FormatUtils.formatDurationES(this.duration), "Duración: " + FormatUtils.formatDurationES(duration));
		}
		
		this.duration = duration;
	}

	public Iterable<TaskComment> getComments() {
		return comments;
	}

	public Iterable<WorkReport> getWorkReports() {
		return reports;
	}
	
	public void open(User editor, MessagingService messagingService) {
		this.setStatus(Status.OPEN, editor, messagingService);
	}
	
	public void startWorking(User editor, MessagingService messagingService) {
		
		this.setStatus(Status.ONGOING, editor, messagingService);
	}
	
	public void solve(Task.Solution solution,User editor , MessagingService messagingService) {
		this.setStatus(Status.COMPLETED,editor ,messagingService);
		this.setSolution(solution, editor);
	}
	
	public void close(User editor, MessagingService messagingService) {
		this.setStatus(Status.CLOSED, editor, messagingService);
	}
	
	public void addComment(User author, String comment) {
		ValidationUtils.checkAdminOrMember(author, this.project);
		comments.add(new TaskComment(author, new DateTime(System.currentTimeMillis()), comment));
	}
	
	public void addReport(User author, Duration duration, String description) {
		ValidationUtils.checkAdminOrMember(author, this.project);
		reports.add(new WorkReport(author, new DateTime(System.currentTimeMillis()), duration, description));
	}
	
	public Duration getWorkedTime() {
		int ret = 0;
		for(WorkReport wr : reports) {
			ret += wr.getDuration().getMinutes();
		}
		return new Duration(ret);
	}

	public boolean hasVoted(User voter){
		for(Vote vote : this.votes) {
			if(vote.getVoter().equals(voter)){
				return true;
			}
		}
		return false;
	}

	public void addVote(User voter) {
		checkVoter(voter);
		this.votes.add(new Vote(voter));
	}
	
	public void removeVote(User voter) {
		checkVoter(voter);
		if(hasVoted(voter)) {
			this.votes.remove(new Vote(voter));
		}
	}
	
	public int getTotalVotes(){
		return this.votes.size();
	}
	
	private void checkVoter(User voter) {
		if(ValidationUtils.isNull(voter)) {
			throw new PrivilegeException("The voter must be logged to vote");
		}else if(voter.equals(getCreator())){
			throw new DomainException("The creator can not vote");
		}else if(!project.isPublic() && !ValidationUtils.isAdminOrMember(voter, this.project)) {
			throw new PrivilegeException("The voter must be a member or an admin");
		}
	}
	
	public Iterable<ProjectVersion> getVersions() {
		return versions;
	}

	public void setVersions(List<ProjectVersion> versions, User editor) {
		
		if(!isNew() && !ValidationUtils.areEquals(getVersions(), versions)){
			logChange(editor, "Versiones: " + FormatUtils.printVersionsWithCommas(getVersions()), "Versiones: " + FormatUtils.printVersionsWithCommas(versions));
		}
		if(!ValidationUtils.isNull(versions)) {
			this.versions.retainAll(versions);
			this.versions.addAll(versions);
		}else{
			this.versions.clear();
		}
	}
	
	public Iterable<ProjectVersion> getAffectedVersions() {
		return affectedVersions;
	}

	public void setAffectedVersions(List<ProjectVersion> affectedVersions, User editor) {
		if(!isNew() && !ValidationUtils.areEquals(this.affectedVersions, affectedVersions)){
			logChange(editor, "Versiones afectadas: " + FormatUtils.printVersionsWithCommas(this.affectedVersions), "Versiones afectadas: " + FormatUtils.printVersionsWithCommas(affectedVersions));
		}
		if(!ValidationUtils.isNull(affectedVersions)) {
			this.affectedVersions.retainAll(affectedVersions);
			this.affectedVersions.addAll(affectedVersions);
		}else{
			this.affectedVersions.clear();
		}
	}

	public TType getType() {
		return type;
	}

	public SortedSet<ChangesLogger> getChangesLogger(){
		return this.changesLogger;
	}
	
	public void setType(TType type, User editor) {
		if(!isNew() && ValidationUtils.areDifferent(this.type, type)) {
			if(this.type != null) {
				if(type == null) {
					logChange(editor, "Tipo: " + this.type.getName(), "Tipo: No asignado");
				} else {
					logChange(editor, "Tipo: " + this.type.getName(), "Tipo: " + type.getName());
				}
			}else{
				if(type == null) {
					logChange(editor, "Tipo: No asignado", "Tipo: No asignado");
				} else {
					logChange(editor, "Tipo: No asignado", "Tipo: " + type.getName());
				}
			}
		}
		this.type = type;
	}

	public Iterable<TaskFile> getFiles(){
		return this.taskFiles;
	}
	
	public void addTaskFile(TaskFile taskFile){
		this.taskFiles.add(taskFile);
	}
	
	public TaskRelationships getRelationships() {
		return new TaskRelationships(this.relationships);
	}
	
	public void addRelationship(Task taskB, RelationshipType type) {
		if(!ValidationUtils.isNull(taskB)){
			this.relationships.add(new Relationship(taskB, type));
			if(type.equals(RelationshipType.DEPENDSON)) {
				taskB.relationships.add(new Relationship(this, RelationshipType.REQUIREDFOR));
			}else if(type.equals(RelationshipType.REQUIREDFOR)){
				taskB.relationships.add(new Relationship(this, RelationshipType.DEPENDSON));
			}else{ 
				taskB.relationships.add(new Relationship(this, type));
			}
		}
	}
	
	public void updateRelationship(Collection<Task> newRelationships, RelationshipType type) {
		if(!ValidationUtils.isNull(newRelationships)) {
			retainRelationships(newRelationships, type);
			for(Task task : newRelationships) {
				this.addRelationship(task, type);
			}
		}else{
			removeAllRelationships(type);
		}
	}
	
	private void removeAllRelationships(RelationshipType type){
		Set<Relationship> relationshipCopy = new HashSet<Relationship>(this.relationships);
		for(Relationship rel : relationshipCopy){
			if(rel.getRelationshipType().equals(type)) {
				this.removeRelationship(rel);
			}
		}
	}
	
	private void retainRelationships(Collection<Task> newRelationships, RelationshipType type) {
		
		Set<Relationship> auxRelationship = new HashSet<Relationship>(this.relationships);
		
		for(Relationship relationship : auxRelationship) {
			if(!newRelationships.contains(relationship.getTaskB()) && relationship.getRelationshipType().equals(type) ) {
				this.removeRelationship(relationship);
			}
		}
	}

	private void logChange(User editor, String oldV, String newV) {
		changesLogger.add(new ChangesLogger(editor, new DateTime(System.currentTimeMillis()), oldV, newV));
	}
	
	private void removeRelationship(Relationship relationship){
		this.relationships.remove(relationship);
		if(relationship.getRelationshipType().equals(RelationshipType.DEPENDSON)) {
			relationship.getTaskB().relationships.remove(new Relationship(this, RelationshipType.REQUIREDFOR));
		}else if(relationship.getRelationshipType().equals(RelationshipType.REQUIREDFOR)){
			relationship.getTaskB().relationships.remove(new Relationship(this, RelationshipType.DEPENDSON));
		}else { 
			relationship.getTaskB().relationships.remove(new Relationship(this, relationship.getRelationshipType()));
		}
	}

	@Override
	public int compareTo(Task o) {
		return this.index - o.index;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
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
		Task other = (Task) obj;
		if (index != other.index)
			return false;
		return true;
	}
	
	public int getProgress() {
		Duration aux;
		int wt = (aux = getWorkedTime()) == null ? 0 : aux.getMinutes();
		int et = (aux = getDuration()) == null ? 0 : aux.getMinutes();
		if(et == 0) {
			return wt > 0 ? 100 : 0;
		}else{
			if(wt >= et) {
				return 100;
			}else{
				return (wt*100)/et;
			}
		}
	}
	
	public int getViews() {
		return views;
	}
	
	public void addView() {
		this.views++;
	}

	public boolean isOwner(User user) {
		return this.owner != null && this.owner.equals(user);
	}
	
	public boolean isOpen() {
		return this.status.equals(Status.OPEN);
	}
	
	public boolean isStarted() {
		return this.status.equals(Status.ONGOING);
	}
	
	public boolean isClosed() {
		return this.status.equals(Status.CLOSED);
	}
	
	public boolean hasFiles() {
		return !this.taskFiles.isEmpty();
	}
	
	public boolean hasWorkReports() {
		return !this.reports.isEmpty();
	}
	
	public boolean isTheCreator(User user) {
		return this.getCreator().equals(user);
	}
	
}
