package ar.edu.itba.it.paw.web.common;

import java.util.Arrays;

import org.apache.wicket.markup.html.form.DropDownChoice;

import ar.edu.itba.it.paw.domain.task.Task;

public class SolutionDropDownChoice extends DropDownChoice<Task.Solution>{

	public SolutionDropDownChoice(String id) {
		super(id, Arrays.asList(Task.Solution.values()));
	}

}
