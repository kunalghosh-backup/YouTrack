package ar.edu.itba.it.paw.web.common;

import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.user.User;

public class MembersDropDownChoice extends DropDownChoice<User>{

	public MembersDropDownChoice(String id, final IModel<Project> projectModel) {
		super(id, new LoadableDetachableModel<List<User>>() {
			@SuppressWarnings("unchecked")
			@Override
			protected List<User> load() {
				return IteratorUtils.toList(projectModel.getObject().getMembers().iterator());
			}
		});
		setNullValid(true);
	}
	
}
