package ar.edu.itba.it.paw.web.task.detail.comment;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.StringValidator.LengthBetweenValidator;

import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.web.base.BasePanel;

public class CommentForm extends BasePanel {

	private transient String message;
	
	public CommentForm(String id, final IModel<Task> taskModel) {
		super(id, taskModel);
		
		Form<CommentForm> form = new Form<CommentForm>("commentForm", new CompoundPropertyModel<CommentForm>(this)) {
			@Override
			protected void onSubmit() {
				taskModel.getObject().addComment(getTrackerSession().getUser(), message);
				message = null;
			}
		};
		
		form.add(new TextArea<String>("message").add(LengthBetweenValidator.maximumLength(600)).setRequired(true));
		
		add(form);
		
	}

}
