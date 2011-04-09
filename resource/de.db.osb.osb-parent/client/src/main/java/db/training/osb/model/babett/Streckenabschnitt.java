/**
 * 
 */
package db.training.osb.model.babett;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentExpirationObject;
import db.training.osb.model.VzgStrecke;

/**
 * @author michels
 * @see Feinkonzept (25.1.10), 7.1.3.8 Streckenkategorie
 */
@Entity
@Table(name = "streckenkategorie")
@SuppressWarnings("serial")
public class Streckenabschnitt extends EasyPersistentExpirationObject {

	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private Streckenkategorie kategorie;

	@Column(name = "km_von", nullable = false)
	private float kmVon;

	@Column(name = "km_bis", nullable = false)
	private float kmBis;

	@ManyToOne(optional = false)
	private VzgStrecke vzgStrecke;

	public Streckenkategorie getKategorie() {
		return kategorie;
	}

	public void setKategorie(Streckenkategorie kategorie) {
		this.kategorie = kategorie;
	}

	public float getKmVon() {
		return kmVon;
	}

	public void setKmVon(float kmVon) {
		this.kmVon = kmVon;
	}

	public float getKmBis() {
		return kmBis;
	}

	public void setKmBis(float kmBis) {
		this.kmBis = kmBis;
	}

	public VzgStrecke getVzgStrecke() {
		return vzgStrecke;
	}

	public void setVzgStrecke(VzgStrecke vzgStrecke) {
		this.vzgStrecke = vzgStrecke;
	}
}
