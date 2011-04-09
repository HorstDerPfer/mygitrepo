package db.training.bob.web.statistics;

public class EscalationReportBean extends AbstractReportBean {

	private int totalCount;
	
	private int escalationCount;
	
	private int vetoCount;
	
	public EscalationReportBean(String key)
	{
		this(key, 0, 0, 0);
	}

	public EscalationReportBean(String key, int totalCount, int escalationCount, int vetoCount)
	{
		super(key);
		this.setTotalCount(totalCount);
		this.setEscalationCount(escalationCount);
		this.setVetoCount(vetoCount);
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
	 * @param escalationCount the escalationCount to set
	 */
	public void setEscalationCount(int escalationCount) {
		this.escalationCount = escalationCount;
	}

	/**
	 * @return the escalationCount
	 */
	public int getEscalationCount() {
		return escalationCount;
	}

	/**
	 * @param vetoCount the vetoCount to set
	 */
	public void setVetoCount(int vetoCount) {
		this.vetoCount = vetoCount;
	}

	/**
	 * @return the vetoCount
	 */
	public int getVetoCount() {
		return vetoCount;
	}
}
