package ar.edu.itba.it.paw.domain.task;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.it.paw.domain.common.AbstractHibernateRepository;
import ar.edu.itba.it.paw.domain.common.Duration;
import ar.edu.itba.it.paw.domain.project.Project;
import ar.edu.itba.it.paw.domain.project.ProjectVersion;
import ar.edu.itba.it.paw.domain.task.Relationship.RelationshipType;
import ar.edu.itba.it.paw.domain.task.Task.Priority;
import ar.edu.itba.it.paw.domain.task.Task.Status;
import ar.edu.itba.it.paw.domain.task.Task.TType;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.utils.ValidationUtils;
import ar.edu.itba.it.paw.web.utils.WebUtils;

import com.google.common.collect.Iterables;

@Repository
public class HibernateTaskRepository extends AbstractHibernateRepository<Task> implements TaskRepository {
	
	@Autowired
	public HibernateTaskRepository(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public List<Task> getAll() {
		return find("from Task");
	}

	@Override
	public Task get(int id) {
		return get(Task.class, id);
	}

	@Override
	public List<Task> getByProject(Project project) {
		return find("from Task where project_id = ? order by index", project.getId());
	}
	
	@Override
	public List<Task> getByProject(Project project, int first, int count) {
		return find(first, count, "from Task where project_id = ? order by index", project.getId());
	}

	@Override
	public List<Task> getByProjectUser(Project project, User user) {
		return find("from Task where project_id = ? AND user_id = ? order by index", project.getId(), user.getId());
	}
	
	@Override
	public List<Task> getByProjectUser(Project project, User user, int first,
			int count) {
		return find(first, count, "from Task where project_id = ? AND user_id = ? order by index", project.getId());
	}
	
	@Override
	public List<Task> getByProjectUserStatuses(Project project, User user, List<Status> statuses) {
		List<Task> ret = new LinkedList<Task>();
		List<Task> aux;
		for(Task.Status sts : statuses) {
			aux = find("from Task where project_id = ? AND user_id = ? AND status = ? order by index", project.getId(), user.getId(), sts);
			ret.addAll(aux);
		}
		return ret;
	}
	
	@Override
	public List<Task> getByProjectUserStatuses(Project project, User user, List<Status> statuses, int first, int count) {
		SortedSet<Task> all = new TreeSet<Task>();
		List<Task> aux = new LinkedList<Task>();
		
		for(Task.Status sts : statuses) {
			aux = find(first, count, "from Task where project_id = ? AND user_id = ? AND status = ? order by index", project.getId(), user.getId(), sts);
			all.addAll(aux);
		}
		
		List<Task> ret = new LinkedList<Task>();
		int i = 0;
		for(Task t : all) {
			if(i >= first && i < (first + count)) {
				ret.add(t);
			}
		}
		return ret;
	}
	
	@Override
	public int count(Project project, User user, List<Status> statuses) {
		return getByProjectUserStatuses(project, user, statuses).size();
	}

	@Override
	public TaskStatistics getStatistics(Project project) {
		return new TaskStatistics(getByProject(project));
	}

	
	@Override
	public synchronized Integer generateIndex(Project project) {
		return getByProject(project).size() + 1;
	}

	@Override
	public List<Task> getByVersion(ProjectVersion version) {
		return find("from Task where ? in elements(versions)", version);
	}

	@Override
	public List<Task> filter(SearchFilter filter, Project project) {
		
		DetachedCriteria query = getFilterQuery(filter, project);

		@SuppressWarnings("unchecked")
		List<Task> ret = query.getExecutableCriteria(getSession()).list();
		
		if(!ValidationUtils.isNull(filter.getCode())) {
			List<Task> retfil = new LinkedList<Task>();
			for(Task t : ret) {
				if(t.getCode().contains(filter.getCode())) {
					retfil.add(t);
				}
			}
			ret = retfil;
		}
		
		if(!ValidationUtils.isNull(filter.getVersion())) {
			List<Task> retfil = new LinkedList<Task>();
			for(Task t : ret) {
				if(Iterables.contains(t.getVersions(), filter.getVersion())) {
					retfil.add(t);
				}
			}
			ret = retfil;
		}
		
		if(!ValidationUtils.isNull(filter.getAffectedVersion())) {
			List<Task> retfil = new LinkedList<Task>();
			for(Task t : ret) {
				if(Iterables.contains(t.getAffectedVersions(), filter.getAffectedVersion())) {
					retfil.add(t);
				}
			}
			ret = retfil;
		}
		Collections.sort(ret);
		return ret;
	}
	
	private DetachedCriteria getFilterQuery(SearchFilter filter, Project project) {
		DetachedCriteria query = DetachedCriteria.forClass(Task.class);
		
		query.add(Restrictions.eq("project", project));
		
		if(ValidationUtils.hasText(filter.getTitle())) {
			query.add(Restrictions.like("title", "%" + filter.getTitle() + "%").ignoreCase());
		}
		
		if(ValidationUtils.hasText(filter.getDescription())) {
			query.add(Restrictions.like("description", "%" + filter.getDescription() + "%").ignoreCase());
		}
		
		if(!ValidationUtils.isNull(filter.getCreator())) {
			query.add(Restrictions.eq("creator", filter.getCreator()));
		}
		
		if(!ValidationUtils.isNull(filter.getOwner())) {
			query.add(Restrictions.eq("owner", filter.getOwner()));
		}
		
		if(!ValidationUtils.isNull(filter.getDatefrom())) {
			query.add(Restrictions.ge("createdAt", filter.getDatefrom()));
		}
		
		if(!ValidationUtils.isNull(filter.getDateto())) {
			query.add(Restrictions.le("createdAt", filter.getDateto()));
		}
		
		if(!ValidationUtils.isNull(filter.getPriority())) {
			query.add(Restrictions.eq("priority", filter.getPriority()));
		}
		
		if(!ValidationUtils.isNull(filter.getStatus())) {
			query.add(Restrictions.eq("status", filter.getStatus()));
		}
		
		if(!ValidationUtils.isNull(filter.getType())) {
			query.add(Restrictions.eq("type", filter.getType()));
		}
		
		return query;
	}
	
	
	@Override
	public List<Task> filter(SearchFilter filter, Project project, int first, int count) {
		List<Task> all = filter(filter, project);
		List<Task> ret = new LinkedList<Task>();
		for(int i = first; i < first + count; i++) {
			ret.add(all.get(i));
		}
		return ret;
	}
	

	@Override
	public List<Task> getByUser(User user) {
		return find("from Task as t where t.owner = ?", user);
	}

	@Override
	public TaskStatistics getVersionStatistics(Project project, ProjectVersion version) {
		List<Task> tasks = find("from Task where project = ? and ? in elements(versions)", project, version);
		return new TaskStatistics(tasks);
	}

	@Override
	public Map<User, Duration> getMembersWork(Project project, DateTime from, DateTime to) {
		Map<User, Duration> ret = new HashMap<User, Duration>();
		
		for(User member : project.getMembers()) {
			ret.put(member, new Duration(0));
		}
		
		List<Task> tasks = getByProject(project);
		
		if(ValidationUtils.hasAllNull(from, to)) {
			getAllReports(ret, tasks);
		}else if(from != null) {
			getFromReports(ret, tasks, from);
		}else if(to != null) {
			getToReports(ret, tasks, to);
		}else {
			getPeriodReports(ret, tasks, from, to);
		}
		return ret;
	}
	
	private Map<User, Duration> getAllReports(Map<User, Duration> ans, List<Task> tasks){
		for(Task t : tasks) {
			for(WorkReport wr : t.getWorkReports()) {
				Duration d = ans.get(wr.getAuthor());
				d = d.add(wr.getDuration());
				ans.put(wr.getAuthor(), d);
			}
		}
		return ans;
	}
	
	private Map<User, Duration> getPeriodReports(Map<User, Duration> ans, List<Task> tasks, DateTime from, DateTime to){
		DateTimeComparator cmp = DateTimeComparator.getInstance();
		for(Task t : tasks) {
			for(WorkReport wr : t.getWorkReports()) {
				if(cmp.compare(from, wr.getReportedAt()) <= 0 && cmp.compare(to, wr.getReportedAt()) >= 0 ){
					Duration d = ans.get(wr.getAuthor());
					d = d.add(wr.getDuration());
					ans.put(wr.getAuthor(), d);
				}
			}
		}
		return ans;
	}
	
	private Map<User, Duration> getFromReports(Map<User, Duration> ans, List<Task> tasks, DateTime from){
		for(Task t : tasks) {
			for(WorkReport wr : t.getWorkReports()) {
				if(from.isBefore(wr.getReportedAt()) ){
					Duration d = ans.get(wr.getAuthor());
					d = d.add(wr.getDuration());
					ans.put(wr.getAuthor(), d);
				}
			}
		}
		return ans;
	}
	
	private Map<User, Duration> getToReports(Map<User, Duration> ans, List<Task> tasks, DateTime to){
		for(Task t : tasks) {
			for(WorkReport wr : t.getWorkReports()) {
				if(to.isAfter(wr.getReportedAt()) ){
					Duration d = ans.get(wr.getAuthor());
					d = d.add(wr.getDuration());
					ans.put(wr.getAuthor(), d);
				}
			}
		}
		return ans;
	}

	public TaskFile getTaskFileById(int id) {
		return (TaskFile) find("from TaskFile where id =  ?",id).get(0);
	}
	
	public Task getTaskByChangesLogers(ChangesLogger changeLogger, Project project){
		return (Task) find("select t from Task t inner join t.changesLogger c where t.project = ? AND c = ?", project, changeLogger).get(0);
	}
	
	public List<ChangesLogger> getLastTaskChanges(Project project){
		
		return findMax("select c from Task t inner join t.changesLogger c where t.project = ? ORDER BY c.createdAt DESC", 5,project);
	}
	
	public Relationship getRelationShipById(int id){
		return (Relationship) find("from Relationship where id = ?", id).get(0);
	}
	
	public boolean existRelationship(Task taskA, Task taskB, RelationshipType type) {
		return ValidationUtils.isEmpty(find("from Relationship r  where id = ? AND taskB = ? and relationshipType = ?",taskA.getId(), taskB, type));
			
	}

	@Override
	public int count() {
		return count(Task.class);
	}

	@Override
	public List<Task> getAll(int first, int count) {
		return getAll(Task.class, first, count);
	}

	@Override
	public int count(Project project, User user) {
		return getByProjectUser(project, user).size();
	}

	@Override
	public int count(Project project) {
		return getByProject(project).size();
	}

	@Override
	public Map<Priority, Duration> getTaskByPriority(Project project,ProjectVersion projectVersion) {
		Map<Priority, Duration> ans = new HashMap<Priority, Duration>();
		
		for(Priority priority : Priority.values()) {
			ans.put(priority, new Duration(0));
		}
		
		for(Task task : getByProject(project)) {
			if(WebUtils.asList(task.getVersions()).contains(projectVersion) )
			ans.put(task.getPriority(), ans.get(task.getPriority()).add(task.getDuration()));
		}
		
		return ans;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Task> topFive(Project project) {
		Session session = getSession();
		Query query = session.createQuery("from Task where project_id = ? and views > 0 order by views DESC, index");
		query.setParameter(0, project.getId());
		query.setMaxResults(5);
		return query.list();
	}

	@Override
	public List<Task> getByVersionAndType(ProjectVersion pv, TType type) {
		return find("from Task where ? in elements(versions) and type = ?", pv, type);
	}
	
}
