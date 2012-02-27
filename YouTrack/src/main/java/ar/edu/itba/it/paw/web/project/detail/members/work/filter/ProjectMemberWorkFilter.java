package ar.edu.itba.it.paw.web.project.detail.members.work.filter;

import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.DateTime;

import ar.edu.itba.it.paw.domain.common.Duration;
import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.web.base.SecuredPage;
import ar.edu.itba.it.paw.web.common.ErrorFeedbackPanel;
import ar.edu.itba.it.paw.web.common.textfield.DatePickerTextField;
import ar.edu.itba.it.paw.web.project.detail.ProjectDetail;
import ar.edu.itba.it.paw.web.project.detail.members.work.list.ProjectMemberWorkReportPanel;
import ar.edu.itba.it.paw.web.validator.PeriodOfTimeValidator;

public class ProjectMemberWorkFilter extends SecuredPage {
	
	transient private DateTime datefrom;
	transient private DateTime dateto;
	private Component list;
	
	@SpringBean
	TaskRepository tasks;
	
	public ProjectMemberWorkFilter(final IModel<Project> projectModel) {
		super(projectModel);
		Form<ProjectMemberWorkFilter> form = new Form<ProjectMemberWorkFilter>("logWorkForm", new CompoundPropertyModel<ProjectMemberWorkFilter>(this)) {
			@Override
			protected void onSubmit() {
				super.onSubmit();
				Component aux;
				list.replaceWith(aux = getWorkReports(projectModel));
				list = aux;
				//cleanForm();
			}
		};
		addPeriodFields(form);
		form.add(new ErrorFeedbackPanel("feedback"));
		add(form);
		
		add(list = getWorkReports(projectModel));
		
		add(new Link<Void>("backToProjectDetail"){

			@Override
			public void onClick() {
				setResponsePage(new ProjectDetail());
			}
			
		});
	}

	protected void cleanForm() {
		datefrom = null;
		dateto = null;
		
	}

	protected Component getWorkReports(final IModel<Project> projectModel) {
		ProjectMemberWorkReportPanel ans = new ProjectMemberWorkReportPanel("memberReportList", new LoadableDetachableModel<Map<User,Duration>>() {

			@Override
			protected Map<User, Duration> load() {
				return tasks.getMembersWork(projectModel.getObject(), getDatefrom(), getDateto());
			}
		});

		return ans;
	}

	private void addPeriodFields(Form<?> form) {
		DatePickerTextField from = new DatePickerTextField("datefrom");
		DatePickerTextField to = new DatePickerTextField("dateto");
		form.add(from);
		form.add(to);
		form.add(new PeriodOfTimeValidator(from, to));
	}
	
	public DateTime getDateto() {
		return dateto;
	}

	public void setDateto(DateTime dateto) {
		this.dateto = dateto;
	}

	public DateTime getDatefrom() {
		return datefrom;
	}

	public void setDatefrom(DateTime datefrom) {
		this.datefrom = datefrom;
	}
	
}
