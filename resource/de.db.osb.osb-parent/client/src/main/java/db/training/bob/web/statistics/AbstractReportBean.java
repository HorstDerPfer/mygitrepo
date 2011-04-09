package db.training.bob.web.statistics;

import org.apache.struts.util.MessageResources;

public abstract class AbstractReportBean {

	private static MessageResources msgRes = null;
	
	private String key = null;
	
	private String label = null;
	
	static {
		msgRes = MessageResources.getMessageResources("MessageResources");
	}
	
	public AbstractReportBean(String key)
	{
		this.label = msgRes.getMessage(key);
		
		if(this.label == null)
		{
			this.label = key;
		}
		else
		{
			this.key = key;
		}
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
	 * @param key the key to set
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
}
