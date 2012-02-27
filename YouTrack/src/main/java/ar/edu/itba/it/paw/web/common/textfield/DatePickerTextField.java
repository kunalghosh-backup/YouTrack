package ar.edu.itba.it.paw.web.common.textfield;

import org.apache.wicket.markup.html.form.TextField;
import org.joda.time.DateTime;
import org.wicketstuff.jwicket.ui.datepicker.DatePicker;

public class DatePickerTextField extends TextField<DateTime> {

	public DatePickerTextField(String id) {
		super(id);
		add(new DatePicker());
	}

}
