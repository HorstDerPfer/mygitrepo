package db.training.bob.web.statistics;

import java.util.Date;

import org.apache.struts.util.MessageResources;

import db.training.bob.model.StatusType;

/**
 * beschreibt eine Zeile des Berichts 'Termineinhaltung Meilensteine für mehrere Maßnahmen'
 * 
 * @author michels
 * 
 */
public class ComplexAccuracyReportBean {

	private static MessageResources msgRes = null;

	private String key = null;

	private String label = null;

	private int totalCount;

	private int delayedCount;

	private int delayed20DaysCount;

	private int delayed10DaysCount;

	private int delayed5DaysCount;

	private int delayedLess5DaysCount;

	static {
		msgRes = MessageResources.getMessageResources("MessageResources");
	}

	public ComplexAccuracyReportBean(String key, int total, int delayed, int delayed20Days,
	    int delayed10Days, int delayed5Days, int delayedLessThan5Days) {
		this.setLabel(msgRes.getMessage(key));
		this.setKey(key);
		this.setTotalCountValue(total);
		this.setDelayedCountValue(delayed);
		this.setDelayed20DaysCountValue(delayed20Days);
		this.setDelayed10DaysCountValue(delayed10Days);
		this.setDelayed5DaysCountValue(delayed5Days);
		this.setDelayedLess5DaysCountValue(delayedLessThan5Days);
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	public void addDate(Date soll, Date ist, StatusType status) {
		if (ist == null)
			ist = new Date();

		if (status == StatusType.GREEN) {
			totalCount += 1;
		}

		if (status == StatusType.RED) {
			final int MILLSECS_PER_DAY = 1000 * 60 * 60 * 24;
			long deltaDays = 0;
			deltaDays = (ist.getTime() - soll.getTime()) / MILLSECS_PER_DAY;

			if (deltaDays >= 20) {
				delayed20DaysCount++;
			} else if (deltaDays >= 10) {
				delayed10DaysCount++;
			} else if (deltaDays >= 5) {
				delayed5DaysCount++;
			} else {
				delayedLess5DaysCount++;
			}

			totalCount++;
			delayedCount++;
		}
	}

	/**
	 * versucht die übergebene Zeichenfolge in einen ganzzahligen Wert zu konvertieren. Wenn die
	 * Zeichenfolge nicht konvertiert werden kann wird 0 zurückgegeben.
	 * 
	 * @param value
	 * @return
	 */
	private int toInteger(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * @param totalCount
	 *            the totalCount to set
	 */
	public void setTotalCount(String totalCount) {
		this.setTotalCountValue(toInteger(totalCount));
	}

	/**
	 * @return the totalCount
	 */
	public String getTotalCount() {
		return String.valueOf(getTotalCountValue());
	}

	/**
	 * @param totalCount
	 *            the totalCount to set
	 */
	public void setTotalCountValue(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return the totalCount
	 */
	public int getTotalCountValue() {
		return totalCount;
	}

	/**
	 * @param delayedCount
	 *            the delayedCount to set
	 */
	public void setDelayedCount(String delayedCount) {
		this.setDelayedCountValue(toInteger(delayedCount));
	}

	/**
	 * @return the delayedCount
	 */
	public String getDelayedCount() {
		return String.valueOf(getDelayedCountValue());
	}

	/**
	 * @param delayedCount
	 *            the delayedCount to set
	 */
	public void setDelayedCountValue(int delayedCount) {
		this.delayedCount = delayedCount;
	}

	/**
	 * @return the delayedCount
	 */
	public int getDelayedCountValue() {
		return delayedCount;
	}

	/**
	 * @param delayed20DaysCount
	 *            the delayed20DaysCount to set
	 */
	public void setDelayed20DaysCount(String delayed20DaysCount) {
		this.setDelayed20DaysCountValue(toInteger(delayed20DaysCount));
	}

	/**
	 * @return the delayed20DaysCount
	 */
	public String getDelayed20DaysCount() {
		return String.valueOf(getDelayed20DaysCountValue());
	}

	/**
	 * @param delayed20DaysCount
	 *            the delayed20DaysCount to set
	 */
	public void setDelayed20DaysCountValue(int delayed20DaysCount) {
		this.delayed20DaysCount = delayed20DaysCount;
	}

	/**
	 * @return the delayed20DaysCount
	 */
	public int getDelayed20DaysCountValue() {
		return delayed20DaysCount;
	}

	/**
	 * @param delayed10DaysCount
	 *            the delayed10DaysCount to set
	 */
	public void setDelayed10DaysCount(String delayed10DaysCount) {
		this.setDelayed10DaysCountValue(toInteger(delayed10DaysCount));
	}

	/**
	 * @return the delayed10DaysCount
	 */
	public String getDelayed10DaysCount() {
		return String.valueOf(getDelayed10DaysCountValue());
	}

	/**
	 * @param delayed10DaysCount
	 *            the delayed10DaysCount to set
	 */
	public void setDelayed10DaysCountValue(int delayed10DaysCount) {
		this.delayed10DaysCount = delayed10DaysCount;
	}

	/**
	 * @return the delayed10DaysCount
	 */
	public int getDelayed10DaysCountValue() {
		return delayed10DaysCount;
	}

	/**
	 * @param delayed5DaysCount
	 *            the delayed5DaysCount to set
	 */
	public void setDelayed5DaysCount(String delayed5DaysCount) {
		this.setDelayed5DaysCountValue(toInteger(delayed5DaysCount));
	}

	/**
	 * @return the delayed5DaysCount
	 */
	public String getDelayed5DaysCount() {
		return String.valueOf(getDelayed5DaysCountValue());
	}

	/**
	 * @param delayed5DaysCount
	 *            the delayed5DaysCount to set
	 */
	public void setDelayed5DaysCountValue(int delayed5DaysCount) {
		this.delayed5DaysCount = delayed5DaysCount;
	}

	/**
	 * @return the delayed5DaysCount
	 */
	public int getDelayed5DaysCountValue() {
		return delayed5DaysCount;
	}

	/**
	 * @param delayedLess5DaysCount
	 *            the delayedLess5DaysCount to set
	 */
	public void setDelayedLess5DaysCount(String delayedLess5DaysCount) {
		this.setDelayedLess5DaysCountValue(toInteger(delayedLess5DaysCount));
	}

	/**
	 * @return the delayedLess5DaysCount
	 */
	public String getDelayedLess5DaysCount() {
		return String.valueOf(getDelayedLess5DaysCountValue());
	}

	/**
	 * @param delayedLess5DaysCount
	 *            the delayedLess5DaysCount to set
	 */
	public void setDelayedLess5DaysCountValue(int delayedLess5DaysCount) {
		this.delayedLess5DaysCount = delayedLess5DaysCount;
	}

	/**
	 * @return the delayedLess5DaysCount
	 */
	public int getDelayedLess5DaysCountValue() {
		return delayedLess5DaysCount;
	}
}
