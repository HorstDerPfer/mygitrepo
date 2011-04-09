package db.training.bob.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import db.training.easy.core.model.EasyPersistentObject;
import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "bench_mark")
public class Benchmark extends EasyPersistentObject implements Historizable {

	@Column(name = "uebergabeueb")
	@Temporal(TemporalType.DATE)
	private Date uebergabeUeB;

	@Column(name = "trassenmitks")
	private Integer trassenMitKS;

	@Column(name = "geregeltetrassenbiuee")
	private Integer geregelteTrassenBiUeE;

	@Column(name = "ueberarbeitetetrassenbiue")
	private Integer ueberarbeiteteTrassenBiUe;

	@Column(name = "konstruiertetrassenueb")
	private Integer konstruierteTrassenUeB;

	@Column(name = "trassenmitqs")
	private Integer trassenMitQS;

	@Column(name = "ausfaelle")
	private Integer ausFaelle;

	@Column(name = "veroeff_trassenZvF")
	private Integer veroeffentlichteTrassenZvF;

	@Column(name = "erstellte_biue")
	private Integer erstellteBiUe;

	public Benchmark() {
		uebergabeUeB = null;
		trassenMitKS = 0;
		geregelteTrassenBiUeE = 0;
		ueberarbeiteteTrassenBiUe = 0;
		konstruierteTrassenUeB = 0;
		trassenMitQS = 0;
		ausFaelle = 0;
		veroeffentlichteTrassenZvF = 0;
		erstellteBiUe = 0;
	}

	public Date getUebergabeUeB() {
		return uebergabeUeB;
	}

	public void setUebergabeUeB(Date uebergabeUeB) {
		this.uebergabeUeB = uebergabeUeB;
	}

	public Integer getTrassenMitKS() {
		return trassenMitKS;
	}

	public void setTrassenMitKS(Integer trassenMitKS) {
		this.trassenMitKS = trassenMitKS;
	}

	public Integer getGeregelteTrassenBiUeE() {
		return geregelteTrassenBiUeE;
	}

	public void setGeregelteTrassenBiUeE(Integer geregelteTrassenBiUeE) {
		this.geregelteTrassenBiUeE = geregelteTrassenBiUeE;
	}

	public Integer getUeberarbeiteteTrassenBiUe() {
		return ueberarbeiteteTrassenBiUe;
	}

	public void setUeberarbeiteteTrassenBiUe(Integer ueberarbeiteteTrassenBiUe) {
		this.ueberarbeiteteTrassenBiUe = ueberarbeiteteTrassenBiUe;
	}

	public Integer getKonstruierteTrassenUeB() {
		return konstruierteTrassenUeB;
	}

	public void setKonstruierteTrassenUeB(Integer konstruierteTrassenUeB) {
		this.konstruierteTrassenUeB = konstruierteTrassenUeB;
	}

	public Integer getTrassenMitQS() {
		return trassenMitQS;
	}

	public void setTrassenMitQS(Integer trassenMitQS) {
		this.trassenMitQS = trassenMitQS;
	}

	public Integer getAusFaelle() {
		return ausFaelle;
	}

	public void setAusFaelle(Integer ausFaelle) {
		this.ausFaelle = ausFaelle;
	}

	public Integer getVeroeffentlichteTrassenZvF() {
		return veroeffentlichteTrassenZvF;
	}

	public void setVeroeffentlichteTrassenZvF(Integer veroeffentlichteTrassenZvF) {
		this.veroeffentlichteTrassenZvF = veroeffentlichteTrassenZvF;
	}

	public Integer getErstellteBiUe() {
		return erstellteBiUe;
	}

	public void setErstellteBiUe(Integer erstellteBiUe) {
		this.erstellteBiUe = erstellteBiUe;
	}

}
