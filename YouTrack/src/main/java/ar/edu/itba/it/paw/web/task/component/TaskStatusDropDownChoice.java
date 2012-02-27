package ar.edu.itba.it.paw.web.task.component;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.LoadableDetachableModel;

import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.Task.Status;

public class TaskStatusDropDownChoice extends DropDownChoice<Task.Status> {

	public TaskStatusDropDownChoice(String id) {
		super(id, new LoadableDetachableModel<List<Task.Status>>() {
			@Override
			protected List<Status> load() {
				return Arrays.asList(Task.Status.values());
			}
		});
	}

	
	
}
