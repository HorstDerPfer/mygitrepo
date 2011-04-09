package db.training.bob.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "zvfausfallverkehrshalt")
public class ZvFAusfallVerkehrshalt extends ZvF implements Historizable {

	@Column(name = "ausfallenderVerkehrshalt")
	private String ausfallenderVerkehrshalt;

	@Column(name = "vorgeschlagenerErsatzhalt")
	private String vorgeschlagenerErsatzhalt;

	/**
	 * @return the ausfallenderVerkehrshalt
	 */
	public String getAusfallenderVerkehrshalt() {
		return ausfallenderVerkehrshalt;
	}

	/**
	 * @param ausfallenderVerkehrshalt
	 *            the ausfallenderVerkehrshalt to set
	 */
	public void setAusfallenderVerkehrshalt(String ausfallenderVerkehrshalt) {
		this.ausfallenderVerkehrshalt = ausfallenderVerkehrshalt;
	}

	/**
	 * @return the vorgeschlagenerErsatzhalt
	 */
	public String getVorgeschlagenerErsatzhalt() {
		return vorgeschlagenerErsatzhalt;
	}

	/**
	 * @param vorgeschlagenerErsatzhalt
	 *            the vorgeschlagenerErsatzhalt to set
	 */
	public void setVorgeschlagenerErsatzhalt(String vorgeschlagenerErsatzhalt) {
		this.vorgeschlagenerErsatzhalt = vorgeschlagenerErsatzhalt;
	}
}
