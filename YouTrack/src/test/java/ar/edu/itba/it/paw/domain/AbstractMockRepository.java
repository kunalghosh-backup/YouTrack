package ar.edu.itba.it.paw.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.itba.it.paw.domain.common.PersistentEntity;
import ar.edu.itba.it.paw.domain.common.Repository;

public abstract class AbstractMockRepository<T extends PersistentEntity> implements Repository<T>{

	protected Map<Integer, T> db = new HashMap<Integer, T>();
	
	@Override
	public List<T> getAll() {
		return new ArrayList<T>(db.values());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAll(int first, int count) {
		return Arrays.asList((T[]) db.values().toArray()).subList(first, first + count);
	}
	
	public List<T> list(List<T> list, int first, int count) {
		return list.subList(first, first + count);
	}

	@Override
	public int count() {
		return db.values().size();
	}

	@Override
	public T get(int id) {
		if(db.containsKey(id)) {
			T ret = db.get(id);
			ret.setId(id);
			return ret;
		}
		return null;
	}

	@Override
	public void add(T obj) {
		int nextId = db.keySet().size() + 1;
		obj.setId(nextId);
		db.put(nextId, obj);
	}

	@Override
	public void remove(T obj) {
		if(db.containsKey(obj.getId())) {
			db.remove(obj.getId());
		}
	}

}
