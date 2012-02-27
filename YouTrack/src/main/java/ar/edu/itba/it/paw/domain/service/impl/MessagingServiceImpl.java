package ar.edu.itba.it.paw.domain.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.service.MessagingService;
import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.Task.Status;
import ar.edu.itba.it.paw.domain.user.User;

@Service
public class MessagingServiceImpl implements MessagingService {

	private JavaMailSender mailSender;
	private Logger log = Logger.getRootLogger();
	
	@Value("${messaging.on}")
	private boolean serviceOn;
	
	@Value("${messaging.mail.from}")
	private String from;
	
	@Value("${messaging.newMember.subject}")
	private String newMemberSubject;

	@Value("${messaging.newMember}")
	private String newMemberTemplate;
	
	@Value("${messaging.newTask.subject}")
	private String newTaskSubject;
	
	@Value("${messaging.newTask}")
	private String newTaskTemplate;
	
	@Value("${messaging.taskStatus.subject}")
	private String taskStatusSubject;
	
	@Value("${messaging.taskStatus}")
	private String taskStatusTemplate;
	
	@Autowired
	public MessagingServiceImpl(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public void newMemberNotification(Project project, User newMember) {
		
		for(User m : project.getMembers()) {
			String message = newMemberTemplate.replaceAll("#USER#", m.getName());
			message = message.replaceAll("#NEWMEMBER#", newMember.getUserName() + "(" + newMember.getLastName() + ", " + newMember.getName() + ")");
			message = message.replaceAll("#PROJECT#", project.getTitle() + "(" + project.getCode() + ")");
			sendMail(m.getEmail(), newMemberSubject, message);
		}
	}

	@Override
	public void newTaskNotification(Project project, Task newTask) {
		
		for(User m : project.getMembers()) {
			String message = newTaskTemplate.replaceAll("#USER#", m.getName());
			message = message.replaceAll("#CREATOR#", newTask.getCreator().getUserName() + "(" + newTask.getCreator().getLastName() + ", " + newTask.getCreator().getName() + ")");
			message = message.replaceAll("#TASK#", newTask.getTitle() + "(" + newTask.getCode() + ")");
			message = message.replaceAll("#PROJECT#", project.getTitle() + "(" + project.getCode() + ")");
			sendMail(m.getEmail(), newTaskSubject, message);
		}
		
	}

	@Override
	public void TaskStatusNotification(Project project, Task task, Status oldStatus) {
		
		for(User m : project.getMembers()) {
			String message = taskStatusTemplate.replaceAll("#USER#", m.getName());
			message = message.replaceAll("#TASK#", task.getTitle() + "(" + task.getCode() +")");
			message = message.replaceAll("#PROJECT#", project.getTitle() + "(" + project.getCode() + ")");
			message = message.replaceAll("#OLDSTATUS#", oldStatus.getName());
			message = message.replaceAll("#NEWSTATUS#", task.getStatus().getName());
			sendMail(m.getEmail(), taskStatusSubject, message);
		}
	}
	
	private void sendMail(String to, String subject, String text) {
		if(serviceOn) {
			try{
				SimpleMailMessage message = new SimpleMailMessage();
				message.setSubject(subject);
				message.setTo(to);
				message.setFrom(this.from);
				message.setText(text);
				mailSender.send(message);
			}catch(MailSendException e) {
				log.warn("Messaging Service Error: Can't send email to " + to);
			}
		}
	}

}
