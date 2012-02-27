package ar.edu.itba.it.paw.web.common.link;

import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class UnvoteLink extends AbstractVoteLink {

	public UnvoteLink(String id, IModel<Task> taskModel) {
		super(id, taskModel);
	}

	@Override
	public boolean isVisible() {
		return this.commonVisible() && getModelObject().hasVoted(WebUtils.getCurrentUser());
	}

	@Override
	public void onClick() {
		getModelObject().removeVote(WebUtils.getCurrentUser());
	}
	

}
