package pii.exception;

public class UncheckedSQLException extends RuntimeException {

	private static final long serialVersionUID = 7362880289658438786L;

	public UncheckedSQLException(String message) {
		super(message);
	}
	
	public UncheckedSQLException(String message, Throwable cause) {
		super(message, cause);
	}
}
