package ar.edu.itba.it.paw.web.common.dataprovider.version;

import java.util.Iterator;

import ar.edu.itba.it.paw.domain.project.ProjectVersion;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class UnreleasedVersionSortableDataProvider extends VersionSortableDataProvider {

	@Override
	public Iterator<? extends ProjectVersion> iterator(int first, int count) {
		return WebUtils.subIterator(getUnreleasedVersions(), first, count);
	}

	@Override
	public int size() {
		return WebUtils.size(getUnreleasedVersions());
	}
	
	private Iterable<ProjectVersion> getUnreleasedVersions() {
		return WebUtils.getCurrentProject().getUnreleasedVersions();
	}
	
}
