/**
 * 
 */
package db.training.osb.model.babett.monitoring;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentExpirationObject;

/**
 * Statusinformationen für die Auswertungen "Monitoring" und "Realisierung Jahresbaubetriebsplan"
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.1.1.13 Monitoring-Status
 * 
 */
@Table(name = "monitoring_status")
@Entity
@SuppressWarnings("serial")
public class Status extends EasyPersistentExpirationObject {

	@Column(length = 24, nullable = false)
	private String kuerzel;

	@Column(length = 64, nullable = false)
	private String name;

	@Column(nullable = false)
	private boolean auswertungRealisierung;

	@Column(nullable = false)
	private boolean auswertungMonitoring;

	public String getKuerzel() {
		return kuerzel;
	}

	public void setKuerzel(String kuerzel) {
		this.kuerzel = kuerzel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAuswertungRealisierung() {
		return auswertungRealisierung;
	}

	public void setAuswertungRealisierung(boolean auswertungRealisierung) {
		this.auswertungRealisierung = auswertungRealisierung;
	}

	public boolean isAuswertungMonitoring() {
		return auswertungMonitoring;
	}

	public void setAuswertungMonitoring(boolean auswertungMonitoring) {
		this.auswertungMonitoring = auswertungMonitoring;
	}
}
