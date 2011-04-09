/**
 * 
 */
package db.training.osb.model.babett.monitoring;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentExpirationObject;

/**
 * dient der Ablage der Ausfallgründe für das Monitoring und der zugehörigen Steuerdaten für die
 * Auswertung.
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.1.1.12 Monitoring-Gründe
 */
@Table(name = "monitoring_grund")
@Entity(name = "MonitoringGrund")
@SuppressWarnings("serial")
public class Grund extends EasyPersistentExpirationObject {

	@Column(length = 16, nullable = false)
	private String kuerzel;

	@Column(length = 64, nullable = false)
	private String name;

	/** default: wahr */
	@Column(nullable = false)
	private boolean auswertung;

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

	public boolean isAuswertung() {
		return auswertung;
	}

	public void setAuswertung(boolean auswertung) {
		this.auswertung = auswertung;
	}
}
