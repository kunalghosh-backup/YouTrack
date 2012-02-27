package ar.edu.itba.it.paw.web.task.detail.resolve;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.service.MessagingService;
import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.web.base.BasePanel;
import ar.edu.itba.it.paw.web.common.SolutionDropDownChoice;


public class ResolveFormPanel extends BasePanel {

	private transient Task.Solution solution;
	
	@SpringBean
	private MessagingService messagingService;
	
	public ResolveFormPanel(String id, final IModel<Task> taskModel) {
		super(id, taskModel);
		
		Form<ResolveFormPanel> f = new Form<ResolveFormPanel>("resolveForm", new CompoundPropertyModel<ResolveFormPanel>(this)) {
			@Override
			protected void onSubmit() {
				taskModel.getObject().solve(solution, getTrackerSession().getUser(), messagingService);
			}
		};
		f.add(new SolutionDropDownChoice("solution").setRequired(true));
		add(f);
	}
	
}
