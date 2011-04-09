package db.training.bob.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import db.training.easy.core.model.EasyPersistentObject;

@SuppressWarnings("serial")
@Entity
@Table(name = "meilenstein")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Meilenstein extends EasyPersistentObject {

	@Column(name = "art")
	private Art art;

	@Column(name = "schnittstelle")
	private Schnittstelle schnittstelle;

	@Column(name = "bezeichnung")
	private Meilensteinbezeichnungen bezeichnung;

	@Column(name = "anzWochen")
	private int anzahlWochenVorBaubeginn;

	@Column(name = "wochentag")
	private int wochentag;

	/**
	 * Die Mindestfrist gibt an, ob der Soll-Termin mindestens <code>anzahlWochenVorBaubeginn</code>
	 * Wochen vor Baubeginn liegen muss. Bsp: anzahlWochenVorBaubeginn = 2 -> Soll-Termin muss
	 * mindestens 14 Tage vor Baubeginn liegen
	 */
	@Column(name = "mindestfrist", nullable = false)
	private boolean mindestfrist = false;

	@Column(name = "gueltigAb")
	@Temporal(TemporalType.DATE)
	private Date gueltigAbBaubeginn;

	public void setArt(Art art) {
		this.art = art;
	}

	public Art getArt() {
		return art;
	}

	public void setSchnittstelle(Schnittstelle schnittstelle) {
		this.schnittstelle = schnittstelle;
	}

	public Schnittstelle getSchnittstelle() {
		return schnittstelle;
	}

	public void setBezeichnung(Meilensteinbezeichnungen bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public Meilensteinbezeichnungen getBezeichnung() {
		return bezeichnung;
	}

	public void setAnzahlWochenVorBaubeginn(int anzahlWochenVorBaubeginn) {
		this.anzahlWochenVorBaubeginn = anzahlWochenVorBaubeginn;
	}

	public int getAnzahlWochenVorBaubeginn() {
		return anzahlWochenVorBaubeginn;
	}

	public void setWochentag(int wochentag) {
		this.wochentag = wochentag;
	}

	public int getWochentag() {
		return wochentag;
	}

	public void setMindestfrist(boolean mindestfrist) {
		this.mindestfrist = mindestfrist;
	}

	public boolean isMindestfrist() {
		return mindestfrist;
	}

	public void setGueltigAbBaubeginn(Date gueltigAbBaubeginn) {
		this.gueltigAbBaubeginn = gueltigAbBaubeginn;
	}

	public Date getGueltigAbBaubeginn() {
		return gueltigAbBaubeginn;
	}
}
