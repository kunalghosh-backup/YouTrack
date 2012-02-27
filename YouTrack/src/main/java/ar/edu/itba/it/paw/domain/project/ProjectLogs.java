package ar.edu.itba.it.paw.domain.project;

import ar.edu.itba.it.paw.domain.task.ChangesLogger;
import ar.edu.itba.it.paw.domain.task.Task;

public class ProjectLogs {

	private ChangesLogger changes;
	private Task task;
	
	public ProjectLogs(Task task, ChangesLogger changes) {
		super();
		setTask(task);
		setChanges(changes);
		
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Task getTask() {
		return task;
	}


	public ChangesLogger getChanges() {
		return changes;
	}


	public void setChanges(ChangesLogger changes) {
		this.changes = changes;
	}

	
}
