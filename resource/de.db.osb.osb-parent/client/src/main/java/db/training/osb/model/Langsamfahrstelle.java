/**
 * 
 */
package db.training.osb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import db.training.osb.model.babett.LaTyp;

import java.util.Date;

/**
 * @author michels
 * 
 */
@Entity
@Table(name = "langsamfahrstelle")
@SuppressWarnings("serial")
public class Langsamfahrstelle extends Regelung {

	@Column(length = 32)
	private String gleisWeicheBezeichnung;

	@Column
	@Enumerated(EnumType.STRING)
	private LaTyp laTyp;

	/**
	 * Geschwindigkeit in der Langsamfahrstelle. Wertebereich 10 <= wert <= 350. Als Werte sind nur
	 * durch 10 ohne Rest teilbare Geschwindigkeiten erlaubt. V_La muss immer kleiner als V_VzG
	 * sein.
	 */
	@Column(scale = 4, nullable = true)
	private Integer geschwindigkeitLa;

	@Column(nullable = true)
	private Integer geschwindigkeitVzg;

	@Column(scale = 4, precision = 1)
	private Float fzvMusterzug;

	@Column(name = "km_von")
	private Float kmVon;

	@Column(name = "km_bis")
	private Float kmBis;

    public Langsamfahrstelle() {
    }

    public Langsamfahrstelle(Date zeitVon, Date zeitBis) {
        super(zeitVon, zeitBis);
    }

    public String getGleisWeicheBezeichnung() {
		return gleisWeicheBezeichnung;
	}

	public void setGleisWeicheBezeichnung(String gleisWeicheBezeichnung) {
		this.gleisWeicheBezeichnung = gleisWeicheBezeichnung;
	}

	public LaTyp getLaTyp() {
		return laTyp;
	}

	public void setLaTyp(LaTyp laTyp) {
		this.laTyp = laTyp;
	}

	public int getGeschwindigkeitLa() {
		return geschwindigkeitLa;
	}

	public void setGeschwindigkeitLa(int la) {
		geschwindigkeitLa = la;
	}

	public Integer getGeschwindigkeitVzg() {
		return geschwindigkeitVzg;
	}

	public void setGeschwindigkeitVzg(Integer geschwindigkeitVzg) {
		this.geschwindigkeitVzg = geschwindigkeitVzg;
	}

	public Float getFzvMusterzug() {
		return fzvMusterzug;
	}

	public void setFzvMusterzug(Float fzvMusterzug) {
		this.fzvMusterzug = fzvMusterzug;
	}

	public Float getKmVon() {
		return kmVon;
	}

	public void setKmVon(Float kmVon) {
		this.kmVon = kmVon;
	}

	public Float getKmBis() {
		return kmBis;
	}

	public void setKmBis(Float kmBis) {
		this.kmBis = kmBis;
	}
}
