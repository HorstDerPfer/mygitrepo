/**
 * 
 */
package db.training.osb.model.babett.kundeninformation;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentObject;
import db.training.osb.model.babett.EvuKundentyp;

/**
 * Die Fachklasse <code>Verkehrssegment</code> enthält die Informationen zu einem Verkehrssegment
 * und eine Zusammenfassung der Statements der EVU dieses Verkehrssegments durch die KBBT.
 * 
 * @author michels
 * 
 */
@Table(name = "kdinfo_verkehrssegment")
@Entity
@SuppressWarnings("serial")
public class Verkehrssegment extends EasyPersistentObject {

	@Enumerated(EnumType.STRING)
	private EvuKundentyp evuTyp;

	@ManyToOne
	private Master master;

	@Lob
	private String text;

	@OneToMany(mappedBy = "segment")
	private Set<EvuKommentar> kommentare;

	@Column(length = 16)
	private String bbpMassnahmeId;

	private Integer bbpRglNr;

	/**
	 * @return EVU Typ
	 */
	public EvuKundentyp getEvuTyp() {
		return evuTyp;
	}

	/**
	 * @param evuTyp
	 *            EVU Typ
	 */
	public void setEvuTyp(EvuKundentyp evuTyp) {
		this.evuTyp = evuTyp;
	}

	/**
	 * @return der zugehörige Kundeninformation-Master
	 */
	public Master getMaster() {
		return master;
	}

	/**
	 * @param master
	 *            der zugehörige Kundeninformation-Master
	 */
	public void setMaster(Master master) {
		this.master = master;
	}

	/**
	 * @return Zusammenfassung der Kommentare der EVU, erstellt durch die KBBT.
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            Zusammenfassung der Kommentare der EVU, erstellt durch die KBBT.
	 */
	public void setText(String text) {
		this.text = text;
	}

	public Set<EvuKommentar> getKommentare() {
		return kommentare;
	}

	public void setKommentare(Set<EvuKommentar> kommentare) {
		this.kommentare = kommentare;
	}

	public String getBbpMassnahmeId() {
		return bbpMassnahmeId;
	}

	public void setBbpMassnahmeId(String bbpMassnahmeId) {
		this.bbpMassnahmeId = bbpMassnahmeId;
	}

	public Integer getBbpRglNr() {
		return bbpRglNr;
	}

	public void setBbpRglNr(Integer bbpRglNr) {
		this.bbpRglNr = bbpRglNr;
	}
}
