package pii.exception;

public class ConflictException extends RuntimeException {

	private static final long serialVersionUID = 8489340089750570747L;
	
	public ConflictException(String message) {
		super(message);
	}
}
