package ar.edu.itba.it.paw.web.common.dataprovider.task.list;

import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.task.Task;

public class RelatedWithDataProvider extends AbstractRelationshipDataProvider {

	public RelatedWithDataProvider(IModel<Task> taskModel) {
		super(taskModel);
	}

	@Override
	protected Iterable<Task> getRelatedTasks() {
		return getRelationships().getRelatedWith();
	}

}
