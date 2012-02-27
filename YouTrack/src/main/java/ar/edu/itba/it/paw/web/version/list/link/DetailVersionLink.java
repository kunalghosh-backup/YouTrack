package ar.edu.itba.it.paw.web.version.list.link;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.project.ProjectVersion;
import ar.edu.itba.it.paw.web.base.BasePanel;
import ar.edu.itba.it.paw.web.version.detail.VersionDetail;

public class DetailVersionLink extends BasePanel {

	public DetailVersionLink(String id, IModel<ProjectVersion> versionModel) {
		super(id, versionModel);
		add(new Link<ProjectVersion>("link", versionModel){
			@Override
			public void onClick() {
				setResponsePage(new VersionDetail(getModel()));
			}
		});
	}
	
}
