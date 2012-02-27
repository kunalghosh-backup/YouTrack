package ar.edu.itba.it.paw.domain.exception;

@SuppressWarnings("serial")
public class DomainException extends RuntimeException {

	public DomainException() {
		
	}
	
	public DomainException(String message) {
		super(message);
	}
	
}
