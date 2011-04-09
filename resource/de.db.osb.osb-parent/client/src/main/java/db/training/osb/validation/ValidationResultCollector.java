package db.training.osb.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationResultCollector implements ResultHandler<ValidationResult> {

	private List<ValidationResult> results;

	public ValidationResultCollector() {
		results = new ArrayList<ValidationResult>();
	}

	@Override
	public void handleResult(ValidationResult value) throws ValidationAbortedException {
		results.add(value);
		System.out.println(value.toString());
	}

	public List<ValidationResult> getResults() {
		return results;
	}
}
