package ar.edu.itba.it.paw.domain.user;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.it.paw.domain.common.AbstractHibernateRepository;
import ar.edu.itba.it.paw.domain.utils.ValidationUtils;

@Repository
public class HibernateUserRequestRepository extends AbstractHibernateRepository<UserRequest> implements UserRequestRepository {

	@Autowired
	public HibernateUserRequestRepository(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public List<UserRequest> getAll() {
		return find("from UserRequest");
	}

	@Override
	public UserRequest get(int id) {
		return get(UserRequest.class, id);
	}

	@Override
	public UserRequest getByUsername(String username) {
		List<UserRequest> ret =  find("from UserRequest where username = ?", username);
		return ValidationUtils.emptyList(ret) ? null : ret.get(0);
	}

	@Override
	public List<UserRequest> getAll(int first, int count) {
		return getAll(UserRequest.class, first, count);
	}

	@Override
	public int count() {
		return count(UserRequest.class);
	}

}
