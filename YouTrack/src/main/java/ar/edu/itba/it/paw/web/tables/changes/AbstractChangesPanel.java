package ar.edu.itba.it.paw.web.tables.changes;

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.common.EntityModel;
import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.task.ChangesLogger;
import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.web.base.BasePanel;

public abstract class AbstractChangesPanel extends BasePanel {

	@SpringBean
	private TaskRepository tasks;
	
	public AbstractChangesPanel(String id) {
		super(id);
	}

	public AbstractChangesPanel(String id, IModel<?> model) {
		super(id, model);
	}

	public AbstractChangesPanel withProjectModel(IModel<Project> projectModel) {
		addTable(getLastTaskChangesLoggerModel(projectModel));
		return this;
	}
	
	public AbstractChangesPanel withTaskModel(IModel<Task> taskModel) {
		addTable(getTaskChangesLoggerModel(taskModel));
		return this;
	}

	private void addTable(IModel<List<IModel<ChangesLogger>>> listModel){
		add(new ListView<IModel<ChangesLogger>>("elements",listModel) {

			@Override
			protected void populateItem(ListItem<IModel<ChangesLogger>> item) {
				AbstractChangesPanel.this.populateItem(item);
			}
			
		});
	}
	
	protected abstract void populateItem(ListItem<IModel<ChangesLogger>> item);
	
	private IModel<List<IModel<ChangesLogger>>> getTaskChangesLoggerModel(final IModel<Task> taskModel) {
		return new LoadableDetachableModel<List<IModel<ChangesLogger>>>() {

			@Override
			protected List<IModel<ChangesLogger>> load() {
				List<IModel<ChangesLogger>> ans = new LinkedList<IModel<ChangesLogger>>();
				for(ChangesLogger cl : taskModel.getObject().getChangesLogger()){
					ans.add(new EntityModel<ChangesLogger>(ChangesLogger.class, cl));
				}
				return ans;
			}
		};
		
	}

	private IModel<List<IModel<ChangesLogger>>>  getLastTaskChangesLoggerModel(final IModel<Project> projectModel) {
		return new LoadableDetachableModel<List<IModel<ChangesLogger>>>() {

			@Override
			protected List<IModel<ChangesLogger>> load() {
				List<IModel<ChangesLogger>> ans = new LinkedList<IModel<ChangesLogger>>();
				for(ChangesLogger cl : tasks.getLastTaskChanges(projectModel.getObject())){
					ans.add(new EntityModel<ChangesLogger>(ChangesLogger.class, cl));
				}
				return ans;
			}
		};
	}
}
