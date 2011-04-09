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

import db.training.easy.core.model.EasyPersistentObject;
import db.training.osb.model.Fahrplanregelung;

/**
 * beschreibt die Regelungsart einer Fahrplanregelung.
 * 
 * @author michels
 * 
 */
@Entity
@Table(name = "regelungsart_fahrplanregelung")
@SuppressWarnings("serial")
public class Regelungsart extends EasyPersistentObject {

	@Enumerated(EnumType.STRING)
	private RegelungsartKbbt art;

	@ManyToOne(optional = false)
	private Fahrplanregelung fahrplanregelung;

	private Integer anzahlSpfv;

	private Integer anzahlSpnv;

	private Integer anzahlSgv;

	@Column(length = 64)
	private String relation;

	private Integer fplAbweichungSpfv;

	private Integer fplAbweichungSpnv;

	private Integer fplAbweichungSgv;

	private String bemerkung;

	@Column(length = 16)
	private String bbpMassnahmeId;

	private Integer bbpRglNr;

	public RegelungsartKbbt getArt() {
		return art;
	}

	public void setArt(RegelungsartKbbt art) {
		this.art = art;
	}

	public Fahrplanregelung getFahrplanregelung() {
		return fahrplanregelung;
	}

	public void setFahrplanregelung(Fahrplanregelung fahrplanregelung) {
		this.fahrplanregelung = fahrplanregelung;
	}

	public Integer getAnzahlSpfv() {
		return anzahlSpfv;
	}

	public void setAnzahlSpfv(Integer anzahlSpfv) {
		this.anzahlSpfv = anzahlSpfv;
	}

	public Integer getAnzahlSpnv() {
		return anzahlSpnv;
	}

	public void setAnzahlSpnv(Integer anzahlSpnv) {
		this.anzahlSpnv = anzahlSpnv;
	}

	public Integer getAnzahlSgv() {
		return anzahlSgv;
	}

	public void setAnzahlSgv(Integer anzahlSgv) {
		this.anzahlSgv = anzahlSgv;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public Integer getFplAbweichungSpfv() {
		return fplAbweichungSpfv;
	}

	public void setFplAbweichungSpfv(Integer fplAbweichungSpfv) {
		this.fplAbweichungSpfv = fplAbweichungSpfv;
	}

	public Integer getFplAbweichungSpnv() {
		return fplAbweichungSpnv;
	}

	public void setFplAbweichungSpnv(Integer fplAbweichungSpnv) {
		this.fplAbweichungSpnv = fplAbweichungSpnv;
	}

	public Integer getFplAbweichungSgv() {
		return fplAbweichungSgv;
	}

	public void setFplAbweichungSgv(Integer fplAbweichungSgv) {
		this.fplAbweichungSgv = fplAbweichungSgv;
	}

	public String getBemerkung() {
		return bemerkung;
	}

	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
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
