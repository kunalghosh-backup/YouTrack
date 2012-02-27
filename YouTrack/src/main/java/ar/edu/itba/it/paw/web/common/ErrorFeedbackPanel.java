package ar.edu.itba.it.paw.web.common;

import org.apache.wicket.markup.html.panel.FeedbackPanel;

public class ErrorFeedbackPanel extends FeedbackPanel {

	public ErrorFeedbackPanel(String id) {
		super(id);
	}

	@Override
	public boolean isVisible() {
		return anyErrorMessage();
	}

}
