package ar.edu.itba.it.paw.web.common.link;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.utils.ValidationUtils;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public abstract class AbstractVoteLink extends Link<Task> {

	public AbstractVoteLink(String id, IModel<Task> taskModel) {
		super(id, taskModel);
		add(new Label("votes", new PropertyModel<Integer>(taskModel, "totalVotes")));
	}

	protected boolean commonVisible() {
		return WebUtils.getSession().isSignedIn() && !getModelObject().isTheCreator(WebUtils.getCurrentUser()) && 
				ValidationUtils.isMemberOrIsPublicProject(WebUtils.getCurrentProject(), WebUtils.getCurrentUser());
	}
	
	@Override
	public abstract boolean isVisible();

}
