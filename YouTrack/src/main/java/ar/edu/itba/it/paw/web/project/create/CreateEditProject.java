package ar.edu.itba.it.paw.web.project.create;

import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.DateTime;

import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.project.ProjectRepository;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepository;
import ar.edu.itba.it.paw.web.base.SecuredPage;
import ar.edu.itba.it.paw.web.common.ErrorFeedbackPanel;
import ar.edu.itba.it.paw.web.project.component.ProjectCodeTextField;
import ar.edu.itba.it.paw.web.project.component.ProjectDescriptionTextArea;
import ar.edu.itba.it.paw.web.project.component.ProjectTitleTextField;
import ar.edu.itba.it.paw.web.project.list.ProjectList;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class CreateEditProject extends SecuredPage {
	
	private transient String title;
	private transient String code;
	private transient String description;
	private transient User lider;
	

	@SpringBean
	private ProjectRepository projects;
	
	@SpringBean
	private UserRepository users;
	
	transient private IModel<Project> editedProject;
	
	public CreateEditProject() {
		Form<CreateEditProject> form = new Form<CreateEditProject>("createProjectForm", new CompoundPropertyModel<CreateEditProject>(this));
		
		form.add(new DropDownChoice<User>("lider", getUsers()).setRequired(true));
		
		form.add(new ErrorFeedbackPanel("feedback"));
		
		form.add(new ProjectTitleTextField("title"));
		form.add(new ProjectCodeTextField("code"){
			@Override
			public boolean isEnabled() {
				return !isEdit();
			}
		});
		form.add(new ProjectDescriptionTextArea("description"));

		form.add(new Button("createProjectButton") {
			@Override
			public void onSubmit() {
				projects.add(build());
				setResponsePage(ProjectList.class);
				cleanForm();
			}

			@Override
			public boolean isVisible() {
				return !isEdit();
			}
		});
		
		form.add(new Button("saveProjectButtonTaskButton"){
			@Override
			public void onSubmit() {
				update();
				setResponsePage(ProjectList.class);
				cleanForm();
			}

			@Override
			public boolean isVisible() {
				return isEdit();
			}
		});
		
		form.add(new Link<Void>("cancel") {
			@Override
			public void onClick() {
				setResponsePage(ProjectList.class);
				cleanForm();
			}
		});

		add(form);
	}
	
	protected void cleanForm() {
		setTitle(null);
		setCode(null);
		setDescription(null);
		setLider(null);
		
	}

	private IModel<List<User>> getUsers() {
		return new LoadableDetachableModel<List<User>>() {
			@Override
			protected List<User> load() {
				if(isEdit()) {
					return WebUtils.asList(editedProject.getObject().getMembers());
				}else{
					return users.getAll();
				}
			}
		};
	}

	public CreateEditProject(IModel<Project> editedProject){
		this();
		this.editedProject = editedProject;
		Project auxProject = editedProject.getObject();
		setTitle(auxProject.getTitle());
		setCode(auxProject.getCode());
		setDescription(auxProject.getDescription());
		setLider(auxProject.getOwner());
	}
	
	protected void update() {
		editedProject.getObject().setDescription(getDescription(), getCurrentUser());
		editedProject.getObject().setOwner(getLider(), getCurrentUser());
		editedProject.getObject().setTitle(getTitle(), getCurrentUser());
	}

	protected boolean isEdit() {
		return editedProject != null;
	}

	private Project build(){
		return new Project(getTrackerSession().getUser(), new DateTime(), getTitle(), getCode(), getDescription(), Project.Status.PRIVATE, getLider());
	}

	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getLider() {
		return lider;
	}

	public void setLider(User lider) {
		this.lider = lider;
	}
	
}
