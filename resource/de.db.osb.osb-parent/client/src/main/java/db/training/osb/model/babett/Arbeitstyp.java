/**
 * 
 */
package db.training.osb.model.babett;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import db.training.easy.core.model.EasyPersistentExpirationObject;

/**
 * @author michels
 * @see Feinkonzept (25.1.10), 7.1.1.1 Arbeiten
 * 
 */
@Entity()
@Table(name = "arbeiten")
@SuppressWarnings("serial")
public class Arbeitstyp extends EasyPersistentExpirationObject {

	/** AGRP_ID */
	@ManyToOne(optional = false)
	private ArbeitstypGruppe gruppe;

	@Column(length = 8, nullable = false)
	private String kuerzel;

	/** arbeiten */
	@Column(length = 32, nullable = false)
	private String name;

	@Column(name = "t", nullable = false)
	private boolean textErforderlich;

	@Column(name = "o", nullable = false)
	private boolean ortErforderlich;

	/**
	 * @return Oberbegriff des Typs
	 */
	public ArbeitstypGruppe getGruppe() {
		return gruppe;
	}

	/**
	 * @param Oberbegriff
	 *            des Typs
	 */
	public void setGruppe(ArbeitstypGruppe gruppe) {
		this.gruppe = gruppe;
	}

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

	/**
	 * @return gibt an, ob zu den Arbeiten ein erläuternder Text erforderlich ist
	 */
	public boolean isTextErforderlich() {
		return textErforderlich;
	}

	/**
	 * @param textErforderlich
	 *            gibt an, ob zu den Arbeiten ein erläuternder Text erforderlich ist
	 */
	public void setTextErforderlich(boolean textErforderlich) {
		this.textErforderlich = textErforderlich;
	}

	/**
	 * @return gibt an, ob zu den Arbeiten eine Ortsangabe (=Betriebsstelle) erforderlich ist
	 */
	public boolean isOrtErforderlich() {
		return ortErforderlich;
	}

	/**
	 * @param ortErforderlich
	 *            gibt an, ob zu den Arbeiten eine Ortsangabe (=Betriebsstelle) erforderlich ist
	 */
	public void setOrtErforderlich(boolean ortErforderlich) {
		this.ortErforderlich = ortErforderlich;
	}

	@Transient
	public String getCaption() {
		StringBuilder caption = new StringBuilder();
		if (kuerzel != null)
			caption.append("[").append(kuerzel).append("]");
		if (kuerzel != null && name != null)
			caption.append(" ");
		if (name != null)
			caption.append(name);
		return caption.toString();
	}
}
