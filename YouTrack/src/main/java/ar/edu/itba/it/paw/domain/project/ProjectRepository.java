package ar.edu.itba.it.paw.domain.project;

import java.util.List;

import ar.edu.itba.it.paw.domain.common.Repository;
import ar.edu.itba.it.paw.domain.user.User;

public interface ProjectRepository extends Repository<Project>{

	public List<Project> getPublics();
	
	public List<Project> getPublics(int first, int count);
	
	public Project getByCode(String code);
	
	public ProjectVersion getVersion(Integer id);
	
	public List<Project> getAll(int first, int count, User user);
	
	public boolean isEmpty();
	
	public int count(User user);
	
}
