package ar.edu.itba.it.paw.web.project.detail.members.work.list;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;

import ar.edu.itba.it.paw.domain.common.Duration;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.web.base.BasePanel;
import ar.edu.itba.it.paw.web.converter.WorkDurationConverter;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class ProjectMemberWorkReportPanel extends BasePanel {

	public ProjectMemberWorkReportPanel(String id, IModel<Map<User,Duration>> workReportsModel) {
		super(id, workReportsModel);
		add(new ListView<Entry<User, Duration>>("workReports", getWorkReports(workReportsModel)) {

			@Override
			protected void populateItem(ListItem<Entry<User, Duration>> item) {
//				item.setDefaultModel(new CompoundPropertyModel<Entry<User, Duration>>(item.getDefaultModel()));
				item.add(new Label("key.userName", WebUtils.createUserModel(item.getModelObject().getKey())));
				item.add(new Label("value.duration", new PropertyModel<Duration>(item.getModel(), "value.duration")){
					
					@Override
					public IConverter getConverter(Class<?> type) {
						if(type.equals(Duration.class)){
							return new WorkDurationConverter();
						}
						return super.getConverter(type);
					}
				});
			}

		});
	}

	private IModel<List<Entry<User, Duration>>> getWorkReports(final IModel<Map<User, Duration>> workReportsModel) {
		return new LoadableDetachableModel<List<Entry<User,Duration>>>() {

			@Override
			protected List<Entry<User, Duration>> load() {
				return WebUtils.asList(workReportsModel.getObject().entrySet());
			}
		};
	}


}
