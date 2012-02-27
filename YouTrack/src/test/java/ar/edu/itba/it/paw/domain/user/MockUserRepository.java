package ar.edu.itba.it.paw.domain.user;

import java.util.List;

import ar.edu.itba.it.paw.domain.AbstractMockRepository;

public class MockUserRepository extends AbstractMockRepository<User> implements UserRepository {

	@Override
	public User getByUsername(String username) {
		for(User u :db.values()) {
			if(u.getUserName().equals(username)) {
				return u;
			}
		}
		return null;
	}

	@Override
	public User getByEmail(String email) {
		for(User u : db.values()) {
			if(u.getEmail().equals(email)) {
				return u;
			}
		}
		return null;
	}

	@Override
	public User login(String username, String password) {
		User user = getByUsername(username);
		if(!user.isBanned() && user.getPassword().equals(password)) {
			return user;
		}
		return null;
	}

	@Override
	public List<User> getAll(int first, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		return 0;
	}


}
