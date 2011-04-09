package db.training.bob.web.statistics;

public class AusfallReportBean extends AbstractReportBean {

	private int totalCount = 0;
	
	private int ausfallCount = 0;
	
	private int costs = 0;
	
	public AusfallReportBean(String key)
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
	public void setAusfallCount(int ausfallCount) {
		this.ausfallCount = ausfallCount;
	}

	/**
	 * @return the changedCount
	 */
	public int getAusfallCount() {
		return ausfallCount;
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
