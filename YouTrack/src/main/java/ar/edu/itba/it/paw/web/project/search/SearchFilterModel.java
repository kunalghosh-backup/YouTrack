package ar.edu.itba.it.paw.web.project.search;

import org.apache.wicket.model.IModel;
import org.joda.time.DateTime;

import ar.edu.itba.it.paw.domain.common.EntityModel;
import ar.edu.itba.it.paw.domain.project.ProjectVersion;
import ar.edu.itba.it.paw.domain.task.SearchFilter;
import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class SearchFilterModel implements IModel<SearchFilter> {

	private transient SearchFilter searchFilter;
	
	private String code;
	private String title;
	private String description;
	private IModel<User> creator;
	private DateTime datefrom;
	private DateTime dateto;
	private IModel<User> owner;
	private Task.Priority priority;
	private Task.Status status;
	private Task.TType type;
	private IModel<ProjectVersion> version;
	private IModel<ProjectVersion> affectedVersion;
	
	public SearchFilterModel() {
		this.creator = new EntityModel<User>(User.class);
		this.owner = new EntityModel<User>(User.class);
		this.version = new EntityModel<ProjectVersion>(ProjectVersion.class);
		this.affectedVersion = new EntityModel<ProjectVersion>(ProjectVersion.class);
	}
	
	public SearchFilter getSearchFilter() {
		SearchFilter ret = new SearchFilter();
		ret.setCode(code);
		ret.setTitle(title);
		ret.setDescription(description);
		ret.setCreator(WebUtils.getObjectSafely(creator));
		ret.setDatefrom(datefrom);
		ret.setDateto(dateto);
		ret.setOwner(WebUtils.getObjectSafely(owner));
		ret.setPriority(priority);
		ret.setStatus(status);
		ret.setType(type);
		ret.setVersion(WebUtils.getObjectSafely(version));
		ret.setAffectedVersion(WebUtils.getObjectSafely(affectedVersion));
		return ret;
	}

	@Override
	public SearchFilter getObject() {
		return this.searchFilter == null ? this.searchFilter = getSearchFilter() : searchFilter;
	}

	@Override
	public void setObject(SearchFilter sf) {
		this.code = sf.getCode();
		this.title = sf.getTitle();
		this.description = sf.getDescription();
		this.creator.setObject(sf.getCreator());
		this.datefrom = sf.getDatefrom();
		this.dateto = sf.getDateto();
		this.owner.setObject(sf.getOwner());
		this.priority = sf.getPriority();
		this.status = sf.getStatus();
		this.type = sf.getType();
		this.version.setObject(sf.getVersion());
		this.affectedVersion.setObject(sf.getVersion());
		
	}
	
	@Override
	public void detach() {
		if(creator != null) {
			creator.detach();
		}
		if(owner != null) {
			owner.detach();
		}
		if(version != null) {
			version.detach();
		}
		if(affectedVersion != null){
			affectedVersion.detach();
		}
	}

	@Override
	public String toString() {
		return "SearchFilterModel [code=" + code + ", title=" + title
				+ ", description=" + description + ", creator=" + creator
				+ ", datefrom=" + datefrom + ", dateto=" + dateto + ", owner="
				+ owner + ", priority=" + priority + ", status=" + status
				+ ", type=" + type + ", version=" + version
				+ ", affectedVersion=" + affectedVersion + "]";
	}
	
}
