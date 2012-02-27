package ar.edu.itba.it.paw.web.task.detail;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.DynamicWebResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.common.EntityModel;
import ar.edu.itba.it.paw.domain.project.ProjectVersion;
import ar.edu.itba.it.paw.domain.service.MessagingService;
import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.TaskComment;
import ar.edu.itba.it.paw.domain.task.TaskFile;
import ar.edu.itba.it.paw.web.base.BasePage;
import ar.edu.itba.it.paw.web.common.ErrorFeedbackPanel;
import ar.edu.itba.it.paw.web.common.dataprovider.task.list.DependsOnDataProvider;
import ar.edu.itba.it.paw.web.common.dataprovider.task.list.DuplicatedWithDataProvider;
import ar.edu.itba.it.paw.web.common.dataprovider.task.list.RelatedWithDataProvider;
import ar.edu.itba.it.paw.web.common.dataprovider.task.list.RequiredForDataProvider;
import ar.edu.itba.it.paw.web.common.label.DataLabel;
import ar.edu.itba.it.paw.web.common.label.DateTimeLabel;
import ar.edu.itba.it.paw.web.common.link.UnvoteLink;
import ar.edu.itba.it.paw.web.common.link.VoteLink;
import ar.edu.itba.it.paw.web.common.progressbar.Progress;
import ar.edu.itba.it.paw.web.common.progressbar.ProgressBar;
import ar.edu.itba.it.paw.web.common.progressbar.ProgressModel;
import ar.edu.itba.it.paw.web.project.component.menu.WorkingProjectMenu;
import ar.edu.itba.it.paw.web.tables.changes.taskchanges.TaskChangesPanel;
import ar.edu.itba.it.paw.web.task.component.repeater.VersionTable;
import ar.edu.itba.it.paw.web.task.component.repeater.relationship.RelationDataTable;
import ar.edu.itba.it.paw.web.task.detail.comment.CommentForm;
import ar.edu.itba.it.paw.web.task.detail.file.FileForm;
import ar.edu.itba.it.paw.web.task.detail.resolve.ResolveFormPanel;
import ar.edu.itba.it.paw.web.task.workreport.WorkReports;
import ar.edu.itba.it.paw.web.utils.WebUtils;

public class TaskDetail extends BasePage {

	@SpringBean
	private MessagingService messagingService;
	
	public TaskDetail(final IModel<Task> taskModel) {

		setDefaultModel(new CompoundPropertyModel<IModel<Task>>(taskModel));
		
		add(new ErrorFeedbackPanel("feedback"));
		
		add(new WorkingProjectMenu("workingProjectMenu"));
		
		add(new Label("title"));
		add(new Label("code"));
		add(new Label("creator"));
		add(new Label("createdAt"));
		
		add(new DataLabel("description").setEmptyText(getString("emptyDescription")));
		
		add(new DataLabel("owner").setEmptyText(getString("emptyOwner")));
		
		add(new Label("priority"));
		
		add(new DataLabel("duration").setEmptyText(getString("emptyDuration")));
		
		add(new DataLabel("type").setEmptyText(getString("emptyType")));
		
		add(new Label("status"));

		add(new Label("solution") {
			@Override
			public boolean isVisible() {
				return taskModel.getObject().getStatus().equals(Task.Status.COMPLETED);
			}
		});
		
		addProgressBar(taskModel);
		
		add(new DataLabel("workedTime").setEmptyText(getString("emptyWorkedTime")));
		
		add(new RelationDataTable("dependsOn", Model.of(getString("dependsOn")), new DependsOnDataProvider(taskModel)));
		add(new RelationDataTable("requiredFor", Model.of(getString("requiredFor")), new RequiredForDataProvider(taskModel)));
		add(new RelationDataTable("relatedWith", Model.of(getString("relatedWith")), new RelatedWithDataProvider(taskModel)));
		add(new RelationDataTable("duplicatedWith", Model.of(getString("duplicatedWith")), new DuplicatedWithDataProvider(taskModel)));
		
		add(new VersionTable("versionList", getVersionListModel(), getString("versions")));
		add(new VersionTable("affectedVersionList", getAffectedVersionListModel(), getString("affectedVersion")));
		
		addComments(taskModel);
		
		add(new CommentForm("commentForm", taskModel) {
			@Override
			public boolean isVisible() {
				return WebUtils.isMember();
			}
		});
		
		add(new FileForm("fileForm", taskModel) {
			@Override
			public boolean isVisible() {
				return WebUtils.isMember();
			}
		});
		
		add(new TaskChangesPanel("changesPanel", taskModel));
		
		add(new Link<Task>("takeTaskButton", taskModel) {
			@Override
			public void onClick() {
				getModelObject().setOwner(getCurrentUser(), getCurrentUser());
			}

			@Override
			public boolean isVisible() {
				return WebUtils.isMember() && !WebUtils.isTaskOwner(getModelObject());
			}
		});
		
		add(new Link<Task>("startWorkButton", taskModel) {
			@Override
			public void onClick() {
				getModelObject().startWorking(getCurrentUser(), messagingService);
			}

			@Override
			public boolean isVisible() {
				return WebUtils.isTaskOwner(getModelObject()) && getModelObject().isOpen();
			}
		});
		
		add(new Link<Task>("backToOpenButton", taskModel){
			@Override
			public void onClick() {
				getModelObject().open(getCurrentUser(), messagingService);
			}
			@Override
			public boolean isVisible() {
				return WebUtils.isTaskOwner(getModelObject()) && getModelObject().isStarted();
			}
		});
		
		add(new Link<Task>("closeTaskButton", taskModel) {
			@Override
			public void onClick() {
				getModelObject().close(getCurrentUser(), messagingService);
			}
			@Override
			public boolean isVisible() {
				return WebUtils.isLeader() && !getModelObject().isClosed();
			}
		});
		
		addFiles(taskModel);
		
		add(new ResolveFormPanel("resolvePanel", taskModel){
			@Override
			public boolean isVisible() {
				return WebUtils.isMember() && taskModel.getObject().isStarted();
			}
		});
		
		add(new WorkReports("works", taskModel));
		
		add(new VoteLink("vote", taskModel));
		add(new UnvoteLink("unvote", taskModel));
		
	}
	
	private IModel<List<ProjectVersion>> getVersionListModel() {
		return new LoadableDetachableModel<List<ProjectVersion>>() {
			@Override
			protected List<ProjectVersion> load() {
				return WebUtils.asList(getTaskModel().getObject().getVersions());
			}
		};
	}
	
	private IModel<List<ProjectVersion>> getAffectedVersionListModel() {
		return new LoadableDetachableModel<List<ProjectVersion>>() {
			@Override
			protected List<ProjectVersion> load() {
				return WebUtils.asList(getTaskModel().getObject().getAffectedVersions());
			}
		};
	}
	
	private void addProgressBar(final IModel<Task> taskModel) {
		ProgressBar pb = new ProgressBar("progress", new ProgressModel() {
			@Override
			public Progress getProgress() {
				return new Progress(taskModel.getObject().getProgress());
			}
		});
		add(pb);
	}
	
	private void addComments(final IModel<Task> taskModel) {
		
		final IModel<List<TaskComment>> commentsModel = new LoadableDetachableModel<List<TaskComment>>() {
			@Override
			protected List<TaskComment> load() {
				return WebUtils.asList(taskModel.getObject().getComments());
			}
		};
		
		add(new PropertyListView<TaskComment>("comments", commentsModel){
			@Override
			protected void populateItem(ListItem<TaskComment> item) {
				item.add(new DateTimeLabel("createdAt"));
				item.add(new Label("comment"));
				item.add(new Label("author"));
			}
			
			@Override
			public boolean isVisible() {
				return !commentsModel.getObject().isEmpty();
			}
		});
		
		add(new Label("emptyComments", getString("emptyComments")) {
			@Override
			public boolean isVisible() {
				return commentsModel.getObject().isEmpty(); 
			}
		});
		
	}
	
	private void addFiles(final IModel<Task> taskModel) {

		add(new Label("emptyFiles", getString("emptyFiles")) {
			@Override
			public boolean isVisible() {
				return !taskModel.getObject().hasFiles();
			}
		});
		
		add(new RefreshingView<TaskFile>("files"){
			@Override
			protected Iterator<IModel<TaskFile>> getItemModels() {
				List<IModel<TaskFile>> ret = new LinkedList<IModel<TaskFile>>();
				for(TaskFile tf : taskModel.getObject().getFiles()) {
					ret.add(new EntityModel<TaskFile>(TaskFile.class, tf));
				}
				return ret.iterator();
			}

			@Override
			public boolean isVisible() {
				return taskModel.getObject().hasFiles();
			}

			@Override
			protected void populateItem(final Item<TaskFile> item) {
				item.add(new ResourceLink<TaskFile>("download", new DynamicWebResource(item.getModelObject().getFilename()) {
					@Override
					protected ResourceState getResourceState() {
						return new ResourceState() {
							@Override
							public byte[] getData() {
								return item.getModelObject().getBytes();
							}

							@Override
							public String getContentType() {
								return item.getModelObject().getContentType();
							}
						};
					}
				}));
				item.setDefaultModel(new CompoundPropertyModel<TaskFile>(item.getDefaultModel()));
				item.add(new Label("filename"));
				item.add(new Label("contentType"));
			}
		});

	}
	
	@Override
	protected String getPageTitle() {
		return getString("taskDetail");
	}

	@SuppressWarnings("unchecked")
	private IModel<Task> getTaskModel() {
		return ((IModel<Task>) getDefaultModel());
	}

}
