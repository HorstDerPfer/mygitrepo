package db.training.bob.util;

import db.training.bob.service.FetchPlan;

public class ArrayHelper {

	public static FetchPlan[] buildArray(FetchPlan[]... arrays) {
		FetchPlan[] result = null;
		int size = 0;
		for (FetchPlan[] array : arrays) {
			size += array.length;
		}
		result = new FetchPlan[size];
		int positionResult = 0;
		for (FetchPlan[] array : arrays) {
			System.arraycopy(array, 0, result, positionResult, array.length);
			positionResult = positionResult + array.length;
		}
		return result;
	}
}
