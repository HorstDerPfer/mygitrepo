package db.training.bob.web.statistics;

public class QualitaetstrasseReportBean extends AbstractReportBean {
	
	private int submittedCount = 0;
	
	private int approvedCount = 0;
	
	private int rejectedCount = 0;
	
	public QualitaetstrasseReportBean(String key)
	{
		super(key);
	}

	/**
	 * @param approvedCount the approvedCount to set
	 */
	public void setApprovedCount(int approvedCount) {
		this.approvedCount = approvedCount;
	}

	/**
	 * @return the approvedCount
	 */
	public int getApprovedCount() {
		return approvedCount;
	}

	/**
	 * @param rejectedCount the rejectedCount to set
	 */
	public void setRejectedCount(int rejectedCount) {
		this.rejectedCount = rejectedCount;
	}

	/**
	 * @return the rejectedCount
	 */
	public int getRejectedCount() {
		return rejectedCount;
	}

	public int getSubmittedCount() {
		return submittedCount;
	}
	
	public void setSubmittedCount(int submittedCount) {
		this.submittedCount = submittedCount;
	}
}
