/**
 * 
 */
package db.training.osb.model.babett.kundeninformation;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import db.training.bob.model.EVU;
import db.training.easy.core.model.EasyPersistentObject;

/**
 * @author michels
 * 
 */
@Entity
@Table(name="kdinfo_kbeleg")
public class Kommunikationsbeleg extends EasyPersistentObject {

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=true)
	private Date absendedatum;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=true)
	private Date rueckmeldedatum;

	@ManyToOne(optional = false)
	private EVU evu;

	@ManyToOne(optional = false)
	private EvuKommentar kommentar;

	@ManyToMany()
	private Set<Master> master;

	public Date getAbsendedatum() {
		return absendedatum;
	}

	public void setAbsendedatum(Date absendedatum) {
		this.absendedatum = absendedatum;
	}

	public Date getRueckmeldedatum() {
		return rueckmeldedatum;
	}

	public void setRueckmeldedatum(Date rueckmeldedatum) {
		this.rueckmeldedatum = rueckmeldedatum;
	}

	public EVU getEvu() {
		return evu;
	}

	public void setEvu(EVU evu) {
		this.evu = evu;
	}

	public EvuKommentar getKommentar() {
		return kommentar;
	}

	public void setKommentar(EvuKommentar kommentar) {
		this.kommentar = kommentar;
	}

	public Set<Master> getMaster() {
		return master;
	}

	public void setMaster(Set<Master> master) {
		this.master = master;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
