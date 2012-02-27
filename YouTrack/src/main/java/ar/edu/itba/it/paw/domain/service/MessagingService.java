package ar.edu.itba.it.paw.domain.service;

import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.user.User;

public interface MessagingService {

	public void newMemberNotification(Project project, User newMember);
	
	public void newTaskNotification(Project project, Task newTask);
	
	public void TaskStatusNotification(Project project, Task task, Task.Status oldStatus);
	
}
