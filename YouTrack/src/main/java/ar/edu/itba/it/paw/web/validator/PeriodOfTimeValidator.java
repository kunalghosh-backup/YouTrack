package ar.edu.itba.it.paw.web.validator;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.joda.time.DateTime;

import ar.edu.itba.it.paw.domain.exception.DomainException;
import ar.edu.itba.it.paw.domain.utils.ValidationUtils;
import ar.edu.itba.it.paw.web.common.textfield.DatePickerTextField;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class PeriodOfTimeValidator implements IFormValidator {

	private final DatePickerTextField[] cmpts = new DatePickerTextField[2];
	
	public PeriodOfTimeValidator(DatePickerTextField from, DatePickerTextField to) {
		super();
		if(ValidationUtils.hasNull(from, to)) {
			throw new IllegalArgumentException("Invalid parameters");
		}
		this.cmpts[0] = from;
		this.cmpts[1] = to;
	}

	@Override
	public FormComponent<?>[] getDependentFormComponents() {
		return cmpts;
	}
	
	private DatePickerTextField[] getDependentDateFields() {
		return this.cmpts;
	}

	@Override
	public void validate(Form<?> form) {
		DatePickerTextField[] fields = getDependentDateFields();
		if(fields.length == 2) {
			DateTime from = fields[0].getConvertedInput();
			DateTime to = fields[1].getConvertedInput();
			if(!ValidationUtils.hasNull(from, to) && from.isAfter(to)) {
				form.error(WebUtils.getString("invalidPeriodOfTime"));
			}
		} else {
			throw new DomainException("Invalid form components");
		}
	}

}
