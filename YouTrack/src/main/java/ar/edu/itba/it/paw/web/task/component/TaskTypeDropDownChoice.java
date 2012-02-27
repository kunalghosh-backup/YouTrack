package ar.edu.itba.it.paw.web.task.component;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.LoadableDetachableModel;

import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.Task.TType;

public class TaskTypeDropDownChoice extends DropDownChoice<Task.TType> {

	public TaskTypeDropDownChoice(String id) {
		super(id, new LoadableDetachableModel<List<Task.TType>>() {
			@Override
			protected List<TType> load() {
				return Arrays.asList(Task.TType.values());
			}
		});
	}

}
