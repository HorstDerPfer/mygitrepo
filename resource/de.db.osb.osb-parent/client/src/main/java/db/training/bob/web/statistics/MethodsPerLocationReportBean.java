package db.training.bob.web.statistics;

public class MethodsPerLocationReportBean {
	private String label = null;
	
	private int count = 0;

	public MethodsPerLocationReportBean(String label, int count)
	{
		this.label = label;
		this.count = count;
	}
	
	/**
	 * @param label the label to set
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
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	
	
}
