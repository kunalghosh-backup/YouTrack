package ar.edu.itba.it.paw.domain.common;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;

public abstract class AbstractHibernateRepository<V extends PersistentEntity> implements Repository<V> {

	private final SessionFactory sessionFactory;
	
	public AbstractHibernateRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> type, Serializable id) {
		return (T) getSession().get(type, id);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> find(String hql, Object... params) {
		Session session = getSession();

		Query query = session.createQuery(hql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, params[i]);
		}
		List<T> list = query.list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> find(int first, int count, String hql, Object... params) {
		Session session = getSession();

		Query query = session.createQuery(hql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, params[i]);
		}
		query.setFirstResult(first);
		query.setMaxResults(count);
		List<T> list = query.list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findMax(String hql, int maxResult, Object... params) {
		Session session = getSession();

		Query query = session.createQuery(hql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, params[i]);
		}
		query.setMaxResults(maxResult);
		List<T> list = query.list();
		return list;
	}

	protected org.hibernate.classic.Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public Serializable save(Object o) {
		return getSession().save(o);
	}
	
	public <T> int count(Class<T> clazz) {
		Number ret = (Number) find("select COUNT(*) from " + clazz.getName()).get(0);
		return ret.intValue();
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getAll(Class<T> clazz, int first, int count) {
			Criteria c = getSession().createCriteria(clazz);
			c.setFirstResult(first);
			c.setMaxResults(count);
			return c.list();
	}

	@Override
	public void add(V obj) {
		save(obj);
	}

	@Override
	public void remove(V obj) {
		getSession().delete(obj);
	}
	
}
