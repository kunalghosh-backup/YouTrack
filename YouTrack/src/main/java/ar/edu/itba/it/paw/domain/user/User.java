package ar.edu.itba.it.paw.domain.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;

import ar.edu.itba.it.paw.domain.common.PersistentEntity;
import ar.edu.itba.it.paw.domain.exception.DomainException;
import ar.edu.itba.it.paw.domain.exception.PrivilegeException;
import ar.edu.itba.it.paw.domain.exception.UserWorkingException;
import ar.edu.itba.it.paw.domain.task.Task;
import ar.edu.itba.it.paw.domain.task.TaskRepository;
import ar.edu.itba.it.paw.domain.utils.ValidationUtils;

@Entity
@Table(name="yt_user")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class User extends PersistentEntity {

	public static enum Privilege {
		LOGIN, ADMIN;
	}
	
	public static enum Status {
		FREE, WORKING, BANNED;
	}
	
	@Column(name="name", nullable=false)
	private String name;
	
	@Column(name="lastname", nullable=false)
	private String lastName;
	
	@Column(name="username", nullable=false, unique=true)
	private String userName;
	
	@Column(name="password", nullable=false)
	private String password;
	
	@Column(name="email", nullable=false, unique=true)
	private String email;

	@CollectionOfElements
	@JoinColumn(name="user_id")
	@Column(name="privilege", table="user_privilege", nullable = false)
	@Enumerated(EnumType.STRING)
	private Set<Privilege> privileges = new HashSet<Privilege>();
	
	User(){
	}
	
	User(String name, String lastName, String userName, String password, String email) {
		this.setName(name);
		this.setLastName(lastName);
		this.setUserName(userName);
		this.setPassword(password);
		this.setEmail(email);
		this.privileges = new HashSet<User.Privilege>();
	}
	
	public User(User creator, String name, String lastName, String userName, String password, String email) {
		this(name, lastName, userName, password, email);
		if(!ValidationUtils.isAdminUser(creator)) {
			throw new PrivilegeException("The creator must be an admin");
		}
	}
	
	public String getName() {
		return name;
	}

	private void setName(String name) {
		if (ValidationUtils.between(name, 4, 30)) {
			this.name = name;
		} else {
			throw new DomainException("Invalid name");
		}
	}

	public String getLastName() {
		return lastName;
	}

	private void setLastName(String lastName) {
		if ( ValidationUtils.between(lastName, 4, 30)) {
			this.lastName = lastName;
		} else {
			throw new DomainException("Incorrect Last Name parameter");
		}
	}

	public String getUserName() {
		return userName;
	}

	private void setUserName(String userName) {
		if (ValidationUtils.between(userName, 4, 30)) {
			this.userName = userName;
		} else {
			throw new DomainException("Incorrect User Name parameter");
		}
	}

	public String getPassword() {
		return password;
	}

	private void setPassword(String password) {
		if (ValidationUtils.between(password, 6, 30)) {
			this.password = password;
		} else {
			throw new DomainException("Incorrect Password parameter");
		}
	}

	public String getEmail() {
		return email;
	}

	private void setEmail(String email) {
		if (ValidationUtils.between(email, 4, 60) && ValidationUtils.isAnEmail(email)) {
			this.email = email;
		} else {
			throw new DomainException("Incorrect Email parameter");
		}
	}

	public Iterable<Privilege> getPrivileges() {
		return this.privileges;
	}
	
	public void addPrivilegeTo(User userToChange, User.Privilege p) {
		ValidationUtils.checkAdminUser(this);
		userToChange.addPrivilege(p);
	}
	
	protected void addPrivilege(Privilege up) {
		this.privileges.add(up);
	}
	
	public void revokePrivilegeTo(User userToChange, Privilege p, TaskRepository tasks) {
		ValidationUtils.checkAdminUser(this);
		if(this.equals(userToChange)) {
			throw new DomainException("Invalid privilege revoke");
		}
		if(p.equals(Privilege.LOGIN) && userToChange.isWorking(tasks)) {
			throw new UserWorkingException();
		}
		userToChange.revokePrivilege(p);
	}
	
	public String getFullName() {
		return new StringBuilder(getLastName()).append(", ").append(getName()).toString();
	}
	
	private void revokePrivilege(User.Privilege up) {
		this.privileges.remove(up);
	}
	
	public boolean isAdmin() {
		return this.privileges.contains(User.Privilege.ADMIN);
	}
	
	public boolean isBanned() {
		return !this.privileges.contains(User.Privilege.LOGIN);
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", lastName=" + lastName + ", userName="
				+ userName + ", password=" + password + ", email=" + email
				+ ", privileges=" + privileges + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
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
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	
	public boolean isWorking(TaskRepository tasks) {
		List<Task> all = tasks.getByUser(this);
		for(Task t : all) {
			if(t.getStatus().equals(Task.Status.OPEN) || t.getStatus().equals(Task.Status.ONGOING)) {
				return true;
			}
		}
		return false;
	}
	
	public Status getStatus(TaskRepository tasks) {
		if(this.isBanned()) {
			return Status.BANNED;
		}else{
			if(this.isWorking(tasks)) {
				return Status.WORKING;
			}else{
				return Status.FREE;
			}
		}
	}
	
}
