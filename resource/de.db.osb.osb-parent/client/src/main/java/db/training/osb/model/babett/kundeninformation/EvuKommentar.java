/**
 * 
 */
package db.training.osb.model.babett.kundeninformation;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import db.training.bob.model.EVU;
import db.training.easy.core.model.EasyPersistentObject;

/**
 * Die Fachklasse <code>EvuKommentar</code> dient der Ablage der Kommentare der EVU, die zu einem
 * <code>Verkehrssegment</code> gehören.
 * 
 * @author michels
 * 
 */
@Entity
@Table(name = "kdinfo_evukommentar")
@SuppressWarnings("serial")
public class EvuKommentar extends EasyPersistentObject {

	@ManyToOne()
	private EVU evu;

	@Lob
	private String text;

	@ManyToOne(optional = false)
	private Verkehrssegment segment;

	/**
	 * @return EVU Typ
	 */
	public EVU getEvu() {
		return evu;
	}

	/**
	 * @param evuTyp
	 */
	public void setEvu(EVU evu) {
		this.evu = evu;
	}

	/**
	 * @return Kommentar des EVU
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            Kommentar des EVU
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return Verkehrssegment, zu dem das EVU gehört, das den Kommentar abgegeben hat
	 */
	public Verkehrssegment getSegment() {
		return segment;
	}

	/**
	 * @param segment
	 *            Verkehrssegment, zu dem das EVU gehört, das den Kommentar abgegeben hat
	 */
	public void setSegment(Verkehrssegment segment) {
		this.segment = segment;
	}
}
