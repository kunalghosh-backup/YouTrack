package ar.edu.itba.it.paw.web.project.detail.versions.create;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.joda.time.DateTime;

import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.project.ProjectVersion;
import ar.edu.itba.it.paw.domain.project.ProjectVersion.Status;
import ar.edu.itba.it.paw.web.base.SecuredPage;
import ar.edu.itba.it.paw.web.common.ErrorFeedbackPanel;
import ar.edu.itba.it.paw.web.common.ProjectVersionDropDownChoice;
import ar.edu.itba.it.paw.web.common.textfield.DatePickerTextField;
import ar.edu.itba.it.paw.web.project.component.ProjectDescriptionTextArea;
import ar.edu.itba.it.paw.web.project.component.ProjectVersionNameTextField;
import ar.edu.itba.it.paw.web.project.detail.ProjectDetail;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class CreateEditProjectVersion extends SecuredPage {

	private transient String name;
	private transient String description;
	private transient DateTime releaseDate;
	private transient Status status;
	
	public CreateEditProjectVersion() {
		addCommon();
	}
	
	private void addCommon() {
		Form<CreateEditProjectVersion> form = new Form<CreateEditProjectVersion>("createVersionForm", new CompoundPropertyModel<CreateEditProjectVersion>(this));
		form.add(new ErrorFeedbackPanel("feedback"));
		form.add(new ProjectVersionNameTextField("name").add(new IValidator<String>() {
			@Override
			public void validate(IValidatable<String> validatable) {
				if(getCurrentProject().existsVersion(validatable.getValue())) {
					error(getString("duplicatedVersion"));
				}
			}
		}));
		form.add(new ProjectDescriptionTextArea("description"));
		form.add(new DatePickerTextField("releaseDate").setRequired(true));
		form.add(new ProjectVersionDropDownChoice("status"){
			@Override
			public boolean isVisible() {
				return isEdit();
			}
		});
		form.add(new Button("createProjectVersionButton") {
			@Override
			public void onSubmit() {
				CreateEditProjectVersion.this.build();
				setResponsePage(new ProjectDetail());
			}

			@Override
			public boolean isVisible() {
				return !isEdit();
			}
		});
		form.add(new Button("saveProjectVersionButtonTaskButton"){
			@Override
			public void onSubmit() {
				update();
				setResponsePage(new ProjectDetail());
			}

			@Override
			public boolean isVisible() {
				return isEdit();
			}
		});
		form.add(new Link<Void>("cancel") {
			@Override
			public void onClick() {
				setResponsePage(new ProjectDetail());
			}
		});
		add(form);
		add(new Link<Project>("backToProjectDetail") {
			@Override
			public void onClick() {
				setResponsePage(new ProjectDetail());
			}
		});
	}
	
	private void update() {
		ProjectVersion old = getOldProjectVersion();
		old.setDescription(this.description);
		old.setName(this.name);
		old.setReleaseDate(this.releaseDate);
		old.setStatus(this.status);
	}
	
	public ProjectVersion getOldProjectVersion() {
		return (ProjectVersion) getDefaultModelObject();
	}

	public CreateEditProjectVersion(IModel<ProjectVersion> oldProjectVersion) {
		super(oldProjectVersion);
		ProjectVersion pv = oldProjectVersion.getObject();
		this.name = pv.getName();
		this.description = pv.getDescription();
		this.releaseDate = pv.getReleaseDate();
		this.status = pv.getStatus();
		addCommon();
	}
	
	protected boolean isEdit() {
		return WebUtils.getObjectSafely(getDefaultModel()) != null;
	}
	
	public void build() {
		getCurrentProject().addVersion(getCurrentUser(), this.releaseDate, ProjectVersion.Status.OPEN, this.name, this.description);
	}
	
}
