package ar.edu.itba.it.paw.web.task.component;

import java.util.List;

import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class TaskMultipleChoice extends ListMultipleChoice<Task>{
	
	public TaskMultipleChoice(String id, final TaskRepository tasks) {
		super(id, new LoadableDetachableModel<List<Task>>() {
			@Override
			protected List<Task> load() {
				return tasks.getByProject(WebUtils.getCurrentProject());
			}
		});
	}
	
	public TaskMultipleChoice(String id, final TaskRepository tasks, final IModel<Task> avoid) {
		super(id, new LoadableDetachableModel<List<Task>>() {
			@Override
			protected List<Task> load() {
				List<Task> ret = tasks.getByProject(WebUtils.getCurrentProject());
				if(avoid != null) {
					ret.remove(avoid.getObject());
				}
				return ret;
			}
		});
	}
	
}
