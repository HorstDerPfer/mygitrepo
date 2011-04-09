package db.training.bob.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "zvfverspaetung")
public class ZvFVerspaetung extends ZvF implements Historizable {

	@Column(name = "verspaetung")
	private boolean verspaetung;

	@Column(name = "qs_ks")
	private Art qs_ks;

	/**
	 * @return the verspaetung
	 */
	public boolean isVerspaetung() {
		return verspaetung;
	}

	/**
	 * @param verspaetung
	 *            the verspaetung to set
	 */
	public void setVerspaetung(boolean verspaetung) {
		this.verspaetung = verspaetung;
	}

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
