package ar.edu.itba.it.paw.domain.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.itba.it.paw.domain.AbstractMockRepository;
import ar.edu.itba.it.paw.domain.user.User;

public class MockProjectRepository extends AbstractMockRepository<Project> implements ProjectRepository{

	private Map<Integer, ProjectVersion> dbVersion = new HashMap<Integer, ProjectVersion>();
	
	@Override
	public Project getByCode(String code) {
		for(Project p : getAll()) {
			if(p.getCode().equals(code)) {
				return p;
			}
		}
		return null;
	}

	@Override
	public ProjectVersion getVersion(Integer id) {
		return dbVersion.get(id);
	}

	@Override
	public List<Project> getAll(int first, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count() {
		return db.values().size();
	}

	@Override
	public boolean isEmpty() {
		return count() == 0;
	}

	@Override
	public List<Project> getAll(int first, int count, User user) {
		return null;
	}

	@Override
	public List<Project> getPublics(int first, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Project> getPublics() {
		// TODO Auto-generated method stub
		return null;
	}

}
