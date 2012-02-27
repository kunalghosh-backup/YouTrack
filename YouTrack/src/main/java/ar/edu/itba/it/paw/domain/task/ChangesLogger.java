package ar.edu.itba.it.paw.domain.task;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ar.edu.itba.it.paw.domain.common.PersistentEntity;
import ar.edu.itba.it.paw.domain.user.User;

@Entity
public class ChangesLogger extends PersistentEntity implements Comparable<ChangesLogger> {
	
	@Column(name="created_at")
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime createdAt;
	
	@ManyToOne
	private User editor;
	
	private String oldValue;
	
	private String newValue;

	ChangesLogger() {
		super();
	}
	
	ChangesLogger(User edithor, DateTime creationDate, String oldValue, String newValue) {
		super();
		 setCreatedAt(creationDate);
		 setEdithor(edithor);
		 setOldValue(oldValue);
		 setNewValue(newValue);
	}

	public DateTime getCreatedAt() {
		return createdAt;
	}

	private void setCreatedAt(DateTime creationDate) {
		if(creationDate != null) {
			this.createdAt = creationDate;
		}
	}

	public User getEdithor() {
		return editor;
	}

	private void setEdithor(User edithor) {
		if(edithor != null) {
			this.editor = edithor;
		}
	}

	public String getOldValue() {
		return oldValue;
	}

	private void setOldValue(String oldValue) {
		if(oldValue != null) {
			this.oldValue = oldValue;
		}
	}

	public String getNewValue() {
		return newValue;
	}

	private void setNewValue(String newValue) {
		if(newValue != null) {
			this.newValue = newValue;
		}
	}

	@Override
	public int compareTo(ChangesLogger loggerEntry) {
		
		int aux;
		if((aux = loggerEntry.createdAt.compareTo(this.createdAt)) != 0) {
			return aux;
		}else if((aux = this.oldValue.compareTo(loggerEntry.oldValue)) != 0) {
			return aux;
		}else if((aux = this.newValue.compareTo(loggerEntry.oldValue)) != 0) {
			return aux;
		}else{
			return 0;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + ((editor == null) ? 0 : editor.hashCode());
		result = prime * result
				+ ((newValue == null) ? 0 : newValue.hashCode());
		result = prime * result
				+ ((oldValue == null) ? 0 : oldValue.hashCode());
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
		ChangesLogger other = (ChangesLogger) obj;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		if (editor == null) {
			if (other.editor != null)
				return false;
		} else if (!editor.equals(other.editor))
			return false;
		if (newValue == null) {
			if (other.newValue != null)
				return false;
		} else if (!newValue.equals(other.newValue))
			return false;
		if (oldValue == null) {
			if (other.oldValue != null)
				return false;
		} else if (!oldValue.equals(other.oldValue))
			return false;
		return true;
	}

}
