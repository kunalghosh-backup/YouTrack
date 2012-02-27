package ar.edu.itba.it.paw.web.errorpages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import ar.edu.itba.it.paw.web.base.BasePage;

public abstract class ErrorPage extends BasePage {
	
	public ErrorPage() {
		super();
		add(new Label("error", Model.of(getErrorCode())));
	}

	@Override
	protected void configureResponse() {
		super.configureResponse();
		getWebRequestCycle().getWebResponse().getHttpServletResponse().setStatus(getErrorCode());
	}

	@Override
	protected String getPageTitle() {
		return "Error " + getErrorCode();
	}

	@Override
	public boolean isErrorPage() {
		return true;
	}
	
	public abstract int getErrorCode();
	
}
