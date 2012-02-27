package ar.edu.itba.it.paw.domain.project;

import java.util.Collections;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.it.paw.domain.common.AbstractHibernateRepository;
import ar.edu.itba.it.paw.domain.user.User;

@Repository
public class HibernateProjectRepository extends AbstractHibernateRepository<Project> implements ProjectRepository {

	@Autowired
	public HibernateProjectRepository(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public List<Project> getAll() {
		return find("from Project");
	}

	@Override
	public Project get(int projectId) {
		return get(Project.class, projectId);
	}

	@Override
	public void add(Project obj) {
		save(obj);
	}

	@Override
	public void remove(Project obj) {
		getSession().delete(obj);
	}

	@Override
	public List<Project> getPublics() {
		return find("from Project where status = ?", Project.Status.PUBLIC);
	}
	
	@Override
	public List<Project> getPublics(int first, int count) {
		return find(first, count, "from Project where status = ?", Project.Status.PUBLIC);
	}

	@Override
	public Project getByCode(String code) {
		List<Project> aux = find("from Project where code = ?", code);
		if(aux.size() == 0){
			return null;
		}
		return aux.get(0);
	}

	@Override
	public ProjectVersion getVersion(Integer id) {
		List<ProjectVersion> aux = find("from ProjectVersion where id = ?", id);
		if(aux.size() == 0){
			return null;
		}
		return aux.get(0);
	}

	@Override
	public int count() {
		return count(Project.class);
	}

	@Override
	public List<Project> getAll(int first, int count) {
		List<Project> ret = getAll(Project.class, first, count);
		Collections.sort(ret);
		return ret;
	}

	@Override
	public boolean isEmpty() {
		return count() == 0;
	}

	@Override
	public List<Project> getAll(int first, int count, User user) {
		if(user != null) {
			if(user.isAdmin()) {
				return getAll(first, count);
			}
			return find(first, count,"from Project as p where ? in elements(p.members) or p.status = ? order by p.code", user, Project.Status.PUBLIC);
		}else{
			return getPublics(first, count);
		}
	}

	@Override
	public int count(User user) {
		if(user != null) {
			if(user.isAdmin()) {
				return count();
			}
			return find("from Project as p where ? in elements(p.members) or p.status = ?", user, Project.Status.PUBLIC).size();
		}else{
			return getPublics().size();
		}
	}
	
}
