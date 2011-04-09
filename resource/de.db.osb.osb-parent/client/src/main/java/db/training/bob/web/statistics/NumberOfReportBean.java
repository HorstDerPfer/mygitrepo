package db.training.bob.web.statistics;

public class NumberOfReportBean extends AbstractReportBean {

	private int value;
	
	public NumberOfReportBean(String key)
	{
		super(key);
	}
	
	public NumberOfReportBean(String key, int value)
	{
		super(key);
		setValue(value);
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}
	
	
}
