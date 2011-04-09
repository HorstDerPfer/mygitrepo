package db.training.easy.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Month {

    private String name;

    private int number;

    private static List<Month> months;

    public Month(String name, int number) {
	super();
	this.name = name;
	this.number = number;
	Month.months = getMonths();
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public int getNumber() {
	return number;
    }

    public void setNumber(int number) {
	this.number = number;
    }

    public static List<Month> getMonths() {
	if (months == null) {
	    months = new ArrayList<Month>();
	    months.add(new Month("Januar", 1));
	    months.add(new Month("Februar", 2));
	    months.add(new Month("März", 3));
	    months.add(new Month("April", 4));
	    months.add(new Month("Mai", 5));
	    months.add(new Month("Juni", 6));
	    months.add(new Month("Juli", 7));
	    months.add(new Month("August", 8));
	    months.add(new Month("September", 9));
	    months.add(new Month("Oktober", 10));
	    months.add(new Month("November", 11));
	    months.add(new Month("Dezember", 12));
	}
	return months;
    }

    public static int getMonthDifference(Calendar startDate, Calendar endDate) {
	if (startDate == null || endDate == null)
	    return 0;
	int months = (endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR)) * 12
		+ endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH);
	if (startDate.before(endDate) || startDate.equals(endDate))
	    months++;
	return months;
    }
}
