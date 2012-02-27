package ar.edu.itba.it.paw.web.task.workreport;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.common.Duration;
import ar.edu.itba.it.paw.domain.common.EntityModel;
import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.WorkReport;
import ar.edu.itba.it.paw.web.base.BasePanel;
import ar.edu.itba.it.paw.web.common.label.DateTimeLabel;

public class WorkReports extends BasePanel {

	private transient Duration duration;
	private transient String description;
	
	public WorkReports(String id, final IModel<Task> taskModel) {
		super(id, taskModel);
		
		add(new RefreshingView<WorkReport>("reports") {

			@Override
			protected Iterator<IModel<WorkReport>> getItemModels() {
				List<IModel<WorkReport>> ans = new LinkedList<IModel<WorkReport>>();
				for(WorkReport wr : taskModel.getObject().getWorkReports()) {
					ans.add(new EntityModel<WorkReport>(WorkReport.class, wr));
				}
				return ans.iterator();
			}

			@Override
			protected void populateItem(Item<WorkReport> item) {
				item.setDefaultModel(new CompoundPropertyModel<WorkReport>(item.getModel()));
				item.add(new Label("author"));
				item.add(new Label("description"));
				item.add(new DateTimeLabel("reportedAt"));
				item.add(new Label("duration"));
			}

			@Override
			public boolean isVisible() {
				return taskModel.getObject().hasWorkReports();
			}
		});
		
		add(new Label("noReports", getString("noReports")) {

			@Override
			public boolean isVisible() {
				return !taskModel.getObject().hasWorkReports();
			}
			
		});
		
		Form<WorkReports> form = new Form<WorkReports>("reportWorkForm", new CompoundPropertyModel<WorkReports>(this)) {
			@Override
			protected void onSubmit() {
				taskModel.getObject().addReport(getCurrentUser(), duration, description);
				cleanForm();
			}

			@Override
			public boolean isVisible() {
				return taskModel.getObject().getProject().isMember(getCurrentUser());
			}
		};
		
		form.add(new TextField<String>("duration").setRequired(true));
		form.add(new TextArea<String>("description").setRequired(true));
		
		add(form);
		
	}
	
	private void cleanForm() {
		this.duration = null;
		this.description = null;
	}
	
}
