package ar.edu.itba.it.paw.web.project.detail.statistics.version;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.common.Duration;
import ar.edu.itba.it.paw.domain.project.ProjectVersion;
import ar.edu.itba.it.paw.domain.task.Task.Priority;
import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.web.base.BasePanel;

public class ProjectVersionStatistics extends BasePanel {
	
	@SpringBean
	TaskRepository tasks;
	
	public ProjectVersionStatistics(String id, IModel<ProjectVersion> projectVersionModel) {
		super(id, projectVersionModel);
	}

	@Override
	public void renderHead(HtmlHeaderContainer container) {
		String versionsTypeJS = "var versions = " + versionTypeJS(tasks.getTaskByPriority(getCurrentProject(), getVersion()))  + ";";
		container.getHeaderResponse().renderJavascript(versionsTypeJS, "versionsTypeJS");
		super.renderHead(container);
	}
	
	private String versionTypeJS(Map<Priority, Duration> values){
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		Iterator<Entry<Priority, Duration>> it = values.entrySet().iterator();
		while(it.hasNext()){
			Entry<Priority, Duration> entry = it.next();
			sb.append("{ category:'" + getString(entry.getKey().toString()) + "', value: " + entry.getValue().getMinutes() + "}");
			if( it.hasNext() ){
				sb.append(", ");
			}
		}
		
		sb.append("]");
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	private IModel<ProjectVersion> getVersionModel() {
		return (IModel<ProjectVersion>) getDefaultModel();
	}
	
	private ProjectVersion getVersion() {
		return getVersionModel().getObject();
	}
}
