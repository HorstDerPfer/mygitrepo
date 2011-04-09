package db.training.bob.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "zvfumleitung")
public class ZvFUmleitung extends ZvF implements Historizable {

	@Column(name = "qs_ks")
	private Art qs_ks;

	/**
	 * @return the qs_ks
	 */
	public Art getQs_ks() {
		return qs_ks;
	}

	/**
	 * @param qs_ks
	 *            the qs_ks to set
	 */
	public void setQs_ks(Art qs_ks) {
		this.qs_ks = qs_ks;
	}
}
