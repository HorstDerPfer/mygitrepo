package db.training.bob.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "zvfausfallzug")
public class ZvFAusfallZug extends ZvF implements Historizable {

	@Column(name = "ausfallAb")
	private String ausfallAb;

	@Column(name = "ausfallBis")
	private String ausfallBis;

	/**
	 * @return the ausfallAb
	 */
	public String getAusfallAb() {
		return ausfallAb;
	}

	/**
	 * @param ausfallAb
	 *            the ausfallAb to set
	 */
	public void setAusfallAb(String ausfallAb) {
		this.ausfallAb = ausfallAb;
	}

	/**
	 * @return the ausfallBis
	 */
	public String getAusfallBis() {
		return ausfallBis;
	}

	/**
	 * @param ausfallBis
	 *            the ausfallBis to set
	 */
	public void setAusfallBis(String ausfallBis) {
		this.ausfallBis = ausfallBis;
	}
}
