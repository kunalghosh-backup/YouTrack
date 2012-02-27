package ar.edu.itba.it.paw.web.project.component;

import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IErrorMessageSource;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.validator.StringValidator.LengthBetweenValidator;

import ar.edu.itba.it.paw.domain.project.ProjectRepository;

public class ProjectCodeTextField extends RequiredTextField<String> {

	@SpringBean
	ProjectRepository projects;
	
	public ProjectCodeTextField(String id) {
		super(id);
		add(LengthBetweenValidator.maximumLength(6));
		
	}
	
	@Override
	public void validate() {
		super.validate();
			if(projects.getByCode(getValue()) != null){
				error(new IValidationError() {
					
					@Override
					public String getErrorMessage(IErrorMessageSource messageSource) {
						return getString("duplicatedCode");
					}
				});
			}
	}

}
