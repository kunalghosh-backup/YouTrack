package ar.edu.itba.it.paw.domain.exception;

@SuppressWarnings("serial")
public class DuplicatedUserException extends RuntimeException {

	public DuplicatedUserException() {
		super();
	}

	public DuplicatedUserException(String message) {
		super(message);
	}
	
}
