package ar.edu.itba.it.paw.domain.task;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import ar.edu.itba.it.paw.domain.common.PersistentEntity;
import ar.edu.itba.it.paw.domain.utils.ValidationUtils;

@Entity
public class Relationship extends PersistentEntity {

	public static enum RelationshipType {
		DEPENDSON("Depende de"), REQUIREDFOR("Es necesaria para"), RELATEDWITH("Esta relacionada con"), DUPLICATEDWITH("Esta duplicada con");
		
		private String name;
		
		RelationshipType(String name) {
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
		
		public String getValue() {
			return this.toString();
		}
	};
	
	@ManyToOne
	private Task taskB;
	
	@Enumerated(EnumType.STRING)
	private RelationshipType relationshipType;

	Relationship() {
	}
	
	Relationship (Task taskB, RelationshipType type ) {
		setTaskB(taskB);
		setRelationshipType(type);

	}
	
	public Task getTaskB() {
		return taskB;
	}

	private void setTaskB(Task taskB) {
		if(!ValidationUtils.isNull(taskB)) {
			this.taskB = taskB;
		}
	}

	public RelationshipType getRelationshipType() {
		return relationshipType;
	}

	private void setRelationshipType (RelationshipType relationship) {
		if(!ValidationUtils.isNull(relationship)){
			this.relationshipType = relationship;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((relationshipType == null) ? 0 : relationshipType.hashCode());
		result = prime * result + ((taskB == null) ? 0 : taskB.hashCode());
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
		Relationship other = (Relationship) obj;
		if (relationshipType != other.relationshipType)
			return false;
		if (taskB == null) {
			if (other.taskB != null)
				return false;
		} else if (!taskB.equals(other.taskB))
			return false;
		return true;
	}

	
	
	
}
