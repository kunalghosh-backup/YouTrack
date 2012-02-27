package ar.edu.itba.it.paw.domain.user;

import ar.edu.itba.it.paw.domain.common.Repository;

public interface UserRepository extends Repository<User> {

	public User getByUsername(String username);
	
	public User getByEmail(String email);
	
	public User login(String username, String password);
	
}
