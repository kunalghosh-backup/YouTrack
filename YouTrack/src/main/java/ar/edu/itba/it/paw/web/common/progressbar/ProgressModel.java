package ar.edu.itba.it.paw.web.common.progressbar;

import org.apache.wicket.model.AbstractReadOnlyModel;

public abstract class ProgressModel extends AbstractReadOnlyModel<Progress> {
	
	@Override
	public Progress getObject() {
		return getProgress();
	}

	public abstract Progress getProgress();

}
