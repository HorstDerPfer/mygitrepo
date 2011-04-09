package db.training.osb.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import db.training.easy.core.model.EasyPersistentExpirationObject;

/**
 * @see Feinkonzept (25.1.10), 7.1.3.5 Verknüpfung Betriebsstelle-Betriebsstellentyp
 * @author michels
 * 
 */
@Entity
@Table(name = "bst_bsttyp", uniqueConstraints = { @UniqueConstraint(columnNames = {
        "bst_ID", "typ_ID", "gueltig_von", "gueltig_bis" }) })
@SuppressWarnings("serial")
public class BetriebsstelleBetriebsstellentypLink extends EasyPersistentExpirationObject {

	@ManyToOne(optional = false)
	private Betriebsstelle bst;

	@ManyToOne(optional = false)
	private Betriebsstellentyp typ;

	public Betriebsstelle getBst() {
		return bst;
	}

	public void setBst(Betriebsstelle bst) {
		this.bst = bst;
	}

	public Betriebsstellentyp getTyp() {
		return typ;
	}

	public void setTyp(Betriebsstellentyp typ) {
		this.typ = typ;
	}
}
