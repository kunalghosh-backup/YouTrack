package ar.edu.itba.it.paw.domain.user;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.it.paw.domain.common.AbstractHibernateRepository;
import ar.edu.itba.it.paw.domain.utils.ValidationUtils;

@Repository
public class HibernateUserRepository extends AbstractHibernateRepository<User> implements UserRepository {

	@Autowired
	public HibernateUserRepository(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public List<User> getAll() {
		return find("from User");
	}

	public User get(int id) {
		return get(User.class, id);
	}

	public User getByUsername(String username) {
		List<User> ret = find("from User where username = ?", username);
		return ValidationUtils.emptyList(ret) ? null : ret.get(0);
	}
	
	public User getByEmail(String email) {
		List<User> ret = find("from User where email = ?", email);
		return ValidationUtils.emptyList(ret) ? null : ret.get(0);
	}

	public void add(User object) {
		save(object);
	}

	public void remove(User obj) {
		getSession().delete(obj);
	}

	@Override
	public User login(String username, String password) {
		User user = getByUsername(username);
		if(user != null && user.getPassword().equals(password)) {
			return user;
		}
		return null;
	}

	@Override
	public List<User> getAll(int first, int count) {
		return getAll(User.class, first, count);
	}

	@Override
	public int count() {
		return count(User.class);
	}

}
