package ar.edu.itba.it.paw.web.common.dataprovider.task.list;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.TaskRelationships;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public abstract class AbstractRelationshipDataProvider extends SortableDataProvider<Task> {

	protected final IModel<Task> taskModel;
	
	public AbstractRelationshipDataProvider(IModel<Task> taskModel) {
		this.taskModel = taskModel;
	}

	@Override
	public IModel<Task> model(Task t) {
		return WebUtils.createTaskModel(t);
	}

	@Override
	public Iterator<? extends Task> iterator(int first, int count) {
		return WebUtils.subIterator(getRelatedTasks(), first, count);
	}

	@Override
	public int size() {
		return WebUtils.size(getRelatedTasks());
	}

	
	
	protected TaskRelationships getRelationships() {
		return taskModel.getObject().getRelationships();
	}
	
	protected abstract Iterable<Task> getRelatedTasks();

}
