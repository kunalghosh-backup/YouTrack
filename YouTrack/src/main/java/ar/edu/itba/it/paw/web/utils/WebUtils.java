package ar.edu.itba.it.paw.web.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.IteratorUtils;
import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.util.convert.IConverter;

import ar.edu.itba.it.paw.domain.common.EntityModel;
import ar.edu.itba.it.paw.domain.common.PersistentEntity;
import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.project.ProjectVersion;
import ar.edu.itba.it.paw.domain.task.SearchFilter;
import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.web.TrackerSession;

import com.google.common.collect.Iterables;

public class WebUtils {

	@SuppressWarnings("unchecked")
	public static <T> List<T> asList(Iterable<T> iterable) {
		return IteratorUtils.toList(iterable.iterator());
	}
	
	public static String getString(String key) {
		return Application.get().getResourceSettings().getLocalizer().getString(key, null);
	}
	
	public static <T> List<T> asList(Set<T> aSet) {
		List<T> ans = new LinkedList<T>();
		for(T value : aSet){
			ans.add(value);
		}
		return ans;
	}
	
	public static IModel<User> createUserModel(User u) {
		return new EntityModel<User>(User.class, u);
	}
	
	public static IModel<Project> createProjectModel(Project p) {
		return new EntityModel<Project>(Project.class, p);
	}
	
	public static IModel<Task> createTaskModel(Task t) {
		return new EntityModel<Task>(Task.class, t);
	}
	
	public static IModel<ProjectVersion> createVersionModel(ProjectVersion pv) {
		return new EntityModel<ProjectVersion>(ProjectVersion.class, pv);
	}
	
	public static <T> IModel<List<T>> createListModel(final List<T> list) {
		return new LoadableDetachableModel<List<T>>() {
			@Override
			protected List<T> load() {
				return list;
			}
		};
	}

	public static <T> IConverter getConverter(Class<T> type) {
		return Application.get().getConverterLocator().getConverter(type);
	}
	
	public static <T> String convertToString(Class<T> type, Object value) {
		return getConverter(type).convertToString(value, getSession().getLocale());
	}
	
	@SuppressWarnings("unchecked")
	public static IModel<Project> castProject(IModel<?> model) {
		return (IModel<Project>) model;
	}
	
	@SuppressWarnings("unchecked")
	public static IModel<User> castUser(IModel<?> model) {
		return (IModel<User>) model;
	}
	
	public static TrackerSession castSession(Session s) {
		return (TrackerSession) s;
	}
	
	public static TrackerSession getSession() {
		return (TrackerSession) Session.get();
	}
	
	@SuppressWarnings("unchecked")
	public static IModel<SearchFilter> castSearchFilter(IModel<?> model) {
		return (IModel<SearchFilter>) model;
	}
	
	public static Project getCurrentProject() {
		return WebUtils.castSession(Session.get()).getProject();
	}
	
	public static User getCurrentUser() {
		return WebUtils.castSession(Session.get()).getUser();
	}
	
	public static Project getProject(IModel<?> model) {
		return castProject(model).getObject();
	}
	
	public static <T> int size(Iterable<T> elements) {
		return Iterables.size(elements);
	}
	
	public static <T> T getObjectSafely(IModel<T> model) {
		return model != null ? model.getObject() : null;
	}
	
	public static <T extends PersistentEntity> List<IModel<T>> toModelList(Iterable<T> list, Class<T> type) {
		List<IModel<T>> ret = new LinkedList<IModel<T>>();
		for(T e : list) {
			ret.add(new EntityModel<T>(type, e));
		}
		return ret;
	}
	
	public static boolean isMember() {
		return getCurrentProject().isMember(getCurrentUser());
	}
	
	public static boolean isTaskOwner(Task t) {
		if(isMember()) {
			return getCurrentUser().equals(t.getOwner());
		}
		return false;
	}
	
	public static boolean isLeader() {
		return getCurrentProject().getOwner().equals(getCurrentUser());
	}
	
	public static boolean isAdmin() {
		return getCurrentUser() == null ? false : getCurrentUser().isAdmin();
	}
 	
	public static <T> Iterator<T> subIterator(Iterable<T> elements, int first, int count) {
		return WebUtils.asList(elements).subList(first, first + count).iterator();
	}
	
}
