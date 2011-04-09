package db.training.osb.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentObject;

@SuppressWarnings("serial")
@Entity
@Table(name = "ankermassnahme_art")
public class AnkermassnahmeArt extends EasyPersistentObject {

	@Column(length = 16)
	private String kurzname;

	@Column(length = 128)
	private String langname;

	@Column(name = "gueltig_ab")
	private Date gueltigAb;

	@Column(name = "gueltig_bis")
	private Date gueltigBis;

	public AnkermassnahmeArt() {
		super();
	}

	public String getKurzname() {
		return kurzname;
	}

	public void setKurzname(String kurzname) {
		this.kurzname = kurzname;
	}

	public String getLangname() {
		return langname;
	}

	public void setLangname(String langname) {
		this.langname = langname;
	}

	public Date getGueltigAb() {
		return gueltigAb;
	}

	public void setGueltigAb(Date gueltigAb) {
		this.gueltigAb = gueltigAb;
	}

	public Date getGueltigBis() {
		return gueltigBis;
	}

	public void setGueltigBis(Date gueltigBis) {
		this.gueltigBis = gueltigBis;
	}

}
