package ar.edu.itba.it.paw.domain.task;

import java.util.LinkedList;
import java.util.List;

public class TaskRelationships {

	private List<Task> dependsOn = new LinkedList<Task>();
	
	private List<Task> duplicatedWith = new LinkedList<Task>();
	
	private List<Task> relatedWith = new LinkedList<Task>();
	
	private List<Task> requiredFor = new LinkedList<Task>();
	
	
	public TaskRelationships(Iterable<Relationship> rels) {
		for(Relationship r : rels) {
			if(r.getRelationshipType().equals(Relationship.RelationshipType.DEPENDSON)) {
				dependsOn.add(r.getTaskB());
			}else if(r.getRelationshipType().equals(Relationship.RelationshipType.DUPLICATEDWITH)) {
				duplicatedWith.add(r.getTaskB());
			}else if(r.getRelationshipType().equals(Relationship.RelationshipType.RELATEDWITH)) {
				relatedWith.add(r.getTaskB());
			}else if(r.getRelationshipType().equals(Relationship.RelationshipType.REQUIREDFOR)) {
				requiredFor.add(r.getTaskB());
			}
		}
	}


	public Iterable<Task> getDependsOn() {
		return dependsOn;
	}


	public Iterable<Task> getDuplicatedWith() {
		return duplicatedWith;
	}


	public Iterable<Task> getRelatedWith() {
		return relatedWith;
	}


	public Iterable<Task> getRequiredFor() {
		return requiredFor;
	}

}
