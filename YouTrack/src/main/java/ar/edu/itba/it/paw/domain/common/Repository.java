package ar.edu.itba.it.paw.domain.common;

import java.util.List;

public interface Repository<T extends PersistentEntity> {

	public List<T> getAll();
	
	public List<T> getAll(int first, int count);
	
	public T get(int id);
	
	public void add(T obj);
	
	public void remove(T obj);
	
	public int count();
	
}
