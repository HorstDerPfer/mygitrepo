package db.training.osb.validation;

public interface ResultHandler<T> {

	void handleResult(T value) throws ValidationAbortedException;

}
