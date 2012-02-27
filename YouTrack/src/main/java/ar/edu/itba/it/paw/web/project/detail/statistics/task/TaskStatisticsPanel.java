package ar.edu.itba.it.paw.web.project.detail.statistics.task;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.domain.task.TaskStatistics;
import ar.edu.itba.it.paw.web.base.BasePanel;


public class TaskStatisticsPanel extends BasePanel {
	
	@SpringBean
	private TaskRepository tasks;
	
	public TaskStatisticsPanel(String id, IModel<Project> projectModel) {
		super(id, projectModel);
		setDefaultModel(new CompoundPropertyModel<TaskStatistics>(getTaskStatistics(projectModel)));
		add(new Label("statusOpen", Model.of(Task.Status.OPEN)));
		add(new Label("openAmount"));
		add(new Label("openEstimatedTime"));
		
		add(new Label("statusOnGoing", Model.of(Task.Status.ONGOING)));
		add(new Label("onGoingAmount"));
		add(new Label("onGoingEstimatedTime"));
		
		add(new Label("statusCompleted", Model.of(Task.Status.COMPLETED)));
		add(new Label("completedAmount"));
		add(new Label("completedEstimatedTime"));
		
		add(new Label("statusClosed", Model.of(Task.Status.CLOSED)));
		add(new Label("closedAmount"));
		add(new Label("closedEstimatedTime"));

	}

	private  IModel<TaskStatistics> getTaskStatistics(final IModel<Project> projectModel) {
		return new LoadableDetachableModel<TaskStatistics>() {

			@Override
			protected TaskStatistics load() {
				return tasks.getStatistics(projectModel.getObject());
			}
		};
		
	}


}
