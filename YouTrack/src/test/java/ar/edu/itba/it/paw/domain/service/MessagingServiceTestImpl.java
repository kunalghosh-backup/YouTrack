package ar.edu.itba.it.paw.domain.service;

import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.Task.Status;
import ar.edu.itba.it.paw.domain.user.User;

public class MessagingServiceTestImpl implements MessagingService {

	public int mailSent = 0;
	
	@Override
	public void newMemberNotification(Project project, User newMember) {
		mailSent++;
	}

	@Override
	public void newTaskNotification(Project project, Task newTask) {
		mailSent++;
	}

	@Override
	public void TaskStatusNotification(Project project, Task task, Status oldStatus) {
		mailSent++;
	}

}
