package ar.edu.itba.it.paw.domain.task;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import ar.edu.itba.it.paw.domain.common.PersistentEntity;
import ar.edu.itba.it.paw.domain.user.User;

@Entity
public class Vote extends PersistentEntity {
	
	@ManyToOne(optional=false)
	private User voter;

	Vote() {
	}
	
	public Vote(User voter) {
		setVoter(voter);
	}

	private void setVoter(User voter) {
		this.voter = voter;
	}

	public User getVoter() {
		return voter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((voter == null) ? 0 : voter.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vote other = (Vote) obj;
		if (voter == null) {
			if (other.voter != null)
				return false;
		} else if (!voter.equals(other.voter))
			return false;
		return true;
	}

}
