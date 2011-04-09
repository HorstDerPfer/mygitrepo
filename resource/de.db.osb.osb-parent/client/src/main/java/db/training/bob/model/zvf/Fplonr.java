package db.training.bob.model.zvf;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.IndexColumn;

import db.training.easy.core.model.EasyPersistentObject;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "niederlassungen" })
@Entity
@Table(name = "fplonr")
public class Fplonr extends EasyPersistentObject {

	/**
	 * FetchPlan.MN_FPLO_NIEDERLASSUNGEN
	 */
	@XmlElement(name = "niederlassung")
	@IndexColumn(name = "nr")
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	protected List<Niederlassung> niederlassungen;

	public Fplonr() {
		this.niederlassungen = new ArrayList<Niederlassung>();
	}

	public List<Niederlassung> getNiederlassungen() {
		return niederlassungen;
	}

	public void setNiederlassungen(List<Niederlassung> niederlassungen) {
		this.niederlassungen = niederlassungen;
	}
}
