package ar.edu.itba.it.paw.web.project.search;

import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import ar.edu.itba.it.paw.domain.project.ProjectVersion;
import ar.edu.itba.it.paw.domain.task.SearchFilter;
import ar.edu.itba.it.paw.web.base.BasePage;
import ar.edu.itba.it.paw.web.common.ErrorFeedbackPanel;
import ar.edu.itba.it.paw.web.common.textfield.DatePickerTextField;
import ar.edu.itba.it.paw.web.common.textfield.autocomplete.MemberAutocompleteTextField;
import ar.edu.itba.it.paw.web.project.working.TaskFilterWorkingProject;
import ar.edu.itba.it.paw.web.task.component.TaskPriorityDropDownChoice;
import ar.edu.itba.it.paw.web.task.component.TaskStatusDropDownChoice;
import ar.edu.itba.it.paw.web.task.component.TaskTypeDropDownChoice;
import ar.edu.itba.it.paw.web.validator.PeriodOfTimeValidator;

public class SearchForm extends BasePage {

	private final IModel<SearchFilter> filter;
	
	public SearchForm(SearchFilterModel searchFilterModel) {
		this.filter = searchFilterModel;
		Form<SearchFilter> form = new Form<SearchFilter>("searchForm", new CompoundPropertyModel<SearchFilter>(filter)){
			@Override
			protected void onSubmit() {
				setResponsePage(new TaskFilterWorkingProject(filter));
			}
		};
		
		form.add(new ErrorFeedbackPanel("feedback"));
		
		form.add(new TextField<String>("code"));
		form.add(new TextField<String>("title"));
		form.add(new TextField<String>("description"));
		form.add(new MemberAutocompleteTextField("creator"));
		
		addPeriodFields(form);

		form.add(new MemberAutocompleteTextField("owner"));
		
		form.add(new TaskPriorityDropDownChoice("priority"));
		form.add(new TaskStatusDropDownChoice("status"));
		form.add(new TaskTypeDropDownChoice("type"));
		
		IModel<List<ProjectVersion>> versionModel =  new PropertyModel<List<ProjectVersion>>(getCurrentProjectModel(), "versionList");
		form.add(new DropDownChoice<ProjectVersion>("version", versionModel));
		form.add(new DropDownChoice<ProjectVersion>("affectedVersion", versionModel));
		
		add(form);
	}
	
	private void addPeriodFields(Form<?> form) {
		DatePickerTextField from = new DatePickerTextField("datefrom");
		DatePickerTextField to = new DatePickerTextField("dateto");
		form.add(from);
		form.add(to);
		form.add(new PeriodOfTimeValidator(from, to));
	}

	@Override
	public void detachModels() {
		filter.detach();
	}
	
	
	
}
