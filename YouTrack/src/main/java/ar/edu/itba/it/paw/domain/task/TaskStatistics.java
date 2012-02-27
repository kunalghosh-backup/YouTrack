package ar.edu.itba.it.paw.domain.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ar.edu.itba.it.paw.domain.common.Duration;

public class TaskStatistics {

	private List<Task> tasks;
	
	private Map<Task.Status, Integer> amounts = new HashMap<Task.Status, Integer>();
	private Map<Task.Status, Duration> estimatedTotal = new HashMap<Task.Status, Duration>();
	
	TaskStatistics(List<Task> tasks) {
		
		this.tasks = tasks;
		
		for(Task.Status sts : Task.Status.values()) {
			amounts.put(sts, 0);
			estimatedTotal.put(sts, new Duration(0));
		}
		
		for(Task task : tasks) {
			Task.Status sts = task.getStatus();
			amounts.put(sts, amounts.get(sts) + 1);
			estimatedTotal.put(sts, estimatedTotal.get(sts).add(task.getDuration()));
		}
	}
	
	public Integer getOpenAmount() {
		return amounts.get(Task.Status.OPEN);
	}
	
	public Duration getOpenEstimatedTime() {
		return estimatedTotal.get(Task.Status.OPEN);
	}
	
	public Integer getOnGoingAmount() {
		return amounts.get(Task.Status.ONGOING);
	}
	
	public Duration getOnGoingEstimatedTime() {
		return estimatedTotal.get(Task.Status.ONGOING);
	}
	
	public Integer getCompletedAmount() {
		return amounts.get(Task.Status.COMPLETED);
	}
	
	public Duration getCompletedEstimatedTime() {
		return estimatedTotal.get(Task.Status.COMPLETED);
	}
	
	public Integer getClosedAmount() {
		return amounts.get(Task.Status.CLOSED);
	}
	
	public Duration getClosedEstimatedTime() {
		return estimatedTotal.get(Task.Status.CLOSED);
	}
	
	public Duration getTotalEstimatedTime() {
		Duration ret = new Duration(0);
		for(Duration d : estimatedTotal.values()) {
			ret = ret.add(d);
		}
		return ret;
	}
	
	public Duration getTotalWorkedTime() {
		Duration ret = new Duration(0);
		for(Task t : tasks) {
			ret = ret.add(t.getWorkedTime());
		}
		return ret;
	}
	
	public Duration getEstimatedCompletionTime() {
	
		List<Task.Status> sts = new ArrayList<Task.Status>(2);
		sts.add(Task.Status.OPEN);
		sts.add(Task.Status.ONGOING);
		
		int ret = 0, aux;
		
		for(Task t : tasks) {
			if(sts.contains(t.getStatus())) {
				if((aux = (t.getDuration().getMinutes() - t.getWorkedTime().getMinutes())) > 0) {
					ret += aux;
				}
			}
		}
		
		return new Duration(ret);
	}
	
	
	public List<Task> getTypeError() {
		return getTasks(Task.TType.ERROR);
	}

	public List<Task> getTypeImprovement() {
		return getTasks(Task.TType.IMPROVEMENT);
	}
	
	public List<Task> getTypeNewFeature() {
		return getTasks(Task.TType.NEW_FEATURE);
	}
	
	public List<Task> getTypeTask() {
		return getTasks(Task.TType.TASK);
	}
	
	public List<Task> getTypeTechnical() {
		return getTasks(Task.TType.TECHNICAL);
	}
	
	public List<Task> getTasks(Task.TType type) {
		List<Task> ret = new LinkedList<Task>();
		for(Task t : this.tasks) {
			if(t.getType().equals(type)) {
				ret.add(t);
			}
		}
		Collections.sort(ret);
		return ret;
	}
}
