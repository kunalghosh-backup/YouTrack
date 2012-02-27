package ar.edu.itba.it.paw.web.task.create;

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.validator.StringValidator;
import org.joda.time.DateTime;

import ar.edu.itba.it.paw.domain.common.Duration;
import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.project.ProjectVersion;
import ar.edu.itba.it.paw.domain.service.MessagingService;
import ar.edu.itba.it.paw.domain.task.Relationship.RelationshipType;
import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepository;
import ar.edu.itba.it.paw.web.base.SecuredPage;
import ar.edu.itba.it.paw.web.common.ErrorFeedbackPanel;
import ar.edu.itba.it.paw.web.common.textfield.autocomplete.MemberAutocompleteTextField;
import ar.edu.itba.it.paw.web.converter.MemberConverter;
import ar.edu.itba.it.paw.web.project.working.TasksWorkingProject;
import ar.edu.itba.it.paw.web.task.component.TaskMultipleChoice;
import ar.edu.itba.it.paw.web.task.component.TaskPriorityDropDownChoice;
import ar.edu.itba.it.paw.web.task.component.TaskTypeDropDownChoice;
import ar.edu.itba.it.paw.web.task.component.VersionMultipleChoice;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class CreateEditTask extends SecuredPage {

	@SpringBean
	private TaskRepository tasks;
	
	@SpringBean
	private UserRepository users;
	
	@SpringBean
	private MessagingService messagingService;
	
	private transient String title;
	private transient String description;
	private transient Duration duration;
	private transient User owner;
	private transient List<ProjectVersion> affectedVersions = new LinkedList<ProjectVersion>();
	private transient List<ProjectVersion> tversions = new LinkedList<ProjectVersion>();

	private transient Task.Priority priority;
	private transient Task.TType type;

	private transient List<Task> dependsOnList = new LinkedList<Task>();
	private transient List<Task> relatedWithList = new LinkedList<Task>();
	private transient List<Task> duplicatedWithList = new LinkedList<Task>();
	private transient List<Task> requiredForList = new LinkedList<Task>();
	
	public CreateEditTask() {
		addForm();
	}
	
	public CreateEditTask(IModel<Task> taskModel) {
		super(taskModel);
		this.updateFormValues();
		addForm();
	}
	
	private void updateFormValues() {
		Task t = getTask();
		this.title = t.getTitle();
		this.description = t.getDescription();
		this.duration = t.getDuration();
		this.owner = t.getOwner();
		this.affectedVersions = WebUtils.asList(t.getAffectedVersions());
		this.tversions = WebUtils.asList(t.getVersions());
		this.priority = t.getPriority();
		this.type = t.getType();
		this.dependsOnList = WebUtils.asList(t.getRelationships().getDependsOn());
		this.relatedWithList = WebUtils.asList(t.getRelationships().getRelatedWith());
		this.duplicatedWithList = WebUtils.asList(t.getRelationships().getDuplicatedWith());
		this.requiredForList = WebUtils.asList(t.getRelationships().getRequiredFor());
	}
	
	private void addForm() {
		
		final Form<CreateEditTask> form = new Form<CreateEditTask>("createTaskForm", new CompoundPropertyModel<CreateEditTask>(this));
		
		form.add(new ErrorFeedbackPanel("feedback"));
		form.add(new RequiredTextField<String>("title").add(StringValidator.lengthBetween(4, 30)));
		form.add(new TextArea<String>("description").add(StringValidator.maximumLength(200)));
		form.add(new TextField<Duration>("duration"));
		
		form.add(new MemberAutocompleteTextField("owner") {
			@Override
			public IConverter getConverter(Class<?> type) {
				if(User.class.equals(type)) {
					return new MemberConverter(users);
				}
				return super.getConverter(type);
			}
		});
		
		form.add(new VersionMultipleChoice("affectedVersions", getCurrentProjectModel()));
		form.add(new VersionMultipleChoice("tversions", getCurrentProjectModel()));
		
		form.add(new TaskMultipleChoice("dependsOnList", tasks, getTaskModel()));
		form.add(new TaskMultipleChoice("relatedWithList", tasks, getTaskModel()));
		form.add(new TaskMultipleChoice("duplicatedWithList", tasks, getTaskModel()));
		form.add(new TaskMultipleChoice("requiredForList", tasks, getTaskModel()));
		
		form.add(new TaskPriorityDropDownChoice("priority").setRequired(true));
		form.add(new TaskTypeDropDownChoice("type").setNullValid(true));
		
		form.add(new Button("createTaskButton") {
			@Override
			public void onSubmit() {
				Project p = getCurrentProject();
				Task t = form.getModelObject().build(getCurrentUser(), p, tasks.generateIndex(p), messagingService);
				tasks.add(t);
				setResponsePage(TasksWorkingProject.class);
			}

			@Override
			public boolean isVisible() {
				return !CreateEditTask.this.isEdit();
			}
			
		});
		
		form.add(new Button("saveTaskButton"){
			@Override
			public void onSubmit() {
				form.getModelObject().updateTask(getTask(), getCurrentUser());
				setResponsePage(TasksWorkingProject.class);
			}
			
			@Override
			public boolean isVisible() {
				return CreateEditTask.this.isEdit();
			}
		});
		
		form.add(new Link<Void>("cancel") {
			@Override
			public void onClick() {
				setResponsePage(TasksWorkingProject.class);
			}
		});
		
		add(form);
	}
	
	public Task build(User user, Project project, Integer index, MessagingService messagingService) {
		Task ret = new Task(user, project, new DateTime(System.currentTimeMillis()), title, priority, index, owner, description, duration, type, tversions, affectedVersions, messagingService, dependsOnList, duplicatedWithList, relatedWithList,requiredForList);
		return ret;
	}

	public void updateTask(Task task, User user) {
		task.setTitle(title, user);
		task.setDescription(description, user);
		task.setDuration(duration, user);
		task.setOwner(owner, user);
		task.setPriority(priority, user);
		task.setType(type, user);
		task.setVersions(tversions, user);
		task.setAffectedVersions(affectedVersions, user);
		task.updateRelationship(dependsOnList, RelationshipType.DEPENDSON);
		task.updateRelationship(duplicatedWithList, RelationshipType.DUPLICATEDWITH);
		task.updateRelationship(relatedWithList, RelationshipType.RELATEDWITH);
		task.updateRelationship(requiredForList, RelationshipType.REQUIREDFOR);
	}
	
	public boolean isEdit() {
		return getDefaultModelObject() != null;
	}
	
	public Task getTask() {
		return (Task) getDefaultModelObject();
	}
	
	@SuppressWarnings("unchecked")
	public IModel<Task> getTaskModel() {
		return (IModel<Task>) getDefaultModel();
	}
	
}
