package ar.edu.itba.it.paw.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;

import ar.edu.itba.it.paw.domain.common.PersistentEntity;
import ar.edu.itba.it.paw.domain.exception.DomainException;
import ar.edu.itba.it.paw.domain.exception.DuplicatedUserException;
import ar.edu.itba.it.paw.domain.exception.PrivilegeException;
import ar.edu.itba.it.paw.domain.utils.ValidationUtils;

@Entity
public class UserRequest extends PersistentEntity {

	@Column(name="name", nullable=false)
	private String name;
	
	@Column(name="lastname", nullable=false)
	private String lastName;
	
	@Column(name="username", nullable=false)
	private String userName;
	
	@Column(name="password", nullable=false)
	private String password;
	
	@Column(name="email", nullable=false)
	private String email;

	UserRequest() {
	}
	
	public UserRequest(String name, String lastName, String userName,
			String password, String email) {
		super();
		this.setName(name);
		this.setLastName(lastName);
		this.setUserName(userName);
		this.setPassword(password);
		this.setEmail(email);
	}
	
	public String getName() {
		return name;
	}

	private void setName(String name) {
		if (ValidationUtils.between(name, 4, 30)) {
			this.name = name;
		} else {
			throw new DomainException("Incorrect Name parameter");
		}
	}

	public String getLastName() {
		return lastName;
	}

	private void setLastName(String lastName) {
		if ( ValidationUtils.between(lastName, 4, 30)) {
			this.lastName = lastName;
		} else {
			throw new DomainException("Invalid LastName");
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
		if (ValidationUtils.between(email, 4, 60)) {
			this.email = email;
		} else {
			throw new DomainException("Incorrect Email parameter");
		}
	}

	public String getFullName() {
		return new StringBuilder(getLastName()).append(", ").append(getName()).toString();
	}
	
	private void drop(UserRequestRepository userRequests) {
		userRequests.remove(this);
	}
	
	public void accept(User admin, UserRequestRepository userRequests, UserRepository users) {
		if(!ValidationUtils.isAdminUser(admin)) {
			throw new PrivilegeException("The user must be an admin");
		}
		if(!ValidationUtils.isNull(users.getByUsername(this.userName))){
			throw new DuplicatedUserException("The user " + this.userName + " is an existing user");
		}
		User created = new User(admin, name, lastName, userName, password, email);
		users.add(created);
		admin.addPrivilegeTo(created, User.Privilege.LOGIN);
		drop(userRequests);
	}
	
	public void reject(User admin, UserRequestRepository userRequests) {
		if(!ValidationUtils.isAdminUser(admin)) {
			throw new PrivilegeException("The user must be an admin");
		}
		drop(userRequests);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
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
		UserRequest other = (UserRequest) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	
}
