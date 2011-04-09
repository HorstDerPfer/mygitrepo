package db.training.bob.web.statistics;

public class ChangesReportBean extends AbstractReportBean {

	private int totalCount = 0;
	
	private int changedCount = 0;
	
	private int costs = 0;
	
	public ChangesReportBean(String key)
	{
		super(key);
	}

	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return the totalCount
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * @param changedCount the changedCount to set
	 */
	public void setChangedCount(int changedCount) {
		this.changedCount = changedCount;
	}

	/**
	 * @return the changedCount
	 */
	public int getChangedCount() {
		return changedCount;
	}

	/**
	 * @param costs the costs to set
	 */
	public void setCosts(int costs) {
		this.costs = costs;
	}

	/**
	 * @return the costs
	 */
	public int getCosts() {
		return costs;
	}
}
