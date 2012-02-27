package ar.edu.itba.it.paw.domain.task;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ar.edu.itba.it.paw.domain.common.PersistentEntity;
import ar.edu.itba.it.paw.domain.exception.DomainException;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.utils.ValidationUtils;

@Entity
@Table(name="taskcomment")
public class TaskComment extends PersistentEntity implements Comparable<TaskComment>{

	@ManyToOne
	private User author;
	
	@Column(name="created_at")
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime createdAt;
	
	private String comment;
	
	TaskComment(){
	}
	
	TaskComment(User author, DateTime createdAt, String comment) {
		this.setAuthor(author);
		this.setCreatedAt(createdAt);
		this.setComment(comment);
	}

	public User getAuthor() {
		return author;
	}

	private void setAuthor(User author) {
		if(ValidationUtils.isNull(author)) {
			throw new DomainException("Invalid author");
		}else{
			if(author.isBanned()){
				throw new DomainException("Author is banned");
			}
			this.author = author;
		}
	}

	public DateTime getCreatedAt() {
		return createdAt;
	}

	private void setCreatedAt(DateTime createdAt) {
		if(createdAt == null) {
			throw new DomainException("Invalid date");
		}
		this.createdAt = createdAt;
	}

	public String getComment() {
		return comment;
	}

	private void setComment(String comment) {
		if(!ValidationUtils.between(comment, 1, 600)) {
			throw new DomainException("Invalid comment");
		}
		this.comment = comment;
	}

	@Override
	public int compareTo(TaskComment tc) {
		int aux;
		if((aux = this.createdAt.compareTo(tc.createdAt)) != 0) {
			return aux;
		}else if((aux = this.comment.compareTo(tc.comment)) != 0) {
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
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result
				+ ((createdAt == null) ? 0 : createdAt.hashCode());
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
		TaskComment other = (TaskComment) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		return true;
	}

}
