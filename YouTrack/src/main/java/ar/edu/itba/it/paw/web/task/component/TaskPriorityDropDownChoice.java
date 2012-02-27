package ar.edu.itba.it.paw.web.task.component;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.LoadableDetachableModel;

import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.Task.Priority;

public class TaskPriorityDropDownChoice extends DropDownChoice<Task.Priority>{

	public TaskPriorityDropDownChoice(String id) {
		super(id, new LoadableDetachableModel<List<Task.Priority>>() {
			@Override
			protected List<Priority> load() {
				return Arrays.asList(Task.Priority.values());
			}
		});
	}

}
