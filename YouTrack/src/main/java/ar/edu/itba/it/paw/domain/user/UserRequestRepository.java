package ar.edu.itba.it.paw.domain.user;

import ar.edu.itba.it.paw.domain.common.Repository;

public interface UserRequestRepository extends Repository<UserRequest>{

	public UserRequest getByUsername(String username);
	
}
