package db.training.osb.validation;

@SuppressWarnings("serial")
public class ValidationAbortedException extends Exception {

	public ValidationAbortedException() {
		super();
	}

	public ValidationAbortedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidationAbortedException(String message) {
		super(message);
	}

	public ValidationAbortedException(Throwable cause) {
		super(cause);
	}

}
