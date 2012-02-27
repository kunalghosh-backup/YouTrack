package ar.edu.itba.it.paw.web.common.dataprovider.version;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.domain.project.ProjectVersion;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class VersionSortableDataProvider extends SortableDataProvider<ProjectVersion> {

	@Override
	public Iterator<? extends ProjectVersion> iterator(int first, int count) {
		return WebUtils.subIterator(getVersions(), first, count);
	}

	@Override
	public int size() {
		return WebUtils.size(getVersions());
	}

	@Override
	public IModel<ProjectVersion> model(ProjectVersion pv) {
		return WebUtils.createVersionModel(pv);
	}
	
	private Iterable<ProjectVersion> getVersions() {
		return WebUtils.getCurrentProject().getVersions();
	}

}
