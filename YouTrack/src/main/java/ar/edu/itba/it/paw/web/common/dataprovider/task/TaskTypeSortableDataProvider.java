package ar.edu.itba.it.paw.web.common.dataprovider.task;

import java.util.Iterator;

import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.project.ProjectVersion;
import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class TaskTypeSortableDataProvider extends AbstractTaskDataProvider {

	private Task.TType type;
	private IModel<ProjectVersion> versionModel;
	
	public TaskTypeSortableDataProvider(TaskRepository tasks, IModel<ProjectVersion> versionModel, Task.TType type) {
		super(tasks);
		this.type = type;
		this.versionModel = versionModel;
	}

	@Override
	public Iterator<? extends Task> iterator(int first, int count) {
		return WebUtils.subIterator(tasks.getByVersionAndType(getVersion(), type), first, count);
	}

	@Override
	public int size() {
		return WebUtils.size(tasks.getByVersionAndType(getVersion(), type));
	}
	
	protected ProjectVersion getVersion() {
		return versionModel.getObject();
	}
}
