package db.training.bob.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentObject;
import db.training.easy.util.FrontendHelper;
import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "aenderung")
public class Aenderung extends EasyPersistentObject implements Historizable {

	@Column(name = "aenderungsnr")
	private Integer aenderungsNr;

	@ManyToOne(fetch = FetchType.EAGER)
	private Grund grund;

	@Column(name = "aufwand")
	private Integer aufwand;

	public Integer getAenderungsNr() {
		return aenderungsNr;
	}

	public void setAenderungsNr(Integer aenderungsNr) {
		this.aenderungsNr = aenderungsNr;
	}

	public Grund getGrund() {
		return grund;
	}

	public void setGrund(Grund grund) {
		this.grund = grund;
	}

	public Integer getAufwand() {
		return aufwand;
	}

	public void setAufwand(Integer aufwand) {
		this.aufwand = aufwand;
	}

	/**
	 * Minuten in Format hh:mm konvertieren und ggfs. mit 0 auffüllen
	 * 
	 * @return
	 */
	public String getAufwandTimeString() {
		return FrontendHelper.castMinutesToTimeString(getAufwand());
	}

	public void setAufwandTimeString(String s) {
		if (s == null) {
			return;
		}

		aufwand = FrontendHelper.castTimeStringToMinutes(s);
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		Aenderung aenderung = (Aenderung) obj;
		return (aenderungsNr == aenderung.aenderungsNr || (aenderungsNr != null && aenderungsNr
		    .equals(aenderung.aenderungsNr)))
		    && (aufwand == aenderung.aufwand || (aufwand != null && aufwand
		        .equals(aenderung.aufwand)))
		    && (grund == aenderung.grund || (grund != null && grund.equals(aenderung.grund)));
	}

	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (null == aenderungsNr ? 0 : aenderungsNr.hashCode());
		hash = 31 * hash + (null == aufwand ? 0 : aufwand.hashCode());
		hash = 31 * hash + (null == grund ? 0 : grund.hashCode());
		return hash;
	}
}