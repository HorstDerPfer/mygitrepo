package db.training.bob.web.statistics;

import org.apache.struts.util.MessageResources;

import db.training.bob.model.StatusType;

/**
 * beschreibt eine Zeile des Berichts 'Termineinhaltung Meilensteine für eine Maßnahme'
 * 
 * @author michels
 *
 */
public class SimpleAccuracyReportBean {
	
	private static MessageResources msgRes = null;
	
	private String label = null;
	
	private String onTime = null;

	static {
		msgRes = MessageResources.getMessageResources("MessageResources");
	}
	
	public SimpleAccuracyReportBean(String label, String onTime)
	{
		this.label = label;
		this.onTime = onTime;
	}
	
	public SimpleAccuracyReportBean(String key, StatusType status)
	{
		this.label = msgRes.getMessage(key);
		
		if(status == StatusType.RED)
		{
			this.onTime = msgRes.getMessage("common.nein");
		}
		if(status == StatusType.GREEN)
		{
			this.onTime = msgRes.getMessage("common.ja");
		}
		if(status == StatusType.NEUTRAL)
		{
			this.onTime = msgRes.getMessage("common.ja");
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
	 * @param value the value to set
	 */
	public void setOnTime(String value) {
		this.onTime = value;
	}

	/**
	 * @return the value
	 */
	public String getOnTime() {
		return onTime;
	}
}
