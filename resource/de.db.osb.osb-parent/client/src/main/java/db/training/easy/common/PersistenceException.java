package db.training.easy.common;

public class PersistenceException extends Exception {

	private static final long serialVersionUID = -619662952403756825L;

	public PersistenceException() {
		super();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public PersistenceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public PersistenceException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public PersistenceException(Throwable arg0) {
		super(arg0);
	}
}