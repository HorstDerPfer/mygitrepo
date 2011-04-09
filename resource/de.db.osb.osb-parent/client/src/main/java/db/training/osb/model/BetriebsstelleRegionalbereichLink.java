package db.training.osb.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import db.training.bob.model.Regionalbereich;
import db.training.easy.core.model.EasyPersistentExpirationObject;

@Entity
@Table(name = "bst_regionalbereich", uniqueConstraints = { @UniqueConstraint(columnNames = {
        "bst_ID", "regionalbereich_ID", "gueltig_von", "gueltig_bis" }) })
public class BetriebsstelleRegionalbereichLink extends EasyPersistentExpirationObject {

	private static final long serialVersionUID = 6441220071915912704L;

	@ManyToOne(optional = false)
	private Betriebsstelle bst;

	@ManyToOne(optional = false)
	private Regionalbereich regionalbereich;

	public Betriebsstelle getBst() {
		return bst;
	}

	public void setBst(Betriebsstelle bst) {
		this.bst = bst;
	}

	public Regionalbereich getRegionalbereich() {
		return regionalbereich;
	}

	public void setRegionalbereich(Regionalbereich regionalbereich) {
		this.regionalbereich = regionalbereich;
	}
}